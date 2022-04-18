package teamexpress.velo9.member.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.security.oauth.SessionConst;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication)  {

		Member member = (Member) authentication.getPrincipal();

		HttpSession session = request.getSession();
		session.setAttribute(SessionConst.LOGIN_MEMBER, member.getId());

		response.setStatus(HttpStatus.OK.value());
	}
}
