import {defineStore} from "pinia";

export const useUserInfo = defineStore("userInfo", {
    state: () => {
        return {
            id: 0,
            userAvatar: '',
            userName: 'XXX',
            userProfile: 'xxxxx',
            userRole: 'user'
        }
    },
    persist: true,// 配置持久化
})
