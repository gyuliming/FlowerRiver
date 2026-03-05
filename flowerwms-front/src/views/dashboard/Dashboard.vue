<template>
  <div>
    <!-- 상단 4카드 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <!-- ADMIN -->
      <template v-if="isAdmin()">
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align:center;">
              <div style="font-size:14px; color:#999; margin-bottom:8px;">전체 창고 수</div>
              <div style="font-size:32px; font-weight:700; color:#404F7B;">{{ summary.totalWarehouses }}</div>
              <div style="font-size:12px; color:#999;">개</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align:center;">
              <div style="font-size:14px; color:#999; margin-bottom:8px;">현재 총 재고</div>
              <div style="font-size:32px; font-weight:700; color:#404F7B;">{{ summary.totalStockBox }}</div>
              <div style="font-size:12px; color:#999;">박스</div>
            </div>
          </el-card>
        </el-col>
      </template>

      <!-- MANAGER -->
      <template v-else>
        <el-col :span="8">
          <el-card shadow="hover">
            <div style="text-align:center;">
              <div style="font-size:14px; color:#999; margin-bottom:8px;">내 창고 총 재고</div>
              <div style="font-size:32px; font-weight:700; color:#404F7B;">{{ summary.totalStockBox }}</div>
              <div style="font-size:12px; color:#999;">박스</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div style="text-align:center;">
              <div style="font-size:14px; color:#999; margin-bottom:8px;">오늘 입고</div>
              <div style="font-size:32px; font-weight:700; color:#67C23A;">{{ summary.todayInboundCount }}</div>
              <div style="font-size:12px; color:#999;">건 / {{ summary.todayInboundBox }} 박스</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div style="text-align:center;">
              <div style="font-size:14px; color:#999; margin-bottom:8px;">오늘 출고</div>
              <div style="font-size:32px; font-weight:700; color:#F56C6C;">{{ summary.todayOutboundCount }}</div>
              <div style="font-size:12px; color:#999;">건 / {{ summary.todayOutboundBox }} 박스</div>
            </div>
          </el-card>
        </el-col>
      </template>
    </el-row>

    <!-- 차트 -->
    <el-card style="margin-bottom:16px;">
      <template #header>
        <span style="font-weight:600;">
          {{ isAdmin() ? '창고별 재고 현황 (TOP 10)' : '상품별 재고 현황 (TOP 10)' }}
        </span>
      </template>
      <div ref="chartRef" style="width:100%; height:300px;" />
    </el-card>

    <!-- 최근 입출고 -->
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span style="font-weight:600;">최근 입고 5건</span>
          </template>
          <el-table :data="recentInbound" border style="width:100%">
            <el-table-column prop="code" label="코드" min-width="140" />
            <el-table-column prop="productName" label="상품명" min-width="120" />
            <el-table-column prop="warehouseName" label="창고" min-width="120" />
            <el-table-column prop="boxQty" label="박스" min-width="80" align="right" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span style="font-weight:600;">최근 출고 5건</span>
          </template>
          <el-table :data="recentOutbound" border style="width:100%">
            <el-table-column prop="code" label="코드" min-width="140" />
            <el-table-column prop="productName" label="상품명" min-width="120" />
            <el-table-column prop="warehouseName" label="창고" min-width="120" />
            <el-table-column prop="boxQty" label="박스" min-width="80" align="right" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { useAuth } from '../../stores/auth'
import {
  fetchDashboardSummary,
  fetchWarehouseUsage,
  fetchRecentInbound,
  fetchRecentOutbound,
  fetchProductStock
} from '../../api/dashboardApi'

const { isAdmin } = useAuth()

const summary = ref({
  totalWarehouses: 0,
  totalStockBox: 0,
  todayInboundCount: 0,
  todayInboundBox: 0,
  todayOutboundCount: 0,
  todayOutboundBox: 0
})

const recentInbound = ref([])
const recentOutbound = ref([])
const chartRef = ref(null)

function renderChart(data, labelKey, valueKey) {
  const chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: data.map(d => d[labelKey])
    },
    yAxis: { type: 'value', name: '박스' },
    series: [{
      name: '재고(박스)',
      type: 'bar',
      data: data.map(d => d[valueKey]),
      itemStyle: { color: '#404F7B' }
    }]
  })
}

onMounted(async () => {
  const [summaryRes, inboundRes, outboundRes] = await Promise.all([
    fetchDashboardSummary(),
    fetchRecentInbound(),
    fetchRecentOutbound()
  ])

  summary.value = summaryRes.data.data
  recentInbound.value = inboundRes.data.data
  recentOutbound.value = outboundRes.data.data

  if (isAdmin()) {
    const usageRes = await fetchWarehouseUsage()
    renderChart(usageRes.data.data, 'warehouseName', 'totalStockBox')
  } else {
    const [productRes] = await Promise.all([
      fetchProductStock()
    ])
    renderChart(productRes.data.data, 'productName', 'boxQty')
  }
})
</script>