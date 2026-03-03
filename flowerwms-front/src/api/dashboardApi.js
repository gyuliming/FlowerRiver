import http from './http.js'

export function fetchDashboardSummary() {
    return http.get('/dashboard/summary')
}

export function fetchWarehouseUsage() {
    return http.get('/dashboard/warehouse-usage')
}

export function fetchRecentInbound() {
    return http.get('/dashboard/recent-inbound')
}

export function fetchRecentOutbound() {
    return http.get('/dashboard/recent-outbound')
}