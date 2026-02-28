import http from './http.js'

// 창고 목록
export function fetchWarehouses({ keyword, code, name, address, page, size }) {
    return http.get('/warehouses', {
        params: { keyword, code, name, address, page, size }
    })
}

// 창고 상세
export function fetchWarehouseDetail(id) {
    return http.get(`/warehouses/${id}`)
}
