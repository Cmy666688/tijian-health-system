# 医生工作台前端

本地开发地址：http://127.0.0.1:8089/ 。页面通过开发代理访问 `127.0.0.1:8088` 的医生后端。先启动 MySQL 和医生后端，再启动前端。

在 `tijian` 目录的 PowerShell 中分别打开两个窗口：

```powershell
# 窗口一：医生后端
. .\启用项目环境.ps1
$env:TIJIAN_DB_PASSWORD = '你的数据库密码'
mvn.cmd -f .\tijian-cms-server\pom.xml spring-boot:run
```

```powershell
# 窗口二：医生前端
cd .\tijian-cms-front
npm.cmd install
npm.cmd run serve
```

登录示例账号 `doctor01`，密码 `123456`。本机还添加了虚构患者预约与已归档报告；列表中的其他用户和预约可能是使用者自行创建的数据。

当前前端使用 Vue 3、Vue Router、Axios、Element Plus 和 Vue CLI Service 5.0.9；核心界面仍集中在 App.vue，使用 hash 路由。登录使用服务端会话，归档报告不可通过接口修改。当前仅供本机教学，正式使用前需完善角色权限、HTTPS、CSRF 防护和登录限速。

文件：`package.json` 与 `package-lock.json` 定义依赖；`vue.config.js` 定义端口和代理；`public/index.html` 是页面入口；`src/main.js` 挂载应用、路由和 Element Plus；`src/router/index.js` 定义登录、列表和详情路由；`src/App.vue` 包含登录、预约和报告管理页面。

预约列表支持按预约手机号、体检人姓名、性别、体检日期、套餐和归档状态组合筛选，展示体检人与预约人关系。归档前需保存全部检查结果并添加总检结论。

报告详情的体检人信息会展示姓名、性别、出生日期、脱敏证件号和预约账号，使用原始 `user.png` 素材。

页面会在恢复服务端会话时刷新医生资料；查询当前医生资料的接口为 `GET /tijiancms/doctor/me`。
