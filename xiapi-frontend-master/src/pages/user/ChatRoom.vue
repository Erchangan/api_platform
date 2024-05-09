<script lang="ts" setup>
import {ref} from "vue";


const messages = ref<string[]>([])
const message = ref<string>("")
const webSocket = new WebSocket(`ws://127.0.0.1:8101/api/websocket`)
webSocket.onopen = () => {
  console.log('WebSocket connected')
}

webSocket.onmessage = (event) => {
  debugger
  const msg = event.data
  messages.value.push(msg)
  console.log(msg)
}

const sendMessage = () => {
  webSocket.send(JSON.stringify({
    targetUser: "xiaoxi",
    message: message.value
  }))
  message.value = ''
}

</script>

<template>
  <div>

  </div>
</template>


<style scoped>

</style>