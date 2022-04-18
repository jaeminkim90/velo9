package teamexpress.velo9.common.controller;

import javax.servlet.http.HttpSession;
import teamexpress.velo9.member.security.oauth.SessionConst;

public class BaseController {

	protected Long getMemberId(HttpSession session) {
		Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
		if (memberId == null) {
			throw new NullPointerException("로그인 되어있지 않습니다.");
		}
		return memberId;
	}
}
