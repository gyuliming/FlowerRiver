import { createRouter, createWebHistory } from 'vue-router'
import WarehouseList from '../views/WarehouseList.vue'
import WarehouseDetail from '../views/WarehouseDetail.vue'

const routes = [
    { path: '/', redirect: '/warehouses' },
    { path: '/warehouses', component: WarehouseList, meta: { title: '창고 목록' } },
    { path: '/warehouses/:id', component: WarehouseDetail, props: true, meta: { title: '창고 상세' } }
]

export default createRouter({
    history: createWebHistory(),
    routes
})