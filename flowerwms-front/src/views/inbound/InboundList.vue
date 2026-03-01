<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <div style="display:flex; gap:8px; align-items:center;">
            <el-select v-model="warehouseId" placeholder="창고 선택" clearable style="width: 160px;">
              <el-option
                  v-for="w in warehouseOptions"
                  :key="w.id"
                  :label="w.name"
                  :value="w.id"
              />
            </el-select>
            <el-button type="primary" @click="search">검색</el-button>
          </div>
        </div>
      </template>

      <el-table :data="rows" v-loading="loading" style="width: 100%" border>
        <el-table-column prop="code" label="입고 코드" min-width="160" />
        <el-table-column prop="warehouseName" label="창고" min-width="150" />
        <el-table-column prop="sectionCode" label="구역" min-width="150" />
        <el-table-column prop="palletCode" label="팔레트" min-width="130" />
        <el-table-column prop="productName" label="상품명" min-width="150" />
        <el-table-column prop="boxQty" label="수량(박스)" min-width="110" align="right" />
        <el-table-column label="입고일시" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
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
import { fetchInbounds } from '../../api/inboundApi'
import { fetchWarehouses } from '../../api/warehouseApi'

const router = useRouter()

const warehouseId = ref(null)
const warehouseOptions = ref([])
const page = ref(1)
const size = ref(10)
const rows = ref([])
const totalElements = ref(0)
const loading = ref(false)
const error = ref('')

function formatDate(dt) {
  if (!dt) return ''
  return dt.replace('T', ' ').substring(0, 19)
}

function goRegister() {
  router.push('/inbound/register')
}

async function loadWarehouses() {
  const res = await fetchWarehouses({ page: 1, size: 100 })
  warehouseOptions.value = res.data.data.items
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await fetchInbounds({
      warehouseId: warehouseId.value,
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

onMounted(async () => {
  await loadWarehouses()
  load()
})
</script>