<script setup lang="ts">

import request from "~/utils/request.ts";
import {message} from "ant-design-vue";
import {ref, watch, inject} from "vue";
import {InterfaceInfo} from "~/model/interfaceinfo/InterfaceInfo.ts";
import {MinusCircleOutlined, PlusOutlined, ToolOutlined, ShoppingCartOutlined} from '@ant-design/icons-vue';
import type {FormInstance} from 'ant-design-vue';
import * as dayjs from "dayjs";

const reload = inject('reload')

const id = defineProps<{ id: number }>()
const interfaceInfo = ref<InterfaceInfo>({
  id: 0,
  name: 'XX',
  description: 'XX',
  host: 'XX',
  url: 'XX',
  requestParams: '{XX}',
  requestHeader: '{XX}',
  responseHeader: '{XX}',
  status: 0,
  method: 'XX',
  userId: 0,
  createTime: 'XX',
  updateTime: 'XX'
})


const getById = async (id: number) => {
  const res = await request.get('interfaceInfo/get', {
    params: {
      'id': id
    }
  })
  if (res.code === 0) {
    Object.assign(interfaceInfo.value, res.data)
  } else {
    message.error(res.message);
  }
}

watch(() => id.id, (newId) => {
  getById(newId)
  invokeResult.value = '请调用接口'
  activeKey.value = '1'
  dynamicValidateForm.params = []
  invokeParam.value.id = id.id
  getByInterfaceId()
})

onMounted(() => {
  getById(id.id)
  getByInterfaceId()
})

// GET表单
interface Param {
  paramKey: string;
  paramValue: string;
  id: number;
}

const loading = ref<boolean>(false);
const formRef = ref<FormInstance>();
const dynamicValidateForm = reactive<{ params: Param[] }>({
  params: [],
});
const removeParam = (item: Param) => {
  const index = dynamicValidateForm.params.indexOf(item);
  if (index !== -1) {
    dynamicValidateForm.params.splice(index, 1);
  }
};
const addParam = () => {
  dynamicValidateForm.params.push({
    paramKey: '',
    paramValue: '',
    id: Date.now(),
  });
};

const invokeResult = ref('请调用接口')

interface InvokeParam {
  id: number,
  userRequestParams: string
}

const invokeParam = ref<InvokeParam>({
  id: id.id,
  userRequestParams: ''
})

const invokeInterfaceInfo = async (param: InvokeParam) => {
  const res = await request.post('interfaceInfo/invoke', param);
  if (res.code === 0) {
    invokeResult.value = res.data
    loading.value = false
    message.success("调用成功!")
  } else {
    message.error(res.message)
  }
}

const onFinish = values => {
  loading.value = true
  invokeParam.value.userRequestParams = JSON.stringify(values)
  invokeInterfaceInfo(invokeParam.value)
  // console.log(JSON.stringify(values))
  // console.log('Received values of form:', values);
  // console.log('dynamicValidateForm.users:', dynamicValidateForm.params);
};

// Tab
const activeKey = ref('1');

// 商品列表
const interfaceInfoProductColumns = ref([
  {
    title: '接口id',
    dataIndex: 'interfaceInfoId',
    key: 'interfaceInfoId',
  },
  {
    title: '接口名称',
    dataIndex: 'interfaceInfoName',
    key: 'interfaceInfoName',
  },
  {
    title: '规格(次)',
    dataIndex: 'specification',
    key: 'specification',
  },
  {
    title: '价格(元)',
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
])

const interfaceInfoProductList = ref([])
const orderId = ref<number>(0)

const getByInterfaceId = async () => {
  const res = await request.get('InterfaceInfoProduct/getByInterfaceId', {
    params: {
      'interfaceInfoId': id.id
    }
  })
  if (res.code === 0) {
    interfaceInfoProductList.value = res.data
  }
}

// 购买
const addOrderData = ref<{interfaceInfoProductId: number, interfaceInfoId: number}>({
  interfaceInfoProductId: 0,
  interfaceInfoId: 0
})

const handleBuy = (record) => {
  addOrderData.value.interfaceInfoProductId = record.id
  addOrderData.value.interfaceInfoId = record.interfaceInfoId
}
const addInterfaceInfoProduct = async () => {
  const res = await request.post('interfaceInfoOrder/add', addOrderData.value)
  if (res.code === 0){
    orderId.value = res.data
  }else {
    message.error(res.message)
  }
}

const buyConfirm = () => {
  addInterfaceInfoProduct()
  showModal()
}

const buyCancel = () => {

}

// 支付弹窗
const open = ref<boolean>(false);
const showModal = () => {
  open.value = true;
};

const payOrderData = ref<{
  id: number,
  interfaceInfoId: number,
  interfaceInfoProductId: number
}>({
  id: 0,
  interfaceInfoId: 0,
  interfaceInfoProductId: 0
})

const payOrder = async () => {
  payOrderData.value.id = orderId.value
  payOrderData.value.interfaceInfoId = addOrderData.value.interfaceInfoId
  payOrderData.value.interfaceInfoProductId = addOrderData.value.interfaceInfoProductId

  const res = await request.post('interfaceInfoOrder/pay', payOrderData.value);
  if (res.code === 0){
    message.success("支付成功")
    open.value = false
    reload()
  }else {
    message.error(res.message)
  }
}

// 格式化时间
const formatDate = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}
</script>

