import { createRouter, createWebHashHistory } from 'vue-router'
const routes = [
  { path: '/', redirect: '/ordersList' },
  ...['login', 'ordersList', 'ordersContent', 'consultations', 'doctorQueue', 'serviceRequests'].map(name => ({ path: '/' + name, name, component: { render: () => null } }))
]
const router = createRouter({ history: createWebHashHistory(), routes })
router.beforeEach(to => {
  if (to.name !== 'login' && !sessionStorage.getItem('tijianDoctor')) return { name: 'login' }
})
export default router
