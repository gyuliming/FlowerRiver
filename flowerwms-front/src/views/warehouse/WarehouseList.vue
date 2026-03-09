<template>
  <el-card>
    <template #header>
      <div style="display:flex; justify-content:space-between; align-items:center;">
        <span style="font-weight:600;">창고 목록</span>
        <div style="display:flex; gap:8px; align-items:center;">
          <el-input v-model="keyword" placeholder="검색어를 입력해주세요." clearable @keyup.enter="search" style="max-width:300px;" />
          <el-select v-model="searchType" style="width:120px;">
            <el-option label="전체" value="all" />
            <el-option label="코드" value="code" />
            <el-option label="창고명" value="name" />
            <el-option label="주소" value="address" />
          </el-select>
          <el-button type="primary" @click="search">검색</el-button>
        </div>
      </div>
    </template>

    <el-table :data="rows" v-loading="loading" style="width: 100%" border>
      <el-table-column prop="code" label="코드" width="130" />
      <el-table-column prop="name" label="창고명" width="180" />
      <el-table-column prop="address" label="주소" min-width="200" />
      <el-table-column label="상태" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 'NORMAL' ? 'success' : 'danger'">
            {{ row.status === 'NORMAL' ? '정상' : '폐쇄' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalCapacityBox" label="총용량" width="120" align="right" />
      <el-table-column prop="usedBox" label="사용량" width="120" align="right" />
      <el-table-column label="사용률" width="120" align="right">
        <template #default="{ row }">
          {{ usageRate(row.usedBox, row.totalCapacityBox) }} %
        </template>
      </el-table-column>
      <el-table-column label="" width="90" align="center">
        <template #default="{ row }">
          <el-button size="small" @click="goDetail(row.id)">상세</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex; justify-content:flex-end; margin-top: 12px;">
      <el-pagination
          background
          layout="prev, pager, next, jumper, total"
          :current-page="page"
          :page-size="size"
          :total="totalElements"
          @current-change="onPageChange"
      />
    </div>

    <el-alert v-if="error" type="error" :title="error" show-icon style="margin-top:12px;" />
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchWarehouses } from '../../api/warehouseApi.js'

const router = useRouter()

const keyword = ref('')
const page = ref(1)
const size = ref(10)

const rows = ref([])
const totalElements = ref(0)

const loading = ref(false)
const error = ref('')

function usageRate(used, total) {
  if (!total || total <= 0) return '0.0'
  return ((used / total) * 100).toFixed(1)
}

function goDetail(id) {
  router.push(`/warehouses/${id}`)
}

const searchType = ref('all')

async function load() {
  loading.value = true
  error.value = ''
  try {
    const params = {
      page: page.value,
      size: size.value
    }

    // searchType에 따라 파라미터 다르게 -> 전체, 코드, 이름, 주소
    if (keyword.value) {
      if (searchType.value === 'all') {
        params.keyword = keyword.value
      } else if (searchType.value === 'code') {
        params.code = keyword.value
      } else if (searchType.value === 'name') {
        params.name = keyword.value
      } else if (searchType.value === 'address') {
        params.address = keyword.value
      }
    }

    const res = await fetchWarehouses(params)
    const data = res.data?.data
    rows.value = data?.items ?? []
    totalElements.value = data?.total ?? 0
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || '요청 실패'
  } finally {
    loading.value = false
  }
}

function search() {
  page.value = 1
  load()
}

function onPageChange(p) {
  page.value = p
  load()
}

onMounted(load)
</script>