package isa.jutjubic.service;

import java.util.List;

import isa.jutjubic.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findByName(String name);
}
