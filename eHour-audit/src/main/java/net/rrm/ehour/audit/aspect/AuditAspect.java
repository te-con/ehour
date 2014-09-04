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

package net.rrm.ehour.audit.aspect;

import net.rrm.ehour.audit.annot.Auditable;
import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.PageRequestHandlerTracker;
import org.apache.wicket.request.cycle.RequestCycle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

/**
 * Auditable Aspect
 */
@Aspect
@Service
public class AuditAspect {
    @Autowired
    private AuditService auditService;

    @Autowired
    private ConfigurationService configurationService;

    @Pointcut("execution(public * net.rrm.ehour.*.service.*Service*.get*(..)) && " +
            "!@annotation(net.rrm.ehour.audit.annot.Auditable) && " +
            "!@annotation(net.rrm.ehour.audit.annot.NonAuditable)")
    public void publicGetMethod() {
    }

    @Pointcut("execution(public * net.rrm.ehour.*.service.*Service.persist*(..)) && " +
            "!@annotation(net.rrm.ehour.audit.annot.Auditable) && " +
            "!@annotation(net.rrm.ehour.audit.annot.NonAuditable)")
    public void publicPersistMethod() {
    }

    @Pointcut("execution(public * net.rrm.ehour.*.service.*Service.delete*(..)) && " +
            "!@annotation(net.rrm.ehour.audit.annot.Auditable) && " +
            "!@annotation(net.rrm.ehour.audit.annot.NonAuditable)")
    public void publicDeleteMethod() {
    }

    /**
     * Around advise for read-only get methods
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("net.rrm.ehour.audit.aspect.AuditAspect.publicGetMethod()")
    public Object auditOnGet(ProceedingJoinPoint pjp) throws Throwable {
        return doAudit(pjp, AuditActionType.READ);
    }

    /**
     * Around advise for persist methods
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("net.rrm.ehour.audit.aspect.AuditAspect.publicPersistMethod()")
    public Object auditOnPersist(ProceedingJoinPoint pjp) throws Throwable {
        return doAudit(pjp, AuditActionType.UPDATE);
    }

    /**
     * Around advise for delete methods
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("net.rrm.ehour.audit.aspect.AuditAspect.publicDeleteMethod()")
    public Object auditOnDelete(ProceedingJoinPoint pjp) throws Throwable {
        return doAudit(pjp, AuditActionType.DELETE);
    }

    /**
     * Audit
     *
     * @param pjp
     * @param auditable
     * @return
     * @throws Throwable
     */
    @Around("@annotation(auditable)")
    public Object doAudit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        return doAudit(pjp, auditable.actionType());
    }

    /**
     * @param pjp
     * @param auditActionType
     * @return
     * @throws Throwable
     */
    private Object doAudit(ProceedingJoinPoint pjp, AuditActionType auditActionType) throws Throwable {
        Object returnObject;

        boolean isAuditable = isAuditable(pjp) && isAuditEnabled(auditActionType);

        User user = getUser();

        try {
            returnObject = pjp.proceed();
        } catch (Exception t) {
            if (isAuditable) {
                auditService.doAudit(createAudit(user, Boolean.FALSE, auditActionType, pjp));
            }

            throw t;
        }

        if (isAuditable) {
            auditService.doAudit(createAudit(user, Boolean.TRUE, auditActionType, pjp));
        }

        return returnObject;
    }

    /**
     * Is audit type enabled
     *
     * @param actionType
     * @return
     */
    private boolean isAuditEnabled(AuditActionType actionType) {
        EhourConfig config = configurationService.getConfiguration();

        if (config.getAuditType() == AuditType.NONE) {
            return false;
        }

        if (config.getAuditType() == AuditType.WRITE &&
                actionType.getAuditType() == config.getAuditType()) {
            return true;
        }

        return config.getAuditType() == AuditType.ALL;

    }

    /**
     * Check if type is annotated with NonAuditable annotation
     *
     * @param pjp
     * @return
     */
    private boolean isAuditable(ProceedingJoinPoint pjp) {
        Annotation[] annotations = pjp.getSignature().getDeclaringType().getAnnotations();

        boolean nonAuditable = false;

        for (Annotation annotation : annotations) {
            nonAuditable = annotation.annotationType() == NonAuditable.class;

            if (nonAuditable) {
                break;
            }
        }

        return !nonAuditable;
    }

    private User getUser() {
        User user;

        try {
            user = EhourWebSession.getUser();
        } catch (Exception t) {
            user = null;
        }

        return user;
    }

    private Audit createAudit(User user, Boolean success, AuditActionType auditActionType, ProceedingJoinPoint pjp) {
        String parameters = getAuditParameters(pjp);

        String page = null;

        RequestCycle cycle = RequestCycle.get();

        if (cycle != null) {
            IPageRequestHandler lastHandler = PageRequestHandlerTracker.getLastHandler(cycle);

            if (lastHandler != null) {
                Class<? extends IRequestablePage> pageClass = lastHandler.getPageClass();

                if (pageClass != null) {
                    page = pageClass.getCanonicalName();
                }
            }
        }
        return new Audit()
                .setUser(user)
                .setUserFullName(user != null ? user.getFullName() : null)
                .setDate(new Date())
                .setSuccess(success)
                .setAction(pjp.getSignature().toShortString())
                .setAuditActionType(auditActionType)
                .setParameters(parameters)
                .setPage(page)
                ;
    }

    /**
     * Get parameters of advised method
     *
     * @param pjp
     * @return
     */
    private String getAuditParameters(ProceedingJoinPoint pjp) {
        StringBuilder parameters = new StringBuilder();

        int i = 0;

        for (Object object : pjp.getArgs()) {
            parameters.append(i++).append(":");

            if (object == null) {
                parameters.append("null");
            } else if (object instanceof Calendar) {
                parameters.append(((Calendar) object).getTime().toString());
            } else {
                parameters.append(object.toString());
            }
        }


        return parameters.length() > 1024 ? parameters.substring(0, 1023) : parameters.toString();
    }


    /**
     * @param auditService the auditService to set
     */
    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * @param configurationService the configurationService to set
     */
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
