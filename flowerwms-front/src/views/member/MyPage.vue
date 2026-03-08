<template>
  <div>
    <el-card>
      <template #header>
        <span style="font-weight:600;">내 정보</span>
      </template>

      <el-form :model="form" label-width="150px" style="max-width:500px;">
        <el-form-item label="아이디">
          <el-input v-model="form.loginId" disabled />
        </el-form-item>
        <el-form-item label="이름">
          <el-input v-model="form.name" disabled/>
        </el-form-item>
        <el-form-item  label="현재 비밀번호" >
          <el-input v-model="passwordForm.currentPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="새 비밀번호">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="새 비밀번호 확인">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
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
          <el-button type="primary" @click="handleSubmit">수정</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyInfo, updateMyInfo, changePassword } from '../../api/memberApi.js'


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

const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

async function handleSubmit() {
  // 비밀번호 입력했을 때만 검증
  if (passwordForm.value.newPassword || passwordForm.value.currentPassword) {
    if (!passwordForm.value.currentPassword) {
      ElMessage.warning('현재 비밀번호를 입력해주세요.')
      return
    }
    if (!passwordForm.value.newPassword) {
      ElMessage.warning('새 비밀번호를 입력해주세요.')
      return
    }
    if (passwordForm.value.newPassword.length < 8 || passwordForm.value.newPassword.length > 20) {
      ElMessage.warning('비밀번호는 8자 이상 20자 이하로 입력해주세요.')
      return
    }
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
      ElMessage.warning('새 비밀번호가 일치하지 않습니다.')
      return
    }
  }

  try {
    // 내 정보 수정
    await updateMyInfo({
      name: form.value.name,
      phone: form.value.phone,
      email: form.value.email
    })

    // 비밀번호 입력했으면 비밀번호도 변경
    if (passwordForm.value.newPassword) {
      await changePassword({
        currentPassword: passwordForm.value.currentPassword,
        newPassword: passwordForm.value.newPassword
      })
    }

    ElMessage.success('수정되었습니다.')
    passwordForm.value = { currentPassword: '', newPassword: '', confirmPassword: '' }
    load()
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '수정 실패')
  }
}

async function load() {
  try {
    const res = await getMyInfo()
    form.value = res.data.data
  } catch (e) {
    ElMessage.error('내 정보를 불러오지 못했습니다.')
  }
}

onMounted(() => {
  load()
})
</script>