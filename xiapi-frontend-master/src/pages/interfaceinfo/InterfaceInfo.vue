<script setup lang="ts">

import request from "~/utils/request.ts";
import {DownOutlined, UpOutlined} from '@ant-design/icons-vue';
import {computed, inject, reactive, ref} from "vue";
import type {FormInstance, PaginationProps} from 'ant-design-vue';
import {message} from "ant-design-vue";
import {usePagination} from 'vue-request';
import AddInterfaceInfo from "~/pages/interfaceinfo/AddInterfaceInfo.vue";
import UpdateInterfaceInfo from "~/pages/interfaceinfo/UpdateInterfaceInfo.vue";

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '接口名称',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '描述',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '接口地址',
    dataIndex: 'host',
    key: 'host',
  },
  {
    title: '访问路径',
    key: 'url',
    dataIndex: 'url',
  },
  {
    title: '请求方法',
    key: 'method',
    dataIndex: 'method',
  },
  {
    title: '请求参数',
    key: 'requestParams',
    dataIndex: 'requestParams',
  },
  {
    title: '请求头',
    key: 'requestHeader',
    dataIndex: 'requestHeader',
  },
  {
    title: '响应头',
    key: 'responseHeader',
    dataIndex: 'responseHeader',
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
  },
  {
    title: '创建人',
    key: 'userId',
    dataIndex: 'userId',
  },
  {
    title: '操作',
    key: 'action',
    width: 150
  },
];


const dto = ref({current: 1, pageSize: 5})

const {data, total, run} = usePagination(
    (d) => request.get('interfaceInfo/list/page', {params: d}),
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

const interfaceInfos = computed(() => {
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
// const interfaceInfo = useInterfaceInfo()
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

const delInterfaceInfo = async (id: number) => {
  const res = await request.post('interfaceInfo/delete', {
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
  delInterfaceInfo(deleteId.value)
};

const delCancel = (e: MouseEvent) => {
};

// 开启关闭

const onlineOrOfflineInterfaceInfo = async (id: number, status: number) => {
  if (status === 0) {
    // 上线
    const res = await request.post('interfaceInfo/online', {id: id})
    if (res.code === 0) {
      message.success("上线成功!")
      reload()
    } else {
      message.error(res.message)
    }
  } else if (status === 1) {
    // 下线
    const res = await request.post('interfaceInfo/offline', {id: id})
    if (res.code === 0) {
      message.success("下线成功!")
      reload()
    } else {
      message.error(res.message)
    }
  }
}

const handleOnOrOff = (id: number, status: number) => {
  onlineOrOfflineInterfaceInfo(id, status)
}
</script>

<template>
  <div>
    <a-card title="接口管理">
      <template #extra>
        <a-button type="primary" @click="showModal()">
          添加
        </a-button>
      </template>
      <a-table
          :columns="columns"
          :data-source="interfaceInfos"
          :pagination="pagination"
          @change="tableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <template v-if="record.status === 0">
              <a-tag :bordered="false" color="error">已关闭</a-tag>
            </template>
            <template v-else-if="record.status === 1">
              <a-tag :bordered="false" color="success">运行中</a-tag>
            </template>
          </template>
          <template v-if="column.key === 'action'">
        <span>
          <template v-if="record.status === 0">
            <a style="color: green" @click="handleOnOrOff(record.id, record.status)">上线</a>
          </template>
          <template v-else-if="record.status === 1">
            <a style="color: red" @click="handleOnOrOff(record.id, record.status)">下线</a>
          </template>
          <a-divider type="vertical"/>
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
        <AddInterfaceInfo @closeOpen="handleCancel"></AddInterfaceInfo>
      </a-modal>
    </div>
    <div>
      <a-modal v-model:open="openUpdate" title="修改接口信息">
        <template #footer>
        </template>
        <UpdateInterfaceInfo :updateId="updateId" @closeOpen="handleCancelUpdate"></UpdateInterfaceInfo>
      </a-modal>
    </div>
  </div>
</template>

<style scoped lang="less">

</style>
