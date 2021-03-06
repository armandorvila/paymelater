package com.armandorv.paymelater.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.armandorv.paymelater.Application;
import com.armandorv.paymelater.domain.PersistentToken;
import com.armandorv.paymelater.domain.User;
import com.armandorv.paymelater.exception.UserAlreadyExistsException;
import com.armandorv.paymelater.repository.PersistentTokenRepository;
import com.armandorv.paymelater.repository.UserRepository;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
public class UserServiceTest {

	@Inject
	private PersistentTokenRepository persistentTokenRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserService userService;

	@Test
	public void testRemoveOldPersistentTokens() {
		assertThat(persistentTokenRepository.findAll()).isEmpty();
		User admin = userRepository.findOne("admin");
		generateUserToken(admin, "1111-1111", new LocalDate());
		LocalDate now = new LocalDate();
		generateUserToken(admin, "2222-2222", now.minusDays(32));
		assertThat(persistentTokenRepository.findAll()).hasSize(2);
		userService.removeOldPersistentTokens();
		assertThat(persistentTokenRepository.findAll()).hasSize(1);
	}

	@Transactional
	@Test(expected = UserAlreadyExistsException.class)
	public void testCreateUser() {
		assertThat(userRepository.findAll()).hasSize(2);
		userService.createUser("test", "Test", "Junit", "test@paymelater.com",
				"test");
		assertThat(userRepository.findAll()).hasSize(3);

		assertThat(userRepository.findOne("test")).isNotNull();
		assertThat(userRepository.findOne("test").getAuthorities()).isNotNull();

		userService.createUser("test", "Test", "Junit", "test@paymelater.com",
				"test");
	}


	private void generateUserToken(User user, String tokenSeries,
			LocalDate localDate) {
		PersistentToken token = new PersistentToken();
		token.setSeries(tokenSeries);
		token.setUser(user);
		token.setTokenValue(tokenSeries + "-data");
		token.setTokenDate(localDate);
		token.setIpAddress("127.0.0.1");
		token.setUserAgent("Test agent");
		persistentTokenRepository.saveAndFlush(token);
	}

}
