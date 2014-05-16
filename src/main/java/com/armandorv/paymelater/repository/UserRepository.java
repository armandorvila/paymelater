package com.armandorv.paymelater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.armandorv.paymelater.domain.User;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, String> {
	
	@Query("select u from User u where u.firstName like ?1 or u.lastName like ?1")
	List<User> find(String name);
}
