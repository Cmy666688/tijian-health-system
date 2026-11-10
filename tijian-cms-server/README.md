# 医生端后端

依据实践手册创建，使用 Java 8、Spring Boot 2.6.4、MyBatis 2.2.2 和 MySQL Connector/J 8.0.33。默认连接与用户端相同的 `tijian` 数据库；端口 `8088`，路径前缀 `/tijiancms`。

## 启动

在 `tijian` 目录的 PowerShell 中执行：

```powershell
. .\启用项目环境.ps1
$env:TIJIAN_DB_PASSWORD = '你的数据库密码'
mvn.cmd -f .\tijian-cms-server\pom.xml spring-boot:run
```

数据库账号默认 `root`，可通过 `TIJIAN_DB_USER` 和 `TIJIAN_DB_URL` 覆盖。密码通过环境变量提供，不写入项目文件。

## 医生登录

`POST /tijiancms/doctor/getDoctorByCodeByPass`，JSON 示例：

```json
{"docCode":"doctor01","password":"123456"}
```

账号为初始化脚本中的本地虚构测试账号。成功时返回医生编号、姓名、性别及科室等信息，不返回密码；账号或密码错误时返回 HTTP 200 和空响应；缺少参数或长度超过 20 时返回 HTTP 400。

医生登录建立 30 分钟空闲超时的服务端会话；旧明文密码首次成功登录后升级为 BCrypt。Cookie 使用 HttpOnly 和 SameSite=Strict。示例密码仅供本地教学。

## 套餐和预约管理

以下均为 POST，除套餐列表无需请求体外，使用 JSON；需要医生登录会话。

| 接口 | 请求示例 | 返回 |
| --- | --- | --- |
| `/tijiancms/setmeal/listSetmeal` | 无 | 全部套餐数组。 |
| `/tijiancms/orders/listOrders` | `{"pageNum":1,"pageSize":10,"userId":"13800000001","realName":"张","sex":1,"orderDate":"2028-01-01","state":1,"smId":1}` | `total`、`pageNum`、`pageSize`、`records`；筛选字段均可省略。 |
| `/tijiancms/orders/getOrdersById` | `{"orderId":1}` | 预约详情，查不到时响应为空。 |
| `/tijiancms/orders/updateOrdersState` | `{"orderId":1,"state":2}` | 将未归档预约改为已归档；返回更新数量，已归档或不存在时为 0。 |

分页默认第 1 页、每页 10 条，单页最多 100 条。手机号和日期为精确筛选，姓名为体检人姓名包含匹配；性别筛选也针对体检人，1 为男、0 为女。状态 1 为未归档、2 为已归档。列表区分预约人和体检人，并附带医院及套餐名称。

手册未细化状态回退规则；当前仅实现从 1 到 2 的归档，不支持重新打开已归档预约，以维持同一用户只能有一个未归档预约的约束。归档前必须创建报告模板、至少有一条检查明细、所有明细结果非空并保存，且至少有一条总检结论；不满足时返回 409。

## 报告管理接口

均为 JSON POST，使用 `orderId` 指定预约：

- `/ciReport/createReportTemplate`：根据预约套餐生成检查项及明细，返回创建的检查项数量；模板已存在返回 409，预约不存在返回 404。
- `/ciReport/listCiReport`：返回检查项报告和 `details` 明细。
- `/ciDetailedReport/updateCiDetailedReport`：传入明细数组；每项包括 `cidrId`、`ciId`（报告编号）、`orderId`、`value`、`isError`。批量更新在同一事务中执行。
- `/overallResult/listOverallResultByOrderId`、`saveOverallResult`、`updateOverallResult`、`removeOverallResult`：查询、新增、修改、删除总检结论；修改和删除需同时传入 `orId` 与 `orderId`。

接口需要医生会话；归档后服务器拒绝修改报告及结论。当前所有医生具有相同管理权限；正式环境还需细化角色权限、HTTPS、CSRF 防护和登录限速。

`GET /tijiancms/auth/session` 查询当前医生会话，`POST /tijiancms/auth/logout` 退出并使会话失效。

自动化测试：在 `tijian` 目录启用项目环境后运行 `mvn.cmd -f .\tijian-cms-server\pom.xml test`，验证报告未完成时拒绝归档及完整时成功归档。

`GET /tijiancms/doctor/me` 返回当前登录医生资料，用于刷新页面显示；不返回密码。
