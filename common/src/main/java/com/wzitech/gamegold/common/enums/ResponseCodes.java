/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ResponseCodes
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-common
 *	作	者：		HeJian
 *	创建时间：	2014-1-12
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-12 下午5:10:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 服务响应码枚举类
 * @author HeJian
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * HeJian 创建于 2014-1-12 下午5:10:36
 * 2017/05/12  liuchanghua         ZW_C_JB_00008 商城增加通货
 */
public enum ResponseCodes {
	//7bao错误返回
	EmptyUserOrNotAccount("Z7B001","当前用户不存在或登录用户信息错误"),
	FundShortage("Z7B002","当前用户资金不足"),
	SignError("Z7B003","签名加密错误"),
	EmptyTransferState("Z7B004","当前充值单信息不存在"),
    NotUniqueZZorderId("Z7B005","主站传送的id不唯一"),
    //返回银行图标上传出错
    NOTALLOWEDUPLOAD("Z7B006","图片尺寸不合格，请选择103*35的图片！"),
    ErrorPayType("Z7B007","错误的充值方式"),
    MoneyZeroErrorType("Z7B008","错误的资金参数"),
    ClosePayOrderError("Z7B009","关闭充值单失败"),


    //参考价库存限制配置
    RepositoryIsEnableWrong("C1011", "配置已经启用"),
    RepositoryIsDisableWrong("C1009", "配置已经禁用"),
    RepositoryIsEmptyIdWrong("C1008", "该配置已经删除"),

    //
    Success("00", "操作成功"),
    TypeMismatchAccessError("10", "类型不匹配异常"),
    UnKnownError("11", "服务器发生异常，请重试"),
    ImageTypeWrong("12", "上传的图片格式不对，必须是jpg格式"),
    Undefined("13", "未定义"),
    UserNotExist("14","7bao不存在改用户"),

    EmptyId("S001", "ID不能为空"),
    IllegalArguments("S002", "系统参数错误"),
    EmptyParams("S003", "参数不能为空"),

    //用户
    NotExistedUser("B1001", "该帐号不存在，请重新登录或注册"),
    WrongPassword("B1002", "帐号登录密码错误，请重新登录"),
    InvalidAuthkey("B1003", "非法的用户认证码，请重新登录"),
    ExitedAccount("B1004", "该帐号名已被占用"),
    ExitedNickName("B1005", "该昵称已被占用"),
    OldPasswordWrong("B1006", "原密码错误"),
    PasswordEmptyWrong("B1007", "输入的原密码或新密码不能为空"),
    UserEmptyWrong("B1008", "用户不能为空"),
    UserIsDisableWrong("B1009", "用户已经禁用"),
    AccountEmptyWrong("B1010", "帐号名不能为空"),
    UserIsEnableWrong("B1011", "用户已经启用"),
    RealNameEmpty("B1012", "真实姓名不能为空"),
    NickNameEmpty("B1013", "昵称不能为空"),
    UserPasswordEmpty("B1014", "输入的密码不能为空"),
    UserTypeEmpty("B1015", "用户类型不能为空"),
    UserIdEmptyWrong("B1016", "用户ID不能为空"),
    UserNotExitedWrong("B1017", "用户不存在"),
    UserIdDisabled("B1019", "该用户已被禁用"),
    UserThumbWrong("B1020", "用户头像的缩略图生成失败"),
    SellerSettingEmptyWrong("B1021", "店铺配置不能为空"),
    NotRegisterMall("B1022", "该帐号还未入驻商城"),

