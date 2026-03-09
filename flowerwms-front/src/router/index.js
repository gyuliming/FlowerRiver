    import { createRouter, createWebHistory } from 'vue-router'
    import MyPage from '../views/member/MyPage.vue'

    import Dashboard from "../views/dashboard/Dashboard.vue"

    import WarehouseList from '../views/warehouse/WarehouseList.vue'
    import WarehouseDetail from '../views/warehouse/WarehouseDetail.vue'
    import WarehouseRegister from '../views/warehouse/WarehouseRegister.vue'

    import ProductList from '../views/product/ProductList.vue'
    import ProductRegister from '../views/product/ProductRegister.vue'

    import InboundList from '../views/inbound/InboundList.vue'
    import InboundRegister from '../views/inbound/InboundRegister.vue'

    import OutboundList from '../views/outbound/OutboundList.vue'
    import OutboundRegister from '../views/outbound/OutboundRegister.vue'

    import StockList from '../views/stock/StockList.vue'
    import StockHistory from '../views/stock/StockHistory.vue'

    import MemberList from '../views/member/MemberList.vue'
    import Register from "../views/auth/Register.vue";
    import Login from "../views/auth/Login.vue";

    import { useAuth } from '../stores/auth.js';

    const routes = [
        { path: '/login', component: Login, meta: { public: true, title: '로그인' } },
        { path: '/register', component: Register, meta: { public: true, title: '회원가입' } },

        { path: '/my-page', component: MyPage, meta: { title: '내 정보' } },

        { path: '/', redirect: '/dashboard' },
        { path: '/dashboard', component: Dashboard, meta: { title: '대시보드' } },

        { path: '/members', component: MemberList, meta: { adminOnly: true, title: '회원 관리' } },

        { path: '/warehouses', component: WarehouseList, meta: { title: '창고 관리' } },
        { path: '/warehouses/register', component: WarehouseRegister, meta: { title: '창고 관리' } },
        { path: '/warehouses/:id', component: WarehouseDetail, props: true, meta: { title: '창고 관리' } },

        { path: '/products', component: ProductList, meta: { title: '상품 관리' } },
        { path: '/products/register', component: ProductRegister, meta: { title: '상품 관리' } },

        { path: '/inbound', component: InboundList, meta: { title: '입고 관리' } },
        { path: '/inbound/register', component: InboundRegister, meta: { title: '입고 관리' } },

        { path: '/outbound', component: OutboundList, meta: { title: '출고 관리' } },
        { path: '/outbound/register', component: OutboundRegister, meta: { title: '출고 관리' } },

        { path: '/stocks', component: StockList, meta: { title: '재고 관리' } },
        { path: '/stocks/history', component: StockHistory, meta: { title: '재고 관리' } },
    ]

    const router = createRouter({
        history: createWebHistory(),
        routes
    })

    router.beforeEach((to, from) => {
        const { isLoggedIn, isAdmin } = useAuth()

        if (to.meta.public) {
            if (isLoggedIn()) return '/dashboard'
            return true
        }

        if (!isLoggedIn()) return '/login'

        if (to.meta.adminOnly && !isAdmin()) return '/dashboard'

        return true
    })

    export default router