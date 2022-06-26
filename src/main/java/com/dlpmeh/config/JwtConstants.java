package com.dlpmeh.config;

/**
 * Defines constants for JWT authentication such as header name, secret key, and
 * expiration time.
 * 
 * Author: D Mehata
 */

public class JwtConstants {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String SECRET_KEY = "your-256-bit-secret-goes-here-change-it-now!";
	public static final long EXPIRATION_TIME = 86400000;
}
