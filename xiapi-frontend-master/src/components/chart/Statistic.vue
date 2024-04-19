<script setup lang="ts">

import request from "~/utils/request.ts";
import {ref} from "vue";
import {message} from "ant-design-vue";

const statisticData = ref<{
  shopInterfaceCount: number,
  invokeInterfaceTotalCount: number,
  invokeInterfaceLeftCount: number,
  invokeMoreInterface: string
}>({
  shopInterfaceCount: 0,
  invokeInterfaceTotalCount: 0,
  invokeInterfaceLeftCount: 0,
  invokeMoreInterface: "XXX"
})

const getStatistic = async () => {
  const res = await request.get('analyze/getStatistic')
  if (res.code === 0){
    statisticData.value = res.data
  }else {
    message.error(res.message)
  }
}

onMounted(() => {
  getStatistic()
})
</script>

<template>
  <a-card title="数值统计">
    <a-row>
      <a-col :span="6">
        <a-statistic title="已购买接口数量" :value="statisticData.shopInterfaceCount" style="margin-right: 50px"/>
      </a-col>
      <a-col :span="6">
        <a-statistic title="接口总调用次数" :value="statisticData.invokeInterfaceTotalCount"/>
      </a-col>
      <a-col :span="6">
        <a-statistic title="剩余总调用次数" :value="statisticData.invokeInterfaceLeftCount" style="margin-right: 50px"/>
      </a-col>
      <a-col :span="6">
        <a-statistic title="调用最多的接口" :value="statisticData.invokeMoreInterface"/>
      </a-col>
    </a-row>
  </a-card>
</template>

<style scoped lang="less">

</style>
