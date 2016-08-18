package ye.common.web.session;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ye.core.web.Constants;

public class HttpSessionProvider implements SessionProvider {

	@Override
	public void setAttribute(HttpServletRequest request, HttpServletResponse response, String name,
			Serializable value) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute(name, value);
		}
	}

	@Override
	public Serializable getAttribute(HttpServletRequest request, HttpServletResponse response, String name) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (Serializable) session.getAttribute(name);
		} else {
			return null;
		}

	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		/* 清空浏览器cookies */
		Cookie c = new Cookie(Constants.SESSION_ID, null);
		c.setMaxAge(0);
		response.addCookie(c);

	}

	@Override
	public String getSessionId(HttpServletRequest request, HttpServletResponse response) {
		// request.getRequestedSessionId();
		// //Http://localhost:8080/html/sfsf.shtml?JESSIONID=ewrqwrq234123412
		return request.getSession().getId();
	}

}
