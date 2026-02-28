import { createRouter, createWebHistory } from 'vue-router'
import WarehouseList from '../views/warehouse/WarehouseList.vue'
import WarehouseDetail from '../views/warehouse/WarehouseDetail.vue'
import WarehouseRegister from '../views/warehouse/WarehouseRegister.vue'

const routes = [
    { path: '/', redirect: '/warehouses' },
    { path: '/warehouses', component: WarehouseList, meta: { title: '창고 목록' } },
    { path: '/warehouses/register', component: WarehouseRegister, meta: { title: '창고 등록' } },
    { path: '/warehouses/:id', component: WarehouseDetail, props: true, meta: { title: '창고 상세' } }
]

export default createRouter({
    history: createWebHistory(),
    routes
})