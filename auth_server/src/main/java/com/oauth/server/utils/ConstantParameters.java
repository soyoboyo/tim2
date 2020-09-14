package com.oauth.server.utils;

public class ConstantParameters {

	public static final Integer SESSION_EXPIRE_SECONDS = 3600;
	public static final String CLIENT_GRANT_TYPES = "password,refresh_token,client_credentials";
	public static final String CLIENT_SCOPE = "read,write";

	public static final String TOKEN_PATH = "/oauth/token";
}
