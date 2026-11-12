<template><div class="recovery"><section><p>东软体检</p><h1>找回密码</h1><p>输入注册手机号提交申请。请联系管理员核验身份后处理，处理完成后使用新密码登录。</p><form @submit.prevent="submit"><label>注册手机号<input v-model.trim="userId" required inputmode="numeric" pattern="[0-9]{11}" maxlength="11" placeholder="请输入 11 位手机号"></label><button class="primary" :disabled="busy">{{busy?'提交中…':'提交申请'}}</button></form><p v-if="message" role="status">{{message}}</p><router-link to="/login">← 返回登录</router-link></section></div></template>
<script setup>
import {ref} from 'vue'
import axios from 'axios'
const userId=ref(''),busy=ref(false),message=ref('')
async function submit(){busy.value=true;message.value='';try{await axios.post('/tijian/passwordRecovery/request',{userId:userId.value});message.value='如该账号存在，申请已提交。请联系管理员核验并处理。'}catch(e){message.value='申请未提交，请检查手机号或服务。'}finally{busy.value=false}}
</script>
<style scoped>.recovery{min-height:100vh;display:grid;place-items:center;padding:20px}.recovery section{max-width:440px;width:100%;padding:38px;background:white;border:1px solid #e3eeeb;border-radius:16px}.recovery h1{margin:15px 0}.recovery p{line-height:1.7;color:#6b8880;margin:12px 0}.recovery form{display:grid;gap:20px;margin:25px 0}.recovery label{display:grid;gap:8px}.recovery input{padding:12px;border:1px solid #dcebe4;border-radius:8px}.recovery a{color:#138c75}</style>