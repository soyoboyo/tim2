package org.tim.task.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserProfileCreationDTO {

	private String name;
	private String surname;
	private String login;
	private String password;
	private List<String> phoneNumbers;

}
