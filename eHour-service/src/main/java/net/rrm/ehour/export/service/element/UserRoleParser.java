package net.rrm.ehour.export.service.element;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.export.service.ParseStatus;
import net.rrm.ehour.export.service.ParserUtil;
import net.rrm.ehour.persistence.export.dao.ExportType;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * User: thies
 * Date: 11/29/10
 * Time: 11:28 PM
 */
public class UserRoleParser
{
    private UserRoleParserDao dao;

    public UserRoleParser(UserRoleParserDao dao)
    {
        this.dao = dao;
    }

    public void parseUserRoles(XMLEventReader reader, ParseStatus status) throws XMLStreamException
    {
        XMLEvent event;

        while ((event = reader.nextTag()).isStartElement())
        {
            parseUserRole(reader, status);
        }
    }

    private void parseUserRole(XMLEventReader reader, ParseStatus status) throws XMLStreamException
    {
        XMLEvent event;
        String role = null;
        String userId = null;

        while ((event = reader.nextTag()).isStartElement())
        {
            StartElement element = event.asStartElement();
            String name = element.getName().getLocalPart();
            String data = ParserUtil.parseNextEventAsCharacters(reader);

            if (name.equalsIgnoreCase("ROLE"))
            {
                role = data;
            } else if (name.equalsIgnoreCase("USER_ID"))
            {
                userId = data;
            }
        }

        if (userId != null && role != null)
        {
            User user = dao.findUser(Integer.parseInt(userId));
            UserRole userRole = dao.findUserRole(role);

            user.addUserRole(userRole);
            dao.persistUser(user);

            status.addInsertion(ExportType.USER_ROLE);
        } else
        {
            status.addError(ExportType.USER_ROLE, "No userID (" + userId + ") or role (" + role + ") found");
        }

    }
}
