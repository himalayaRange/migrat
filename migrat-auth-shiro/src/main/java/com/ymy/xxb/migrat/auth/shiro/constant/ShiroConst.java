package com.ymy.xxb.migrat.auth.shiro.constant;
/**
 * ShiroConst
 *
 * @author: wangyi
 *
 */
public class ShiroConst {
	
	/**
	 * The Idempotent prefix in redis
	 */
	public static final String AUTO_IDEMPOTENT_PREFIX = "auto_idempotent_";
	
	/**
	 *  验证码 Session Key
	 */
    public static final String CAPTCHA_PREFIX = "migrat_captcha_";
    
	/**
	 * system initial password
	 */
	public static final String SYSTEM_INITIAL_PASSWORD = "1234qwer";
	
	/**
	 * shiro sessionId key
	 */
	public static final String HEADER_SESSION_ID = "Authorization";
	
	/**
	 * If the method has annotaion @AutoIdempotent
	 * then client ajax request header need AutoIdempotent token 
	 * else will throw exception
	 */
	public static final String HEADER_IDEMPOTENT = "AutoIdempotent";
	
	/***
	 * The shiro custom realm name
	 */
	public static final String SHIRO_CUSTOM_REALM_NAME = "migratPermissionRealm";
	
    /**
     * 散列的算法
     */
    public static final String SHIRO_HASHALGORITHMNAME = "md5";
   
    /**
     * 散列的次数
     */
    public static final int SHIRO_HASHITERATIONS = 3;
	
    /**
     * 用户角色
     */
    public static final String PROFILE_ROLES = "roles";
    
    /**
     * 用户权限
     */
    public static final String PROFILE_PERMS = "perms";
    
	/**
	 * 权限类型: 菜单 type = 1
	 */
	public static final int PERMESSION_TYPE_1 = 0;
	
	/**
	 * 权限类型：按钮 type = 2
	 */
	public static final int PERMESSION_TYPE_2 = 2;
	
	/**
	 * 用户层级：saasAdmin
	 */
	public static final String BS_USER_LEVEL_SAASADMIN = "saasAdmin";
	
	/**
	 * 用户层级：coAdmin
	 */
	public static final String BS_USER_LEVEL_COADMIN = "coAdmin";
	
	/**
	 * 用户层级： user
	 */
	public static final String BS_USER_LEVEL_USER = "user";
	
}
