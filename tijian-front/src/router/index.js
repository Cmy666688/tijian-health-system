import { createRouter, createWebHashHistory } from 'vue-router'
const routes = [
  { path: '/', redirect: '/home' },
  ...['login','register','home','appointment','hospital','setmeal','selectDate','confirmOrder','appointmentSuccess','appointmentList','reportList','report','personal','doctors','doctor','consultation','doctorAppointments','assessments','healthNews'].map(name => ({ path: '/' + name, name, component: { render: () => null } }))
]
const router = createRouter({ history: createWebHashHistory(), routes })
router.beforeEach(to => {
  if (!['login','register'].includes(to.name) && !sessionStorage.getItem('tijianUser')) return { name: 'login' }
})
export default router
