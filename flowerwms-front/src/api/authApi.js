import http from './http.js'

export function login(data) {
    return http.post('/auth/login', data)
}

export function register(data) {
    return http.post('/members/register', data)
}