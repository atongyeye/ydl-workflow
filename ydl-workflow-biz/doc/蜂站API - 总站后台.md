## 蜂站 - 总站后台 API

### 信息

- 版本

  0.1.0

- 作者

  Allen.Wu (三爷)

- 创建时间

  2018.10.02

- 修改时间

  2018.10.08


### 说明

#### CDN Domain

TODO: 待补充

#### API Domain

- 正式环境

  TODO:

- 测试环境

  TODO:

#### 数据传输格式

```
如无特殊说明，均采用 JSON 作为数据传输格式
```

#### Response

此处定义返回信息所共有的，在每个API中描述data字段返回数据

```json
{
    code: 返回码. 200为正常，系统错误码同Http Status. 业务错误码为 4 位数。
    err: 错误信息，如发生错误，会带上英文描述信息。成功即为null
    data: Object. 每个API实际返回的数据，可以是{}，也可以是列表 []，也可以是int，string等基本类型
}
```



### 错误码

```
TODO: 待补充
```



### 验证码

- URL

  ```
  /api/backend/captcha?mobile=&type
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  mobile: string, 手机号
  from: string, 站点。 可选值: "base"总站后台 | "site"站点后台
  type: TODO:待定
  ```

- Response

  ```
  null. (测试服务返回验证码 - string)
  ```



### 字典模块

#### 角色字典

TODO:

#### 部门字典

TODO:

#### 站点字典

TODO: province, city, area 搜索

#### 短信模板字典

TODO:



### 账号模块

#### 登录

- [ ] 完成

- URL

  ```
  /api/backend/account/login
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  userName: string, 账号
  password: string, 密码
  from: string, 站点。 可选值: "base"总站后台 | "site"站点后台
  rememberMe: boolean, 是否自动登录。可选值: true | false
  ```

- Response

  ```json
  accountId: long, 账号Id,
  userName: string, 用户名
  nick: string, 昵称
  avatar: string, 头像
  realName: string, 真实姓名
  mobile: string, 手机号
  roleName: 所属角色名称
  perms: [ // 资源列表
  	{
          menuId: long, 菜单Id,
          name: string, 菜单名称,
          parentId: long, 父菜单Id,
          rootId: long, 根菜单Id,
          canRead: boolean, 是否可查询
          canEdit: boolean, 是否可编辑
          canDelete: boolean, 是否可删除
  	},
  	...
  ],
  department: {
      departmentId: long, 部门Id,
      name: string, 部门名称
  }
  ```

#### 登出

- [ ] 完成

- URL

  ```
  /api/backend/account/logout
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  null
  ```

- Response

  ```
  null
  ```

#### 忘记密码

TODO: 待定

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/account/list?page=&count=
  ```

- Method

  ```
  GET
  ```

- Request

  ```json
  page: 分页参数。从 0 开始
  count: 每次请求个数。上限 100 条一次请求
  ```

- Response

  ```
  accounts: [
      {
          accountId: long, 账号Id,
          username: string, 账号,
          name: string, 名字,
          departmentName: string, 部门名称
          roleName: string, 角色名称
      },
      ...
  ],
  count: int, 记录总数
  ```

#### 详情

- [ ] 完成

- URL

  ```
  /api/backend/account/detail?accountId=
  ```

- Method

  ```
  GET
  ```

- Request

  ```json
  accountId: long, 账号Id
  ```

- Response

  ```
  name: string, 姓名
  deparmentId: long, 部门Id
  roleId: long, 角色Id
  username: string, 账号
  password: string, 密码
  ```

#### 新增

- [ ] 完成

- URL

  ```
  /api/backend/account/add
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  name: string, 姓名
  deparmentId: long, 部门Id
  roleId: long, 角色Id
  username: string, 账号
  password: string, 密码
  ```

- Response

  ```
  null
  ```

#### 修改

- [ ] 完成

- URL

  ```
  /api/backend/account/modify
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  accountId: long, 账号Id
  name: string, 姓名
  deparmentId: long, 部门Id
  roleId: long, 角色Id
  username: string, 账号
  password: string, 密码
  ```

- Response

  ```
  null
  ```

#### 禁用

- [ ] 完成

- URL

  ```
  /api/backend/account/forbidden
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  accountId: long, 账号Id
  ```

- Response

  ```
  null
  ```

#### 删除

- [ ] 完成

- URL

  ```
  /api/backend/account/remove
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  accountId: long, 账号Id
  ```

- Response

  ```
  null
  ```

#### 重置密码

- [ ] 完成

- URL

  ```
  /api/backend/account/password/reset
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  accountId: long, 账号Id
  ```

- Response

  ```
  null
  ```



### 角色权限

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/role/list?page=&count=
  ```

