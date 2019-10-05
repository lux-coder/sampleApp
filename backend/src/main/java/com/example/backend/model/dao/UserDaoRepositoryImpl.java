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
    private static final String UPDATE_SQL = "";
    private static final String DELETE_SQL = "";
    private static final String FIND_ALL_SQL = "SELECT id, username, password, email, firstName, lastName, dateOfBirth FROM user";
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
    public void update(User user) { }

    @Override
    public void delete(int id) { }

    @Override
    public User findById(int id) {
        return null;
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
    public User getByUsername(String username) throws SQLException {
        logger.info("In getByUsername");

        PreparedStatement statement = null;

            statement = connection.prepareStatement(GET_BY_USERNAME);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.first()){
                try {
                    logger.info("STATMENT {}", resultSet.toString());
                    Integer id = resultSet.getInt("id");
                    username = resultSet.getString("userName");
                    String password =resultSet.getString("password");
                    String email = resultSet.getString("email");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    Date dateOfBirth = resultSet.getDate("dateOfBirth");
                    Integer userRole = resultSet.getInt("userRole");

                    return new User(id,username,password,email,firstName,lastName, dateOfBirth, userRole);
                } catch (Exception e){
                    logger.error("Exception due to {} with stacktrace {}", e.getMessage(), e);
                }
            } else{

                logger.info("NO USER RETURN NULL");
                return null;
            }



            return null;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }
}
