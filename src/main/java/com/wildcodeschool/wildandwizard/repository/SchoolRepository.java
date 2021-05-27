package com.wildcodeschool.wildandwizard.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wildcodeschool.wildandwizard.entity.School;

import util.JdbcUtils;

public class SchoolRepository {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/spring_jdbc_quest?serverTimezone=GMT";
    private final static String DB_USER = "h4rryp0tt3r";
    private final static String DB_PASSWORD = "Horcrux4life!";

    public School save(String name, Long capacity, String country) {

        Connection connection = null;
        PreparedStatement request = null;
        ResultSet generatedKeys = null;
        
        try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			request= connection.prepareStatement("insert into school (name,capacity,country) values (?,?,?);", Statement.RETURN_GENERATED_KEYS);
			request.setString(1, name);
			request.setLong(2, capacity);
			request.setString(3, country);
			
			if (request.executeUpdate() != 1 ) {
				throw new SQLException("failed to insert data");
			}
			
			generatedKeys = request.getGeneratedKeys();
			
			if (generatedKeys.next()) {
				Long id = generatedKeys.getLong(1);
				return new School(id,name,capacity,country);
			} else {
				throw new SQLException("failed to get inserted id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeResultSet(generatedKeys);
			JdbcUtils.closeStatement(request);
			JdbcUtils.closeConnection(connection);
		}
        return null;
    }
}