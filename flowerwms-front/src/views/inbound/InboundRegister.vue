<template>
  <el-card>
    <template #header>
      <span style="font-weight: 600;">입고 등록</span>
    </template>

    <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" style="max-width: 600px;" hide-required-asterisk>

      <!-- 상품 선택 -->
      <el-form-item label="상품" prop="productId">
        <el-select v-model="form.productId" placeholder="상품 선택" style="width:100%;" @change="onProductChange">
          <el-option v-for="p in productOptions" :key="p.id" :label="p.name" :value="p.id" />
        </el-select>
      </el-form-item>

      <!-- 창고 선택 -->
      <el-form-item label="창고" prop="warehouseId">
        <el-select v-model="form.warehouseId" placeholder="창고 선택" style="width:100%;" @change="onWarehouseChange" :disabled="!form.productId">
          <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.name" :value="w.id" />
        </el-select>
      </el-form-item>

      <!-- 구역 선택 -->
      <el-form-item label="구역" prop="sectionId">
        <el-select v-model="form.sectionId" placeholder="구역 선택" style="width:100%;" @change="onSectionChange" :disabled="!form.warehouseId">
          <el-option v-for="s in sectionOptions" :key="s.sectionId" :label="s.sectionCode" :value="s.sectionId" />
        </el-select>
      </el-form-item>

      <!-- 팔레트 선택 -->
      <el-form-item label="팔레트" prop="palletId">
        <el-select v-model="form.palletId" placeholder="팔레트 선택" style="width:100%;" :disabled="!form.sectionId">
          <el-option
              v-for="p in palletOptions"
              :key="p.id"
              :label="`${p.code} (여유: ${p.maxBoxQty - p.usedBoxQty}박스)`"
              :value="p.id"
          />
        </el-select>
      </el-form-item>

      <!-- 수량 -->
      <el-form-item label="입고 수량(박스)" prop="boxQty">
        <el-input-number v-model="form.boxQty" :min="1" style="width:100%;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submit">등록</el-button>
        <el-button @click="goBack">취소</el-button>
      </el-form-item>
    </el-form>

    <el-alert v-if="error" type="error" :title="error" show-icon style="margin-top:12px;" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createInbound } from '../../api/inboundApi'
import { fetchWarehouses, fetchWarehouseDetail, fetchPallets } from '../../api/warehouseApi'
import { fetchProducts } from '../../api/productApi'
import { useAuth } from '../../stores/auth'

const router = useRouter()
const formRef = ref(null)
const error = ref('')

const form = ref({
  productId: null,
  warehouseId: null,
  sectionId: null,
  palletId: null,
  boxQty: 1
})

const productOptions = ref([])
const warehouseOptions = ref([])
const sectionOptions = ref([])
const palletOptions = ref([])
const selectedProduct = ref(null)

const rules = {
  productId: [{ required: true, message: '상품을 선택해주세요.', trigger: 'change' }],
  warehouseId: [{ required: true, message: '창고를 선택해주세요.', trigger: 'change' }],
  sectionId: [{ required: true, message: '구역을 선택해주세요.', trigger: 'change' }],
  palletId: [{ required: true, message: '팔레트를 선택해주세요.', trigger: 'change' }],
  boxQty: [{ required: true, message: '수량을 입력해주세요.', trigger: 'blur' }]
}


const { state, isAdmin } = useAuth()

onMounted(async () => {
  const [productRes, warehouseRes] = await Promise.all([
    fetchProducts({ page: 1, size: 100 }),
    fetchWarehouses({ page: 1, size: 100 })
  ])
  productOptions.value = productRes.data.data.items

  const allWarehouses = warehouseRes.data.data.items.filter(w => w.status === 'NORMAL')

  if (isAdmin()) {
    warehouseOptions.value = allWarehouses
  } else {
    // MANAGER 는 자기 창고만
    warehouseOptions.value = allWarehouses.filter(w => w.id === Number(state.warehouseId))
    // 자동 선택
    if (warehouseOptions.value.length === 1) {
      form.value.warehouseId = warehouseOptions.value[0].id
      // await onWarehouseChange(form.value.warehouseId)
    }
  }
})

function onProductChange(id) {
  selectedProduct.value = productOptions.value.find(p => p.id === id)
  form.value.warehouseId = null
  form.value.sectionId = null
  form.value.palletId = null
  sectionOptions.value = []
  palletOptions.value = []
}

async function onWarehouseChange(id) {
  form.value.sectionId = null
  form.value.palletId = null
  palletOptions.value = []

  const res = await fetchWarehouseDetail(id)
  const sections = res.data.data.sections

  // 상품 storageType에 맞는 섹션만 필터
  sectionOptions.value = sections.filter(s => s.type === selectedProduct.value.storageType)
}

async function onSectionChange(id) {
  form.value.palletId = null
  const res = await fetchPallets(id)
  palletOptions.value = res.data.data
}

async function submit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    const res = await createInbound(form.value)
    ElMessage.success(res.data.message)
    router.push('/inbound')
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || '등록 실패'
  }
}

function goBack() {
  router.push('/inbound')
}
</script>
