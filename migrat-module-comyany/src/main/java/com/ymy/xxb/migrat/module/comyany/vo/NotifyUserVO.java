package com.ymy.xxb.migrat.module.comyany.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;
/**
 * 通知人信息 ，数据由Project类信息获取对应系统中的用户信息
 * 
 * @author wangyi .
 */
@Data
public class NotifyUserVO implements Serializable{

	private static final long serialVersionUID = -3587601761431114532L;
	
	/**
	 * Primary Key.
	 */
	private Long id;
	
	/**
	 * 通知人账户，对应各个系统中账号.
	 */
	private String account;
	
	/**
	 * 通知人手机号.
	 */
	private String phone;
	
	/**
	 * 通知人邮箱.
	 */
	private String email;
	
	/**
	 * 是否有效
	 */
	private Integer enabled;
	 
	/**
     * created time.
     */
    private Timestamp dateCreated;
    
    /**
     * created user.
     */
    private String userCreated;

    /**
     * updated time.
     */
    private Timestamp dateUpdated;
    
    /**
     * updated user.
     */
    private String userUpdated;

}