    ErrorFormatName("1006", "用户名不合法"),
    OrigPasswordWrong("1007", "原密码错误"),
    ExistNoWord("1008", "包含不允许注册的词语"),
    ErrorFormatEmail("1009", "Email格式错误"),
    NoRegisEmail("1010", "Email不允许注册"),
    ExistedEmail("1011", "该Email已经被注册"),
    NullPhoneNumber("1012", "手机号码不能为空"),
    WrongVerifyCode("1013", "手机验证码错误"),
    LinkFailure("1014", "该链接已失效或为非法链接"),
    NullNewPassword("1015", "新密码不能为空"),
    BoundExistdMobile("1016", "该手机已被绑定或注册，不能再次被绑定"),
    DisableUser("1017", "该用户已被禁用，无法登录"),
    WrongImageVerifyCode("1018", "图片验证码错误"),
    EmptyUserType("1019", "用户类型不能为空"),
    EmptyLoginAccount("1020", "注册账户不能为空"),
    EmptyPassword("1021", "注册密码不能为空"),
    ErrorSellerUid("1022", "获取用户UID出错"),
    EmptyAlterServiceID("1023", "所选的客服不存在。"),
    /**
     * 我的信息管理
     */
    EmptyCollectionId("2001", "添加收藏，收藏Id不能为空"),
    EmptyCollectionType("2002", "添加收藏，收藏类型不能为空"),
    AlreadyCollected("2003", "已经收藏过该商品或店铺"),
    EmptyComplaintContent("2004", "投诉建议内容不能为空"),
    EmptyDealContent("2005", "处理内容不能为空"),
    EmptyComplaintId("2006", "投诉建议信息Id不能为空"),
    EmptyComplaintCustomerUid("2007", "投诉人Id不能为空"),
    EmptySuggestContent("2008", "建议内容不能为空"),
    EmptyConsult("2009", "咨询内容不能为空"),
    EmptyMobilePhone("2010", "联系方式不能为空"),

    /**
     * 商品管理
     */
    EmptyGoodsInfo("3001", "商品不能为空"),
    EmptyGoodsId("3002", "商品Id不能为空"),
    EmptyUploadImage("3003", "商品图片不能为空"),
    EmptyGoodsTitle("3004", "商品名称不能为空"),
    EmptyGoodsDes("3005", "商品设计说明不能为空"),
    EmptyGoodsPrice("3006", "商品价格不能为空"),
    EmptyGoodsType("3007", "商品类型不能为空"),
    EmptyGameName("3008", "游戏名称不能为空"),
    EmptyRegion("3009", "游戏所在区不能为空"),
    EmptyGameServer("3010", "游戏所在服务器不能为空"),
    EmptyUid("3011", "商品所属用户不能为空"),
    EmptyDiscountInfo("3012", "商品对应折扣不能为空"),
    EmptyGoodsCount("3013", "购买商品数量不能为空"),
    EmptyDiscount("3014", "折扣不能为空"),
    EmptyDiscountId("3015", "折扣Id不能为空"),
    EmptyImageUrls("3016", "商品图片不能为空"),
    EmptyGoodsCat("3017", "商品所属类目不能为空,请把浏览器缓存先清除一下再刷新页面"),
    ExistGoodsCat("3018", "待增加的商品所属类目已经存在"),
    EmptyUploadFile("3019", "上传文件不能为空"),
    ConfigPower("3020", "自动配单权限配置信息有误"),
    EmptyGameId("3021", "游戏id不能为空"),
    EmptyRegionId("3022", "区id不能为空"),
    EmptyServerId("3023", "服id不能为空"),
    EmptyGameRace("3024", "游戏阵营不能为空"),
    EmptyGoodsSeller("3025", "商品所属卖家不能为空,请把浏览器缓存先清除一下再刷新页面"),
    NoGame("3026", "该游戏不存在"),
    NoRegion("3027", "该游戏区不存在"),
    NoServer("3028", "该游戏服不存在"),

    /**********ZW_C_JB_00008_20170516 START ADD*************/
    EmptyWarning("3029", "友情提示不能为空"),
    EmptyWarningId("3030", "友情提示Id不能为空"),
    NoGoodsTypeName("3031", "商品类目不能为空"),
    ExistWarning("3032", "该类型友情提示已存在"),
    GoldCountNoInteger("3033", "库存数量不能小于O且不能为小数"),
    NoGoldCount("3034", "商品数量必须大于0"),
    /*********ZW_C_JB_00008_20170516 END******************/
    ErrorRemoveGoodsByTrading("3035", "商品正在交易中，不能删除"),

