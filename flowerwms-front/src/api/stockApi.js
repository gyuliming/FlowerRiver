import http from './http.js'

// 재고 있는 상품 목록
export function fetchStockProducts(warehouseId = null) {
    return http.get('/stocks/products', { params: { warehouseId } })
}

// 해당 상품 보유 창고 목록
export function fetchStockWarehouses(productId) {
    return http.get('/stocks/warehouses', {
        params: { productId }
    })
}

// 재고 목록
export function fetchStocks({ warehouseId, productId, page, size }) {
    return http.get('/stocks', {
        params: { warehouseId, productId, page, size }
    })
}

// 재고 이력
export function fetchStockHistories({ warehouseId, productId, page, size }) {
    return http.get('/stocks/history', {
        params: { warehouseId, productId, page, size }
    })
}