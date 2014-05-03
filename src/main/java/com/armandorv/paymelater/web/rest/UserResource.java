package com.armandorv.paymelater.web.rest;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.armandorv.paymelater.domain.User;
import com.armandorv.paymelater.repository.UserRepository;
import com.armandorv.paymelater.security.AuthoritiesConstants;
import com.armandorv.paymelater.service.UserService;
import com.armandorv.paymelater.web.rest.dto.UserDTO;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/app")
public class UserResource {

	private final Logger log = LoggerFactory.getLogger(UserResource.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserService userService;

	/**
	 * GET /rest/users/:login -> get the "login" user.
	 */
	@RequestMapping(value = "/rest/users/{login}", method = RequestMethod.GET, produces = "application/json")
	@Timed
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	public User getUser(@PathVariable String login, HttpServletResponse response) {
		log.debug("REST request to get User : {}", login);
		User user = userRepository.findOne(login);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return user;
	}

	/**
	 * GET /rest/users/:login -> get the "login" user.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/public/users", method = RequestMethod.POST, produces = "application/json")
	@Timed
	@RolesAllowed(AuthoritiesConstants.ANONYMOUS)
	public void createUser(@RequestBody UserDTO dto) throws IOException {

		log.info("REST request to create User : {}", dto);

		userService.createUser(dto.getLogin(), dto.getFirstName(),
				dto.getLastName(), dto.getEmail(), dto.getPassword());
		
		log.info("User created ", dto.getLogin());
	}
}