    /**
     * 订单管理
     */
    EmptyOrderInfo("4001", "订单不能为空"),
    EmptyOrderDetail("4002", "订单详情不能为空"),
    EmptyOrderState("4003", "订单状态不能为空"),
    EmptyOrderId("4004", "订单号不能为空"),
    EmptyOrderPrice("4005", "订单总金额不能为空"),
    EmptyGoldCount("4006", "金币数不能为空或者小于零"),
    EmptyDeliveryTime("4007", "发货速度不能为空"),
    EmptyTradeType("4008", "交易方式不能为空"),
    RepositoryGoldLessConfigGold("40009", "请选择游戏币数目大于订单游戏币数目的库存"),
    EmptyOrderIsHaveStore("40010", "订单是否无货必须设置"),
    AlreadyCancel("40011", "订单已被取消"),
    ChangeOrderInfoError("40012", "订单状态修改失败，请重试"),
    StateNotChange("40013", "订单状态未改变"),
    StateAfterNotIn("40014", "订单先前状态不是可改变的状态"),
    EmptyRepositoryList("40015", "请选择配置给订单的库存"),
    ConfigGoldNotEqOrderGold("400016", "配置的数量不等于订单的数量"),
    EmptyConfigResult("400017", "配置对象不能为空"),
    EmptyConfigResultId("400018", "配置对象ID不能为空"),
    SomeChildOrderIsDelivery("40019", "有部分子订单已经发货，订单状态不可修改"),
    IllegalGameLevel("40020", "收货角色等级不合法"),
    ReplaceConfigOrderIsDeleted("40021", "该条取消子订单已经重配置过"),
    ConfigResultIsEditor("40022", "子订单已在配置中"),
    notEnoughOrderPrice("40023", "订单总金额不能低于{0}元"),
    OrderIsEditor("40024", "订单已在配置中，请刷新页面"),
    LessGoldCount("40025", "qq三国游戏最少购买量不可以少于1000万"),
    EmptyConfigPower("40026", "自动配单信息不存在"),
    RepositoryGoldNotEnough("40027", "库存游戏币数目不足"),
    ExistMultiConfigResult("40028", "该订单有多个子订单，不能直接移交，请提供子订单号"),
    NotFoundOrder("40029", "未找到订单"),
    TransferOrderFailed("40030", "移交失败，只能移交担保的订单"),
    ConfigOrderIsDelivery("40031", "子订单已发货"),
	LessThanTimeCantRefund("40032", "刚配单成功，30秒内不能退款"),
	NotYourOrder("40033", "不是您的订单"),
	TransferOrderFailed2("40034", "移交失败，只能移交寄售的订单"),
	EmptyRefundReason("40035", "退款原因不能为空"),
	NotJsRobotOrder("40036", "不是寄售全自动的订单"),
	RefundFailed("40037", "退款失败，请去酷宝后台查询确认下是否已经被退款"),
	UseShopCouponCantResetConfigOrder("40038", "使用了店铺券，该子订单不允许重配"),
	UseShopCouponCantUseOtherSellerRepository("40039", "使用了店铺券，不能配其他卖家的库存"),
	ErrorDeliveryType("40040", "当前订单不支持该收货模式"),
	LimitConfigurationIsNotOpen("40041","金币商城对应的库存限制配置禁用"),
	ExceedOrderMaxPrice("40042","金币订单总金额不能大于999999元"),
	ConfigPowerIsExist("40043","配单信息已存在"),////ZW_C_JB_00008_20170512 ADD
	notEnoughOrderPriceTZS("40044", "挑战书订单总金额不能低于50元"),
	IllegalGameNumberID("40045","收货角色数字ID不合法"),
    OrderType("40046","订单类型不能为空"),
	/**
	 * 资金管理
	 */
	EmptyDividedDetail("5001", "分成信息不能设为空"),
	EmptyDividedDetailUid("5002", "所属用户不能为空"),
	EmptyDividedDetailGoodsId("5003", "商品Id不能为空"),
	EmptyDividedDetailOrderId("5004", "订单号不能为空"),
	EmptyDividedFunds("5005", "分成不能为空"),
	EmptyBelongYearMonth("5006", "分成所属月不能为空"),
	EmptyDividedDetailUidAndId("5007", "所属用户ID和资金分成ID至少一个不能为空"),
	EmptyExportStateInfo("5008","导出信息不能为空"),
	ExportBusy("5009","系统正忙,请稍后再执行此操作"),
	ErrorRechargeType("5010","充值渠道为空或非法充值渠道"),
	EmptyPayOrderId("5011","充值订单号不能为空"),
    ErrorRechargeAmount("5012","充值金额为空或不合法"),

