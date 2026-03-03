<template>
  <div>
    <!-- 창고 등록 폼 -->
    <el-card style="margin-bottom: 16px;">
      <template #header>
        <span style="font-weight: 600;">창고 등록</span>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="max-width: 600px;" hide-required-asterisk>
        <el-form-item label="창고명" prop="name">
          <el-input v-model="form.name" placeholder="예) 강남 창고" />
        </el-form-item>
        <el-form-item label="주소" prop="address">
          <div style="display:flex; gap:8px; width:100%;">
            <el-input v-model="form.address" placeholder="주소 검색 버튼을 클릭해주세요." readonly />
            <el-button @click="searchAddress">주소 검색</el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">등록</el-button>
          <el-button @click="goBack">취소</el-button>
        </el-form-item>
      </el-form>

      <el-alert v-if="error" type="error" :title="error" show-icon style="margin-top:12px;" />
    </el-card>

    <!-- 지도 -->
    <el-card>
      <template #header>
        <span style="font-weight: 600;">위치</span>
      </template>
      <div id="registerMap" style="width:100%; height:400px; border-radius:8px;" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { fetchWarehouseRegister } from '../../api/warehouseApi.js'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const error = ref('')

const form = ref({
  code: '',
  name: '',
  address: ''
})

const rules = {
  name: [{ required: true, message: '창고명을 입력해주세요.', trigger: 'blur' }],
  address: [{ required: true, message: '주소를 입력해주세요.', trigger: 'blur' }]
}

async function submit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    const res = await fetchWarehouseRegister(form.value)
    ElMessage.success(res.data.message) // "창고 등록 완료"
    router.push('/warehouses')
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || '등록 실패'
  }
}

function goBack() {
  router.push('/warehouses')
}

let kakaoMap = null
let kakaoMarker = null

onMounted(() => {
  window.kakao.maps.load(() => {
    const container = document.getElementById('registerMap')
    const options = { center: new window.kakao.maps.LatLng(37.5665, 126.9780), level: 1 }
    kakaoMap = new window.kakao.maps.Map(container, options)
    kakaoMarker = new window.kakao.maps.Marker({ position: new window.kakao.maps.LatLng(37.5665, 126.9780) })
  })
})

function searchAddress() {
  new window.daum.Postcode({
    oncomplete: (data) => {
      form.value.address = data.roadAddress || data.jibunAddress

      const geocoder = new window.kakao.maps.services.Geocoder()
      geocoder.addressSearch(form.value.address, (result, status) => {
        if (status === window.kakao.maps.services.Status.OK) {
          const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x)
          kakaoMarker.setPosition(coords)
          kakaoMarker.setMap(kakaoMap)
          kakaoMap.setCenter(coords)
        }
      })
    }
  }).open()
}
</script>