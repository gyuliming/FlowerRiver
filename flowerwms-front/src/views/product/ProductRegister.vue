<template>
  <el-card>
    <template #header>
      <span style="font-weight: 600;">상품 등록</span>
    </template>

    <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" style="max-width: 600px;" hide-required-asterisk>
      <el-form-item label="상품명" prop="name">
        <el-input v-model="form.name" placeholder="예) 장미" />
      </el-form-item>

      <el-form-item label="종류" prop="type">
        <el-select v-model="form.type" placeholder="선택" style="width:100%;">
          <el-option label="절화" value="CUT" />
          <el-option label="화분" value="POTTED" />
          <el-option label="가공화" value="PROCESSED" />
        </el-select>
      </el-form-item>

      <el-form-item label="보관타입" v-if="form.type">
        <el-tag :type="storageType === 'COLD' ? 'primary' : 'warning'">
          {{ storageType === 'COLD' ? '저온' : '상온' }}
        </el-tag>
        <span style="margin-left:8px; color:#999; font-size:12px;"></span>
      </el-form-item>

      <el-form-item label="박스당 가격" prop="pricePerBox">
        <el-input
            v-model="form.pricePerBox"
            placeholder="박스당 가격을 입력하세요."
            type="number"
            style="width:100%;"
        />
      </el-form-item>

      <el-form-item label="박스당 수량" prop="qtyPerBox">
        <el-input
            v-model="form.qtyPerBox"
            placeholder="박스당 수량을 입력하세요."
            type="number"
            style="width:100%;"
        />
      </el-form-item>

      <el-form-item label="단위" prop="unit">
        <el-input v-model="form.unit" placeholder="예) 단, 분, 개" />
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
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createProduct } from '../../api/productApi'

const router = useRouter()
const formRef = ref(null)
const error = ref('')

const form = ref({
  name: '',
  type: '',
  pricePerBox: null,
  qtyPerBox: null,
  unit: ''
})

const rules = {
  name: [{ required: true, message: '상품명을 입력해주세요.', trigger: 'blur' }],
  type: [{ required: true, message: '종류를 선택해주세요.', trigger: 'change' }],
  pricePerBox: [{ required: true, message: '박스당 가격을 입력해주세요.', trigger: 'blur' }],
  qtyPerBox: [{ required: true, message: '박스당 수량을 입력해주세요.', trigger: 'blur' }],
  unit: [{ required: true, message: '단위를 입력해주세요.', trigger: 'blur' }]
}

const storageType = computed(() => {
  return form.value.type === 'CUT' ? 'COLD' : 'NORMAL'
})

async function submit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    const res = await createProduct(form.value)
    ElMessage.success(res.data.message) // "상품 등록 완료"
    router.push('/products')
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || '등록 실패'
  }
}

function goBack() {
  router.push('/products')
}
</script>