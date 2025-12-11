import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/LoginView',
    name: 'LoginView',
    component: () => import('../views/EBuyPlt/LoginView.vue')
  },
  {
    path: '/ShoppingnbView',
    name: 'ShoppingnbView',
    component: () => import('../views/EBuyPlt/ShoppingnbView.vue')
  },
  {
    path:'/ProductDetailView/:pID',
    name:'ProductDetailView',
    component:()=>import('../views/EBuyPlt/ProductDetailView.vue')
  },
  {
    path:'/OrderListView',
    name:'OrderListView',
    component:()=>import('../views/EBuyPlt/OrderListView.vue')
  },
  {
    path:'/MerchantView',
    name:'MerchantView',
    component:()=>import('../views/EBuyPlt/MerchantView.vue')
  },
  {
    path:'/AdminView',
    name:'AdminView',
    component:()=>import('../views/EBuyPlt/AdminView.vue')
  },
  {
    path:'/UserProfileView',
    name:'UserProfileView',
    component:()=>import('../views/EBuyPlt/UserProfileView.vue')
  },
  {
    path: '/',
    redirect: '/LoginView'
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
