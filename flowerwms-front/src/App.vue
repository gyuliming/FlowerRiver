<template>
  <el-container style="min-height: 100vh">
    <el-aside width="220px" style="border-right: 1px solid #eee; background-color: #404F7B;">
      <div style="padding:0; text-align:center;">
        <img :src="logo" style="width: 220px; display:block;" />
      </div>
      <el-divider style="margin: 1px 0; border-color: #D5B270;" />
      <el-menu router :default-active="$route.path" background-color="#42507C" text-color="#D5B270" active-text-color="#ffffff">
        <el-sub-menu index="user">
          <template #title>회원 관리</template>
          <el-menu-item index="/user">회원 목록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="warehouse">
          <template #title>창고 관리</template>
          <el-menu-item index="/warehouses">창고 목록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="inbound">
          <template #title>입고 관리</template>
          <el-menu-item index="/inbound">입고 목록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-sub-menu index="outbound">
          <template #title>출고 관리</template>
          <el-menu-item index="/outbound">출고 목록</el-menu-item>
        </el-sub-menu>
        <el-divider style="margin: 1px 0; border-color: #D5B270;" />

        <el-menu-item index="dashboard">
          <template #title>대시보드</template>
        </el-menu-item>
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

