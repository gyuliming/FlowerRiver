import { createRouter, createWebHistory } from 'vue-router'
import WarehouseList from '../views/warehouse/WarehouseList.vue'
import WarehouseDetail from '../views/warehouse/WarehouseDetail.vue'
import WarehouseRegister from '../views/warehouse/WarehouseRegister.vue'

import ProductList from '../views/product/ProductList.vue'
import ProductRegister from '../views/product/ProductRegister.vue'

import InboundList from '../views/inbound/InboundList.vue'
import InboundRegister from '../views/inbound/InboundRegister.vue'

import OutboundList from '../views/outbound/OutboundList.vue'
import OutboundRegister from '../views/outbound/OutboundRegister.vue'

const routes = [
    { path: '/', redirect: '/warehouses' },
    { path: '/warehouses', component: WarehouseList, meta: { title: '창고 목록' } },
    { path: '/warehouses/register', component: WarehouseRegister, meta: { title: '창고 등록' } },
    { path: '/warehouses/:id', component: WarehouseDetail, props: true, meta: { title: '창고 상세' } },

    { path: '/products', component: ProductList, meta: { title: '상품 목록' } },
    { path: '/products/register', component: ProductRegister, meta: { title: '상품 등록' } },

    { path: '/inbound', component: InboundList, meta: { title: '입고 목록' } },
    { path: '/inbound/register', component: InboundRegister, meta: { title: '입고 등록' } },

    { path: '/outbound', component: OutboundList, meta: { title: '출고 목록' } },
    { path: '/outbound/register', component: OutboundRegister, meta: { title: '출고 등록' } },
]

export default createRouter({
    history: createWebHistory(),
    routes
})