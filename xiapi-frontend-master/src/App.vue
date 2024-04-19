<script setup lang="ts">
import {nextTick, ref} from "vue";

const appStore = useAppStore()

const isRouterAlive = ref<boolean>(true)
const reload = () => {
  isRouterAlive.value = false
  nextTick(() => {
    isRouterAlive.value = true
  })
}

provide('reload', reload)
</script>

<template>
  <a-config-provider
      :theme="appStore.theme"
  >
    <!--    <RouterView/>-->
    <router-view v-if="isRouterAlive"></router-view>
  </a-config-provider>
</template>
