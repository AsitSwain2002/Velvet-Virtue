package com.org.Velvet.Virtue.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.Address;
import com.org.Velvet.Virtue.Model.Roles;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.AddressRepo;
import com.org.Velvet.Virtue.Repo.RolesRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.service.UsersService;
import com.org.Velvet.Virtue.validation.UserValidation;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private ModelMapper mapper;
	@Autowired
	private AddressRepo addressRepo;
	@Autowired
	private UsersRepo usersRepo;
	@Autowired
	private RolesRepo roleRepo;
	@Autowired
	private UserValidation userValidation;

	@Override
	public boolean saveUser(UsersDto usersDto) {
		// --------- validate user -------
		userValidation.validateUser(usersDto);
		Users user = mapper.map(usersDto, Users.class);
		if (usersDto.getId() != null) {

			// only you can update user not address for address I will create another end
			// point
			updateUser(user); 
		}
		setRole(user.getRoles(), user);
		setAddress(user);
		if (!ObjectUtils.isEmpty(usersRepo.save(user))) {
			return true;
		}
		return false;
	}

	private void updateUser(Users user) {
		Users dbUser = usersRepo.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Invalid Id"));
		user.setAddress(dbUser.getAddress());
	}

	private void setRole(List<Roles> roles, Users user) {

		List<Integer> list = roles.stream().map(e -> e.getId()).toList();
		List<Roles> findAllById = roleRepo.findAllById(list);
		// exception write here
		user.setRoles(findAllById);
	}

	private void setAddress(Users user) {
		user.getAddress().forEach(e -> e.setUsers(user));
	}

	@Override
	public UsersDto findById(Integer id) {
		Users user = usersRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		return mapper.map(user, UsersDto.class);
	}

	@Override
	public List<UsersDto> findAll() {
		List<Users> users = usersRepo.findAll();
		return users.stream().map(e -> mapper.map(e, UsersDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deleteUser(Integer id) {
		Users user = usersRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		user.setDeleted(true);
		usersRepo.save(user);

	}

}
