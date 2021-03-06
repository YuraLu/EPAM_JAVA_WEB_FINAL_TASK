package by.epam.lukashevich.domain.command.impl.user;

import by.epam.lukashevich.domain.command.Command;
import by.epam.lukashevich.domain.command.exception.CommandException;
import by.epam.lukashevich.domain.entity.user.User;
import by.epam.lukashevich.domain.service.UserService;
import by.epam.lukashevich.domain.service.exception.ServiceException;
import by.epam.lukashevich.domain.service.exception.user.InvalidEmailException;
import by.epam.lukashevich.domain.service.exception.user.InvalidLoginException;
import by.epam.lukashevich.domain.service.provider.ServiceProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.epam.lukashevich.domain.config.BeanFieldJsp.*;
import static by.epam.lukashevich.domain.config.JSPActionCommand.VIEW_USER_CABINET_COMMAND;
import static by.epam.lukashevich.domain.config.JSPPage.USER_CABINET_PAGE;
import static by.epam.lukashevich.domain.config.Message.*;

public class CommandEditUser implements Command {

    private UserService userService = ServiceProvider.getInstance().getUserService();

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CommandException {

        final HttpSession session = request.getSession();

        final int id = Integer.parseInt(request.getParameter(USER_ID));
        final String name = request.getParameter(USER_NAME);
        final String email = request.getParameter(USER_EMAIL);

        try {
            userService.update(id, name, email);

            final User user = userService.findById(id);

            request.setAttribute(USER_OBJECT, user);
            request.setAttribute(USER_ROLE_ID, user.getRole().getId());

            session.setAttribute(MESSAGE_TO_EDIT_USER, MESSAGE_DATA_CHANGED);
        } catch (InvalidLoginException ex) {
            session.setAttribute(MESSAGE_TO_EDIT_USER, MESSAGE_INVALID_INFO);
        } catch (InvalidEmailException ex) {
            session.setAttribute(MESSAGE_TO_EDIT_USER, MESSAGE_INVALID_EMAIL);
        } catch (ServiceException e) {
            session.setAttribute(MESSAGE_TO_EDIT_USER, MESSAGE_DATA_INVALID_INFO);
        }

        session.setAttribute(REDIRECT_COMMAND, VIEW_USER_CABINET_COMMAND);
        return USER_CABINET_PAGE;
    }
}