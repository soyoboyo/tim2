package com.oauth.server.repositories;

import com.oauth.server.entities.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, Integer> {
}