- Method

  ```
  GET
  ```

- Request

  ```json
  page: 分页参数。从 0 开始
  count: 每次请求个数。上限 100 条一次请求
  ```

- Response

  ```
  roles: [
      {
          roleId: long, 账号Id,
          name: string, 角色名称,
  		deparmentName: string, 部门名称
  		areaName: string, 地区名称
          desc: string, 角色描述
      },
      ...
  ],
  count: int, 记录总数
  ```

#### 权限详情

- [ ] 完成

- URL

  ```
  /api/backend/role/detail?roleId=
  ```

- Method

  ```
  GET
  ```

- Request

  ```json
  roleId: long, 角色Id
  ```

- Response

  ```
  name: string, 角色名称
  departmentName: string, 部门名称
  desc: string, 角色描述
  areaName: string, 地区
  perms: [
      menuId: long, 菜单Id
      parentId: long, 父菜单Id
      rootId: long, 根菜单Id
      name: string, 菜单名称
      canRead: boolean, 是否可查询
      canEdit: boolean, 是否可修改
      canDelete: boolean, 是否可删除
  ],
  ```



#### 新增

- [ ] 完成

- URL

  ```
  /api/backend/role/add
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  name: string, 角色名称
  departmentId: long, 部门Id
  desc: string, 角色描述
  area: {
    	province: string, 省份
      city: string, 城市
      area: string, 区县
  },
  perms: [
      menuId: long, 菜单Id   
      canRead: boolean, 是否可查询
      canEdit: boolean, 是否可修改
      canDelete: boolean, 是否可删除
  ],
  ```

- Response

  ```
  null
  ```

#### 修改

- [ ] 完成

- URL

  ```
  /api/backend/role/modify
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  roleId: long, 角色Id,
  name: string, 角色名称
  departmentId: long, 部门Id
  desc: string, 角色描述
  area: {
    	province: string, 省份
      city: string, 城市
      area: string, 区县
  },
  perms: [
      menuId: long, 菜单Id   
      canRead: boolean, 是否可查询
      canEdit: boolean, 是否可修改
      canDelete: boolean, 是否可删除
  ],
  ```

- Response

  ```
  null
  ```



### 系统日志

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/system-log/list?page=&count=
  ```

- Method

  ```
  GET
  ```

- Request

  ```json
  page: 分页参数。从 0 开始
  count: 每次请求个数。上限 100 条一次请求
  ```

- Response

  ```
  logs: [
      {
          username: string, 操作账号
          name: string, 名字
          catetgory: 日志类型,
          ip: string, 操作IP
          desc: string, 详情
  		date: string, 操作时间。 yyyy-MM-dd HH:mm
      },
      ...
  ],
  count: int, 记录总数
  ```



### 表单配置

#### 表单列表信息

- [ ] 完成

- URL

  ```
  /api/backend/config/list?page=&count=
  ```

- Method

  ```
  GET
  ```

- Request

  ```json
  page: 分页参数。从 0 开始
  count: 每次请求个数。上限 100 条一次请求
  ```

- Response

  ```
  configs: [
      {
         	configId: long, 表单Id,       	
         	name: string, 表单名称,
         	updated: string, 更新时间 yyyy-MM-dd HH:mm
         	operator: string, 操作人
      },
      ...
  ],
  count: int, 记录总数
  ```

#### 新增表单

- [ ] 完成

- URL

  ```
  /api/backend/config/add
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  name: string, 表单名称
  ```

- Response

  ```
  null
  ```

#### 删除表单

- [ ] 完成

- URL

  ```
  /api/backend/config/remove
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  configId: 表单Id
  ```

- Response

  ```
  null
  ```

#### 配置项列表信息

- [ ] 完成

- URL

  ```
  /api/backend/config/item/list?page=&count=
  ```

- Method

  ```
  GET
  ```

- Request

  ```json
  page: 分页参数。从 0 开始
  count: 每次请求个数。上限 100 条一次请求
  ```

- Response

  ```
  items: [
      {
         	itemId: long, 配置项Id
         	configId: long, 表单Id,       	
         	name: string, 表单名称,
      },
      ...
  ],
  count: int, 记录总数
  ```

#### 新增配置项

- [ ] 完成

- URL

  ```
  /api/backend/config/item/add
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  name: string, 配置项名称
  configId: long, 表单Id
  ```

- Response

  ```
  null
  ```

#### 删除配置项

- [ ] 完成

- URL

  ```
  /api/backend/config/item/remove
  ```

- Method

  ```
  POST
  ```

- Request

  ```json
  itemId: 配置项Id
  ```

- Response

  ```
  null
  ```

### 用户模块

// TODO: 待确认同步方式

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/user/list?page=&count=&province=&city=&area=&merchantId=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int, 分页 从 0 开始
  count: int, 每次获取个数
  province: string, 省份
  city: string, 城市
  area: string, 城区
  mechantId: long, 站点Id
  searchContetn: string, 搜索文本
  ```

