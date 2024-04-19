<script setup lang="ts">
import {reactive, ref, toRaw, watch} from 'vue';
import request from "~/utils/request.ts";
import {message} from "ant-design-vue/es";
import {useInterfaceInfo} from "~/stores/InterfaceInfo.ts";

const id = defineProps<{updateId: number}>()

interface UpdateForm {
  id: number
  specification: number,
  price: number,
  stock: number
}

// 获取父组件的handleCancel方法
const emit = defineEmits(['closeOpen'])

const formRef = ref();
const labelCol = {span: 5};
const wrapperCol = {span: 13};


const formState = ref<UpdateForm>({
  id: 0,
  specification: 0,
  price: 0,
  stock: 0
});

const getById = async (id: number) => {
  const res = await request.get('InterfaceInfoProduct/get', {
    params: {
      'id': id
    }
  })
  if (res.code === 0){
    Object.assign(formState.value, res.data)
  }else {
    message.error(res.message);
  }
}

const onSubmit = async () => {
  const res = await request.post('InterfaceInfoProduct/update', formState.value)
  if (res.code === 0) {
    message.success('修改成功 !');
    formRef.value.resetFields();
    // 调用父类handleCancel方法
    emit('closeOpen')
  } else {
    message.error(res.message);
  }
};

watch(() => id.updateId, (id) => {
  getById(id)
})

onMounted(() => {
  getById(id.updateId)
})
</script>

<template>
  <a-form
      ref="formRef"
      :model="formState"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
  >
    <a-form-item ref="name" label="规格(次)" name="name">
      <a-input v-model:value="formState.specification"/>
    </a-form-item>
    <a-form-item ref="name" label="价格(元)" name="name">
      <a-input v-model:value="formState.price"/>
    </a-form-item>
    <a-form-item ref="name" label="库存" name="name">
      <a-input v-model:value="formState.stock"/>
    </a-form-item>
    <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
      <a-button type="primary" @click="onSubmit">更新</a-button>
    </a-form-item>
  </a-form>
</template>

<style scoped lang="less">

</style>
