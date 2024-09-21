package com.main.apigateway.repository;

import com.main.apigateway.models.Role;
import com.main.apigateway.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole eRole);

}
