
package com.wzitech.gamegold.common.enums;

/**
 * 支付类型枚举
 * @author HeJian
 *
 */
public enum Type {
	withdraw(1,"提现"),
	Recharge(2,"充值"),
	Received(3,"收出");


    private int code;

	private String name;

	Type(int code, String name){
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
	public static Type getTypeByCode(int code){
		for(Type type : Type.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
	}
}
