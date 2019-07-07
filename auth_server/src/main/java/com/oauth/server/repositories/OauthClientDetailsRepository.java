package com.oauth.server.repositories;

import com.oauth.server.entities.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails, String> {
}
