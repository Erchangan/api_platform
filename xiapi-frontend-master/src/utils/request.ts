import axios from "axios";
import {message} from "ant-design-vue";
import router from "~/router";

const instance = axios.create({
  baseURL: 'http://127.0.0.1:8101/api/',
  timeout: 50000,
});

instance.defaults.withCredentials = true; // 配置为true

// 添加请求拦截器
instance.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  console.log("请求拦截器:")
  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
  // 未登录则跳转到登录页
  if (response?.data?.code === 40100) {
    message.warning(response?.data?.message);
    // window.location.href="/login"
    router.replace('/login')
  }
  console.log("响应拦截器:", response.data)
  return response.data;
}, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  return Promise.reject(error);
});

export default instance;
