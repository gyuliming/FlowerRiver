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

// 창고 등록
export function fetchWarehouseRegister(data) {
    return http.post(`/warehouses`, data)
}

// 창고 수정
export function updateWarehouse(id, data) {
    return http.put(`/warehouses/${id}`, data)
}

// 창고 폐쇄
export function closeWarehouse(id) {
    return http.patch(`/warehouses/${id}/close`)
}
