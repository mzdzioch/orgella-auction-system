package com.orgella.model;

import com.orgella.exceptions.CredentialsToShortException;
import com.orgella.exceptions.LoginNullException;

public class User {

    private int id;
    private String login;
    private String password;

    public User(String login, String password) throws CredentialsToShortException, LoginNullException {
        validateLoginAndPassword(login, password);
        this.login = login;
        this.password = password;
    }

    public User(int id, String login, String password){
        this.id = id;
        this.login = login;
        this.password = password;
    }

    private void validateLoginAndPassword(String login, String password) throws LoginNullException, CredentialsToShortException {
        if (login == null) {
            throw new LoginNullException("Login can't be empty");
        }

        if (password == null) {
            throw new LoginNullException("Login can't be empty");
        }

        if (login.length() < 5) {
            throw new CredentialsToShortException("Login is too short");
        }


        if (password.length() < 5) {
            throw new CredentialsToShortException("Password is too short");
        }
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}






/*
package com.company;

import javax.security.auth.login.LoginException;

public class User {

    private String login;
    private String password;


    public User(String login, String password) throws CredentialsToShortException, LoginNullException {
        this.login = login;
        this.password = password;


        if (login == null) {
            throw new LoginNullException("Login can't be empty");
        }

        if (password == null) {
            throw new LoginNullException("Login can't be empty");
        }

        if (login.length() < 5) {
            throw new CredentialsToShortException("Login is too short");
        }


        if (password.length() < 5) {
            throw new CredentialsToShortException("Password is too short");
        }

    }


    public String getUserId() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
*/