- Response

  ```
  users: [
      {
  	    userId: long, 用户Id
          merchantName: string, 站点名称
          avatar: string, 头像
          wechat: string, 微信号
          nick: string, 昵称
          mobile: string, 手机号
          packageCount: {
              received: int, 总收件数
              send: int, 总寄件数
          },        
      },
      ...
  ],
  count: 总数
  ```

#### 导出

- [ ] 完成

- URL

  ```
  /api/backend/user/export?page=&count=&province=&city=&area=&merchantId=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int, 分页 从 0 开始
  count: int, 每次获取个数
  province: string, 省份
  city: string, 城市
  area: string, 城区
  mechantId: long, 站点Id
  searchContetn: string, 搜索文本
  ```

- Response

  ```
  TODO: 此处需要修改http header
  ```


### 黑名单 

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/user/black-list/list?page=&count=&province=&city=&area=&merchantId=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int, 分页 从 0 开始
  count: int, 每次获取个数
  province: string, 省份
  city: string, 城市
  area: string, 城区
  mechantId: long, 站点Id
  searchContetn: string, 搜索文本
  ```

- Response

  ```
  users: [
      {
  	    userId: long, 用户Id
          merchantName: string, 站点名称
          nick: string, 昵称
          mobile: string, 手机号
          address: string, 用户地址
          mark: string, 备注    
      },
      ...
  ],
  count: 总数
  ```

#### 导出

- [ ] 完成

- URL

  ```
  /api/backend/user/black-list/export?page=&count=&province=&city=&area=&merchantId=&searchContent=
  ```

- Method

  ```
  GET
  
  ```

- Request

  ```
  page: int, 分页 从 0 开始
  count: int, 每次获取个数
  province: string, 省份
  city: string, 城市
  area: string, 城区
  mechantId: long, 站点Id
  searchContetn: string, 搜索文本
  ```

- Response

  ```
  TODO: 此处需要修改http header
  ```

#### 新增

#### 删除

- [ ] 完成

- URL

  ```
  /api/backend/user/black-list/add
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  
  ```

- Response

  ```
  null
  ```



### 站点管理

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/merchant/list?page=&count=&province=&city=&area=&merchantId=&type=&status=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  province: string, 省份
  city: string, 城市
  area: string, 城区
  merchantId: long, 站点Id
  type: int, 属性
  status: int, 站点状态
  searchContent: string, 搜索文本
  ```

- Response

  ```
  merchants: [
      {
          merchantId: long,
          name: 站点名称,
          type: int,
  		account: string,
  		rentStartDate: string,
  		rentEndDate: string,
  		rentMoney: double,
  		address: string,
  		manager: {
              name: 负责人姓名,
              mobile: 联系电话
  		},
  		status: int
      }
  ],
  count: 个数
  ```

#### 导出

- [ ] 完成

- URL

  ```
  /api/backend/merchant/export?page=&count=&province=&city=&area=&merchantId=&type=&status=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  province: string, 省份
  city: string, 城市
  area: string, 城区
  merchantId: long, 站点Id
  type: int, 属性
  status: int, 站点状态
  searchContent: string, 搜索文本
  ```

- Response

  ```
  TODO:
  ```

#### 新增

- [ ] 完成

