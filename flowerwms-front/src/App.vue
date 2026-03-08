<template>
  <div v-if="!isLoginPage">
    <el-container style="height: 100vh;">
      <!-- 사이드바 -->
      <el-aside width="220px" style="background: linear-gradient(180deg, #2C3E6B 0%, #1a2a4a 100%); overflow-y: auto; overflow-x: hidden">
        <div style="padding:0; text-align:center; cursor:pointer;" @click="$router.push('/dashboard')">
          <img :src="logo" style="width: 220px; display:block;" />
        </div>

        <el-menu
            router
            :default-active="$route.path"
            background-color="transparent"
            text-color="#C8D0E0"
            active-text-color="#FFD700"
            style="border-right: none;"
        >

          <el-menu-item index="/my-page">내 정보</el-menu-item>
          <el-divider style="margin: 1px 0; border-color: #D5B270;" />

          <el-menu-item index="/dashboard">대시보드</el-menu-item>
          <el-divider style="margin: 1px 0; border-color: #D5B270;" />

          <!-- ADMIN만 보임 -->
          <template v-if="isAdmin()">
              <el-menu-item index="/members">회원 목록</el-menu-item>
              <el-divider style="margin: 1px 0; border-color: #D5B270;" />
          </template>

          <el-sub-menu index="warehouse">
            <template #title>창고 관리</template>
            <el-menu-item index="/warehouses">창고 목록</el-menu-item>
            <el-menu-item v-if="isAdmin()" index="/warehouses/register">창고 등록</el-menu-item>
          </el-sub-menu>
          <el-divider style="margin: 1px 0; border-color: #D5B270;" />

          <el-sub-menu index="product">
            <template #title>상품 관리</template>
            <el-menu-item index="/products">상품 목록</el-menu-item>
            <el-menu-item v-if="isAdmin()" index="/products/register">상품 등록</el-menu-item>
          </el-sub-menu>
          <el-divider style="margin: 1px 0; border-color: #D5B270;" />

          <el-sub-menu index="inbound">
            <template #title>입고 관리</template>
            <el-menu-item index="/inbound">입고 목록</el-menu-item>
            <el-menu-item index="/inbound/register">입고 등록</el-menu-item>
          </el-sub-menu>
          <el-divider style="margin: 1px 0; border-color: #D5B270;" />

          <el-sub-menu index="outbound">
            <template #title>출고 관리</template>
            <el-menu-item index="/outbound">출고 목록</el-menu-item>
            <el-menu-item index="/outbound/register">출고 등록</el-menu-item>
          </el-sub-menu>
          <el-divider style="margin: 1px 0; border-color: #D5B270;" />

          <el-sub-menu index="stock">
            <template #title>재고 관리</template>
            <el-menu-item index="/stocks">재고 현황</el-menu-item>
            <el-menu-item index="/stocks/history">재고 이력</el-menu-item>
          </el-sub-menu>
          <el-divider style="margin: 1px 0; border-color: #D5B270;" />
        </el-menu>
      </el-aside>

      <el-container>
        <!-- 헤더 -->
        <el-header style="background:#fff; border-bottom:1px solid #eee; display:flex; align-items:center; justify-content:space-between;">
          <span style="font-size:16px; font-weight:600; color:#404F7B;">{{ $route.meta.title }}</span>
          <div style="display:flex; align-items:center; gap:12px;">
            <span style="font-size:14px; color:#666;">
              {{ auth.state.name }} ({{ auth.state.role === 'ADMIN' ? '총관리자' : '창고관리자' }})
            </span>
            <el-button size="small" @click="handleLogout">로그아웃</el-button>
          </div>
        </el-header>

        <el-main style="background:#f5f5f5; padding:20px;">
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </div>

  <div v-else>
    <RouterView />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuth } from './stores/auth'
import logo from './assets/FlowerRiver.png'

const route = useRoute()
const router = useRouter()
const auth = useAuth()
const { isAdmin, logout } = auth

const isLoginPage = computed(() => {
  return route.path === '/login' || route.path === '/register'
})

function handleLogout() {
  logout()
  ElMessage.success('로그아웃 되었습니다.')
  router.push('/login')
}
</script>