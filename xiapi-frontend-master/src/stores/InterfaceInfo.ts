import {defineStore} from "pinia";

export const useInterfaceInfo = defineStore("interfaceinfo", {
    state: () => {
        return {
            id: 0,
            name: '',
            description: '',
            host: '',
            url: '',
            requestParams: '{}',
            requestHeader: '{}',
            responseHeader: '{}',
            status: 0,
            method: ''
        }
    }
})
