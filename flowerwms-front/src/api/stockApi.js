import http from './http.js'

// 재고 있는 상품 목록
export function fetchStockProducts() {
    return http.get('/stocks/products')
}

// 해당 상품 보유 창고 목록
export function fetchStockWarehouses(productId) {
    return http.get('/stocks/warehouses', {
        params: { productId }
    })
}