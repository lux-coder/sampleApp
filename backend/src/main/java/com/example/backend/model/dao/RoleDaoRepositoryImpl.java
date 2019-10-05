package com.example.backend.model.dao;

import com.example.backend.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

public class RoleDaoRepositoryImpl implements RoleDaoRepository {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FIND_BY_NAME = "SELECT role_id, name FROM role WHERE name = ?";
    private static final String FIND_BY_ID = "SELECT * FROM role WHERE role_id = ?";


    private Connection connection;

    public RoleDaoRepositoryImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public RoleDaoRepositoryImpl(Connection connection){
        this.connection = connection;
    }

    @Override
    public Role findByRoleName(String role) throws SQLException {
        logger.info("In findByRoleName");
        Role role1 = new Role();
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(FIND_BY_NAME);
        preparedStatement.setString(1, role);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.first()){
            logger.info("STATMENT {}", resultSet.toString());
            Integer id = resultSet.getInt("role_id");
            String name = resultSet.getString("name");

            role1.setRoleId(id);
            role1.setName(name);
        }

        return role1;
    }

    @Override
    public Role findById(Integer role_id) {
        logger.info("In findById");
        Role role = new Role();

        try(PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID, role_id);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()){
                Integer id = resultSet.getInt("role_id");
                String name = resultSet.getString("name");

                role.setRoleId(id);
                role.setName(name);
            }

        } catch(SQLException e){
            logger.error("Error occurred {} with stacktrace {} ", e.getMessage(), e);
        }

        logger.info("Found role with name {}", role.getName());
        return role;
    }

    @Override
    public Role save(Role role) {
        return null;
    }
}
