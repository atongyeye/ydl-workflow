# 移动端 API

###  1 入库

- 地址

	/api/mobile/stock/inStorck
	
- 请求方式

	post
	
- 请求参数

	expressCompany：String,快递公司
	shelfNumber：String,货架号
	waybillNumber：String,运单号
	waybillNumbers: String[],关联运单号数组
	customerPhone: String,客户手机号
	noticeStatus:int,通知状态
	status:int,包裹状态(0已录入，1已入库)
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  2 出库

- 地址

	/api/mobile/stock/outStorck
	
- 请求方式

	post
	
- 请求参数

	id：long,主键
	waybillNumber：String,运单号
	waybillNumbers: String[],关联运单号数组
	status:int,包裹状态(2已出库)
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  3 问题件处理(退回快递公司)

- 地址

	/api/mobile/stock/backStorck
	
-  请求方式

	post
	
- 请求参数

	id：long,主键
	waybillNumber：String,运单号
	waybillNumbers: String[],关联运单号数组
	customerPhone: String,客户手机号
	status:int,包裹状态(3已退件)
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  4 更新库单信息

- 地址

	/api/mobile/stock/updateStorck
	
- 请求方式
 
	post
	
- 请求参数

	id：long,主键
	expressCompany：String,快递公司
	waybillNumber：String,运单号
	waybillNumbers: String[],关联运单号数组
	customerPhone: String,客户手机号
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  5 快递移库(货架模式)

- 地址

	/api/mobile/stock/transfStorck
	
- 请求方式

	post
	
- 请求参数

	id：long,主键
	waybillNumber：String,运单号
	shelfNumber：String,货架号
	customerPhone: String,客户手机号
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  6 根据运单号查询库单

- 地址

	/api/mobile/stock/queryStorckByWaybillNumber
	
- 请求方式

	get
	
- 请求参数

	waybillNumber：String,运单号
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  7 根据库单id查询库单

- 地址

	/api/mobile/stock/queryStorckById
	
- 请求方式

	get
	
- 请求参数

	id：long,库单id
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  8 查询库单列表

- 地址

	/api/mobile/stock/queryStorckList
	
- 请求方式

	get
	
- 请求参数

  ```json
  customerPhone：String,客户手机号,
  page: 分页参数。从 0 开始,
  count: 每次请求个数。上限 100 条一次请求
  ```
	
	
- 返回值

	```
	data:[{
		id:long,主键,
		customerPhone:String,客户手机号,
		expressCompany:String,快递公司编码,
		shelfNumber:String,货架号,
		waybillNumber:String,运单号,
		inTime:Date,入库时间,
		outTime:Date,出库时间,
		backTime:Date,退件时间,
		status:int,包裹状态(0:已录入，1:已入库，2:已出库，3:已退件),
		salesmanId:long,业务员id,
		pickNumber:String,取件码
	}],
   count: int, 记录总数
   ```
###  9 寄件管理

- 地址

	/api/mobile/sendermail/addSenderMail
	
- 请求方式

	post
	
- 请求参数

	 waybillNumber：String,运单号
	 waybillNumbers：String[] ,关联运单号数组
    sender：String,寄件人
    senderPhone:String, 寄件人手机号
	 senderAddr:String, 寄件地址
	 receiver：String,收件人
    receiverPhone:String, 收件人手机号
	 receiverAddr: String,收件地址
    freight:double,运费
    weight:double,重量
    payType:int,付款方式
    mailType:int,物品类型
    status:int, 包裹状态(1:待寄出，2:已寄出)
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  10 根据运单号查询寄件

- 地址

	/api/mobile/sendermail/querySenderMailByWaybillNumber
	
- 请求方式

	get
	
- 请求参数

	waybillNumber：String,运单号
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  11 根据寄件id查询寄件

- 地址

	/api/mobile/sendermail/querySenderMailById
	
- 请求方式

	get
	
- 请求参数

	id：long,寄件id
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  12 查询寄件列表

- 地址

	/api/mobile/sendermail/querySenderMailList
	
- 请求方式

	get
	
- 请求参数

	senderPhone：String,寄件人手机号
	
- 返回值

	data:[{
		id:long,主键,
		expressCompany:String,快递公司编码,
		shelfNumber:String,货架号,
		waybillNumber:String,运单号,
		sender：String,寄件人,
		senderPhone:String,寄件人手机号,
		senderAddr：String,寄件人地址,
		receiver：String,收件人,
		receiverPhone:String,收件人手机号,
		receiverAddr：String,收件人地址,
		freight:String,运费,
		weight:String,重量,
		mailType:int,包裹类型,
		payType:int,支付方式,
		status:int,包裹状态(1:待寄出，2:已寄出)
	}]
	
###  13 查询当前登陆账户信息

- 地址

	/api/mobile/user/getCurrentUser
	
- 请求方式

	get
	
- 请求参数

	id：long,用户id
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  14 账户充值

- 地址

	/api/mobile/user/addUserRecharge
	
- 请求方式

	post
	
- 请求参数

	id：long,用户id,
	rechargeType：int,充值方式,
	amount：double,充值金额
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}
	
###  15 密码修改

- 地址

	/api/mobile/user/resetPassword
	
- 请求方式

	post
	
- 请求参数

	id：long,用户id,
	oldPassword：String,旧密码,
	newPassword：String,新密码
	
- 返回值

	{
		data:Object,返回数据,
		code:String,状态码,
		msg:String,提示信息
	}