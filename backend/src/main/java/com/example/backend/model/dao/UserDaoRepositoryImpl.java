package com.example.backend.model.dao;

import com.example.backend.model.User;
import com.example.backend.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoRepositoryImpl implements UserDaoRepository{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CREATE_SQL = "INSERT INTO user (username, password, email, firstName, lastName, dateOfBirth) values (?, ?, ?, ?, ?, ?)";
    private static final String CREATE_SQL_WITH_ROLE = "INSERT INTO user (username, password, email, firstName, lastName, dateOfBirth, userRole) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String REGISTER_SQL = "INSERT INTO user (username, password, email, firstName) values (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE user SET email = ?, firstName = ?, lastName = ?, dateOfBirth = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM user WHERE id = ?";
    private static final String CHANGE_PASSWORD_SQL = "UPDATE user SET password = ? WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, username, password, email, firstName, lastName, dateOfBirth, userRole FROM user";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM user WHERE id = ?";
    //private static final String GET_BY_USERNAME = "SELECT id, username, password, email, firstName, lastName, dateOfBirth FROM user WHERE username = ?";
    private static final String GET_BY_USERNAME = "SELECT * FROM user WHERE username = ?";

    private Connection connection;

    public UserDaoRepositoryImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public UserDaoRepositoryImpl(Connection connection){
        this.connection = connection;
    }

    @Override
    public void create(User user) {
        logger.info("In create User");
        if(user != null){
            try (PreparedStatement ps = connection.prepareStatement(CREATE_SQL_WITH_ROLE, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getFirstName());
                ps.setString(5, user.getLastName());
                ps.setDate(6, user.getDateOfBirth());
                ps.setInt(7, user.getUserRole());
                int numRowAffected = ps.executeUpdate();
                try (ResultSet resultSet = ps.getGeneratedKeys()){
                    if (resultSet.next()){
                        user.setId(resultSet.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            logger.info("User created");
        }
    }

    @Override
    public void register(User user) {
        logger.info("In register User");
        if(user != null){
            try (PreparedStatement ps = connection.prepareStatement(REGISTER_SQL, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getFirstName());

                int numRowAffected = ps.executeUpdate();
                try (ResultSet resultSet = ps.getGeneratedKeys()){
                    if (resultSet.next()){
                        user.setId(resultSet.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            logger.info("User registered");
        }

    }

    @Override
    public void updatePassword(Integer id, String encryptedPassword) {
        logger.info("In updatePassword");

        try {
            PreparedStatement statement = connection.prepareStatement(CHANGE_PASSWORD_SQL);
            statement.setString(1, encryptedPassword);
            statement.setInt(2,id);
            int resultSet = statement.executeUpdate();
            logger.info("User password changed");
        } catch (SQLException e){
            logger.error("Exception occurred due to {} with stacktrace {}  ",e.getMessage(), e);
        }

    }

    @Override
    public void update(User user) {
        logger.info("In update");

        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);
            statement.setString(1,user.getEmail());
            statement.setString(2,user.getFirstName());
            statement.setString(3,user.getLastName());
            statement.setDate(4,user.getDateOfBirth());
            statement.setInt(5, user.getId());
            int resultSet = statement.executeUpdate();
            logger.info("User updated");
        } catch (SQLException e){
            logger.error("Exception occurred due to {} with stacktrace {}  ",e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        logger.info("In delete");

        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
            statement.setInt(1,id);
            int resultSet = statement.executeUpdate();
            logger.info("User deleted");
        } catch (SQLException e){
            logger.error("Exception occurred due to {} with stacktrace {}  ",e.getMessage(), e);
        }
    }

    @Override
    public User findById(int id) {
        logger.info("In in findById");
        User user = new User();

        try {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                user.setId(id);
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
            }
            logger.info("User found");
        } catch (SQLException e){
            logger.error("Exception occurred due to {} with stacktrace {}  ",e.getMessage(), e);
        }
        logger.info("User returned {}", user.toString());
        return user;
    }

    @Override
    public List<User> findAll() {
        logger.info("In findAll");
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL) ){
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                Integer userRole = resultSet.getInt("userRole");

                users.add(new User(id, userName, password, email, firstName, lastName, dateOfBirth, userRole));
            }
        } catch (SQLException e){
            logger.error("Exception occurred due to {} with stacktrace {}  ",e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User getByUsername(String username)  {
        logger.info("In getByUsername");
        User user = new User();

        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_USERNAME);
            statement.setString(1,username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("userName"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
                user.setUserRole(resultSet.getInt("userRole"));
            }
            logger.info("User {}", user.toString());
        } catch (SQLException e){
            logger.error("Exception occurred due to {} with stacktrace {}  ",e.getMessage(), e);
        }
        logger.info("User before return {}", user.toString());
        return user;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }
}
