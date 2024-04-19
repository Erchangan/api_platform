<script setup lang="ts">

import request from "~/utils/request.ts";
import {DownOutlined, UpOutlined} from '@ant-design/icons-vue';
import {computed, inject, reactive, ref} from "vue";
import type {FormInstance, PaginationProps} from 'ant-design-vue';
import {message} from "ant-design-vue";
import {usePagination} from 'vue-request';
import UpdateInterfaceInfoProduct from "~/pages/interfaceinfoproduct/UpdateInterfaceInfoProduct.vue";
import AddInterfaceInfoProduct from "~/pages/interfaceinfoproduct/AddInterfaceInfoProduct.vue";

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '接口名称',
    dataIndex: 'interfaceInfoName',
    key: 'interfaceInfoName',
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
    title: '库存',
    dataIndex: 'stock',
    key: 'stock',
  },
  {
    title: '操作',
    key: 'action',
    width: 150
  },
];


const dto = ref({current: 1, pageSize: 5})

const {data, total, run} = usePagination(
    (d) => request.get('InterfaceInfoProduct/list/page', {params: d}),
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

const interfaceInfoProducts = computed(() => {
  return data.value?.data.records || []
})

// 高级搜索框
const expand = ref(false);
const formRef = ref<FormInstance>();
const formState = reactive({});
const onFinish = (values: any) => {
  console.log('Received values of form: ', values);
  console.log('formState: ', formState);
};

// 添加弹出框
const loading = ref<boolean>(false);
const open = ref<boolean>(false);
const showModal = () => {
  open.value = true;
};

const reload = inject('reload')

const handleCancel = () => {
  open.value = false;
  reload();
};

// 修改弹出框
const openUpdate = ref<boolean>(false);
const updateId = ref<number>(0)
const showUpdateModal = (reload) => {
  updateId.value = Number(reload.id)
  openUpdate.value = true
};


const handleCancelUpdate = () => {
  openUpdate.value = false;
  reload();
};

// 删除接口信息
const deleteId = ref<number>(0)
const handleDelete = (id) => {
  deleteId.value = id
}

const delInterfaceInfoProduct = async (id: number) => {
  const res = await request.post('InterfaceInfoProduct/delete', {
    "id": id
  })
  if (res.code === 0) {
    message.success("删除成功!")
    reload();
  } else {
    message.error(res.message)
  }
}

const delConfirm = (e: MouseEvent) => {
  delInterfaceInfoProduct(deleteId.value)
};

const delCancel = (e: MouseEvent) => {
};

</script>

<template>
<div>
    <a-card title="接口商品管理">
      <template #extra>
        <a-button type="primary" @click="showModal()">
          添加
        </a-button>
      </template>
      <a-table
          :columns="columns"
          :data-source="interfaceInfoProducts"
          :pagination="pagination"
          @change="tableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
        <span>
          <a @click="showUpdateModal(record)">修改</a>
          <a-divider type="vertical"/>
          <a-popconfirm
              title="确定要删除此条信息吗?"
              ok-text="是"
              cancel-text="否"
              @confirm="delConfirm"
              @cancel="delCancel"
          >
          <a @click="handleDelete(record.id)">删除</a>
         </a-popconfirm>
        </span>
          </template>
        </template>
      </a-table>
    </a-card>
  <div>
    <a-modal v-model:open="open" title="添加接口信息">
      <template #footer>
      </template>
      <AddInterfaceInfoProduct @closeOpen="handleCancel"></AddInterfaceInfoProduct>
    </a-modal>
  </div>
  <div>
    <a-modal v-model:open="openUpdate" title="修改接口信息">
      <template #footer>
      </template>
      <UpdateInterfaceInfoProduct :updateId="updateId" @closeOpen="handleCancelUpdate"></UpdateInterfaceInfoProduct>
    </a-modal>
  </div>
</div>
</template>

<style scoped lang="less">

</style>
