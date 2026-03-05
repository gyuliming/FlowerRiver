<template>
  <div style="min-height:100vh; display:flex; align-items:center; justify-content:center; background: linear-gradient(135deg, #2C3E6B 0%, #1a2a4a 100%);">
    <div style="width:420px;">

      <!-- 카드 -->
      <el-card style="border-radius:16px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); border:none;">
        <div style="padding:8px 8px;">
          <div style="text-align:center; margin-bottom:24px;">
            <div style="font-size:22px; font-weight:700; color:#2C3E6B;">회원가입</div>
          </div>

          <el-form :model="form" :rules="rules" ref="formRef" label-position="top" hide-required-asterisk>
            <el-form-item label="아이디" prop="loginId">
              <el-input
                  v-model="form.loginId"
                  placeholder="아이디"
                  size="large"
              />
            </el-form-item>
            <el-form-item label="비밀번호" prop="password">
              <el-input
                  v-model="form.password"
                  type="password"
                  placeholder="비밀번호"
                  size="large"
                  show-password
              />
            </el-form-item>
            <el-form-item label="이름" prop="name">
              <el-input
                  v-model="form.name"
                  placeholder="이름"
                  size="large"
              />
            </el-form-item>
            <el-form-item label="전화번호" prop="phone">
              <el-input
                  v-model="form.phone"
                  placeholder="전화번호"
                  size="large"
              />
            </el-form-item>
            <el-form-item label="이메일" prop="email">
              <el-input
                  v-model="form.email"
                  placeholder="이메일"
                  size="large"
              />
            </el-form-item>

            <el-button
                type="primary"
                style="width:100%; height:44px; font-size:15px; border-radius:8px; background:#2C3E6B; border-color:#2C3E6B; margin-top:8px;"
                @click="submit"
                :loading="loading"
            >
              가입
            </el-button>

            <el-divider style="margin:16px 0;">
              <span style="font-size:12px; color:#999;">이미 계정이 있으신가요?</span>
            </el-divider>

            <el-button
                style="width:100%; height:44px; font-size:14px; border-radius:8px;"
                @click="goLogin"
            >
              로그인으로 돌아가기
            </el-button>
          </el-form>

          <el-alert v-if="error" type="error" :title="error" show-icon style="margin-top:16px;" />
          <el-alert v-if="success" type="success" title="가입 요청이 완료되었습니다. 관리자 승인 후 로그인 가능합니다." show-icon style="margin-top:16px;" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../../api/authApi'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const error = ref('')
const success = ref(false)

const form = ref({
  loginId: '',
  password: '',
  name: '',
  phone: '',
  email: ''
})

const rules = {
  loginId: [{ required: true, message: '아이디를 입력해주세요.', trigger: 'blur' }],
  password: [{ required: true, message: '비밀번호를 입력해주세요.', trigger: 'blur' }],
  name: [{ required: true, message: '이름을 입력해주세요.', trigger: 'blur' }],
  phone: [{ required: true, message: '전화번호를 입력해주세요.', trigger: 'blur' }],
  email: [{ required: true, message: '이메일을 입력해주세요.', trigger: 'blur' }]
}

async function submit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  error.value = ''
  try {
    await register(form.value)
    success.value = true
  } catch (e) {
    error.value = e?.response?.data?.message || '가입 요청 실패'
  } finally {
    loading.value = false
  }
}

function goLogin() {
  router.push('/login')
}
</script>