package teamexpress.velo9.member.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamexpress.velo9.member.security.MemberContext;
import teamexpress.velo9.member.security.token.AjaxAuthenticationToken;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		MemberContext memberContext =
			(MemberContext) userDetailsService.loadUserByUsername(username);

		if (!passwordEncoder.matches(password, memberContext.getMember().getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		return new AjaxAuthenticationToken(
			memberContext.getMember(), null, memberContext.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(AjaxAuthenticationToken.class);
	}
}
