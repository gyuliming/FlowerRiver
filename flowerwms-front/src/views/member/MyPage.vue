<template>
  <div>
    <el-card>
      <template #header>
        <span style="font-weight:600;">내 정보</span>
      </template>

      <el-form :model="form" label-width="100px" style="max-width:500px;">
        <el-form-item label="아이디">
          <el-input v-model="form.loginId" disabled />
        </el-form-item>
        <el-form-item label="이름">
          <el-input v-model="form.name" disabled/>
        </el-form-item>
        <el-form-item label="전화번호">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="이메일">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="담당 창고">
          <el-input v-model="form.warehouseName" disabled />
        </el-form-item>
        <el-form-item label="권한">
          <el-tag>
            {{ form.role === 'ADMIN' ? '총관리자' : '창고관리자' }}
          </el-tag>
        </el-form-item>
        <el-form-item label="상태">
          <el-tag type="success">{{ form.status }}</el-tag>
        </el-form-item>
        <el-form-item label="가입일자">
          <el-input v-model="form.createdAt" disabled />
        </el-form-item>
        <el-form-item label="최근 수정일">
          <el-input v-model="form.updatedAt" disabled />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdate">수정</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyInfo, updateMyInfo } from '../../api/memberApi.js'

const form = ref({
  loginId: '',
  name: '',
  phone: '',
  email: '',
  warehouseName: '',
  role: '',
  status: '',
  createdAt: '',
  updatedAt: ''
})

async function load() {
  try {
    const res = await getMyInfo()
    form.value = res.data.data
  } catch (e) {
    ElMessage.error('내 정보를 불러오지 못했습니다.')
  }
}

async function handleUpdate() {
  try {
    await updateMyInfo({
      name: form.value.name,
      phone: form.value.phone,
      email: form.value.email
    })
    ElMessage.success('수정되었습니다.')
    load()
  } catch (e) {
    ElMessage.error('수정 실패')
  }
}

onMounted(() => {
  load()
})
</script>