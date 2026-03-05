import { reactive } from 'vue'

const state = reactive({
    token: sessionStorage.getItem('token') || null,
    memberId: sessionStorage.getItem('memberId') || null,
    loginId: sessionStorage.getItem('loginId') || null,
    name: sessionStorage.getItem('name') || null,
    role: sessionStorage.getItem('role') || null,
    warehouseId: sessionStorage.getItem('warehouseId') || null
})

export function useAuth() {
    function login(data) {
        state.token = data.token
        state.memberId = data.memberId
        state.loginId = data.loginId
        state.name = data.name
        state.role = data.role
        state.warehouseId = data.warehouseId

        sessionStorage.setItem('token', data.token)
        sessionStorage.setItem('memberId', data.memberId)
        sessionStorage.setItem('loginId', data.loginId)
        sessionStorage.setItem('name', data.name)
        sessionStorage.setItem('role', data.role)
        sessionStorage.setItem('warehouseId', data.warehouseId)
    }

    function logout() {
        state.token = null
        state.memberId = null
        state.loginId = null
        state.name = null
        state.role = null
        state.warehouseId = null

        sessionStorage.clear()
    }

    function isLoggedIn() {
        return !!state.token
    }

    function isAdmin() {
        return state.role === 'ADMIN'
    }

    function isManager() {
        return state.role === 'MANAGER'
    }

    return { state, login, logout, isLoggedIn, isAdmin, isManager }
}