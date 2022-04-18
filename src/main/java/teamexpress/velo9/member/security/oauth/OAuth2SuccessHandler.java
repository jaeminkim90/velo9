package teamexpress.velo9.member.security.oauth;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import teamexpress.velo9.member.service.MemberService;

public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private MemberService memberService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		String targetUri = determineTargetUrl(request, response, authentication);

		getRedirectStrategy().sendRedirect(request, response, targetUri);
	}

	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		HttpSession session = request.getSession();
		boolean checkResult = memberService.getMemberByEmail(session);
		if (checkResult) {
			String targetUri = "http://localhost:3000/success";
			return UriComponentsBuilder.fromUriString(targetUri).build().toUriString();
		}
		String targetUri = "http://localhost:3000/firstLogin";
		return UriComponentsBuilder.fromUriString(targetUri).build().toUriString();
	}
}
