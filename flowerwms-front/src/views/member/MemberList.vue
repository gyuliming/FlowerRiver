<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-weight:600;">회원 목록</span>
          <div style="display:flex; gap:8px;">
            <el-select v-model="status" placeholder="상태 필터" clearable style="width:150px;">
              <el-option label="전체" value="" />
              <el-option label="대기" value="PENDING" />
              <el-option label="승인" value="ACTIVE" />
              <el-option label="거절" value="REJECTED" />
            </el-select>
            <el-button type="primary" @click="search">검색</el-button>
          </div>
        </div>
      </template>

      <el-table :data="rows" v-loading="loading" border style="width:100%">
        <el-table-column prop="loginId" label="아이디" min-width="120" />
        <el-table-column prop="name" label="이름" min-width="100" />
        <el-table-column prop="phone" label="전화번호" min-width="130" />
        <el-table-column prop="email" label="이메일" min-width="180" />
        <el-table-column prop="warehouseName" label="담당 창고" min-width="150" />
        <el-table-column prop="createdAt" label="가입일자" min-width="180" />
        <el-table-column label="상태" min-width="180" align="center">
          <template #default="{ row }">
            <template v-if="row.status === 'PENDING'">
              <el-button size="small" type="success" @click="openApprove(row)">승인</el-button>
              <el-button size="small" type="danger" @click="handleReject(row.id)">거절</el-button>
            </template>
            <template v-else-if="row.status === 'ACTIVE'">
              <el-tag type="success">승인완료</el-tag>
            </template>
            <template v-else-if="row.status === 'REJECTED'">
              <el-tag type="danger">거절됨</el-tag>
            </template>
          </template>
        </el-table-column>
      </el-table>

      <div style="display:flex; justify-content:flex-end; margin-top:12px;">
        <el-pagination
            background
            layout="prev, pager, next, total"
            :current-page="page"
            :page-size="size"
            :total="totalElements"
            @current-change="onPageChange"
        />
      </div>
    </el-card>

    <!-- 승인 다이얼로그 -->
    <el-dialog v-model="approveDialog" title="회원 승인" width="400px">
      <el-form label-width="80px">
        <el-form-item label="창고 선택">
          <el-select v-model="selectedWarehouseId" placeholder="창고 선택" style="width:100%;">
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.name" :value="w.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveDialog = false">취소</el-button>
        <el-button type="primary" @click="handleApprove">승인</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../../api/http.js'
import { fetchWarehouses } from '../../api/warehouseApi'

const status = ref('')
const rows = ref([])
const page = ref(1)
const size = ref(10)
const totalElements = ref(0)
const loading = ref(false)
const approveDialog = ref(false)
const selectedMemberId = ref(null)
const selectedWarehouseId = ref(null)
const warehouseOptions = ref([])

function statusTag(s) {
  if (s === 'PENDING') return { label: '대기', color: 'warning' }
  if (s === 'ACTIVE') return { label: '승인', color: 'success' }
  if (s === 'REJECTED') return { label: '거절', color: 'danger' }
  return { label: s, color: '' }
}

async function load() {
  loading.value = true
  try {
    const res = await http.get('/members', {
      params: { status: status.value || null, page: page.value, size: size.value }
    })
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

function openApprove(row) {
  selectedMemberId.value = row.id
  selectedWarehouseId.value = null
  approveDialog.value = true
}

async function handleApprove() {
  if (!selectedWarehouseId.value) {
    ElMessage.warning('창고를 선택해주세요.')
    return
  }
  try {
    await http.patch(`/members/${selectedMemberId.value}/approve`, { warehouseId: selectedWarehouseId.value })
    ElMessage.success('승인되었습니다.')
    approveDialog.value = false
    load()
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '승인 실패')
  }
}

async function handleReject(memberId) {
  await ElMessageBox.confirm('정말 거절하시겠습니까?', '거절 확인', { type: 'warning' })
  try {
    await http.patch(`/members/${memberId}/reject`)
    ElMessage.success('거절되었습니다.')
    load()
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '거절 실패')
  }
}

onMounted(async () => {
  try {
    const warehouseRes = await fetchWarehouses({ page: 1, size: 100 })
    warehouseOptions.value = warehouseRes.data.data.items.filter(w => w.status === 'NORMAL')
  } catch (e) {
    console.error('창고 로딩 실패', e)
  }
  load()
})
</script>