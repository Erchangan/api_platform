<script setup lang="ts">

import request from "~/utils/request.ts";
import {computed, inject, ref} from "vue";
import type {PaginationProps} from 'ant-design-vue';
import {message} from "ant-design-vue";
import {usePagination} from 'vue-request';
import * as dayjs from "dayjs";

const reload = inject('reload')

const columns = [
  {
    title: '订单ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '接口名称',
    dataIndex: 'interfaceInfoName',
    key: 'interfaceInfoName',
  },
  {
    title: '商品ID',
    dataIndex: 'interfaceInfoProductId',
    key: 'interfaceInfoProductId',
  },
  {
    title: '规格',
    dataIndex: 'specification',
    key: 'specification',
  },
  {
    title: '价格',
    dataIndex: 'price',
    key: 'price',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
  },
  {
    title: '支付时间',
    dataIndex: 'payTime',
    key: 'payTime',
  },
  {
    title: '退款时间',
    dataIndex: 'refundTime',
    key: 'refundTime',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
    width: 150
  },
];


const dto = ref({current: 1, pageSize: 5})

const {data, total, run} = usePagination(
    (d) => request.get('interfaceInfoOrder/getMyOrderPage', {params: d}),
    {
      defaultParams: [dto.value],
      pagination: {
        currentKey: "current",
        pageSizeKey: 'pageSize',
        totalKey: 'data.total'
      }
    }
)

// 在页号或页大小改变时调用
function tableChange(pagination: PaginationProps) {
  dto.value.current = pagination.current ?? 1
  dto.value.pageSize = pagination.pageSize ?? 5
  run(dto.value)
}

const pagination = computed<PaginationProps>(() => {
  return {
    current: dto.value.current, // 当前页
    pageSize: dto.value.pageSize, // 页大小
    total: Number(total.value),       // 总记录数
    showSizeChanger: true,    // 显示页大小的下拉列表
    pageSizeOptions: ["1", "2", "3", "4", "5"] // 自定义下拉列表内容
  }
})

const interfaceInfoMyOrders = computed(() => {
  return data.value?.data.records || []
})

// 支付弹窗
const open = ref<boolean>(false);
const showModal = () => {
  open.value = true;
};

const payOrderData = ref<{
  id: number,
  interfaceInfoId: number,
  interfaceInfoProductId: number
}>({
  id: 0,
  interfaceInfoId: 0,
  interfaceInfoProductId: 0
})

const handleBuy = (record) => {
  payOrderData.value.id = record.id
  payOrderData.value.interfaceInfoProductId = record.interfaceInfoProductId
  payOrderData.value.interfaceInfoId = record.interfaceInfoId
  showModal()
}

const payOrder = async () => {
  const res = await request.post('interfaceInfoOrder/pay', payOrderData.value);
  if (res.code === 0) {
    message.success("支付成功")
    open.value = false
    reload()
  } else {
    message.error(res.message)
  }
}

// 退款
const handleRefund = (record) => {
  message.info("暂不支持退款")
}

// 格式化时间
const formatDate = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}
</script>

<template>
  <div>
    <a-card title="个人订单">
      <a-table
          :columns="columns"
          :data-source="interfaceInfoMyOrders"
          :pagination="pagination"
          @change="tableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'payTime'">
            <template v-if="record.payTime == null">
            </template>
            <template v-else>
              {{ formatDate(record.payTime) }}
            </template>
          </template>
          <template v-if="column.key === 'refundTime'">
            <template v-if="record.refundTime == null">
            </template>
            <template v-else>
              {{ formatDate(record.refundTime) }}
            </template>
          </template>
          <template v-if="column.key === 'createTime'">
            {{ formatDate(record.createTime) }}
          </template>
          <template v-if="column.key === 'status'">
            <template v-if="record.status === 1">
              <a-tag :bordered="false" color="error">未支付</a-tag>
            </template>
            <template v-else-if="record.status === 2">
              <a-tag :bordered="false" color="success">已支付</a-tag>
            </template>
            <template v-else-if="record.status === 3">
              <a-tag :bordered="false">已取消</a-tag>
            </template>
            <template v-else-if="record.status === 4">
              <a-tag :bordered="false" color="warning">退款中</a-tag>
            </template>
            <template v-else-if="record.status === 5">
              <a-tag :bordered="false">已退款</a-tag>
            </template>
          </template>
          <template v-if="column.key === 'action'">
        <span>
              <template v-if="record.status === 1">
                <a style="color: red" @click="handleBuy(record)">去支付</a>
              </template>
              <template v-else-if="record.status === 2">
                <a @click="handleRefund(record)">退款</a>
              </template>
              <template v-else-if="record.status === 3">
                <a>查看</a>
              </template>
        </span>
          </template>
        </template>
      </a-table>
    </a-card>
    <!--    支付对话框-->
    <a-modal v-model:open="open" title="选择支付方式">
      <template #footer>
      </template>
      <a-button style="color: blue" block @click="payOrder">支付宝</a-button>
      <a-button style="color: green" block @click="payOrder">微信支付</a-button>
    </a-modal>
  </div>
</template>

<style scoped lang="less">

</style>