	/**
	 * 收货地址管理
	 */
	EmptyProvince("6001", "省不能设为空"),
	EmptyCity("6002", "市不能设为空"),
	EmptyDistrict("6003", "区不能设为空"),
	EmptyAddress("6004", "街道不能设为空"),
	EmptyReceiver("6005", "收货人不能为空"),
	EmptyPhone("6006", "联系电话不能为空"),
	EmptyQQ("6007", "QQ联系方式不能为空"),
	EmptyDeliveryType("6008", "收货模式不能为空"),
	
	/**
	 * 客服管理
	 */
	EmptyServicer("7001", "客服信息不能为空"),
	EmptyServicerId("7002", "客服Id不能为空"),
    EmptyServiceQq("7003", "客服QQ不能为空"),
	
	/**
	 * 库存管理
	 */
	EmptyRepository("8001", "库存信息不能为空"),
	EmptyUnitPrice("8002", "单价不能为空"),
	EmptyGameAccount("8003","游戏账号不能为空"),
	EmptyGamePassWord("8004","游戏密码不能为空"),
	EmptySonAccount("8005","子账号不能为空"),
	EmptyPasspod("8006","密保卡不能为空"),
	EmptyRepositoryFile("8007","上传的库存文件为空"),
	ReadRepositoryFileError("8008","读取上传的库存文件失败"),
	EmptyConfigState("8009","配置状态不能为空"),
	EmptyRepositoryGold("8010","配置的库存量不能小于0"),
	RepositoryGoldLessZero("8011","第%s行的库存数量为负数"),
	EmptypriceFile("80012","上传的价格文件为空"),
	ReadpriceFileError("80013","读取上传的价格文件失败"),
    LowCapacity("80014", "库存数量不足，请刷新页面获取最新库存数量"),
    InventoryShortage("80015", "库存数量不足"),
    UnitPriceMustGreaterThanZero("80016", "库存单价必须大于0"),
    EmptyRepositoryId("800017", "库存信息ID不能为空"),
    EmptySellableGoldCount("800018", "可销售库存不能为空或者小于0"),
    ErrorRepositoryTemplate("800019", "库存模板格式有误,请重新下载对应游戏的库存模板"),
    EmptySecondaryPwd("800020", "二级密码不能为空"),
    EmptyWarehousePwd("800021", "仓库密码不能为空"),
    ExistSameRepository("800022", "存在相同的库存记录"),
    /*************ZW_C_JB_00008_20170514 ADD START**************/
    QeuryGameRaceInfoError("800023", "查询阵营信息IO异常"),
    QeuryGameRaceInfoParseXMLError("800024", "查询阵营XML格式转换异常"),
    GoldCountLessThanSellenbelCount("80025", "修改的商品数目不能小于可售库存"),
    /*************ZW_C_JB_00008_20170514 ADD END**************/

