package ye.common.web.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.danga.MemCached.MemCachedClient;

import ye.core.web.Constants;

/**
 * 远程Session 存放在Memcached缓存服务器里的Session
 * 
 * @author lx
 *
 */
public class CacheSessionProvider implements SessionProvider {

	@Autowired
	private MemCachedClient memCachedClient;

	private int expiry;

	// 放值
	@Override
	public void setAttribute(HttpServletRequest request, HttpServletResponse response, String name,
			Serializable value) {
		/*
		 * HttpSession session = request.getSession();//true Cookie JSESSIONID
		 * session.setAttribute(name, value);
		 */
//		1、当memCached宕机，所有客户端会重新登录。可以重连memCached
		if (memCachedClient.stats().isEmpty()) {
			// 保存到本机
			HttpSession session = request.getSession();
			if (session != null) {
				session.setAttribute(name, value);
			}
		} else {
			// 保存到Memcached
			Map<String, Serializable> session = new HashMap<String, Serializable>();
			session.put(name, value);
			memCachedClient.set(getSessionId(request, response), session, expiry * 60);
		}
	}

	// 取值
	@SuppressWarnings("unchecked")
	@Override
	public Serializable getAttribute(HttpServletRequest request, HttpServletResponse response, String name) {
		if (memCachedClient.stats().isEmpty()) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				return (Serializable) session.getAttribute(name);
			}
		} else {
			Map<String, Serializable> session = (Map<String, Serializable>) memCachedClient
					.get(getSessionId(request, response));
			if (null != session) {
				return session.get(name);
			}
		}
		return null;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		if (memCachedClient.stats().isEmpty()) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			/* 清空浏览器cookies */
			Cookie c = new Cookie(Constants.SESSION_ID, null);
			c.setMaxAge(0);
			response.addCookie(c);
		} else {
			if (memCachedClient.keyExists(getSessionId(request, response))) {
				memCachedClient.delete(getSessionId(request, response));
			}
		}
	}

	@Override
	public String getSessionId(HttpServletRequest request, HttpServletResponse response) {
		if (memCachedClient.stats().isEmpty()) {
			return request.getRequestedSessionId();
		}
		// 所有的Cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (Constants.SESSION_ID.equals(c.getName())) {
					return c.getValue();
				}
			}
		}
		// 生成一个
		String sessionId = UUID.randomUUID().toString().replaceAll("-", "");
		Cookie cookie = new Cookie(Constants.SESSION_ID, sessionId);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		response.addCookie(cookie);
		return sessionId;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

}