- URL

  ```
  /api/backend/merchant/add
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  name: string, 站点名称
  type: int, 站点性质
  mobile: string, 手机号码
  address: { // 地址
      province: string, 省份
      city: string, 城市
      area: string, 区县
  	detail: string, 详细地址
  },
  bizTime: { // 营业时间
      start: string. 开始时间 HH:mm
      end: string    结束时间 HH:mm
  },
  plot: [ // 小区名称列表
  	string, 小区名称
  	string
  ],
  shop: {
      area: double, 平米
      rentMoney: double, 租金
      rentStartDate: string, 租金起始时间
      rentEndDate: string,   租金结束时间
  },
  manager: { // 负责人
      name: string, 姓名
      mobile: string, 手机号码
      idcard: string, 身份证号
      alipay: string, 支付宝
      wechat: string, 微信
  },
  emergencyContractor: { // 紧急联系人
      name: string, 姓名
     	mobile: string, 手机
  },
  joinMoney: double, 加盟费
  assuranceMoney: double, 保证金
  joinStartDate: string, 加盟起始时间
  joinEndDate: string, 加盟结束时间
  account: string, 账号
  password: string, 密码
  photos: [// 门店照片
      string, 照片地址
  ]
  ```

- Response

  ```
  null
  ```

#### 修改

- [ ] 完成

- URL

  ```
  /api/backend/merchant/modify
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  merchantId: 站点Id,
  name: string, 站点名称
  type: int, 站点性质
  mobile: string, 手机号码
  address: { // 地址
      province: string, 省份
      city: string, 城市
      area: string, 区县
  	detail: string, 详细地址
  },
  bizTime: { // 营业时间
      start: string. 开始时间 HH:mm
      end: string    结束时间 HH:mm
  },
  plot: [ // 小区名称列表
  	string, 小区名称
  	string
  ],
  shop: {
      area: double, 平米
      rentMoney: double, 租金
      rentStartDate: string, 租金起始时间
      rentEndDate: string,   租金结束时间
  },
  manager: { // 负责人
      name: string, 姓名
      mobile: string, 手机号码
      idcard: string, 身份证号
      alipay: string, 支付宝
      wechat: string, 微信
  },
  emergencyContractor: { // 紧急联系人
      name: string, 姓名
     	mobile: string, 手机
  },
  joinMoney: double, 加盟费
  assuranceMoney: double, 保证金
  joinStartDate: string, 加盟起始时间
  joinEndDate: string, 加盟结束时间
  account: string, 账号
  password: string, 密码
  photos: [// 门店照片
      string, 照片地址
  ]
  ```

- Response

  ```
  null
  ```

#### 停用

- [ ] 完成

- URL

  ```
  /api/backend/merchant/close
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  merchantId
  ```

- Response

  ```
  null
  ```



#### 启用

- [ ] 完成

- URL

  ```
  /api/backend/merchant/open
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  merchantId
  ```

- Response

  ```
  null
  
  ```



#### 取件码设置

- [ ] 完成

- URL

  ```
  /api/backend/merchant/pickUpCode/set
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  type: int 取件码设置. 可选值 0货架模式 | 1数字模式
  ```

- Response

  ```
  null
  ```


### 公众号入口统计

#### 统计

- [ ] 完成

- URL

  ```
  /api/backend/merchant/wechat-vipcn/stastic/today
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  null
  ```

