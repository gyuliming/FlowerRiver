import http from './http.js'

// 출고 목록 조회
export function fetchOutbounds({ warehouseId, page, size }) {
    return http.get('/outbound', {
        params: { warehouseId, page, size }
    })
}

// 출고 등록
export function createOutbound(data) {
    return http.post('/outbound', data)
}