package com.example.backend.model.dao;

import com.example.backend.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

public class RoleDaoRepositoryImpl implements RoleDaoRepository {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FIND_BY_NAME = "SELECT role_id, name FROM role where name = ?";


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
    public Role save(Role role) {
        return null;
    }
}
