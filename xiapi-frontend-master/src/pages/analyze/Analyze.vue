<script setup lang="ts">
import Test from "~/components/chart/PieChart1.vue";
import PieChart1 from "~/components/chart/PieChart1.vue";
import CategoryChart from "~/components/chart/CategoryChart.vue";
import Statistic from "~/components/chart/Statistic.vue";
import PieChart2 from "~/components/chart/PieChart2.vue";
</script>

<template>
  <div>
    <a-space direction="vertical" style="width: 100%">
      <Statistic></Statistic>
      <a-row :gutter="16">
        <a-col :span="8">
          <a-card title="接口分析">
            <PieChart1></PieChart1>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card title="接口分析">
            <CategoryChart></CategoryChart>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card title="接口分析">
            <PieChart2></PieChart2>
          </a-card>
        </a-col>
      </a-row>
    </a-space>

  </div>
</template>

<style scoped lang="less">

</style>
