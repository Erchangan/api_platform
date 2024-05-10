<script setup lang="ts">
import {SettingOutlined, EditOutlined, EllipsisOutlined} from '@ant-design/icons-vue';
import {ref} from 'vue';
import {InterfaceInfo} from "~/model/interfaceinfo/InterfaceInfo.ts";
import {Data, Page, Result} from "~/model/common/common.ts";
import request from "~/utils/request.ts";
import {message} from "ant-design-vue";
import Show from "~/pages/index/Show.vue";

const current = ref(1);
const pageInfo = ref<Page>({
  current: 1,
  pageSize: 4
})

const data = ref<InterfaceInfo[]>([])
const total = ref<number>(0)

const listInterfaceInfoByPage = async () => {
  const res: Result<Data<InterfaceInfo>> = await request.get('interfaceInfo/list/page', {
    params: pageInfo.value
  })
  if (res.code === 0) {
    data.value = res.data.records
    total.value = Number(res.data.total)
  } else {
    message.error(res.message)
  }
}


onMounted(() => {
  listInterfaceInfoByPage()
})

watch(current, (newCurrent) => {
  console.log("current:", newCurrent)
  pageInfo.value.current = newCurrent
  listInterfaceInfoByPage()
})

// 弹窗
const open = ref<boolean>(false);
const showId = ref<number>(0)
const showModal = (id) => {
  showId.value = Number(id)
  open.value = true;
};


</script>

<template>
  <div>
    <a-card title="接口市场" class="interface-market-card" >
      <a-list
          :grid="{ gutter: 16, xs: 1, sm: 1, md: 2, lg: 3, xl: 4, xxl: 3 }"
          item-layout="horizontal"
          :data-source="data"
      >
        <template #renderItem="{ item }">
          <a-list-item>
            <a-card hoverable style="width: 300px">
              <template #cover>
                <img
                    alt="example"
                    src="/public/block-chain-6999644_1280.webp"
                />
              </template>
              <template #actions>
                <a @click="showModal(item.id)">查看</a>
                <!--              <setting-outlined key="setting" />-->
                <!--              <edit-outlined key="edit" />-->
                <!--              <ellipsis-outlined key="ellipsis" />-->
              </template>
              <a-card-meta :title="item.name" :description="item.description">
              </a-card-meta>
            </a-card>
          </a-list-item>
        </template>
      </a-list>
      <a-pagination v-model:current="current" :total="total " :page-size="pageInfo.pageSize" show-less-items/>
    </a-card>
    <a-modal
        v-model:open="open"
        title="接口详情"
        width="100%"
        wrap-class-name="full-modal"
        style="top: 10px">
      <template #footer>
      </template>
      <Show :id="showId"></Show>
    </a-modal>
  </div>
</template>

<style scoped lang="less">
.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }

  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh);
  }

  .ant-modal-body {
    flex: 1;
  }
}

.interface-market-card {
  background-image: url('/public/background.jpg');
  background-size: cover;
}
</style>
