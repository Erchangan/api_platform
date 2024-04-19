import { createPinia } from 'pinia'
import { createApp } from 'vue'
import App from './App.vue'
import router from '~/router'
import '~/router/router-guard'
import 'ant-design-vue/dist/reset.css'
import '~/assets/styles/reset.css'
import 'uno.css'
import 'echarts'
import ECharts from 'vue-echarts'

const app = createApp(App)
const pinia = createPinia()
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(router)
app.mount('#app')
app.component('v-chart', ECharts)
