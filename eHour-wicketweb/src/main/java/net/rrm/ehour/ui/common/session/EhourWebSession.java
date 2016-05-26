/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.session;

import com.google.common.base.Optional;
import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigCache;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.security.SecurityRules;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.authorization.AuthUser;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Ehour Web session
 */

public class EhourWebSession extends AuthenticatedWebSession {
    @SpringBean
    private EhourConfig unCachedEhourConfig;

    private EhourConfig ehourConfig;

    @SpringBean
    private AuditService auditService;

    private Calendar navCalendar;
    private UserSelectedCriteria userSelectedCriteria;
    private Boolean hideInactiveSelections = true;

    private Optional<AuthUser> impersonatingAuthUser = Optional.absent();

    private static final Logger LOGGER = Logger.getLogger(EhourWebSession.class);

    public EhourWebSession(Request req) {
        super(req);

        reloadConfig();
    }

    public static EhourWebSession getSession() {
        return (EhourWebSession) Session.get();
    }


    public static User getUser() {
        EhourWebSession session = EhourWebSession.getSession();
        AuthUser authUser = session.getAuthUser();
        return (authUser != null) ? authUser.getUser() : null;
    }

    public final void reloadConfig() {
        WebUtils.springInjection(this);

        ehourConfig = new EhourConfigCache(unCachedEhourConfig);

        if (!ehourConfig.isDontForceLanguage()) {
            setLocale(ehourConfig.getLanguageLocale());
        }
    }

    public Boolean getHideInactiveSelections() {
        return hideInactiveSelections;
    }

    public void setHideInactiveSelections(Boolean hideInactiveSelections) {
        this.hideInactiveSelections = hideInactiveSelections;
    }

    public Boolean toggleHideInactiveSelections() {
        this.hideInactiveSelections = !this.hideInactiveSelections;

        return this.hideInactiveSelections;
    }

    public static EhourConfig getEhourConfig() {
        return EhourWebSession.getSession().ehourConfig;
    }

    public Calendar getNavCalendar() {
        if (navCalendar == null) {
            navCalendar = DateUtil.getCalendar(ehourConfig);
        }

        return (Calendar) navCalendar.clone();
    }

    public void setNavCalendar(Calendar navCalendar) {
        this.navCalendar = navCalendar;
    }

