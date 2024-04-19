<script setup lang="ts">
import {ref} from "vue";
import request from "~/utils/request.ts";
import {message} from "ant-design-vue";

interface dataType {
  value: number,
  name: string
}

const data = ref<dataType[]>([])

const getPerInterfaceLeftCount = async () => {
  const res = await request.get('analyze/getPerInterfaceLeftCount');
  if (res.code === 0) {
    data.value = res.data
  } else {
    message.error(res.message)
  }
}

const option_column = computed(() => {
  return {
    title: {
      text: '用户接口剩余调用总数',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data.value
      }
    ]
  }
})

onMounted(() => {
  getPerInterfaceLeftCount()
})
</script>

<template>
  <v-chart :option="option_column" style="height: 400px"></v-chart>
</template>

<style scoped lang="less">

</style>
