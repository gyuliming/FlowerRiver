import http from './http.js'

export function fetchWarehouses({ keyword, code, name, address, page, size }) {
    return http.get('/warehouses', {
        params: { keyword, code, name, address, page, size }
    })
}

export function fetchWarehouseDetail(id) {
    return http.get(`/warehouses/${id}`)
}