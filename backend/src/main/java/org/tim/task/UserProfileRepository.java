package org.tim.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tim.task.dto.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
