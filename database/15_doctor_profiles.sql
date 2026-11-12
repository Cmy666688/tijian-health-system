-- 虚构教学账号，保留既有医生和预约；密码与项目测试账号相同。
CREATE TABLE IF NOT EXISTS doctor_profile (
 docCode VARCHAR(20) NOT NULL PRIMARY KEY,
 serviceArea VARCHAR(80) NOT NULL,
 introduction VARCHAR(500) NOT NULL,
 specialties VARCHAR(200) NOT NULL,
 FOREIGN KEY(docCode) REFERENCES doctor(docCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO doctor(docCode,realName,password,sex,deptno)
SELECT x.code,x.name,'$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe',x.sex,x.dept FROM (
 SELECT 'doctor04' AS code,'许安' AS name,1 AS sex,1 AS dept UNION ALL
 SELECT 'doctor05','周宁',0,2 UNION ALL SELECT 'doctor06','沈明',1,1 UNION ALL
 SELECT 'doctor07','林嘉',0,2 UNION ALL SELECT 'doctor08','陈远',1,1
) x WHERE NOT EXISTS (SELECT 1 FROM doctor d WHERE d.docCode=x.code);
INSERT INTO doctor_profile(docCode,serviceArea,introduction,specialties)
SELECT d.docCode,x.area,x.introduction,x.specialties FROM doctor d JOIN (
 SELECT 'doctor01' code,'检验与报告' area,'围绕常见检验项目、体检报告和复查问题提供沟通。' introduction,'检验项目,报告沟通,检查准备' specialties UNION ALL
 SELECT 'doctor02','内科体检','围绕内科体检流程、既往史和健康信息提供沟通。','内科检查,既往史,健康记录' UNION ALL
 SELECT 'doctor03','外科体检','围绕外科常规检查及相关准备事项提供沟通。','外科检查,检查准备,报告沟通' UNION ALL
 SELECT 'doctor04','一般检查','围绕一般检查、体检准备和健康记录提供沟通。','一般检查,体检准备,健康记录' UNION ALL
 SELECT 'doctor05','健康咨询','围绕健康生活和体检流程提供信息沟通。','生活方式,体检流程,健康咨询' UNION ALL
 SELECT 'doctor06','报告服务','围绕体检报告阅读及复查准备提供沟通。','报告查看,复查准备,健康记录' UNION ALL
 SELECT 'doctor07','体检项目','围绕体检套餐项目和检查准备提供沟通。','套餐项目,检查准备,体检咨询' UNION ALL
 SELECT 'doctor08','健康管理','围绕日常健康记录和体检安排提供沟通。','健康记录,体检安排,健康咨询'
) x ON x.code=d.docCode WHERE NOT EXISTS(SELECT 1 FROM doctor_profile p WHERE p.docCode=d.docCode);
UPDATE doctor_schedule s JOIN doctor d ON d.docCode=CASE s.profileId WHEN 'doctor-01' THEN 'doctor01' WHEN 'doctor-02' THEN 'doctor02' WHEN 'doctor-03' THEN 'doctor03' WHEN 'doctor-04' THEN 'doctor04' WHEN 'doctor-05' THEN 'doctor05' WHEN 'doctor-06' THEN 'doctor06' WHEN 'doctor-07' THEN 'doctor07' WHEN 'doctor-08' THEN 'doctor08' END SET s.docCode=d.docCode WHERE s.docCode IS NULL;