    /**
     * 游戏交易信息
     */
    EmptyPlaceName("9001", "游戏交易地点不能为空"),
    EmptyTradePlace("9002", "游戏交易地点信息不能为空"),
    EmptyTradePlaceId("9003", "游戏交易地点信息ID不能为空"),
    EmptyMailTime("9004", "邮寄时间不能为空"),
    EmptySellerGameRole("9005", "卖家游戏角色名不能为空"),
    EmptyMoneyName("9006", "卖家所售金币名不能为空"),
    EmptyAutoPlayTime("9007", "自动打款时间不能为空"),
    EmptyCategoryValue("9008", "商品类目不能为空"),//ADD
    NoCategoryrepeat("9009", "相同游戏和商品类型不能重复添加"),//ADD

    /**
     * 卖家信息
     */
    EmptySellerInfo("10001", "卖家不能为空或卖家信息不存在"),
    EmptySellerId("10002", "卖家ID不能为空"),
    EmptySellerName("10003", "联系人不能为空"),
    EmptySellerQQ("10004", "卖家QQ不能为空"),
    ExitSeller("10005", "该卖家信息已存在"),
    EmptySellerType("10006", "卖家类型不能为空"),
    SellerUnAudited("10007", "卖家状态为待审核"),
    SellerUnPassAudited("10008", "卖家状态为审核未通过"),
    SellerDidNotOpenHelperPerm("10009", "卖家未开通小助手权限"),
    SellerNoOpenSh("10010", "卖家未开通收货"),
    SellerNoPriceRob("10011", "卖家未开通自动更新价格权限"),
    SellerWarningMoney("10012", "卖家可用资金不足"),
    SellerMinMoney("10013", "不能小于最小充值限额"),
    SellerMaxMoney("10014", "单次支付金额过大，请分次操作"),
    NoPurchaseData("10015", "不存在采购商数据，请联系管理员"),


    EmptyLogModule("11001", "日志模块名不能为空"),
    EmptyLogOperateInfo("11002", "日志操作内容不能为空"),

    /**
     * 卖家选择的游戏信息
     */
    EmptySellerGame("12001", "请填写您入驻的游戏"),

    /**
     * 投票信息
     */
    EmptyIpAddress("13001", "IP地址不能为空"),
    VotedUserId("13002", "您已经投过票"),
    VotingHasNotStarted("13003", "投票还没有开始"),
    VotingHasEnded("13004", "投票已经结束"),
    EmptyVoteConfig("13005", "投票配置信息为空"),
    MultiVoteConfig("13006", "存在多条投票配置信息"),
    VoteNoOrder("13007", "只有购买过商城订单的用户才能参与本次活动(已发货的订单)"),


    /**
     * 商城游戏币购买数量配置
     */
    EmptyGoldCounts("14001", "游戏币购买数量不能为空"),
    IllegalGoldCounts("14002", "游戏币购买数量参数错误，多个以逗号分隔，如：1000,5000,10000"),
    EmptyCategoryIcon1("14003", "安心买热卖商品栏目1图标不能为空"),
    IllegalCategoryIcon("14004", "热卖商品图标参数错误，多个以逗号分隔，如：1,2,3,4"),
    MultiHotRecommendConfig("14005", "该游戏已经存在配置记录，请不要重复添加"),


    /**
     * 发送短信
     */
    SendMessageErr("15001", "发送短信出错了"),


    EmptyTraderList("16001", "当前没有寄售物服可以安排上号交易"),

    /**
     * 保险功能
     */
    RateCannotBeZero("17001", "保险收费比率不能为零"),

    /**
     * 评价功能
     */
    AlreadyEvaluate("18001", "不允许重复评价"),
    AlreadyReEvaluate("18002", "不允许重复追加评价"),
    IllegalEvaluateScore("18003", "无效的评价分数"),
    IllegalOrderStateCantEvaluate("18004", "该订单不能进行评价"),
    CantReEvaluate("18005", "还未评价，不能追加评价"),
    MoreThan24HourCantReEvaluate("18006", "超过24小时，不能追加评价"),
    MoreThan4HourCantEvaluate("18007", "超过4小时，不能进行评价"),


