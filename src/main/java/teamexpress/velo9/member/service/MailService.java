package teamexpress.velo9.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import teamexpress.velo9.member.dto.FindInfoDTO;
import teamexpress.velo9.member.dto.MailContentDTO;

@RequiredArgsConstructor
@Service
public class MailService {

	private final MailSender mailSender;

	@Async
	public void sendNumberMail(String email, String number) {
		sendNumber(email, number);
	}

	@Async
	public void sendMailFindId(FindInfoDTO findInfoDTO, String findUsername) {
		MailContentDTO mailContent = getFindMailContent(findInfoDTO, findUsername);
		SimpleMailMessage message = setEmailContent(mailContent);
		mailSender.send(message);
	}

	private void sendNumber(String email, String number) {
		MailContentDTO mailContent = getMailContent(email, number);
		SimpleMailMessage message = setEmailContent(mailContent);
		mailSender.send(message);
	}

	private SimpleMailMessage setEmailContent(MailContentDTO emailContent) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailContent.getAddress());
		message.setSubject(emailContent.getTitle());
		message.setText(emailContent.getMessage());

		return message;
	}

	private MailContentDTO getMailContent(String email, String number) {
		String title = "Velo9 이메일 인증번호입니다.";
		String message = "인증번호 : " + number + " 인증 번호는 3분간 유효합니다.";
		return new MailContentDTO(email, title, message);
	}

	private MailContentDTO getFindMailContent(FindInfoDTO mailDTO, String findUsername) {
		String email = mailDTO.getEmail();
		String subStringName = getSubString(findUsername);
		String title = "Velo9 회원님 아이디 관련 이메일입니다..";
		String message = "회원님의 아이디는 : " + subStringName + " 입니다,";
		return new MailContentDTO(email, title, message);
	}

	private String getSubString(String findUsername) {
		int length = findUsername.length();
		String username = findUsername.substring(0, getEndIndex(length));
		return username + "*".repeat(Math.max(0, getEndIndex(length)));
	}

	private int getEndIndex(int length) {
		return (int) (length / 2.0 + 0.5);
	}
}
