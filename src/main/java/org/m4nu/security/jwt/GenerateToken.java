package org.m4nu.security.jwt;

import io.smallrye.jwt.build.Jwt;

public class GenerateToken {
	public static String generateToken(String id, String username) {
		return Jwt.issuer("http://m4nu.dev/issuer").groups("user").claim("id", id).claim("username", username).sign();
	}
}