    IllegalCoupon("19001", "无效的优惠券"),
    DeadlineCoupon("19002", "优惠券已过期"),
    ErrorHbPwdCoupon("19003", "红包密码错误"),
    ErrorDpPwdCoupon("19004", "店铺券密码错误"),
    UsedCoupon("19005", "该优惠券已被使用"),
    UnEnableUseCoupon("19006", "购买金额小于100元，不能使用优惠券(注：购买金额不包含商品保障险费用)"),
    EnableUseCouponLevel1("19007", "购买金额小于250元，不能使用5元或10元优惠券(注：购买金额不包含商品保障险费用)"),
    EnableUseCouponLevel2("19008", "购买金额小于500元，不能使用10元优惠券(注：购买金额不包含商品保障险费用)"),
    NoCoupon("19009", "商城暂时没有该面值的优惠券"),

    CantUseOffline("20001", "1个小时内不能多次使用下线功能"),


    /**
     * 收货功能
     */
    EmptyType("21001", "类型不能为空"),
    EmptyReceivingCount("21002", "收货数量不能为空或小于等于零"),
    EmptySign("21003", "签名不能为空"),
    InvalidSign("21004", "无效的签名"),
    EmptyStatus("21005", "状态不能为空"),
    EmptyPurchaseOrderId("21006", "采购单ID不能为空"),
    EmptyDeliveryCount("21007", "出货数量不能为空或小于0"),
    EmptyRoleName("21008", "游戏角色名不能为空"),
    EmptyPurchaseOrder("21009", "采购单不存在或已下架"),
    DeliveryCountGtPurchaseCount("21010", "出货数量不能大于采购数量"),
    DeliveryCountLtPurchaseCount("21011", "出货数量不能小于单笔最小收货量"),
    NotInMultiplesOf1000("21012", "出货数量必须是1000的整数倍"),
    EmptyChId("21013", "出货单ID不能为空"),
    CannotCancelOrderRealCountGtZero("21014", "实际出货数量大于0，不能撤单"),
    CannotCompletePartRealCountGtCount("21015", "不能部分完单，收货数量大于出货数量"),
    AccessDeliveryOrderPermissionDenied("21016", "不是您的订单，您没有权限访问"),
    NotExistSplitRepoOrder("21017", "不存在该分仓订单"),
    PayAmountMustGtZero("21018", "支付金额必须大于零"),
    OrderStatusHasChangedError("21019", "订单状态已经被改变，操作失败"),
    NotExistPayOrder("21020", "不存在该支付订单"),
    NotFindPurchaser("21021", "未找到采购商入驻信息"),
    PurchaserFundIsNotEnough("21022", "收货商收货资金不足，等资金充足再下单吧"),
    OrderInTradingCantPay("21023", "订单未交易完成，不能进行付款"),
    FundIsNotEnough("21024", "收货商资金异常"),
    ConfigAmountIsNotEqualPayAmount("21025", "配单金额不等于付款金额"),
    NotFoundPayDetails("21026", "没有找到付款明细记录"),
    NotFoundSettlement("21027", "没有找到资金结算记录，请先进行结算再付款"),
    ExistsRefundOrderNotPorcess("21028", "该支付单有未处理的申请退款请求，请耐心等待"),
    NotManualIntervention("21029", "只有交易中或人工介入的订单才能人工完单"),
    ErrorOrderStatusCantCancelOrder("21030", "当前订单状态，不能进行撤单"),
    ZeroCantCompletePartOrder("21031", "出货数量为0不能部分完单"),
    RefundIsFail("21032", "退款失败"),
    NoSubOrder("21033", "该子订单不存在,或已完单"),
    UnknowType("21034", "不能识别的类型"),
    NoDeliveryOrder("21035", "出货单不存在"),
    SubOrderNotFinishCannotAssignRole("21036", "该订单还有子订单未完单，不能分配收货角色"),
    EmptySubOrderId("21037", "收货子订单ID不能为空"),
    SubOrderNotDelivery("21038", "该子订单不是已发货状态，不能确认收货"),
    SubOrderNotDeliveryNotCancel("21038", "该子订单不是未发货或者已发货状态，不能撤消订单"),
    SubOrderNotWaitDeliveryNotConfirm("21038", "该子订单不是待发货状态，不能确认发货"),
    UnknowShOrderType("21039", "不能识别的收货GTR回传订单类型"),
    SubOrderNotOvertimeNotCancel("21039", "该子订单未超过30分钟，不能申请撤单"),
    DeliveryOrderNotWAIT_RECEIVENotConfirm("21040", "该主订单状态不是待确认收货状态,不能确认收货"),
    CompleteOrder("21041", "该订单已结单"),
    EmptyMachineArtificialDeliveryOrder("21042", "该机器转人工订单不存在"),
    EmptyMachineArtificialDeliverySubOrder("21043", "该机器转人工订单的子订单不存在"),
    TroppoMachineArtificialDeliverySubOrder("21044", "该机器转人工订单有多个子订单"),
    NotTurnedMachineArtificialError("21045", "不是转人工失败的订单不能进行寄售客服分配"),
    ErrorOrderStatusDistributionService("21046", "当前订单状态，不能进行寄售客服分配"),
    ErrorConsignmentService("21047", "该寄售客服信息不完整无法进行分配"),
    NullConsignmentService("21048", "没有在线的寄售客服"),


