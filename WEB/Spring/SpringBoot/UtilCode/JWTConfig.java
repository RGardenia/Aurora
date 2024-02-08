package com.zy.service.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description jwt配置类，读取配置文件
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@SuppressWarnings("static-access")
public class JWTConfig {

	/**
	 * 密匙Key
	 */
	public static String secret;

	/**
	 * HeaderKey
	 */
	public static String tokenHeader;

	/**
	 * Token前缀
	 */
	public static String tokenPrefix;

	/**
	 * 过期时间
	 */
	public static Integer expiration;

	/**
	 * 有效时间
	 */
	public static Integer refreshTime;

	/**
	 * 配置白名单
	 */
	public static String antMatchers;

	/**
	 * 将过期时间单位换算成毫秒
	 *
	 * @param expiration 过期时间，单位·秒
	 */
	public void setExpiration(Integer expiration) {
		this.expiration = expiration * 1000;
	}

	/**
	 * 将有效时间单位换算成毫秒
	 *
	 */
	public void setRefreshTime(Integer refreshTime) {
		this.refreshTime = refreshTime * 24 * 60 * 60 * 1000;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}

	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix + " ";
	}

	public void setAntMatchers(String antMatchers) {
		this.antMatchers = antMatchers;
	}

}
