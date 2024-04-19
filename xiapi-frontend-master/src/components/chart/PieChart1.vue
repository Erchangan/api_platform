<script setup lang="ts">
import {ref} from "vue";
import request from "~/utils/request.ts";
import {message} from "ant-design-vue";

interface dataType {
  value: number,
  name: string
}

const data = ref<dataType[]>([])

const getPerInterfaceCount = async () => {
  const res = await request.get('analyze/getPerInterfaceCount');
  if (res.code === 0){
    data.value = res.data
  }else {
    message.error("getPerInterfaceCount失败！")
  }
}

const option_column = computed(() => {
  return {
    title: {
      text: '用户接口调用总数对比',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        type: 'pie',
        radius: '50%',
        data: data.value,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
})

onMounted(() => {
  getPerInterfaceCount()
})
</script>

<template>
  <v-chart :option="option_column" style="height: 400px"></v-chart>
</template>

<style scoped lang="less">

</style>