    NoZeroLevelSh("30001", "等级不能小于0"),
    EmptyShRoleName("30002", "收货角色不能为空"),
    EmptyIsShRole("30003", "是否收货角色不能为空"),
    ValueIsShRole("30003", "是否收货角色只能填是或否"),
    ShGoldCount("30004", "收货数量必须大于0"),
    ShUnitPriceMustGreaterThanZero("30005", "收货单价必须大于0"),
    EmptyShGameAccount("30006", "收货信息不能为空"),
    EmptyShCount("30007", "收货数量不能为空或者小于零"),
    EmptyLevl("30008", "等级不能为空或者小于零"),
    NullShCount("30009", "收货数量不能为空"),
    NullShPrice("30010", "发布单价不能为空"),
    NullLevelSh("30011", "等级不能为空"),
    IntMultiple("30012", "采购数量只能是1000的整数倍"),
    EmptyShPurchaseOrder("30013", "该采购单对象为空"),
    UpdateNoRecord("30014", "没有更新数据"),
    SaveFaile("30015", "保存失败"),
    Over30Days("30016", "订单时间间隔不能超过30天"),
    //退款
    CanNotRefund("30017", "有交易中的订单，无法申请退款操作"),
    OutOfReasonLength("30018", "退款原因不能超过100个字符"),
    NullOfReason("30019", "退款原因不能为空"),
    EmptyShGamecount("30020", "采购数量不能为空或小于0"),

    FreezeException("30021","冻结/解冻资金金额异常"),

    /**
     * 收货商游戏配置
     */
    EmptyPurchaseId("31001", "收货商id不能为空"),
    EmptyPurchaseAccount("31002", "收货商账户不存在"),
    EmptyGoodsTypeId("31004", "交易类目未配置，请联系客服"),
    EmptyGoodsTypeName("31005", "交易类目名称不能为空"),
    EmptyDeliveryTypeId("31006", "请选择一种收货模式"),
    EmptyDeliveryTypeName("31007", "收货模式名称不能为空"),
    EmptyTradeTypeId("31008", "请至少选择一种交易方式！"),
    EmptyTradeTypeName("31009", "交易方式名称不能为空"),
    NotAvailableConfig("31010", "暂无可用游戏配置"),
    EmptyPurchaserGameConfig("31011", "该游戏项下此商品类目未开通"),
    ExistPurchaserGameConfig("31012", "该游戏配置数据已存在"),
    NoPurchaseConfig("31013", "您未开通该游戏当前商品类目的收货"),

    /**
     * 聊天记录
     */
    OrderLogId("40001", "订单ID不能为空"),
    OrderLogIdInvalid("40002", "订单ID无效"),
    userTypeError("40003", "用户没有权限返问"),


