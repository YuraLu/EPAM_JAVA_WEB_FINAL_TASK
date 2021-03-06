package by.epam.lukashevich.dao.impl;

import by.epam.lukashevich.dao.UserDAO;
import by.epam.lukashevich.dao.core.pool.connection.ConnectionWrapper;
import by.epam.lukashevich.dao.core.pool.connection.ProxyConnection;
import by.epam.lukashevich.dao.core.pool.impl.DatabaseConnectionPool;
import by.epam.lukashevich.dao.exception.user.InvalidLoginOrPasswordException;
import by.epam.lukashevich.dao.exception.user.UsedEmailException;
import by.epam.lukashevich.dao.exception.user.UsedLoginException;
import by.epam.lukashevich.dao.exception.user.UserDAOException;
import by.epam.lukashevich.dao.impl.util.SQLUtil;
import by.epam.lukashevich.domain.entity.user.Role;
import by.epam.lukashevich.domain.entity.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.epam.lukashevich.dao.impl.util.SQLQuery.*;

/**
 * Represents CRUD methods for operation with User Entity in DAO.
 *
 * @author Yuri Lukashevich
 * @version 1.0
 * @see User
 * @since JDK1.0
 */
public class SQLUserDAOImpl implements UserDAO {

    private final DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();

    @Override
    public List<User> findAll() throws UserDAOException {

        List<User> users = new ArrayList<>();
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(GET_ALL_USERS)) {

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                User user = SQLUtil.getUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception can't create list of users in findAll()", e);
        }
        return users;
    }

    @Override
    public User findById(Integer id) throws UserDAOException {
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(GET_USER_BY_ID)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return SQLUtil.getUser(rs);
            }
        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception can't find user in findById()", e);
        }
        throw new UserDAOException("No user with ID=" + id + ".");
    }

    @Override
    public boolean add(User user) throws UserDAOException {
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(ADD_NEW_USER)) {

            if (isLoginUsed(user.getLogin())) {
                throw new UsedLoginException("Login is already is use!");
            }

            if (isEmailUsed(user.getEmail())) {
                throw new UsedEmailException("Email is already is use!");
            }

            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getLogin());
            st.setString(4, user.getPassword());
            st.setInt(5, user.getRole().getId());
            st.setBoolean(6, user.getBanned());
            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception during user add()", e);
        }
    }

    @Override
    public boolean delete(Integer id) throws UserDAOException {
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(DELETE_USER_BY_ID)) {
            st.setInt(1, id);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception can't delete user with id=" + id, e);
        }
    }

    @Override
    public boolean update(User user) throws UserDAOException {
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(UPDATE_USER)) {

            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setInt(3, user.getId());
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception during update()", e);
        }
    }

    @Override
    public void updateBanStatus(Integer id) throws UserDAOException {

        final boolean banStatus = getChangedBanStatus(id);

        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(UPDATE_USER_BAN_STATUS)) {

            st.setBoolean(1, banStatus);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception during update Ban status()", e);
        }
    }

    @Override
    public void updateStatus(Integer id) throws UserDAOException {

        final int roleId = getChangedStatus(id);

        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(UPDATE_USER_STATUS)) {

            st.setInt(1, roleId);
            st.setInt(2, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception during update status()", e);
        }
    }

    @Override
    public User getByLoginAndPass(String login, String encodedPass) throws UserDAOException {

        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(GET_USER_BY_LOGIN_AND_PASS)) {

            st.setString(1, login);
            st.setString(2, encodedPass);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return SQLUtil.getUser(rs);
            }

        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception during getByLoginAndPass()", e);
        }
        throw new InvalidLoginOrPasswordException("Invalid login or password!");
    }

    @Override
    public void updatePassword(int id, String newEncodedPass) throws UserDAOException {
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(UPDATE_USER_PASSWORD)) {

            st.setString(1, newEncodedPass);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new UserDAOException("SQL Exception during update password()", e);
        }
    }

    @Override
    public boolean isLoginUsed(String login) throws UserDAOException {
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(GET_USER_BY_LOGIN)) {

            st.setString(1, login);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new UserDAOException("Error during searching user with a such login", e);
        }
        return false;
    }

    @Override
    public boolean isEmailUsed(String email) throws UserDAOException {
        try (ProxyConnection proxyConnection = pool.getConnection();
             ConnectionWrapper con = proxyConnection.getConnectionWrapper();
             PreparedStatement st = con.prepareStatement(GET_USER_BY_EMAIL)) {

            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new UserDAOException("Error during searching user with a such email", e);
        }
        return false;
    }

    private boolean getChangedBanStatus(Integer id) throws UserDAOException {
        final User user = findById(id);
        return !user.getBanned();
    }

    private int getChangedStatus(Integer id) throws UserDAOException {
        final User user = findById(id);
        return user.getRole().getId() == Role.STUDENT.getId() ? Role.TUTOR.getId() : Role.STUDENT.getId();
    }
}