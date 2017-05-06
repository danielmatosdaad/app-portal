package com.app.cliente.controle.acesso;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextHolderUtil {

	public static String getRemoteAddr() {
		String forwardedFor = getRequest().getHeader("X-Forwarded-For");

		if (forwardedFor!=null && !forwardedFor.isEmpty()) {
			return forwardedFor.split("\\s*,\\s*", 2)[0]; // It's a comma
															// client,proxy1,proxy2,...
		}

		return getRequest().getRemoteAddr();
	}

	public static HttpServletRequest getRequest() {
		return getServletRequestAttributes().getRequest();
	}

	public static ServletRequestAttributes getServletRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	}
}
