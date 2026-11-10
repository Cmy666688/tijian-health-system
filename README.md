# 东软体检项目

基于实践手册开发的体检预约与报告管理项目，包含患者端、医生工作台及 MySQL 数据库脚本。

## 项目目录

- `tijian-front`：Vue 3 患者端，默认端口 8081。
- `tijian-server`：Spring Boot 患者端接口，默认端口 8080。
- `tijian-cms-front`：Vue 3 医生工作台，默认端口 8089。
- `tijian-cms-server`：Spring Boot 医生端接口，默认端口 8088。
- `database`：建表、初始化数据和旧库迁移脚本。
- `项目文件说明.md`：文件用途及功能说明。

## 环境与启动

需要 JDK 8、Maven 3、Node.js、MySQL 8；前端使用 Vue CLI Service 5.0.9。创建空数据库 `tijian` 后，依次执行 `database/01_schema.sql` 和 `database/02_seed.sql`，如需测试数据可按数据库说明执行 `database/06_demo_patients.sql`。

在两个后端的终端中分别设置环境变量 `TIJIAN_DB_PASSWORD` 为本地数据库密码，然后分别进入 `tijian-server`、`tijian-cms-server` 运行 `mvn spring-boot:run`。在两个前端目录分别执行 `npm install`、`npm run serve`。详细配置见各目录的 README。

`启用项目环境.ps1` 是原开发电脑的本地工具脚本，依赖未提交的 `.tools` 文件夹；其他环境可直接配置系统 JDK 与 Maven。

患者、医生、体检结果和排班为项目测试数据。医生照片身份与实际出诊信息尚待核实；医生预约属于项目内排队功能。测评建议用于健康信息整理，不构成诊断。
