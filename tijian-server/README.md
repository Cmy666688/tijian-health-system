# 用户端后端

基于实践手册的 Spring Boot 2.6.4、Java 8 和 MyBatis 2.2.2，连接本机 `tijian` 数据库。为了适配 MySQL 8，JDBC 驱动使用 8.0.33。端口 8080，路径前缀 `/tijian`。

## 启动

在 `tijian` 目录的 PowerShell 中执行：

```powershell
. .\启用项目环境.ps1
$env:TIJIAN_DB_PASSWORD = '你的数据库密码'
mvn.cmd -f .\tijian-server\pom.xml spring-boot:run
```

连接账号默认 `root`；可通过 `TIJIAN_DB_USER` 和 `TIJIAN_DB_URL` 覆盖。密码不写入源码。PowerShell 阻止本地脚本时可用进程级 `Set-ExecutionPolicy -Scope Process Bypass` 后再运行。

## 已实现接口

均为 JSON POST。未找到用户或密码错误时返回空响应；成功注册返回数字 1；参数错误为 HTTP 400，手机号重复为 409。

- `/tijian/users/getUsersById`：传入 `{"userId":"13800000001"}`，存在时仅返回用户编号。
- `/tijian/users/getUsersByUserIdByPass`：传入 `userId`、`password`；成功返回用户信息，密码不会返回。
- `/tijian/users/saveUsers`：传入 `userId`、`password`、`realName`、`sex`、`identityCard`、`birthday`（`yyyy-MM-dd`）、`userType`。性别和用户类型按手册数据库设计取值。

新注册用户的密码使用 BCrypt 存储；旧明文密码在首次成功登录时升级。成功登录后建立 30 分钟空闲超时的服务端会话，Cookie 使用 HttpOnly 和 SameSite=Strict。注册账号固定为普通用户。

## 医院和套餐查询接口

以下接口均为 JSON POST。列表未查到时返回 `[]`；详情编号不存在时返回空响应；缺少参数或参数超出范围时返回 HTTP 400。

| 接口路径 | 请求示例 | 作用 |
| --- | --- | --- |
| `/tijian/hospital/listHospital` | `{"state":1}` | 按状态查询医院；1 为正常，2 为其他。 |
| `/tijian/hospital/getHospitalById` | `{"hpId":1}` | 查询医院详情。 |
| `/tijian/setmeal/listSetmealByType` | `{"type":1}` | 按类型查询套餐；1 为男士，0 为女士。 |
| `/tijian/setmeal/getSetmealById` | `{"smId":1}` | 查询套餐基本信息。 |
| `/tijian/setmeal/listCheckItems` | `{"smId":1}` | 查询套餐中的检查项及其明细；套餐不存在返回 404。 |

手册套餐接口描述中出现“医院列表/医院信息”的文字，按接口名称和返回对象在此实现为套餐查询。套餐基本信息接口保留原有结构，新增接口提供检查项目和明细。

## 预约和日历接口

均为 JSON POST。日期格式为 `yyyy-MM-dd`，预约状态 1 为未归档、2 为已归档。以下示例手机号需替换成数据库中已有用户，日期需替换成当前开放且有名额的日期。

| 接口路径 | 请求示例 | 返回和说明 |
| --- | --- | --- |
| `/tijian/calendar/listAppointmentCalendar` | `{"hpId":1,"year":2028,"month":2}` | 返回当月日期数组，每项包含 `date`、`dayOfWeek`（周一=1，周日=7）、`capacity`、`remaining` 和 `available`。医院不存在返回 404。 |
| `/tijian/orders/getOrdersByUserId` | `{"userId":"13800000001"}` | 返回该用户未归档预约的数量。 |
| `/tijian/orders/saveOrders` | `{"userId":"13800000001","hpId":1,"smId":1,"orderDate":"2028-02-20"}` | 成功返回 1；已有未归档预约或名额已满返回 409。状态固定为未归档；服务器在事务内锁定用户和医院并检查名额。 |
| `/tijian/orders/listOrdersByUserId` | `{"userId":"13800000001","state":1}` | 按用户和状态查询预约，查不到返回 `[]`。 |
| `/tijian/orders/getOrdersById` | `{"orderId":1}` | 查询预约，查不到返回空响应。 |
| `/tijian/orders/removeOrders` | `{"orderId":1,"userId":"13800000001"}` | 仅删除该用户的未归档预约；返回删除数量。已关联报告时返回 409。 |

参数无效返回 400。日期必须不早于服务器当天，医院必须为正常状态，用户、医院和套餐必须存在，且该日期须有排班和可用名额。示例医院周一至周五每天 20 个名额；删除预约释放名额，归档不释放。节假日例外与截止时间尚无配置。

用户预约查询、创建和取消需登录；请求中的 userId 必须与当前会话一致，订单详情会校验归属。

## 体检报告查询接口

均为 JSON POST，请求体均为 `{"orderId":1}`，预约编号须为正整数；无数据时返回 `[]`，参数无效时返回 HTTP 400。

| 接口路径 | 作用 |
| --- | --- |
| `/tijian/overallResult/listOverallResultByOrderId` | 查询该预约的总检结论列表。 |
| `/tijian/ciReport/listCiReport` | 查询该预约的检查项报告列表；每项 `details` 数组包含检查明细，无明细时为空数组。 |

查询按预约编号执行，报告明细依据 `cidetailedreport.ciId = cireport.cirId` 且预约编号相同关联，与数据库说明中的约定一致。仅允许用户查询本人已归档预约的报告。

报告查询要求用户登录、订单属于当前用户且订单已归档；其他用户的订单或未归档订单返回 404。

## 登录会话

`GET /tijian/auth/session` 查询当前会话，`POST /tijian/auth/logout` 退出；除登录、注册、手机号查询及 OPTIONS 外的接口需要会话。仅用于本地开发：正式环境还需 HTTPS、安全 Cookie、CSRF 防护、登录限速和完整部署安全审查。

### 本人和家属预约

`saveOrders` 可选 `patientType`：1 为本人（默认，信息从账户读取），2 为家属。家属预约需同时传 `patientName`、`patientSex`（1 男、0 女）、`patientIdentityCard`（18 位格式）和 `patientBirthday`（`yyyy-MM-dd`）。体检人信息以快照保存在订单中，预约及报告仍归登录账户。同一账户存在未归档订单时，本人和家属都不能再次预约。

排班使用 `hospital_schedule`，`weekday` 1—7 对应周一至周日，未配置的星期不开放。可通过数据库调整各医院星期对应的 `dailyCapacity`；当前尚无管理界面或节假日例外。日历显示的是查询时余量，提交时再次校验。

### 自动化测试

在 `tijian` 目录运行 `. .\启用项目环境.ps1` 后执行 `mvn.cmd -f .\tijian-server\pom.xml test`。测试覆盖满额拒绝、锁定顺序和日历剩余名额。

`GET /tijian/users/me` 返回当前登录用户资料，用于刷新页面显示；不返回密码。
