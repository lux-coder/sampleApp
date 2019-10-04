package com.example.backend;

import com.example.backend.configuration.WebSecurityConfig;
import com.example.backend.controller.UserController;
import com.example.backend.service.UserService;
import com.example.backend.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest//(classes = {BackendApplication.class})
//@ContextConfiguration(classes = {WebSecurityConfig.class})
public class BackendApplicationTests {

	@MockBean
	WebSecurityConfig webSecurityConfig;

	@MockBean
	UserController userController;

	@MockBean
	UserService userService;

	@Test
	public void contextLoads() {

	}

}
