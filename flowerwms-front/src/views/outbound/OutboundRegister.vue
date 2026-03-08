<template>
  <el-card>
    <template #header>
      <span style="font-weight: 600;">출고 등록</span>
    </template>

    <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" style="max-width: 600px;" hide-required-asterisk>

      <!-- 상품 선택 (재고 있는 것만) -->
      <el-form-item label="상품" prop="productId">
        <el-select v-model="form.productId" placeholder="상품 선택" style="width:100%;" @change="onProductChange">
          <el-option v-for="p in productOptions" :key="p.id" :label="`${p.name} (재고: ${p.totalStock}박스)`" :value="p.id" />
        </el-select>
      </el-form-item>

      <!-- 창고 선택 (해당 상품 보유 창고만) -->
      <el-form-item label="창고" prop="warehouseId">
        <el-select v-model="form.warehouseId" placeholder="창고 선택" style="width:100%;" @change="onWarehouseChange" :disabled="!form.productId">
          <el-option v-for="w in warehouseOptions" :key="w.id" :label="`${w.name} (재고: ${w.stockQty}박스)`" :value="w.id" />
        </el-select>
      </el-form-item>

      <!-- 수량 -->
      <el-form-item label="출고 수량(박스)" prop="boxQty">
        <el-input-number v-model="form.boxQty" :min="1" :max="selectedWarehouseStock || 1" style="width:100%;" :disabled="!form.warehouseId" />
        <span style="margin-left:8px; color:#999; font-size:12px;" v-if="form.warehouseId">최대 {{ selectedWarehouseStock }} 박스 (선입선출 자동 적용)</span>
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
import { createOutbound } from '../../api/outboundApi'
import { fetchStockProducts, fetchStockWarehouses } from '../../api/stockApi'
import { useAuth } from '../../stores/auth'

const router = useRouter()
const formRef = ref(null)
const error = ref('')

const form = ref({
  productId: null,
  warehouseId: null,
  boxQty: 1
})

const productOptions = ref([])
const warehouseOptions = ref([])
const selectedWarehouseStock = ref(0)

const rules = {
  productId: [{ required: true, message: '상품을 선택해주세요.', trigger: 'change' }],
  warehouseId: [{ required: true, message: '창고를 선택해주세요.', trigger: 'change' }],
  boxQty: [{ required: true, message: '수량을 입력해주세요.', trigger: 'blur' }]
}

const { state, isAdmin } = useAuth()

async function onProductChange(id) {
  form.value.warehouseId = null
  selectedWarehouseStock.value = 0
  warehouseOptions.value = []

  const res = await fetchStockWarehouses(id)
  let warehouses = res.data.data

  if (!isAdmin()) {
    warehouses = warehouses.filter(w => w.id === Number(state.warehouseId))
  }

  warehouseOptions.value = warehouses

  if (!isAdmin() && warehouses.length === 1) {
    form.value.warehouseId = warehouses[0].id
    onWarehouseChange(warehouses[0].id)
  }
}

onMounted(async () => {
  const warehouseId = isAdmin() ? null : state.warehouseId
  const res = await fetchStockProducts(warehouseId)
  productOptions.value = res.data.data
})

function onWarehouseChange(id) {
  const selected = warehouseOptions.value.find(w => w.id === id)
  selectedWarehouseStock.value = selected ? selected.stockQty : 0
  form.value.boxQty = 1
}

async function submit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    const res = await createOutbound(form.value)
    ElMessage.success(res.data.message)
    router.push('/outbound')
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || '등록 실패'
  }
}

function goBack() {
  router.push('/outbound')
}

onMounted(async () => {
  const warehouseId = isAdmin() ? null : state.warehouseId
  const res = await fetchStockProducts(warehouseId)
  productOptions.value = res.data.data
})
</script>
