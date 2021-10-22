package org.tim.task.dto;

import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
public class UserProfile {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String name;
	private String surname;
	private String login;
	private String password;
//	private List<String> phoneNumbers;


	public UserProfile() {
	}

	public UserProfile(String name, String surname, String login, String password) {
		this.name = name;
		this.surname = surname;
		this.login = login;
		this.password = password;
	}


	public String getId() {
		return id;
	}
}
