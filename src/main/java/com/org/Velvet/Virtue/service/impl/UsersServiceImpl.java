package com.org.Velvet.Virtue.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Velvet.Virtue.Dto.DelhiveryResponse;
import com.org.Velvet.Virtue.Dto.MailData;
import com.org.Velvet.Virtue.Dto.ProductDeliveryDto;
import com.org.Velvet.Virtue.Dto.ProductResponse;
import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.ExceptionHandler.ResourceNotFoundException;
import com.org.Velvet.Virtue.Model.ProductDelivery;
import com.org.Velvet.Virtue.Model.Roles;
import com.org.Velvet.Virtue.Model.UserVerification;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.AddressRepo;
import com.org.Velvet.Virtue.Repo.ProductDeliveryRepo;
import com.org.Velvet.Virtue.Repo.RolesRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.Util.MailService;
import com.org.Velvet.Virtue.service.UsersService;
import com.org.Velvet.Virtue.validation.UserValidation;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

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
	private ProductDeliveryRepo deliveryRepo;
	@Autowired
	private UserValidation userValidation;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private MailService mailService;

	@Transactional
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
		UserVerification userVerification = UserVerification.builder().isActive(false).createdOn(LocalDateTime.now())
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

	@Override
	public DelhiveryResponse orders(int pageNumber, int pageSize) {
		Pageable of = PageRequest.of(pageNumber, pageSize);
		Integer userId = CommonUtil.getLoggedUser().getId();
		Users user = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		Page<ProductDelivery> allByUsers = deliveryRepo.findAllByUsers(user, of);
		List<ProductDeliveryDto> list = allByUsers.stream().map(e -> mapper.map(e, ProductDeliveryDto.class)).toList();
		return DelhiveryResponse.builder().productDelhiveryDto(list).totalPage(allByUsers.getTotalPages())
				.pageNumber(allByUsers.getNumber()).pagesize(allByUsers.getSize()).isLastPage(allByUsers.isLast())
				.isfirstPage(allByUsers.isFirst()).totalElement(allByUsers.getTotalElements()).build();
	}

	@Override
	public void forgetPassword(String userName, HttpServletRequest req) throws MessagingException {
		Users user = usersRepo.findByEmail(userName);
		if (!ObjectUtils.isEmpty(user)) {
			String random = UUID.randomUUID().toString();
			user.getUserVerification().setVCode(random);
			usersRepo.save(user);

			String endPoint = "/api/v1/user/verify-password-link?userId=";
			String message = "<b>Hii [[user]],</b><br>"
					+ "We received a request to reset your password. Click the button below to reset it: <br>"
					+ "<a href='[[url]]'>Click Here </a><br>"
					+ "If you didn’t request a password reset, you can safely ignore this email. Your password will not change.<br>"
					+ "If you have any questions, feel free to contact our support team.<br><br>" + "Thanks,<br>"
					+ "VelvetVirtue.com";

			message = message.replace("[[user]]", user.getFirstName());
			message = message.replace("[[url]]", CommonUtil.getUrl(req) + endPoint + user.getId() + "&vCode="
					+ user.getUserVerification().getVCode());
			MailData mailData = MailData.builder().subject("Forget Password").message(message).title("Forget Password")
					.to(user.getEmail()).build();
			mailService.send(mailData);
		}

	}

	@Override
	public boolean passwordReset(int userId, String vCode) {
		Users user = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		if (user.getUserVerification().getVCode().equals(vCode)) {
			user.getUserVerification().setVCode("NULL");
			usersRepo.save(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean resetPassword(String password) {
		Users user = CommonUtil.getLoggedUser();
		user.setPassword(encoder.encode(password));
		usersRepo.save(user);
		return true;
	}

}
