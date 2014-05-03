package com.armandorv.paymelater.web.rest;

import java.io.IOException;

import com.armandorv.paymelater.Application;
import com.armandorv.paymelater.repository.UserRepository;
import com.armandorv.paymelater.service.UserService;
import com.armandorv.paymelater.web.rest.dto.UserDTO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
public class UserResourceTest {

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UserService userService;

    private MockMvc restUserMockMvc;

    @Before
    public void setup() {
        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(userResource, "userService", userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Test
    public void testCreateUser() throws IOException, Exception{
    	
    	UserDTO user = new UserDTO();
    	user.setEmail("armando@gmail.com");
    	user.setFirstName("Armando");
    	user.setLastName("Ramírez");
    	user.setPassword("secret");
    	user.setLogin("armandorv");
    	
    	restUserMockMvc.perform(
    					post("/app/rest/public/users").contentType(
    							TestUtil.APPLICATION_JSON_UTF8).content(
    							TestUtil.convertObjectToJsonBytes(user))).andExpect(
    					status().isOk());
    }
    
    @Test
    public void testGetExistingUser() throws Exception {
        restUserMockMvc.perform(get("/app/rest/users/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lastName").value("Administrator"));
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/app/rest/users/unknown")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
