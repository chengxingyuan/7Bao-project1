
package com.wzitech.gamegold.common.enums;

/**
 * 支付类型枚举
 * @author HeJian
 *
 */
public enum Status {
	PAID(1,"已付款"),
	WAIT_PAYMENT(2,"待付款"),
	check(3,"结单");


    private int code;

	private String name;

	Status(int code, String name){
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

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
	 * 根据code值获取对应的枚举
	 * @param code
	 * @return
	 */
	public static Status getTypeByCode(int code){
		for(Status type : Status.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
	}
}
