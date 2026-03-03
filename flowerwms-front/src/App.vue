<template>
  <el-container style="min-height: 100vh">
    <el-aside width="220px" style="border-right: 1px solid #eee; background-color: #404F7B;">
      <div style="padding:0; text-align:center; cursor:pointer;" @click="$router.push('/dashboard')">
        <img :src="logo" style="width: 220px; display:block;" />
      </div>
      <el-divider style="margin: 1px 0; border-color: #D5B270;" />
      <el-menu router :default-active="$route.path" background-color="#42507C" text-color="#D5B270" active-text-color="#ffffff">
        <el-menu-item index="/dashboard">
          <template #title>대시보드</template>
        </el-menu-item>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-menu-item index="user">
          <template #title>회원 목록</template>
        </el-menu-item>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="warehouse">
          <template #title>창고 관리</template>
          <el-menu-item index="/warehouses">창고 목록</el-menu-item>
          <el-menu-item index="/warehouses/register">창고 등록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="product">
          <template #title>상품 관리</template>
          <el-menu-item index="/products">상품 목록</el-menu-item>
          <el-menu-item index="/products/register">상품 등록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="inbound">
          <template #title>입고 관리</template>
          <el-menu-item index="/inbound">입고 목록</el-menu-item>
          <el-menu-item index="/inbound/register">입고 등록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="stock">
          <template #title>재고 관리</template>
          <el-menu-item index="/stocks">재고 목록</el-menu-item>
          <el-menu-item index="/stocks/history">재고 이력</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="outbound">
          <template #title>출고 관리</template>
          <el-menu-item index="/outbound">출고 목록</el-menu-item>
          <el-menu-item index="/outbound/register">출고 등록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

      </el-menu>
    </el-aside>



    <el-container>
      <el-header style="border-bottom: 1px solid #eee; display:flex; align-items:center;">
        <div style="font-weight:600;">{{ $route.meta.title ?? '관리자 페이지' }}</div>
      </el-header>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import logo from './assets/FlowerRiver.png'
</script>

<style>
.el-menu-item.is-active {
  color: #D5B270;
  background-color: #42507C;
}
</style>

