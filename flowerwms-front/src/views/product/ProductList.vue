<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <div style="display:flex; gap:8px; align-items:center;">
            <el-input
                v-model="keyword"
                placeholder="상품 코드 / 상품명 검색"
                clearable
                @keyup.enter="search"
                style="max-width: 300px;"
            />
            <el-select v-model="type" placeholder="종류" clearable style="width: 120px;">
              <el-option label="절화" value="CUT" />
              <el-option label="화분" value="POTTED" />
              <el-option label="가공화" value="PROCESSED" />
            </el-select>
            <el-button type="primary" @click="search">검색</el-button>
          </div>
        </div>
      </template>

      <el-table :data="rows" v-loading="loading" style="width: 100%" border>
        <el-table-column prop="code" label="코드" min-width="130" />
        <el-table-column prop="name" label="상품명" min-width="180" />
        <el-table-column label="종류" min-width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="typeTag(row.type).color">{{ typeTag(row.type).label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="보관타입" min-width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.storageType === 'COLD' ? 'primary' : 'warning'">
              {{ row.storageType === 'COLD' ? '저온' : '상온' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pricePerBox" label="박스당 가격" min-width="130" align="right" />
        <el-table-column label="박스당 수량" min-width="130" align="right">
          <template #default="{ row }">
            {{ row.qtyPerBox }} {{ row.unit }}
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { fetchProducts } from '../../api/productApi'

const router = useRouter()

const keyword = ref('')
const type = ref('')
const page = ref(1)
const size = ref(10)
const rows = ref([])
const totalElements = ref(0)
const loading = ref(false)
const error = ref('')

function typeTag(type) {
  if (type === 'CUT') return { label: '절화', color: 'success' }
  if (type === 'POTTED') return { label: '화분', color: 'warning' }
  if (type === 'PROCESSED') return { label: '가공화', color: 'info' }
  return { label: type, color: '' }
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await fetchProducts({
      keyword: keyword.value,
      type: type.value || null,
      page: page.value,
      size: size.value
    })
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