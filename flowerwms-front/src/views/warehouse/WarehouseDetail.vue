<template>
  <div>
    <!-- 창고 정보 -->
    <el-card style="margin-bottom: 16px;">
      <template #header>
        <span style="font-weight: 600;">창고 정보</span>
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
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { fetchWarehouseDetail } from '../../api/warehouseApi.js'

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

function usageRate(used, total) {
  if (!total || total <= 0) return '0.0'
  return ((used / total) * 100).toFixed(1)
}

function initMap(address) {
  window.kakao.maps.load(() => {
    const container = document.getElementById('map')
    const options = { center: new window.kakao.maps.LatLng(37.5665, 126.9780), level: 1 }
    const map = new window.kakao.maps.Map(container, options)

    // 주소로 좌표 변환
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
