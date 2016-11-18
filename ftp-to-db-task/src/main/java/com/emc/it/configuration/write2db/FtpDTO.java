package com.emc.it.configuration.write2db;

import java.io.Serializable;

/**
 * 
 * DTO class used to transfer data from <b>CIC</b> to <b>11i</b> database.
 * 
 * @author chaurs
 * 
 */
public class FtpDTO implements Serializable {

	
	private String name;
	private String email;
	private String mobile;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
