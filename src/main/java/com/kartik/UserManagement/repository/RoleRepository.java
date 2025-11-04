package com.kartik.UserManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kartik.UserManagement.model.ERole;
import com.kartik.UserManagement.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	Optional<Role> findByName(ERole name);
}
