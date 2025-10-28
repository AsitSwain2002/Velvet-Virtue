package com.org.Velvet.Virtue.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.MailData;
import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.Address;
import com.org.Velvet.Virtue.Model.Roles;
import com.org.Velvet.Virtue.Model.UserVerification;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.AddressRepo;
import com.org.Velvet.Virtue.Repo.RolesRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.Util.MailService;
import com.org.Velvet.Virtue.service.UsersService;
import com.org.Velvet.Virtue.validation.UserValidation;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

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
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private MailService mailService;

	@Override
	public boolean saveUser(UsersDto usersDto, String reqUrl) throws MessagingException {
		// --------- validate user -------
		userValidation.validateUser(usersDto);
		Users user = mapper.map(usersDto, Users.class);
		if (usersDto.getId() != null) {

			// only you can update user not address for address I will create another end
			// point
			updateUser(user);
		}
		UserVerification userVerification = UserVerification.builder().isActive(false)
				.vCode(UUID.randomUUID().toString()).users(user).build();
		user.setUserVerification(userVerification);
		setPassword(user);
		setRole(user.getRoles(), user);
		setAddress(user);
		if (!ObjectUtils.isEmpty(usersRepo.save(user))) {
			sentEmail(user, reqUrl);
			return true;
		}
		return false;
	}

	private void sentEmail(Users user, String reqUrl) throws MessagingException {

		String message = "Hi, <b> [[userName]] </b> <br><br>" + "Your Account Created Successfully"
				+ "<br>Click the below link to account verify <br>" + "<a href='[[url]]'>Click here</a> <br><br>"
				+ "If it not you please ignore it" + "<br>" + "Thanks, <br>" + "VelvetVirtue team";
		message = message.replace("[[userName]]", user.getFirstName());
		message = message.replace("[[url]]",
				reqUrl + "/api/v1/user/verify?uId=" + user.getId() + "&Vcode=" + user.getUserVerification().getVCode());

		MailData data = MailData.builder().title("Account Create Confirmation").subject("Account Creation Sucessfull")
				.to(user.getEmail()).message(message).build();
		mailService.send(data);

	}

	private void setPassword(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
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
