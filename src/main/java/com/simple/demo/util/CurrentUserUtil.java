package com.simple.demo.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.simple.demo.persistence.entity.User;

public class CurrentUserUtil {

	public static User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static String getEmail() {
		return getUser().getEmail();
	}

	public static String getUserId() {
		return getUser().getId();
	}

}