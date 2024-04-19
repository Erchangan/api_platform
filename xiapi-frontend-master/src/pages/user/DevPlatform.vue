<script setup lang="ts">

import {CloudDownloadOutlined} from "@ant-design/icons-vue";
import {h, ref} from "vue";
import request from "~/utils/request.ts";
import {message} from "ant-design-vue";

const devKey = ref<{accessKey: string, secretKey: string}>({
  accessKey: "",
  secretKey: ""
})

const getDevKey = async () => {
  const res = await request.get('user/getDevKey')
  if (res.code === 0){
    devKey.value.accessKey = res.data.accessKey
    devKey.value.secretKey = res.data.secretKey
  }else {
    message.error(res.message)
  }
}

const updateDevKey = async () => {
  const res = await request.get('user/updateDevKey')
  if (res.code === 0){
    devKey.value.accessKey = res.data.accessKey
    devKey.value.secretKey = res.data.secretKey
  }else {
    message.error(res.message)
  }
}

onMounted(() => {
  getDevKey()
  getByUserId()
})

// 表格
interface UserInterfaceInfo {
  id: number,
  userId: number,
  interfaceInfoId: number,
  interfaceInfoName: string,
  totalNum: number,
  leftNum: number
}
const dataSource = ref<UserInterfaceInfo[]>([])

const columns = ref(
    [
      {
        title: '接口名称',
        dataIndex: 'interfaceInfoName',
        key: 'interfaceInfoName',
      },
      {
        title: '总调用次数',
        dataIndex: 'totalNum',
        key: 'totalNum',
      },
      {
        title: '剩余调用次数',
        dataIndex: 'leftNum',
        key: 'leftNum',
      },
    ],
)

const getByUserId = async () => {
  const res = await request.get('userinterfaceinfo/getByUserId');
  if (res.code === 0){
    dataSource.value = res.data
  }else {
    message.error(res.message)
  }
}
</script>

<template>
  <div>
    <a-space direction="vertical">
      <a-card title="开发者密钥(调用接口的凭证)">
        <template #extra>
          <a-button @click="updateDevKey()">
            重新生成
          </a-button>
        </template>
        <a-descriptions :column="1">
          <a-descriptions-item label="AccessKey">
            <a-typography-paragraph copyable>{{devKey.accessKey}}</a-typography-paragraph>
          </a-descriptions-item>
          <a-descriptions-item label="SecretKey">
            <a-typography-paragraph copyable>{{devKey.secretKey}}</a-typography-paragraph>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
      <a-card title="开发者SDK(让你的程序轻松使用第三方服务)">
        <a-button :icon="h(CloudDownloadOutlined)">Java SDK</a-button>
      </a-card>
      <a-card title="可调用接口">
        <a-table :pagination="false" :dataSource="dataSource" :columns="columns" />
      </a-card>
    </a-space>
  </div>
</template>

<style scoped lang="less">

</style>
