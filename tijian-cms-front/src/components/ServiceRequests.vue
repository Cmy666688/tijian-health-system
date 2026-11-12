<template><section><div class="page-head"><div><h2>服务申请</h2><p class="sub">查看上门护理和团检需求，回复处理结果。</p></div><button class="secondary" @click="load">↻ 刷新</button></div><div class="panel desk"><p v-if="!rows.length" class="muted">暂无申请</p><article v-for="r in rows" :key="r.requestId"><h3>{{r.serviceType==='HOME_CARE'?'上门护理':'团检预约'}} · {{r.realName}}</h3><p>申请 #{{r.requestId}} · {{r.status==='PENDING'?'待处理':'已回复'}}</p><p>{{r.content}}</p><p v-if="r.reply">现有回复：{{r.reply}}</p><form @submit.prevent="send(r)"><label>回复<textarea v-model.trim="drafts[r.requestId]" maxlength="1000" required rows="3"></textarea></label><button class="primary" :disabled="busy">保存回复</button></form></article><p v-if="error" class="error">{{error}}</p><p v-if="notice">{{notice}}</p></div></section></template>
<script setup>
import {ref,reactive,onMounted} from 'vue'
import {useRouter} from 'vue-router'
import axios from 'axios'
const rows=ref([]),drafts=reactive({}),busy=ref(false),error=ref(''),notice=ref(''),router=useRouter()
async function call(path,data={}){try{return (await axios.post('/tijiancms/healthServices/'+path,data)).data}catch(e){if(e.response?.status===401){sessionStorage.removeItem('tijianDoctor');router.replace('/login');window.location.reload()}throw e}}
async function load(){try{rows.value=await call('requests')}catch(e){error.value='申请列表加载失败。'}}
async function send(r){if(busy.value)return;busy.value=true;try{await call('reply',{requestId:r.requestId,reply:drafts[r.requestId]});drafts[r.requestId]='';await load();notice.value='回复已保存。';error.value=''}catch(e){error.value='回复未保存，请重试。'}finally{busy.value=false}}
onMounted(load)
</script>
<style scoped>.desk{padding:24px}.desk article{padding:20px 0;border-bottom:1px solid #edf3f0}.desk h3{font-size:16px}.desk article p{line-height:1.7;margin:10px 0;white-space:pre-wrap}.desk form{display:grid;gap:10px;max-width:650px}.desk form button{justify-self:start}</style>