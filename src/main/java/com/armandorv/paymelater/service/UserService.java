package com.armandorv.paymelater.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.armandorv.paymelater.domain.Authority;
import com.armandorv.paymelater.domain.PersistentToken;
import com.armandorv.paymelater.domain.User;
import com.armandorv.paymelater.exception.UserAlreadyExistsException;
import com.armandorv.paymelater.exception.UserNotFoundException;
import com.armandorv.paymelater.repository.AuthorityRepository;
import com.armandorv.paymelater.repository.PersistentTokenRepository;
import com.armandorv.paymelater.repository.UserRepository;
import com.armandorv.paymelater.security.SecurityUtils;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserRepository userRepository;

	@Inject
	private AuthorityRepository authorityRepository;

	@Inject
	private PersistentTokenRepository persistentTokenRepository;

	public void createUser(String login, String firstName, String lastName,
			String email, String password) throws UserAlreadyExistsException {

		log.debug("Creating new user : " + login);

		if (login == null || userRepository.findOne(login) != null) {
			log.info("User creation rejected -> user already exists.");
			throw new UserAlreadyExistsException("User already exists");
		}

		User user = new User();

		user.setLogin(login);
		user.setPassword(passwordEncoder.encode(password));

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);

		user.setAuthorities(new HashSet<Authority>(Arrays
				.asList(authorityRepository.findOne("ROLE_USER"))));

		userRepository.save(user);
		log.debug("Created new user : ", login);
	}

	public void deleteUser(String login) {

		if (userRepository.findOne(login) == null) {
			log.info("User delete operation rejected -> user not found.");
			throw new UserNotFoundException("User not found");
		}

		// TODO avoid users with debts to be deleted
		// TODO Mark charges as cancelled

		userRepository.delete(login);
	}

	public void updateUserInformation(String firstName, String lastName,
			String email) {
		User currentUser = userRepository.findOne(SecurityUtils
				.getCurrentLogin());
		currentUser.setFirstName(firstName);
		currentUser.setLastName(lastName);
		currentUser.setEmail(email);
		userRepository.save(currentUser);
		log.debug("Changed Information for User: {}", currentUser);
	}

	public void changePassword(String password) {
		User currentUser = userRepository.findOne(SecurityUtils
				.getCurrentLogin());
		String encryptedPassword = passwordEncoder.encode(password);
		currentUser.setPassword(encryptedPassword);
		userRepository.save(currentUser);
		log.debug("Changed password for User: {}", currentUser);
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		User currentUser = userRepository.findOne(SecurityUtils
				.getCurrentLogin());
		currentUser.getAuthorities().size(); // eagerly load the association
		return currentUser;
	}

	/**
	 * Persistent Token are used for providing automatic authentication, they
	 * should be automatically deleted after 30 days.
	 * <p/>
	 * <p>
	 * This is scheduled to get fired everyday, at midnight.
	 * </p>
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void removeOldPersistentTokens() {
		LocalDate now = new LocalDate();
		List<PersistentToken> tokens = persistentTokenRepository
				.findByTokenDateBefore(now.minusMonths(1));

		for (PersistentToken token : tokens) {
			log.debug("Deleting token {}", token.getSeries());
			User user = token.getUser();
			user.getPersistentTokens().remove(token);
			persistentTokenRepository.delete(token);
		}
	}
}
