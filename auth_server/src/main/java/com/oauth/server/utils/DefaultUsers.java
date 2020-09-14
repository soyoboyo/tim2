package com.oauth.server.utils;

public enum DefaultUsers {

	USER_ADMIN("ROLE_ADMIN", "admin"),
	USER_DEVELOPER("ROLE_DEVELOPER", "prog"),
	USER_TRANSLATOR("ROLE_TRANSLATOR", "tran"),
	USER_ADMIN_DEVELOPER("ROLE_ADMIN_DEVELOPER", "adminProg"),
	USER_ADMIN_TRANSLATOR("ROLE_ADMIN_TRANSLATOR", "adminTran");

	public final String roleType;
	public final String defaultCred;

	DefaultUsers(String roleType, String defaultCred) {
		this.roleType = roleType;
		this.defaultCred = defaultCred;
	}

}
