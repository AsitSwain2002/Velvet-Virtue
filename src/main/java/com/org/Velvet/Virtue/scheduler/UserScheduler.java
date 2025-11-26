package com.org.Velvet.Virtue.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.org.Velvet.Virtue.Dto.MailData;
import com.org.Velvet.Virtue.Model.UserVerification;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.UserVerificationRepo;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.Util.CommonUtil;
import com.org.Velvet.Virtue.Util.MailService;

import jakarta.mail.MessagingException;

@EnableScheduling
public class UserScheduler {

	@Autowired
	private UserVerificationRepo userVerificationRepo;
	@Autowired
	private MailService mailService;
	@Value("${app.base.url}")
	private String baseUrl;

	@Scheduled(cron = "0 0 0 * * SAT,SUN")
	public void userVerifyLink() {

		LocalDateTime minusDays = LocalDateTime.now().minusDays(7);
		List<UserVerification> findAllByIsActiveAndCreatedOnBefore = userVerificationRepo
				.findAllByIsActiveAndCreatedOnBefore(false, minusDays);

		List<Users> collect = findAllByIsActiveAndCreatedOnBefore.stream().map(e -> e.getUsers())
				.collect(Collectors.toList());
		collect.stream().forEach(e -> {
			try {
				sentEmail(e, baseUrl);
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	private void sentEmail(Users user, String reqUrl) throws MessagingException {

		String message = "Hi, <b> [[userName]] </b> <br><br>" + "Your Account Created Successfully"
				+ "<br>Click the below link to account verify <br>" + "<a href='[[url]]'>Click here</a> <br><br>"
				+ "If it not you please ignore it" + "<br>" + "Thanks, <br>" + "VelvetVirtue team";
		message = message.replace("[[userName]]", user.getFirstName());
		message = message.replace("[[url]]",
				reqUrl + "/api/v1/user/verify?uId=" + user.getId() + "&Vcode=" + user.getUserVerification().getVCode());

		MailData data = MailData.builder().title("Reminder: Account Create Confirmation").subject("Verify Account")
				.to(user.getEmail()).message(message).build();
		mailService.send(data);

	}

}
