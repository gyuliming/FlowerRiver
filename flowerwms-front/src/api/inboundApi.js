import http from './http.js'

// 입고 목록 조회
export function fetchInbounds({ warehouseId, page, size }) {
    return http.get('/inbound', {
        params: { warehouseId, page, size }
    })
}

// 입고 등록
export function createInbound(data) {
    return http.post('/inbound', data)
}