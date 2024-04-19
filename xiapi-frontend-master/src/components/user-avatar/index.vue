<script setup lang="ts">
import { LogoutOutlined, ProfileOutlined, UserOutlined } from '@ant-design/icons-vue'
import {useUserInfo} from "~/stores/userstore.ts";

const useuserinfo = useUserInfo()

const router = useRouter();

const logout = ()=>{
  // window.location.href="/login"
  router.replace('/login')
}

const toUserCenter = () => {
  router.push('/usercenter')
}

const toDevPlatform = () => {
  router.push('devplatform')
}
</script>

<template>
  <a-dropdown>
    <span hover="bg-[var(--hover-color)]" flex items-center h-48px px-12px cursor-pointer class="transition-all-300">
      <a-avatar :src="useuserinfo.userAvatar" mr-8px size="small" />
      <span class="anticon">{{ useuserinfo.userName }}</span>
    </span>
    <template #overlay>
      <a-menu>
        <a-menu-item key="0">
          <template #icon>
            <UserOutlined />
          </template>
          <a target="_blank" rel="noopener noreferrer" @click="toUserCenter">
            个人中心
          </a>
        </a-menu-item>
        <a-menu-item key="1">
          <template #icon>
            <ProfileOutlined />
          </template>
          <a target="_blank" rel="noopener noreferrer" @click="toDevPlatform">
            开放平台
          </a>
        </a-menu-item>
        <a-menu-divider />
        <a-menu-item key="3" @click="logout()">
          <template #icon>
            <LogoutOutlined />
          </template>
          退出登录
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>
