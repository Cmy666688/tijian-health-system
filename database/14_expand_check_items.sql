-- 扩展套餐检查项目，不修改已经创建或归档的报告。
INSERT INTO checkitem (ciName,ciContent,meaning,remarks)
SELECT x.name,x.content,x.meaning,'项目配置' FROM (
 SELECT '尿常规' AS name,'尿液常规检查' AS content,'记录尿液检查结果' AS meaning
 UNION ALL SELECT '空腹血糖','血糖检查','记录血糖检查结果'
 UNION ALL SELECT '心电图','心电图检查','记录心电图检查结论'
) x WHERE NOT EXISTS (SELECT 1 FROM checkitem c WHERE c.ciName=x.name);
INSERT INTO checkitemdetailed (name,unit,minrange,maxrange,normalValue,normalValueString,type,ciId,remarks)
SELECT x.detail,x.unit,NULL,NULL,NULL,'以检验报告为准',3,c.ciId,'由医生录入'
FROM (SELECT '尿常规' AS item,'尿蛋白' AS detail,'' AS unit UNION ALL SELECT '尿常规','尿糖','' UNION ALL SELECT '空腹血糖','空腹血糖','mmol/L' UNION ALL SELECT '心电图','心电图结论','') x
JOIN checkitem c ON c.ciName=x.item WHERE NOT EXISTS (SELECT 1 FROM checkitemdetailed d WHERE d.ciId=c.ciId AND d.name=x.detail);
INSERT INTO setmealdetailed (smId,ciId)
SELECT s.smId,c.ciId FROM setmeal s JOIN checkitem c ON c.ciName IN ('尿常规','空腹血糖','心电图')
WHERE NOT EXISTS (SELECT 1 FROM setmealdetailed sd WHERE sd.smId=s.smId AND sd.ciId=c.ciId);
