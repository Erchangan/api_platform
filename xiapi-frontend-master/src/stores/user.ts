import type { MenuData } from '~@/layouts/basic-layout/typing'
import dynamicRoutes, {adminRoute, rootRoute} from '~@/router/dynamic-routes'
import { genRoutes } from '~@/router/generate-route'
import {ref} from "vue";
import request from "~/utils/request.ts";
import {useUserInfo} from "~/stores/userstore.ts";

export const useUserStore = defineStore('user', () => {
  const routerData = shallowRef()
  const menuData = shallowRef<MenuData>([])

  const userInfo = ref<{
    id: number,
    userAvatar: string,
    userName: string,
    userProfile: string,
    userRole: string
  }>({
    id: 0,
    userAvatar: '',
    userName: 'XXX',
    userProfile: 'xxxxx',
    userRole: 'user'
  })

  const userName = computed(() => userInfo.value.userName)
  const userAvatar = computed(() => userInfo.value.userAvatar)
  const userProfile = computed(() => userInfo.value.userProfile)
  const userRole = computed(() => userInfo.value.userRole)

  // const userInfo = reactive({
  //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png',
  //   username: 'admin',
  //   nickname: '超级管理员',
  // })
  //
  // const avatar = computed(() => userInfo.avatar)
  // const nickname = computed(() => userInfo.nickname ?? userInfo.username)

  const generateRoutes = async () => {

    const useuserinfo = useUserInfo()

    if (useuserinfo.userRole === 'admin'){
      dynamicRoutes.push(adminRoute)
    }
    const currentRoute = {
      ...rootRoute,
      children: dynamicRoutes,
    }
    menuData.value = genRoutes(dynamicRoutes)
    return currentRoute
  }

  const generateDynamicRoutes = async () => {
    const routerDatas = await generateRoutes()
    routerData.value = routerDatas
    return routerDatas
  }

  return {
    routerData,
    menuData,
    generateDynamicRoutes,
    // avatar,
    // nickname,
    userName,
    userAvatar,
    userProfile,
    userRole
  }
})
