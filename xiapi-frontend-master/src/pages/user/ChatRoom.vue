<script lang="ts" setup>
import {ref} from "vue";
import request from "~/utils/request.ts";

interface ChatMessage {
  message?: string
  reply?: string
}

const messages = ref<ChatMessage[]>([
  {
    message: "你好管理员",
    reply: "你好"
  },
  {
    message: "某个接口有问题",
    reply: "具体是哪一个接口"
  }
])

const message = ref<string>("")
const allLoginUser = ref([])
const toUser = ref<string>("xiaoxi")
const currentLoginUserRole = ref<string>()

onMounted(() => {
  getAllLoginUser()
  let loginUser = JSON.parse(localStorage.getItem("userInfo"));
  currentLoginUserRole.value = loginUser.userRole
})

const webSocket = new WebSocket(`ws://127.0.0.1:8101/api/websocket`)
webSocket.onopen = () => {
  console.log('WebSocket connected')
}

webSocket.onmessage = (event) => {
  let msg = event.data
  const msgObject: ChatMessage = {
    message: undefined,
    reply: undefined
  }
  msgObject.message = msg
  messages.value.push(msgObject)
  console.log(msg)
}

const getMessage = (item) => {
  allLoginUser.value.forEach(item => {
    item.selected = false
  })
  item.selected = !item.selected
  toUser.value = item.userAccount
  console.log(toUser.value)
}

const sendMessage = () => {
  const msgObject: ChatMessage = {
    message: undefined,
    reply: undefined
  }
  webSocket.send(JSON.stringify({
    targetUser: toUser.value,
    message: message.value
  }))
  msgObject.reply = message.value
  messages.value.push(msgObject)
  message.value = ''
}

const getAllLoginUser = async () => {
  const res = await request.get('user/getAllLoginUser')
  if (res.code == 0) {
    let filterList = res.data.filter(item=>item.userRole != 'admin');
    let resWithSelected = filterList.map(item => ({...item, selected: false}));
    allLoginUser.value = resWithSelected
  }
}

</script>

<template>
  <div>
    <div style="display: flex;justify-content: center">
      <a-card :body-style="{width: '700px',height: '550px'}">
        <div>
          <a-card-meta title="系统客服">
            <template #avatar>
              <a-avatar src="http://127.0.0.1:8101/api/user_avatar/2/zJIke0Ma-01.jpg"></a-avatar>
            </template>
          </a-card-meta>
          <a-divider/>
        </div>
        <div>
          <a-layout>
            <a-layout-sider :style="{height:'450px',background:'white' }" v-if="currentLoginUserRole == 'admin'">
              <a-list item-layout="horizontal" :data-source="allLoginUser">
                <template #renderItem="{ item }">
                  <a-list-item @click="getMessage(item)"
                               :style="{backgroundColor: item.selected ? '#b7d8f5' : 'white'}">
                    <a-list-item-meta>
                      <template #title>
                        {{ item.userAccount }}
                      </template>
                      <template #avatar>
                        <a-avatar :src="item.userAvatar"/>
                      </template>
                    </a-list-item-meta>
                  </a-list-item>
                </template>
              </a-list>
            </a-layout-sider>
            <a-layout-content :style="{ height: '450px', padding: '20px', overflow: 'auto' }">
              <div class="chat-container" v-for="item in messages">
                <a-tag v-if="item.message != undefined && item !=null " class="message left">{{ item.message }}</a-tag>
                <a-tag v-if="item.reply != undefined && item !=null " class="message right">{{ item.reply }}</a-tag>
              </div>
            </a-layout-content>
          </a-layout>
        </div>
        <template #actions>
          <a-space>
            <a-input v-model:value="message" :style="{width: '500px'}" placeholder="请输入消息">
            </a-input>
            <a-button type="primary" @click="sendMessage">
              senMessage
            </a-button>
          </a-space>
        </template>
      </a-card>
    </div>
  </div>
</template>


<style scoped lang="less">
.chat-container {
  display: flex;
  flex-direction: column;
}

.message {
  max-width: 70%; /* 聊天气泡的最大宽度 */
  margin-bottom: 10px; /* 聊天气泡之间的间距 */
  align-self: flex-end; /* 右对齐 */
}

.left {
  align-self: flex-start; /* 左对齐 */
  background-color: #f0f0f0; /* 对方发送的消息背景色 */
  color: #000; /* 文字颜色 */
  border-radius: 15px 15px 15px 0px; /* 圆角 */
  padding: 10px 15px; /* 内边距 */
}

.right {
  background-color: #f0f0f0; /* 自己发送的消息背景色 */
  color: #000; /* 文字颜色 */
  border-radius: 15px 15px 0 15px; /* 圆角 */
  padding: 10px 15px; /* 内边距 */
}

/* 聊天气泡之间的间距 */
.message + .message {
  margin-top: 5px;
}
</style>