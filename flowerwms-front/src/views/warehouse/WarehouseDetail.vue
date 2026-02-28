<template>
  <div>
    <!-- 창고 정보 -->
    <el-card style="margin-bottom: 16px;">
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-weight: 600;">창고 정보</span>
          <div style="display:flex; gap:8px;">
            <el-button type="primary" @click="openEditModal" :disabled="warehouse.status === 'CLOSED'">수정</el-button>
            <el-button type="danger" @click="confirmClose" :disabled="warehouse.status === 'CLOSED'">폐쇄</el-button>
          </div>
        </div>
      </template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="코드">{{ warehouse.code }}</el-descriptions-item>
        <el-descriptions-item label="창고명">{{ warehouse.name }}</el-descriptions-item>
        <el-descriptions-item label="상태">
          <el-tag :type="warehouse.status === 'NORMAL' ? 'success' : 'danger'">
            {{ warehouse.status === 'NORMAL' ? '정상' : '폐쇄' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="주소">{{ warehouse.address }}</el-descriptions-item>
        <el-descriptions-item label="총용량">{{ warehouse.totalCapacityBox }} 박스</el-descriptions-item>
        <el-descriptions-item label="사용량">{{ warehouse.usedBox }} 박스</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 카카오맵 -->
    <el-card style="margin-bottom: 16px;">
      <template #header>
        <span style="font-weight: 600;">위치</span>
      </template>
      <div id="map" style="width: 100%; height: 400px;" />
    </el-card>

    <!-- 구역 현황 -->
    <el-card style="margin-bottom: 16px;">
      <template #header>
        <span style="font-weight: 600;">구역 현황</span>
      </template>
      <el-row :gutter="16">
        <el-col :span="12" v-for="section in warehouse.sections" :key="section.sectionId">
          <el-card shadow="hover">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="구역 코드">{{ section.sectionCode }}</el-descriptions-item>
              <el-descriptions-item label="타입">
                <el-tag :type="section.type === 'COLD' ? 'primary' : 'warning'">
                  {{ section.type === 'COLD' ? '저온' : '상온' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="팔레트 수">{{ section.palletCount }} 개</el-descriptions-item>
              <el-descriptions-item label="총용량">{{ section.totalCapacityBox }} 박스</el-descriptions-item>
              <el-descriptions-item label="사용량">{{ section.usedBox }} 박스</el-descriptions-item>
              <el-descriptions-item label="사용률">
                {{ usageRate(section.usedBox, section.totalCapacityBox) }} %
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 수정 모달 -->
    <el-dialog v-model="editModal" title="창고 수정" width="500px">
      <el-form :model="editForm" label-width="100px" hide-required-asterisk>
        <el-form-item label="창고명">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="주소">
          <div style="display:flex; gap:8px; width:100%;">
            <el-input v-model="editForm.address" readonly />
            <el-button @click="searchEditAddress">주소 검색</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editModal = false">취소</el-button>
        <el-button type="primary" @click="submitEdit">수정</el-button>
      </template>
    </el-dialog>
  </div>
</template>


<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {closeWarehouse, fetchWarehouseDetail, updateWarehouse} from '../../api/warehouseApi.js'

const props = defineProps({ id: String })

const warehouse = ref({
  code: '',
  name: '',
  address: '',
  status: '',
  totalCapacityBox: 0,
  usedBox: 0,
  sections: []
})

const editModal = ref(false)
const editForm = ref({ name: '', address: '' })

function usageRate(used, total) {
  if (!total || total <= 0) return '0.0'
  return ((used / total) * 100).toFixed(1)
}

function openEditModal() {
  editForm.value.name = warehouse.value.name
  editForm.value.address = warehouse.value.address
  editModal.value = true
}

async function submitEdit() {
  try {
    await updateWarehouse(props.id, editForm.value)
    ElMessage.success('창고 수정 완료')
    editModal.value = false
    const res = await fetchWarehouseDetail(props.id)
    warehouse.value = res.data.data
    initMap(warehouse.value.address)  // 지도 갱신
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '수정 실패')
  }
}

function confirmClose() {
  ElMessageBox.confirm('창고를 폐쇄하시겠습니까?', '창고 폐쇄', {
    confirmButtonText: '예',
    cancelButtonText: '아니오',
    type: 'warning'
  }).then(async () => {
    try {
      await closeWarehouse(props.id)
      ElMessage.success('창고 폐쇄 완료')
      const res = await fetchWarehouseDetail(props.id)
      warehouse.value = res.data.data
    } catch (e) {
      ElMessage.error(e?.response?.data?.message || '폐쇄 실패')
    }
  }).catch(() => {})
}

function searchEditAddress() {
  new window.daum.Postcode({
    oncomplete: (data) => {
      editForm.value.address = data.roadAddress || data.jibunAddress
    }
  }).open()
}

function initMap(address) {
  window.kakao.maps.load(() => {
    const container = document.getElementById('map')
    const options = { center: new window.kakao.maps.LatLng(37.5665, 126.9780), level: 1 }
    const map = new window.kakao.maps.Map(container, options)

    const geocoder = new window.kakao.maps.services.Geocoder()
    geocoder.addressSearch(address, (result, status) => {
      if (status === window.kakao.maps.services.Status.OK) {
        const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x)
        new window.kakao.maps.Marker({ map, position: coords })
        map.setCenter(coords)
      }
    })
  })
}

onMounted(async () => {
  const res = await fetchWarehouseDetail(props.id)
  warehouse.value = res.data.data
  initMap(warehouse.value.address)
})
</script>
