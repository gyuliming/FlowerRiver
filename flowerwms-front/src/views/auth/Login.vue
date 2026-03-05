<template>
  <div style="min-height:100vh; display:flex; align-items:center; justify-content:center; background: linear-gradient(135deg, #2C3E6B 0%, #1a2a4a 100%);">
    <div style="width:420px;">

      <!-- 카드 -->
      <el-card style="border-radius:16px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); border:none;">
        <div style="padding:8px 8px;">
          <div style="text-align:center; margin-bottom:24px;">
            <div style="font-size:22px; font-weight:700; color:#2C3E6B;">로그인</div>
          </div>

          <el-form :model="form" :rules="rules" ref="formRef" label-position="top" hide-required-asterisk>
            <el-form-item label="아이디" prop="loginId">
              <el-input
                  v-model="form.loginId"
                  placeholder="아이디"
                  size="large"
                  @keyup.enter="submit"
                  style="border-radius:8px;"
              />
            </el-form-item>
            <el-form-item label="비밀번호" prop="password">
              <el-input
                  v-model="form.password"
                  type="password"
                  placeholder="비밀번호"
                  size="large"
                  show-password
                  @keyup.enter="submit"
                  style="border-radius:8px;"
              />
            </el-form-item>

            <el-button
                type="primary"
                style="width:100%; height:44px; font-size:15px; border-radius:8px; background:#2C3E6B; border-color:#2C3E6B; margin-top:8px;"
                @click="submit"
                :loading="loading"
            >
              로그인
            </el-button>

            <el-divider style="margin:16px 0;">
              <span style="font-size:12px; color:#999;">계정이 없으신가요?</span>
            </el-divider>

            <el-button
                style="width:100%; height:44px; font-size:14px; border-radius:8px;"
                @click="goRegister"
            >
              회원가입
            </el-button>
          </el-form>

          <el-alert v-if="error" type="error" :title="error" show-icon style="margin-top:16px;" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login as loginApi } from '../../api/authApi'
import { useAuth } from '../../stores/auth'

const router = useRouter()
const { login } = useAuth()
const formRef = ref(null)
const loading = ref(false)
const error = ref('')

const form = ref({
  loginId: '',
  password: ''
})

const rules = {
  loginId: [{ required: true, message: '아이디를 입력해주세요.', trigger: 'blur' }],
  password: [{ required: true, message: '비밀번호를 입력해주세요.', trigger: 'blur' }]
}

async function submit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  error.value = ''
  try {
    const res = await loginApi(form.value)
    login(res.data.data)
    ElMessage.success('로그인 성공!')
    router.push('/dashboard')
  } catch (e) {
    error.value = e?.response?.data?.message || '로그인 실패'
  } finally {
    loading.value = false
  }
}

function goRegister() {
  router.push('/register')
}
</script>