<template>
  <div>
    <a-descriptions
        bordered
        :column="{ xxl: 2, xl: 2, lg: 2, md: 2, sm: 1, xs: 1 }">
      <a-descriptions-item label="接口名称">{{ interfaceInfo.name }}</a-descriptions-item>
      <a-descriptions-item label="接口地址">{{ interfaceInfo.host }}</a-descriptions-item>
      <a-descriptions-item label="访问路径">{{ interfaceInfo.url }}</a-descriptions-item>
      <a-descriptions-item label="请求参数">{{ interfaceInfo.requestParams }}</a-descriptions-item>
      <a-descriptions-item label="请求头">{{ interfaceInfo.requestHeader }}</a-descriptions-item>
      <a-descriptions-item label="响应头">{{ interfaceInfo.responseHeader }}</a-descriptions-item>
      <a-descriptions-item label="请求方法">{{ interfaceInfo.method }}</a-descriptions-item>
      <a-descriptions-item label="创建人">{{ interfaceInfo.userId }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ formatDate(interfaceInfo.createTime) }}</a-descriptions-item>
      <a-descriptions-item label="更新时间">{{ formatDate(interfaceInfo.updateTime) }}</a-descriptions-item>
      <a-descriptions-item label="接口描述">{{ interfaceInfo.description }}</a-descriptions-item>
    </a-descriptions>

    <div style="margin-top: 20px">
      <a-tabs v-model:activeKey="activeKey">
        <a-tab-pane key="1">
          <template #tab>
        <span>
          <ToolOutlined/>
          测试
        </span>
          </template>
          <div>
            <a-card title="接口调用">
              <a-form
                  ref="formRef"
                  name="dynamic_form_nest_item"
                  :model="dynamicValidateForm"
                  @finish="onFinish"
              >
                <a-space
                    v-for="(param, index) in dynamicValidateForm.params"
                    :key="param.id"
                    style="display: flex; margin-bottom: 8px"
                    align="baseline"
                >
                  <a-form-item
                      :name="['params', index, 'paramKey']"
                      :rules="{
          required: true,
          message: '请填写参数key',
        }"
                  >
                    <a-input v-model:value="param.paramKey" placeholder="参数Key"/>
                  </a-form-item>
                  <a-form-item
                      :name="['params', index, 'paramValue']"
                      :rules="{
          required: true,
          message: '请填写参数Value',
        }"
                  >
                    <a-input v-model:value="param.paramValue" placeholder="参数Value"/>
                  </a-form-item>
                  <MinusCircleOutlined @click="removeParam(param)"/>
                </a-space>
                <a-form-item>
                  <a-button type="dashed" block @click="addParam">
                    <PlusOutlined/>
                    添加参数
                  </a-button>
                </a-form-item>
                <a-form-item>
                  <a-button type="primary" html-type="submit">调用</a-button>
                </a-form-item>
              </a-form>
            </a-card>
            <a-card title="调用返回结果">
              <a-spin :spinning="loading" tip="请耐心等待，如长时间未响应请重试...">
                {{ invokeResult }}
              </a-spin>
            </a-card>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2">
          <template #tab>
        <span>
          <ShoppingCartOutlined/>
          购买
        </span>
          </template>
          <a-table
              :dataSource="interfaceInfoProductList"
              :columns="interfaceInfoProductColumns"
              :pagination="false">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
        <span>
          <a-popconfirm
              title="确定要购买吗?"
              ok-text="是"
              cancel-text="否"
              @confirm="buyConfirm"
              @cancel="buyCancel"
          >
          <a @click="handleBuy(record)">购买</a>
         </a-popconfirm>
        </span>
              </template>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </div>
<!--    支付对话框-->
    <a-modal v-model:open="open" title="选择支付方式">
      <template #footer>
      </template>
        <a-button style="color: blue" block @click="payOrder">支付宝</a-button>
        <a-button style="color: green" block @click="payOrder">微信支付</a-button>
    </a-modal>
  </div>
</template>

<style scoped lang="less">

</style>
