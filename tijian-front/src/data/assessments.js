// 健康回顾使用显式规则，不计算疾病风险或诊断。
const yn=['是','否','不确定']
const q=(id,text,options=yn)=>({id,text,options})
export const assessments=[
{id:'respiratory',title:'呼吸道症状自查',subtitle:'症状、变化与就医提示',intro:'根据近期症状、持续时间和个人情况整理下一步行动。适用于成人。',source:'https://www.cdc.gov/respiratory-viruses/about/',questions:[q('danger','是否有呼吸困难、持续胸痛、意识混乱或难以唤醒？'),q('symptom','是否有发热、咳嗽、咽痛或流涕？'),q('duration','症状持续了多久？',['无症状','1—3 天','4—10 天','超过 10 天','不确定']),q('worse','症状是否加重，或好转后再次变差？'),q('risk','是否怀孕、65 岁及以上、免疫力低下或有心肺疾病、糖尿病、肾病等？'),q('fever','发热是否持续超过 4 天？')]},
{id:'hra',title:'健康风险回顾',subtitle:'活动、饮食和体检',intro:'回顾过去一个月的习惯，找出适合先改善的方面。',source:'https://www.who.int/news-room/fact-sheets/detail/physical-activity',questions:[q('activity','每周中等强度活动累计多久？',['不到 30 分钟','30—149 分钟','150 分钟及以上','不确定']),q('sit','每天久坐是否超过 8 小时？'),q('vegetables','每天是否吃蔬菜和水果？'),q('tobacco','目前是否吸烟或使用烟草？'),q('sleep','睡眠是否经常不足或不规律？'),q('check','是否有需要复查却尚未复查的体检结果？')]},
{id:'life',title:'生活方式回顾',subtitle:'制定可坚持的行动',intro:'围绕近期作息、饮食及活动安排，选择一个可执行的改变。',source:'https://www.who.int/news-room/fact-sheets/detail/self-care-health-interventions/',questions:[q('activity','每周是否有至少 3 天进行步行等身体活动？'),q('sit','是否经常连续坐着而长时间不起身？'),q('sleep','是否经常熬夜或睡眠不足？'),q('meals','是否经常不按时进餐？'),q('tobacco','是否吸烟或使用烟草？'),q('alcohol','是否饮酒？')]},
{id:'mood',title:'情绪与压力记录',subtitle:'梳理压力与支持',intro:'回顾过去两周的感受与生活状态。',source:'https://www.who.int/news-room/questions-and-answers/item/stress',questions:[q('stress','是否常感到紧张、担忧或压力较大？'),q('sleep','是否因情绪或压力影响睡眠？'),q('function','情绪或压力是否影响学习、工作或日常生活？'),q('duration','这种状态已持续多久？',['不足两周','两周或更久','不确定']),q('support','是否有信任的人可以交流和求助？'),q('unsafe','是否感到自己目前有立即伤害自己的危险？')]},
{id:'habits',title:'身体感受与日常调养',subtitle:'记录症状和生活变化',intro:'整理近期身体不适及其变化，为沟通检查提供线索。',source:'https://www.who.int/news-room/fact-sheets/detail/self-care-health-interventions/',questions:[q('danger','是否出现呼吸困难、持续胸痛、意识混乱或突然严重不适？'),q('fatigue','是否经常疲劳或精力不足？'),q('sleep','是否出现持续的睡眠变化？'),q('appetite','是否出现持续的食欲或体重变化？'),q('duration','不适是否持续、反复出现或逐渐加重？'),q('check','是否有异常体检结果尚未复查？')]},
{id:'diabetes',title:'糖尿病相关因素回顾',subtitle:'整理血糖检查相关信息',intro:'梳理可能需要与医务人员讨论的因素。',source:'https://www.cdc.gov/diabetes/risk-factors/',questions:[q('history','是否曾被告知血糖偏高或有糖尿病前期？'),q('family','父母、兄弟姐妹中是否有人患糖尿病？'),q('gestational','是否曾患妊娠期糖尿病？',['是','否','不适用','不确定']),q('activity','是否经常缺少身体活动？'),q('symptoms','近期是否经常口渴、多尿或体重无明显原因下降？'),q('test','是否有医生建议复查的血糖结果尚未复查？')]}
]
export function buildAdvice(id,a){
 let level='日常关注',intro='根据您的回答，建议从以下方面开始。',actions=[]
 const add=(condition,text)=>{if(condition)actions.push(text)}
 if(id==='respiratory'){
  if(a.danger==='是'){level='立即就医';intro='您报告了需要紧急处理的症状，请立即就医或呼叫急救。'}
  else if(a.worse==='是'||a.fever==='是'||a.duration==='超过 10 天'||(a.symptom==='是'&&a.risk==='是')){level='尽快咨询医务人员';intro='您报告了呼吸道症状，同时存在需要进一步关注的情况。'}
  else if(a.symptom==='是'){level='留意症状变化';intro='您报告了呼吸道症状，可记录变化并根据情况就医。'}
  else if(a.symptom==='否'&&a.danger==='否'){intro='您未报告常见呼吸道症状和所列紧急症状。'}
  else {level='信息待确认';intro='部分情况尚不确定；如果症状让您担忧，请咨询医务人员。'}
  add(a.symptom==='是','记录发病时间、体温及症状变化；休息并注意补充水分。')
  add(a.symptom==='是'&&a.risk==='是','您填写了可能增加重症风险的情况，即使症状较轻也应尽快联系医务人员。')
  add(a.worse==='是'||a.fever==='是'||a.duration==='超过 10 天','症状加重、好转后再次变差、发热超过四天或超过十天仍未改善时，请就医。')
  add(a.danger==='不确定','如不确定是否存在呼吸困难、持续胸痛或意识改变，请及时寻求医疗帮助。')
  add(a.symptom==='否','继续观察身体变化，如出现新症状可重新评估。')
 }else if(id==='hra'){
  add(a.activity==='不到 30 分钟'||a.activity==='30—149 分钟','从可坚持的短时步行开始，逐步增加活动；成年人可根据身体状况向每周 150 分钟中等强度活动努力。')
  add(a.activity==='150 分钟及以上','您已达到每周 150 分钟活动量，可继续保持并结合自身情况调整。')
  add(a.sit==='是','为久坐设置起身提醒，在工作或学习间隙适当活动。')
  add(a.vegetables==='否','从每天一餐增加蔬菜或水果开始，逐步保持食物多样化。')
  add(a.tobacco==='是','考虑制定戒烟计划，并向戒烟门诊或医务人员寻求支持。')
  add(a.sleep==='是','尝试固定起床和睡觉时间，记录影响睡眠的因素。')
  add(a.check==='是','带上体检报告，按报告或医生建议安排复查。')
 }else if(id==='life'){
  add(a.activity==='否','从每周增加几次短时间步行开始，逐步形成习惯。')
  add(a.activity==='是','您有规律活动，可继续按身体状况保持。')
  add(a.sit==='是','为久坐安排起身活动的间隔。')
  add(a.sleep==='是','尝试固定作息并记录睡眠状况。')
  add(a.meals==='是','优先选一餐固定时间进食，再逐步调整其他餐次。')
  add(a.tobacco==='是','考虑戒烟，并寻求专业支持。')
  add(a.alcohol==='是','减少饮酒或不饮酒，避免以饮酒帮助睡眠。')
 }else if(id==='mood'){
  if(a.unsafe==='是'){level='立即寻求帮助';intro='请立即联系身边可信赖的人陪伴，并联系当地急救或前往急诊。'}
  else if(a.function==='是'||a.duration==='两周或更久'){level='建议寻求专业支持';intro='您报告了持续或影响生活的压力，建议联系心理或医疗专业人员。'}
  add(a.sleep==='是','您提到睡眠受到影响，可记录睡眠变化，并在咨询时说明。')
  add(a.stress==='是','每天安排短暂休息或轻度活动，尝试与可信赖的人交流。')
  add(a.support==='否','可以主动联系家人、朋友或专业服务，寻找可持续的支持。')
  add(a.support==='是','遇到压力时，可主动与信任的人联系。')
  add(a.unsafe==='不确定','如果担心自己无法保持安全，请立即联系可信赖的人或急救服务。')
 }else if(id==='habits'){
  if(a.danger==='是'){level='立即就医';intro='您报告了需要紧急处理的不适，请立即就医或呼叫急救。'}
  else if(a.duration==='是'||a.appetite==='是'||a.check==='是'){level='建议联系医务人员';intro='您提到持续变化、反复不适或待复查结果，建议安排专业评估。'}
  add(a.danger==='不确定','如不确定是否存在严重不适，请及时寻求医疗帮助。')
  add(a.fatigue==='是','记录疲劳开始的时间、程度和对日常生活的影响。')
  add(a.sleep==='是','记录睡眠时间和近期变化，带到就诊时讨论。')
  add(a.appetite==='是','记录食欲或体重变化及持续时间，与医务人员讨论。')
  add(a.duration==='是','记录不适出现的频率、变化和伴随症状。')
  add(a.check==='是','携带相关体检报告，按建议安排复查。')
 }else if(id==='diabetes'){
  if(a.symptoms==='是'){level='建议尽快就医';intro='您报告了可能与血糖异常相关的症状，请尽快联系医务人员评估。'}
  else if(a.history==='是'||a.gestational==='是'||a.test==='是'){level='建议安排血糖咨询';intro='您填写了既往血糖异常或待复查情况，请与医务人员讨论检查安排。'}
  add(a.family==='是','就诊时主动说明直系亲属的糖尿病史。')
  add(a.history==='是'||a.gestational==='是','携带既往血糖结果或妊娠期糖尿病记录，与医务人员讨论复查。')
  add(a.activity==='是','从适合自己的步行等活动开始，逐步增加活动时间。')
  add(a.test==='是','按医生此前的建议安排血糖复查。')
  add(a.symptoms==='是','记录口渴、多尿、体重变化出现的时间，供就诊时参考。')
 }
 if(Object.values(a).includes('不确定'))actions.push('部分回答为不确定，可以记录疑问，在就诊或咨询时核实。')
 if(!actions.length)actions.push('继续关注身体变化；如出现持续或令您担忧的情况，请咨询医务人员。')
 return {level,intro,actions}
}
