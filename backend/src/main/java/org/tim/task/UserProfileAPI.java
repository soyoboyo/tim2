package org.tim.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim.task.dto.UserProfile;
import org.tim.task.dto.UserProfileCreationDTO;

@RequestMapping("/user")
public interface UserProfileAPI {

	// GET 		/user/get/{id}
	// POST 	/user	body: {}
	// PUT		/user	body: {}
	// DELETE 	/user/delete/{id}
	// PUT 		/user/addNewPhone	body: {}

	@GetMapping("/get/{id}")
	ResponseEntity<UserProfile> getUserProfile(@PathVariable String id);

	@PostMapping()
	ResponseEntity<String> createNewProfile(@RequestBody UserProfileCreationDTO body);

}
