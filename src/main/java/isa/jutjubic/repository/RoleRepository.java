package isa.jutjubic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.jutjubic.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findByName(String name);
}
