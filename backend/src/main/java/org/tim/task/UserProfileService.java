package org.tim.task;

import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.tim.task.dto.UserProfile;
import org.tim.task.dto.UserProfileCreationDTO;

@Service
@RequiredArgsConstructor
public class UserProfileService {

	private final UserProfileRepository userProfileRepository;


	public String createUserProfile(UserProfileCreationDTO body) {

		// mappding body to object
		ObjectMapper mapper = new ObjectMapper();


		UserProfile userProfile = UserProfile.builder()
				.name(body.getName())
				.surname(body.getSurname())
				.build();

//			new UserProfile(
//				body.getName(),
//				body.getSurname(),
//				body.getLogin(),
//				body.getPassword()
//		);


		return userProfileRepository.save(userProfile).getId();
	}

}
