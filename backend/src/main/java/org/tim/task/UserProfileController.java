package org.tim.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tim.task.dto.UserProfile;
import org.tim.task.dto.UserProfileCreationDTO;

@RequiredArgsConstructor
@RestController
public class UserProfileController implements UserProfileAPI {

	private final UserProfileService userProfileService;

	@Override
	public ResponseEntity<UserProfile> getUserProfile(String id) {
		return null;
	}

	@Override
	public ResponseEntity<String> createNewProfile(UserProfileCreationDTO body) {
		return ResponseEntity.ok(userProfileService.createUserProfile(body));
	}


}
