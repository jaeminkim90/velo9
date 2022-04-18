package teamexpress.velo9.member.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.util.StringUtils;
import teamexpress.velo9.member.dto.MemberLoginDTO;
import teamexpress.velo9.member.security.token.AjaxAuthenticationToken;

public class CustomLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public CustomLoginProcessingFilter() {
		super(new AntPathRequestMatcher("/login"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException, IOException {
		if (isPost(request)) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		MemberLoginDTO memberLoginDTO = objectMapper.readValue(request.getReader(), MemberLoginDTO.class);

		if (StringUtils.isEmpty(memberLoginDTO.getUsername()) || StringUtils.isEmpty(memberLoginDTO.getPassword())) {
			throw new IllegalArgumentException("Username or Password is empty");
		}

		AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(
			memberLoginDTO.getUsername(),
			memberLoginDTO.getPassword());

		return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
	}

	private boolean isPost(HttpServletRequest request) {
		return !request.getMethod().equals("POST");
	}
}
