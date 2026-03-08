import http from "./http.js";

export function getMyInfo() {
    return http.get("/members/me")
}

export function updateMyInfo(data) {
    return http.put("/members/me", data)
}