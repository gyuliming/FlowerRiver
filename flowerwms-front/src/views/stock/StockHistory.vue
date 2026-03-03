<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-weight: 600;">재고 이력</span>
          <div style="display:flex; gap:8px;">
            <el-select v-model="warehouseId" placeholder="창고 선택" clearable style="width:160px;">
              <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.name" :value="w.id" />
            </el-select>
            <el-select v-model="productId" placeholder="상품 선택" clearable style="width:160px;">
              <el-option v-for="p in productOptions" :key="p.id" :label="p.name" :value="p.id" />
            </el-select>
            <el-button type="primary" @click="search">검색</el-button>
          </div>
        </div>
      </template>

      <el-table :data="rows" v-loading="loading" style="width:100%" border>
        <el-table-column prop="warehouseName" label="창고" min-width="150" />
        <el-table-column prop="productName" label="상품명" min-width="150" />
        <el-table-column label="유형" min-width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 'INBOUND' ? 'success' : 'danger'">
              {{ row.type === 'INBOUND' ? '입고' : '출고' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="boxQty" label="수량(박스)" min-width="110" align="right" />
        <el-table-column label="일시" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
      </el-table>

      <div style="display:flex; justify-content:flex-end; margin-top:12px;">
        <el-pagination
            background
            layout="prev, pager, next, jumper, total"
            :current-page="page"
            :page-size="size"
            :total="totalElements"
            @current-change="onPageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchStockHistories } from '../../api/stockApi'
import { fetchWarehouses } from '../../api/warehouseApi'
import { fetchProducts } from '../../api/productApi'

const warehouseId = ref(null)
const productId = ref(null)
const warehouseOptions = ref([])
const productOptions = ref([])
const rows = ref([])
const page = ref(1)
const size = ref(10)
const totalElements = ref(0)
const loading = ref(false)

function formatDate(dt) {
  if (!dt) return ''
  return dt.replace('T', ' ').substring(0, 19)
}

async function load() {
  loading.value = true
  try {
    const res = await fetchStockHistories({ warehouseId: warehouseId.value, productId: productId.value, page: page.value, size: size.value })
    const data = res.data?.data
    rows.value = data?.items ?? []
    totalElements.value = data?.total ?? 0
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

onMounted(async () => {
  const [warehouseRes, productRes] = await Promise.all([
    fetchWarehouses({ page: 1, size: 100 }),
    fetchProducts({ page: 1, size: 100 })
  ])
  warehouseOptions.value = warehouseRes.data.data.items
  productOptions.value = productRes.data.data.items
  load()
})
</script>