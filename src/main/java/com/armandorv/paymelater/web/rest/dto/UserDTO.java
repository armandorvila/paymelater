package com.armandorv.paymelater.web.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserDTO {

	private String login;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private List<String> roles;

	@JsonCreator
	public UserDTO() {
	}

	public UserDTO(String login, String firstName, String lastName,
			String email, List<String> roles) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roles = roles;
	}

	public String getLogin() {
		return login;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("UserDTO{");
		sb.append("login='").append(login).append('\'');
		sb.append(", firstName='").append(firstName).append('\'');
		sb.append(", lastName='").append(lastName).append('\'');
		sb.append(", email='").append(email).append('\'');
		sb.append(", roles=").append(roles);
		sb.append('}');
		return sb.toString();
	}

}
