<script setup lang="ts">
import {ref} from "vue";
import request from "~/utils/request.ts";
import {message} from "ant-design-vue";

const nameData = ref<string[]>([])
const valueData = ref<number[]>([])

const getInterfaceInvokeCountFor7 = async () => {
  const res = await request.get('analyze/getInterfaceInvokeCountFor7');
  if (res.code === 0){
    nameData.value = res.data.interfaceInfoName
    valueData.value = res.data.interfaceInfoCountBy7
  }else {
    message.error("getPerInterfaceCount失败！")
  }
}

const option_column = computed(() => {
  return {
    title: {
      text: '用户近七次接口调用总数',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: nameData.value,
        axisTick: {
          alignWithLabel: true
        },
        axisLabel: {
          rotate: 15
        }
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        type: 'bar',
        barWidth: '60%',
        data: valueData.value
      }
    ]
  }
})

onMounted(() => {
  getInterfaceInvokeCountFor7()
})
</script>

<template>
  <v-chart :option="option_column" style="height: 400px"></v-chart>
</template>

<style scoped lang="less">

</style>
