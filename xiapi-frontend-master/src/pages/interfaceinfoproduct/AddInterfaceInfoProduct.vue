<script setup lang="ts">
import {reactive, ref, toRaw} from 'vue';
import type {UnwrapRef} from 'vue';
import request from "~/utils/request.ts";
import {message, SelectProps} from "ant-design-vue/es";

interface AddForm {
  interfaceInfoId: number,
  specification: number,
  price: number,
  stock: number
}

// 获取父组件的handleCancel方法
const emit = defineEmits(['closeOpen'])

const formRef = ref();
const labelCol = {span: 5};
const wrapperCol = {span: 13};
const formState = ref<AddForm>({})
const onSubmit = async () => {
  const res = await request.post('InterfaceInfoProduct/add', formState.value)
  if (res.code === 0) {
    message.success('添加成功 !');
    formRef.value.resetFields();
    // 调用父类handleCancel方法
    emit('closeOpen')
  } else {
    message.error(res.message);
  }
};
const resetForm = () => {

};

// 选择框
const getInterfaceIdAndName = async () => {
  const res = await request.get('interfaceInfo/getInterfaceIdAndName')
  if (res.code === 0){
    options.value = res.data
  }
}

const options = ref<SelectProps['options']>([]);
// 获取焦点时回调
const focus = () => {
  console.log('focus');
};
const handleChange = (value: number) => {
  formState.value.interfaceInfoId = value
  console.log(`selected ${value}`);
};

onMounted(() => {
  getInterfaceIdAndName()
})
</script>

<template>
  <a-form
      ref="formRef"
      :model="formState"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
  >
    <a-form-item ref="name" label="接口" name="name">
      <a-select
          ref="select"
          :options="options"
          @focus="focus"
          @change="handleChange"
          placeholder="请选择接口"
      >
      </a-select>
    </a-form-item>
    <a-form-item ref="name" label="规格(次)" name="name">
      <a-input v-model:value="formState.specification" placeholder="请输入接口商品规格"/>
    </a-form-item>
    <a-form-item ref="name" label="价格(元)" name="name">
      <a-input v-model:value="formState.price" placeholder="请输入接口商品价格"/>
    </a-form-item>
    <a-form-item ref="name" label="库存" name="name">
      <a-input v-model:value="formState.stock" placeholder="请输入接口商品库存"/>
    </a-form-item>
    <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
      <a-button type="primary" @click="onSubmit">添加</a-button>
      <a-button style="margin-left: 10px" @click="resetForm">重置</a-button>
    </a-form-item>
  </a-form>
</template>

<style scoped lang="less">

</style>
