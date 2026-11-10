<template><section><div class="page-head"><div><h2>医生预约排队</h2><p class="sub">按日期及医生编号查看项目内预约。</p></div><button class="secondary" @click="load">↻ 刷新</button></div><div class="panel table-wrap"><table><thead><tr><th>日期</th><th>医生编号</th><th>排队号</th><th>预约人</th><th>状态</th></tr></thead><tbody><tr v-for="a in rows" :key="a.appointmentId"><td>{{String(a.appointmentDate).slice(0,10)}}</td><td>{{a.profileId}}</td><td>{{a.queueNo}}</td><td>{{a.realName}}</td><td>已预约</td></tr></tbody></table><p v-if="!rows.length" class="empty">暂无医生预约</p><p v-if="error" class="error">{{error}}</p></div></section></template>
<script setup>
import {ref,onMounted} from 'vue'
import axios from 'axios'
import {useRouter} from 'vue-router'
const rows=ref([]),error=ref(''),router=useRouter()
async function load(){try{rows.value=(await axios.post('/tijiancms/doctorAppointments/list',{})).data;error.value=''}catch(e){if(e.response?.status===401){sessionStorage.removeItem('tijianDoctor');router.replace('/login');window.location.reload()}else error.value='预约列表加载失败。'}}
onMounted(load)
</script>