package com.orgella.repository;

import com.orgella.exceptions.CredentialsToShortException;
import com.orgella.exceptions.LoginExistException;
import com.orgella.exceptions.LoginNullException;
import com.orgella.helpers.DatabaseAccess;
import com.orgella.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRegistry {

    private DatabaseAccess databaseAccess;

    public UserRegistry(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    public void addUser(User user) throws LoginExistException {
        String login = user.getLogin();
        String password = user.getPassword();

        String addUserQuery = "INSERT INTO users (login, password) VALUES('" + login + "', '" + password + "');";

        if (existUser(user))
            throw new LoginExistException("Login exists.");
        else
            databaseAccess.createData(addUserQuery);
    }

    public List<User> getUserList(){

        String allUsersQuery = "SELECT * FROM users;";
        List<User> userList = new ArrayList<>();

        ResultSet resultSet = databaseAccess.readData(allUsersQuery);

        try {
            while(resultSet.next()) {
                userList.add(
                        new User(
                                resultSet.getString("login"),
                                resultSet.getString("password")
                        ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (CredentialsToShortException e) {
            e.printStackTrace();
        } catch (LoginNullException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public int findUserIdByLogin(String userLogin) {

        String selectUsersQuery = "SELECT id FROM users WHERE login='" + userLogin + "';";

        ResultSet rs = databaseAccess.readData(selectUsersQuery);
        int findId = 0;

        try{
            rs.next();
            findId = rs.getInt("id");
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return findId;
    }

    public boolean existUser(User user) {

        return checkIfUserExist(user);

    }

    public User findUserByLogin(String login) {

        User findUser = null;
        String selectUsersQuery = "SELECT * FROM users WHERE login='" + login + "';";

        ResultSet rs = databaseAccess.readData(selectUsersQuery);

        try{
            rs.next();
            findUser = new User(rs.getInt("id"), rs.getString("login"), rs.getString("password"));
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return findUser;
    }

    public boolean checkIfUserExist(User user) {

        User checkUser = null;

        checkUser = findUserByLogin(user.getLogin());

        if (checkUser != null) {
            if (checkUser.equals(user.getLogin())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLoginAndPasswordCorrect(User user) {

        User lookupUser = findUserByLogin(user.getLogin());

        if (lookupUser.equals(user.getLogin()) && lookupUser.equals(user.getPassword())) {
            return true;
        }

        return false;
    }
}
