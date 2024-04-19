<script setup lang="ts">
import {reactive, ref, toRaw} from 'vue';
import type {UnwrapRef} from 'vue';
import request from "~/utils/request.ts";
import {message} from "ant-design-vue/es";

interface AddForm {
  name: string;
  description: string;
  host: string;
  url: string;
  requestParams: string;
  requestHeader: string;
  responseHeader: string;
  method: string;
}
// 获取父组件的handleCancel方法
const emit = defineEmits(['closeOpen'])

const formRef = ref();
const labelCol = {span: 5};
const wrapperCol = {span: 13};
const formState: UnwrapRef<AddForm> = reactive({
  name: '',
  description: '',
  host: '',
  url: '',
  requestParams: '{}',
  requestHeader: '{}',
  responseHeader: '{}',
  method: ''
});
const onSubmit = async () => {
  const res = await request.post('interfaceInfo/add', formState)
  if (res.code === 0){
    message.success('添加成功 !');
    formRef.value.resetFields();
    // 调用父类handleCancel方法
    emit('closeOpen')
  }else {
    message.error(res.message);
  }
};
const resetForm = () => {
  formRef.value.resetFields();
};


</script>

<template>
  <a-form
      ref="formRef"
      :model="formState"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
  >
    <a-form-item ref="name" label="接口名称" name="name">
      <a-input v-model:value="formState.name"/>
    </a-form-item>
    <a-form-item label="接口描述" name="description">
      <a-textarea v-model:value="formState.description" />
    </a-form-item>
    <a-form-item ref="host" label="接口地址" name="host">
      <a-input v-model:value="formState.host" placeholder="http://127.0.0.1:8081"/>
    </a-form-item>
    <a-form-item ref="url" label="访问路径" name="url">
      <a-input v-model:value="formState.url" placeholder="/getName"/>
    </a-form-item>
    <a-form-item label="请求参数" name="requestParams">
      <a-textarea v-model:value="formState.requestParams" />
    </a-form-item>
    <a-form-item label="请求头" name="requestHeader">
      <a-textarea v-model:value="formState.requestHeader" />
    </a-form-item>
    <a-form-item label="响应头" name="responseHeader">
      <a-textarea v-model:value="formState.responseHeader" />
    </a-form-item>
    <a-form-item label="请求方法" name="method">
      <a-select v-model:value="formState.method" placeholder="请选择请求方法">
        <a-select-option value="GET">GET</a-select-option>
        <a-select-option value="POST">POST</a-select-option>
        <a-select-option value="DELETE">DELETE</a-select-option>
        <a-select-option value="UPDATE">UPDATE</a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
      <a-button type="primary" @click="onSubmit">添加</a-button>
      <a-button style="margin-left: 10px" @click="resetForm">重置</a-button>
    </a-form-item>
  </a-form>
</template>

<style scoped lang="less">

</style>
