package com.orgella.helpers;

import com.orgella.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {

    private Connection connection = null;
    private Statement statement = null;
    private String SQLdriver = "org.postgresql.Driver";
    private String url = "jdbc:postgresql://127.0.0.1:5432/orgella";
    private String user = "postgres";
    private String password = "123";
    private ResultSet resultSet = null;

    public DatabaseAccess() {
    }

    public Connection getConnection(String driver, String url, String user, String password){

        try {
            Class.forName(driver);

            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            System.out.println("Please check JDBC driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check your settings!");
            e.printStackTrace();
        }

//        if(connection != null){
//            //System.out.println("You are connected to database!");
//        } else {
//            //System.out.println("Connection failed!");
//        }

        return connection;

    }

    public ResultSet readData(String query){

        try {

            connection = getConnection(SQLdriver, url, user, password);

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            connection.close();

        } catch (SQLException e) {
            System.out.println("Cannot create a statement");
            e.printStackTrace();
        }

        return resultSet;
    }

    public boolean createData(String query){

        try {
            connection = getConnection(SQLdriver, url, user, password);

            statement = connection.createStatement();
            statement.executeUpdate(query);

            connection.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Cannot create a statement");
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteData(String query) {

        try {
            connection = getConnection(SQLdriver, url, user, password);

            statement = connection.createStatement();
            statement.executeUpdate(query);

            connection.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Cannot create a statement");
            e.printStackTrace();
        }

        return false;
    }


}