    /**
     * 主游戏配置项目
     */
    NotAvailableGameConfig("50001", "当前无可用游戏配置项"),
    GameCategoryConfig("50002", "交易类目重复"),
    MainGameConfigExist("50003", "当前游戏配置项已经在"),

    /**
     * 金币商城打通
     */
    UnitPriceHasChanged("50004", "单价已发生改变"),
    NullUnitPrice("50005", "配置单价不能为空"),
    NullTotalCount("50006", "配置总库存不能为空"),
    NotMatchConfig("50007", "当前配置尚未打通"),
    NotAvaliableRegionAndServer("50008", "当前游戏无对应区服可用"),
    NullGameNameAndRegionAndServer("50009", "游戏名、游戏区、游戏服不能为空"),
    IllegalGoldCount("50010", "游戏币购买数量低于最低限制"),
    IllegalConfigData("50011", "配置数据异常"),
    NullData("50012", "参数异常"),
    TooMuchTotalAccount("50013", "成交总价不可超过100万元"),

    /**
     * 出货系统配置
     */
    EmptySystemConfig("60001", "出货系统配置为空"),
    NullConfigValue("60002", "采购商最小余额限制未配置"),

    /**
     * 转人工配置
     */
    NullMachineArtificialConfigId("70001", "机器转人工配置id为空"),
    MachineNotGameName("70002", "没有此游戏的开关配置"),
    MachineNotOpen("70003", "此游戏没有开启分配物服"),
    GetRcUserLose("70004", "获取物服失败"),
    CreatSubOrderLosr("70005", "创建子订单失败"),
    NoCreatLog("70006", "无法打印日志"),
    NofinishOrder("70007", "异常转人工订单无法操作"),
    OrderToCompletePart("70008", "订单转部分完单"),


    NoCity("80001", "获取不到城市信息"),
    NoProvince("80002", "获取不到城市信息"),
    /**
     * 商城资金
     */
    FeeError("90001", "提现金额不合法"),
    NotEnoughFee("90002", "可提现金额不足,如您需全部提取,请先联系客服关闭收货功能"),
    NullBankNameError("90003", "银行名称不能为空"),
    NullAccountTypeError("90004", "卡类型不能为空"),
    NullAccountPropError("90005", "账号属性不能为空"),
    NullAccountNOError("90006", "银行账号不能为空"),
    NullAccountName("90007", "开户人姓名必须与当前登录用户姓名一致"),
    AnalysisError("90008","解析失败"),
    FailToQueryTenPay("90009","查询支付单失败"),
    NotEqual("90010","金币商城可用金额与zbao平台余额不一致,请联系客服协商解决"),
    FailToCreateConnect("90011","创建连接失败"),
    FailToSetContent("90012","设置返回参数失败"),
    FailToUpdatePurchaseData("90013","商城同步可用余额失败"),
    FailToConntectCaiFuTong("90014","连接财付通失败"),
    FailToGetContentFromCaiFuTong("90015","获取财付通数据失败"),
    CompanyAccountNeedsMoreInfo("90016","公司账户提现信息缺失"),
    FailToGetFundDetailMsg("90017","获取提现资金详情失败"),
    NullResponseFromGameGold("90018","同步金币商城返回数据为空,或因连接失败导致"),
    NullResponseFromGameGoldCrossingSelect("90019","查询金币商城返回数据为空,或因连接失败导致"),
    NullResponseFromGameGoldDeletePayOrder("90020","因提现成功删除采购单,获取金币商城反馈信息失败");

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回码说明
     */
    private String message;

    ResponseCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过code获取对应的ResponseCodes
     * @param code 错误码
     * @return 响应码对应的ResponseCodes枚举
     */
    public static ResponseCodes getResponseByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            throw new NullPointerException("响应编码为空");
        }

        for (ResponseCodes responseCode : ResponseCodes.values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的ResponseCodes:" + code);
    }

    /**
     * 获取响应编码
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 获取编码对应消息
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
