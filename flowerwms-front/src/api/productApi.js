import http from './http.js'

// 상품 목록 조회
export function fetchProducts({ keyword, type, page, size }) {
    return http.get('/products', {
        params: { keyword, type, page, size }
    })
}

// 상품 등록
export function createProduct(data) {
    return http.post('/products', data)
}