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
    private static final String UPDATE_SQL = "";
    private static final String DELETE_SQL = "";
    private static final String FIND_ALL_SQL = "SELECT id, username, password, email, firstName, lastName, dateOfBirth FROM user";

    private Connection connection;

    public UserDaoRepositoryImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public UserDaoRepositoryImpl(Connection connection){
        this.connection = connection;
    }

    @Override
    public void create(User user) {
        if(user != null){
            Set<UserRole> roles = user.getUserRoles();
            try (PreparedStatement ps = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getFirstName());
                ps.setString(5, user.getLastName());
                ps.setDate(6, user.getDateOfBirth());
//                for (UserRole role : roles){
//                    ps.setString(7, role.getRole().toString());
//                }
                int numRowAffected = ps.executeUpdate();
                try (ResultSet resultSet = ps.getGeneratedKeys()){
                    if (resultSet.next()){
                        user.setId(resultSet.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        Set<UserRole> roles = new HashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL) ){
            while (resultSet.next()){
                logger.info(resultSet.toString());
                Integer id = resultSet.getInt("id");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");

                users.add(new User(id, userName, password, email, firstName, lastName, dateOfBirth, null));
            }
        } catch (SQLException e){
            logger.error("Exception occurred due to {} with stacktrace {}  ",e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }
}