    /**
     * Get authenticated user
     */
    public AuthUser getAuthUser() {
        AuthUser authUser = null;

        if (isSignedIn()) {
            if (impersonatingAuthUser.isPresent()) {
                authUser = impersonatingAuthUser.get();
            } else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication != null) {
                    authUser = (AuthUser) authentication.getPrincipal();
                }
            }
        }

        return authUser;
    }

    /**
     * Authenticate based on username/pass
     */
    @Override
    public boolean authenticate(String username, String password) {
        String u = username == null ? "" : username;
        String p = password == null ? "" : password;

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u, p);

        // Attempt authentication.
        try {
            AuthenticationManager authenticationManager = ((EhourWebApplication) getApplication()).getAuthenticationManager();

            if (authenticationManager == null) {
                throw new AuthenticationServiceException("no authentication manager defined");
            }

            Authentication authResult = authenticationManager.authenticate(authRequest);
            setAuthentication(authResult);

            User user = ((AuthUser) authResult.getPrincipal()).getUser();

            auditService.doAudit(new Audit()
                    .setAuditActionType(AuditActionType.LOGIN)
                    .setUser(user)
                    .setUserFullName(user.getFullName())
                    .setDate(new Date())
                    .setSuccess(Boolean.TRUE));

            LOGGER.info("Login by user '" + username + "'.");
            return true;

        } catch (BadCredentialsException e) {
            LOGGER.info("Failed to login for" +
                    " user '" + username + "': " + e.getMessage());
            setAuthentication(null);
            return false;

        } catch (AuthenticationException e) {
            LOGGER.info("Could not authenticate a user", e);
            setAuthentication(null);
            throw e;

        } catch (RuntimeException e) {
            LOGGER.info("Unexpected exception while authenticating a user", e);
            setAuthentication(null);
            throw e;
        }
    }

    @Override
    public Roles getRoles() {
        if (isSignedIn()) {
            if (impersonatingAuthUser.isPresent()) {
                Roles roles = new Roles();

                Set<UserRole> userRoles = getAuthUser().getUser().getUserRoles();

                for (UserRole userRole : userRoles) {
                    roles.add(userRole.getRole());
                }

                return roles;
            }

            return getRolesForSignedInUser();
        }
        return null;
    }

    private Roles getRolesForSignedInUser() {
        // Retrieve the granted authorities from the current authentication. These correspond one on
        // one with user roles.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Roles roles = new Roles();

            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

            for (GrantedAuthority grantedAuthority : authorities) {
                roles.add(grantedAuthority.getAuthority());
            }

            if (roles.isEmpty()) {
                LOGGER.warn("User " + auth.getPrincipal() + " logged in but no roles could be found!");
            }

            return roles;
        } else {
            LOGGER.warn("User is signed in but authentication is not set!");
            return null;
        }
    }

    public boolean isReporter() {
        return SecurityRules.isWithReportRole(getRoles());
    }

    public boolean isProjectManager() {
        return SecurityRules.isWithPmRole(getRoles());
    }

    public boolean isAdmin() {
        return SecurityRules.isWithAdminRole(getRoles());
    }

    public boolean isManager() {
        return SecurityRules.isWithManagerRole(getRoles());
    }

    /**
     * Invalidate authenticated user
     */
    public void signOut() {
        AuthUser user = getAuthUser();

        getSession().clear();

        setAuthentication(null);
        setUserSelectedCriteria(null);

        super.signOut();

        auditService.doAudit(new Audit()
                .setAuditActionType(AuditActionType.LOGOUT)
                .setUser(((user != null) ? user.getUser() : null))
                .setUserFullName(((user != null) ? user.getUser().getFullName() : "N/A"))
                .setDate(new Date())
                .setSuccess(Boolean.TRUE));
        Session.get().replaceSession();
    }

    public void impersonateUser(User userToImpersonate) throws UnauthorizedToImpersonateException {
        boolean allowedToImpersonate = allowedToImpersonate(userToImpersonate);

        if (!allowedToImpersonate) {
            throw new UnauthorizedToImpersonateException();
        }

        User originalUser = getUser();

        impersonatingAuthUser = Optional.of(new AuthUser(userToImpersonate));

        clearUserSelectedReportCriteria();

        logAndAuditImpersonation(originalUser);
    }

    protected boolean allowedToImpersonate(User userToImpersonate) {
        return SecurityRules.allowedToModify(getUser(), userToImpersonate, ehourConfig.isSplitAdminRole());
    }


    private void logAndAuditImpersonation(User originalUser) {
        StringBuilder auditMsg = new StringBuilder((originalUser != null) ? originalUser.getFullName() : "N/A");
        auditMsg.append(" started impersonating as ");
        auditMsg.append(impersonatingAuthUser.get().getUser().getFullName());

        LOGGER.info(auditMsg.toString());

        auditService.doAudit(new Audit()
                .setAuditActionType(AuditActionType.IMPERSONATE)
                .setUser(originalUser)
                .setUserFullName(auditMsg.toString())
                .setDate(new Date())
                .setSuccess(true));
    }

    public void stopImpersonating() {
        if (impersonatingAuthUser.isPresent()) {
            User impUser = impersonatingAuthUser.get().getUser();

            User originalUser = getUser();
            logAndAuditStopImpersonation(originalUser, impUser);

            impersonatingAuthUser = Optional.absent();
        }

        clearUserSelectedReportCriteria();
    }

    private void clearUserSelectedReportCriteria() {
        userSelectedCriteria = null;
    }

    public boolean isImpersonating() {
        return impersonatingAuthUser.isPresent();
    }

    private void logAndAuditStopImpersonation(User originalUser, User impUser) {
        StringBuilder auditMsg = new StringBuilder((originalUser != null) ? originalUser.getFullName() : "N/A");
        auditMsg.append(" stopped impersonating as ");
        auditMsg.append(impUser.getFullName());

        LOGGER.info(auditMsg.toString());

        auditService.doAudit(new Audit()
                .setAuditActionType(AuditActionType.STOP_IMPERSONATE)
                .setUser(originalUser)
                .setUserFullName(auditMsg.toString())
                .setDate(new Date())
                .setSuccess(true));
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserSelectedCriteria getUserSelectedCriteria() {
        return userSelectedCriteria;
    }

    public void setUserSelectedCriteria(UserSelectedCriteria userSelectedCriteria) {
        this.userSelectedCriteria = userSelectedCriteria;
    }

    private static final long serialVersionUID = 93189812483240412L;
}
