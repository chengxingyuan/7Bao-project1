
package com.wzitech.gamegold.common.enums;

/**
 * 支付类型枚举
 * @author jiyangxin
 * （1：充值中，2：充值成功，3：充值失败）
 *
 */
public enum FundType {
	Recharge(1,"充值中"),
	RechargeSuccessfully(2,"充值成功"),
	RechargeFailed(3,"充值失败"),


	;

    private int code;

	private String name;

	FundType(int code, String name){
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
	public static FundType getTypeByCode(int code){
		for(FundType type : FundType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
	}
}