- Response

  ```
  count: 关注总量
  today: 今日新增总数
  ```

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/merchant/wechat-vipcn/list?page=&count=&province=&city=&area=&startTime=&endTime=&smerchantId=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  province: string, 省份
  city: string, 城市
  area: string, 城区
  startTime: long, 开始时间戳 毫秒级
  endTime: long    结束时间戳 毫秒级
  merchantId: long, 站点Id
  searchContent: string, 搜索文本
  ```

- Response

  ```
  vipcns: [
      {
      	vipcnId: 统计Id,
          merchantId: long, 站点Id
          name: string,站点名称,
          mobile: string, 联系方式
  		vipcnQrCode: string, 公众号入口二维码
  		followedCount: int, 关注人数
  		followedAddedToday: int, 今日新增
  		address: string 地址
      }
  ],
  count: 个数
  ```

#### 导出

- [ ] 完成

- URL

  ```
  /api/backend/merchant/wechat-vipcn/export?page=&count=&province=&city=&area=&startTime=&endTime=&smerchantId=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  province: string, 省份
  city: string, 城市
  area: string, 城区
  startTime: long, 开始时间戳 毫秒级
  endTime: long    结束时间戳 毫秒级
  merchantId: long, 站点Id
  searchContent: string, 搜索文本
  ```

- Response

  ```
  TODO:
  ```



### 快递公司

#### 统计

- [ ] 完成

- URL

  ```
  /api/backend/merchant/wechat-vipcn/stastic/list?page=&count=&province=&city=&area=&merchantId=&expressCompanyId=&startTime=&endTime=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int
  count:int
  province: string
  city: string
  area: string
  merchantId: long 站点Id
  expressCompanyId: long 快递公司Id
  startTime: long,
  endTime: long,
  searchContent: string 搜索文本
  ```

- Response

  ```
  items: [
      {
          merchantName: string, 站点名称,
          expressCompanyName: string, 快递公司名称,
          stastic: {
             	store: int, 入库
             	sign: int, 签收
              signRate: double, 签收率
              signToday: int, 今日签收
              signTodayRate: double, 今日签收率
          }
      }
  ],
  count: int, 个数
  ```

#### 导出

- [ ] 完成

- URL

  ```
  /api/backend/merchant/wechat-vipcn/stastic/export?page=&count=&province=&city=&area=&merchantId=&expressCompanyId=&startTime=&endTime=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int
  count:int
  province: string
  city: string
  area: string
  merchantId: long 站点Id
  expressCompanyId: long 快递公司Id
  startTime: long,
  endTime: long,
  searchContent: string 搜索文本
  ```

- Response

  ```
  TODO:
  ```


### 库存管理

#### 列表信息

- [ ] 完成

- URL

  ```
  /api/backend/package/list?page=&count=&merchantId=&expressCompanyId=&startTime=&endTime=&notifyStatus=&packageStatus=&storedDay=
  ```

- Method

  ```
  
  ```

- Request

  ```
  page: int,
  count: int,
  merchantId: long, 总站后台登陆传入该值。网点后台为自身Id
  expressCompanyId: long, 快递公司Id
  startTime: long, 开始时间
  endTime: long, 结束时间 毫秒值
  notifyStatus: int, 通知状态
  packageStatus: int, 包裹状态
  storedDay: int 在库时长
  ```

- Response

  ```
  packages: [
      {
          packageId: long, 包裹Id
          merchantName: string, 站点名称
          waybill: { // 运单
          	number: string, 运单号
          	expressCompanyName: string, 快递公司名称
          },
          packageStatus: int, 包裹状态
          pickupCode: string 取货码
          clientMobile: string, 客户电话
          notifyStatus: int, 通知状态
          storedDay: int, 在库时长
      },
      ...
  ],
  count: 个数
  ```

#### 详情

- [ ] 完成

- URL

  ```
  /api/backend/package/detail?packageId=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  packageId: long 包裹Id
  ```

- Response

  ```
  packageId: long, 包裹Id
  clientMobile: string, 客户号码
  number: string, 运单号
  expressCompanyName: string, 快递公司名字
  notifyStatus: int, 通知状态
  storedDay: int, 在库时长
  storeDate: string, 入库时间 yyyy-MM-dd hh:mm
  signDate: string, 签收时间 yyyy-MM-dd hh:mm
  ```

#### 新增

- [ ] 完成

- URL

  ```
  /api/backend/package/add
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  expressCompanyId: long, 快递公司Id
  number: string, 运单号
  pickupCode: string, 取件码
  clientMobile: string, 客户手机号
  ```

- Response

  ```
  null
  ```

#### 发送短信

- [ ] 完成

- URL

  ```
  /api/backend/package/sendSms
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  packageIds: [long, ] 包裹Id列表
  ```

- Response

  ```
  null
  ```

#### 

### 寄件管理

#### 寄件信息

- [ ] 完成

- URL

  ```
  /api/backend/waybill/list?page=&count=&merchantId=&expressCompanyId=&packageStatus=&startTime=&endTime=&searchContent=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  merchantId: long, 总站后台登陆传入该值。网点后台为自身Id
  expressCompanyId: long, 快递公司Id
  packageStatus: int, 包裹状态
  startTime: long, 开始时间戳 毫秒级
  endTime: long    结束时间戳 毫秒级
  searchContent: string, 搜索文本
  ```

- Response

  ```
  waybills: [
      {
     		waybillId: long, 运单号
      	merchantId: long, 站点Id
  		waybill: { // 运单
              expressCompanyName: string, 快递公司名字
              number: string, 快递单号
  		},
  		package: { // 包裹
              status: int, 包裹状态
              updated: string, 更新时间 yyyy-MM-dd HH:mm
  		},
  		sender: { // 寄件人
              name: string, 姓名
              mobile: string, 电话
  		},
  		money: double, 运费
  		weight: double, 重量
  		address: string 寄件地址
      }
  ],
  count: 个数
  ```

#### 详情

#### 寄件信息

- [ ] 完成

- URL

  ```
  /api/backend/waybill/detail?waybillId=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  waybillId: 运单号
  ```

- Response

  ```
  waybillId: long,
  expressCompanyName: string,
  number: string,
  sender: { // 寄件人信息
      name: string,
      mobile: string
      address: {
          province,
          city,
          area,
          detail
      }    
  },
  receiver: { // 收件人信息
      name: string,
      mobile: string
      address: {
          province: string,
          city: string,
          area: string,
          detail: string
      }    
  },
  package: { // 包裹信息
      type: int,
      weight: double,    
  },
  money: double
  ```

#### 新增 - 寄件码

- [ ] 完成

- URL

  ```
  /api/backend/waybill/add?from=code
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  code: string, 寄件码
  ```

- Response

  ```
  null
  ```

#### 新增 - 页面单

TODO: 原型

#### 删除

- [ ] 完成

- URL

  ```
  /api/backend/waybill/remove
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  waybillId: long, 运单号
  ```

- Response

  ```
  null
  ```


### 短信模块

#### 短信记录

- [ ] 完成

- URL

  ```
  /api/backend/package/list?page=&count=&templateId=&result=&startTime=&endTime=&merchantId=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  templateId: long, 模板Id
  startTime: long, 开始时间
  endTime: long, 结束时间 毫秒值
  result: int, 发送状态
  merchantId: long, 总站后台登陆传入该值。网点后台为自身Id
  ```

- Response

  ```
  items: [
      {
          itemId: long, 记录Id
          templateId: string, 站点名称       
          clientMobile: string, 客户电话
          content: string, 短信内容
          sendDate: string, 发送时间 yyyy-MM-dd HH:mm
          result: { // 发送结果
          	code: int, 结果码
          	reason: string 原因(发送失败时有值)
          },
      },
      ...
  ],
  count: 个数
  ```



#### 短信账户列表

- [ ] 完成

- URL

  ```
  /api/backend/sms/account/list?page=&count=&province=&city=&area=&merchantId=&type=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  province: string,
  city: string,
  area: string,
  merchantId: long,
  type: int
  ```

- Response

  ```
  accounts: [
      {
          merchantId: long, 站点Id
          merchantName: string, 站点名称
          type: int, 属性
          mobile: string, 联系电话
          money: double 月
          smsCount: int 本月短信数
      }
  ],
  count: int 个数
  ```


#### 短信账户导出

- [ ] 完成

- URL

  ```
  /api/backend/sms/account/export?page=&count=&province=&city=&area=&merchantId=&type=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  province: string,
  city: string,
  area: string,
  merchantId: long,
  type: int
  ```

- Response

  ```
  accounts: [
      {
          merchantId: long, 站点Id
          merchantName: string, 站点名称
          type: int, 属性
          mobile: string, 联系电话
          money: double 月
          smsCount: int 本月短信数
      }
  ],
  count: int 个数,
  isAutoChargeOpen: true|false
  ```



#### 短信账户详情

- [ ] 完成

- URL

  ```
  /api/backend/sms/account/detail?merchantId=&page=&count
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  merchantId: long,
  page: int,
  count: int
  ```

- Response

  ```
  money: double 余额
  items: [
      {
          itemId: long,
          money: double 资金明细
          detail: string, 详情
          accountMoney: double 余额
          orderNumber: string, 订单号
          date: string 时间 yyyy年M月d日 HH:mm:ss
      },
  ],
  count: int 个数
  ```


#### 自动充值明细

- [ ] 完成

- URL

  ```
  /api/backend/sms/account/charge/list?page=&count=
  ```

- Method

  ```
  GET
  ```

- Request

  ```
  page: int,
  count: int,
  ```

- Response

  ```
  items: [
      {
          merchantId: long, 站点Id
  		chargedMoney: double
  		date: yyyy年M月d日 HH:mm:ss
      }
  ],
  count: int 个数
  ```



#### 自动充值金额更改

- [ ] 完成

- URL

  ```
  /api/backend/sms/account/auto-charge/modify
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  type: int, 充值站点类型
  minMoney: double 金额下限
  chargeMoney: double 充值金额
  ```

- Response

  ```
  null
  ```

#### 自动充值开关

- [ ] 完成

- URL

  ```
  /api/backend/sms/account/auto-charge/switch
  ```

- Method

  ```
  POST
  ```

- Request

  ```
  isAutoChargeOpen: true|false
  ```

- Response

  ```
  null
  ```



### 收银台模块
微信、支付宝、网银 支付接入
