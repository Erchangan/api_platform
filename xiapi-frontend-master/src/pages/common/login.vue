<script setup lang="ts">
import {
  AlipayCircleFilled,
  LockOutlined,
  MobileOutlined,
  TaobaoCircleFilled,
  UserOutlined,
  WeiboCircleFilled
} from '@ant-design/icons-vue'
import GlobalLayoutFooter from '~/layouts/components/global-footer/index.vue'
import {message} from "ant-design-vue";
import request from "~/utils/request.ts";
import {ref} from "vue";
import {useUserInfo} from "~/stores/userstore.ts";
// import { loginApi } from '~/api/common/login'
const appStore = useAppStore()
const {layoutSetting} = storeToRefs(appStore)
const router = useRouter()

const useuserinfo = useUserInfo()

const userLoginModel = reactive({
  userAccount: undefined,
  userPassword: undefined,
})

const userRegisterModel = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: ""
})

const registerModel = reactive({
  type: 'account'
})

const userInfo = ref({
  id: 0,
  userAvatar: '',
  userName: '',
  userProfile: '',
  userRole: ''
})

const formRef = shallowRef()
const codeLoading = shallowRef(false)
const antdToken = useAntdToken()
const resetCounter = 60
const submitLoading = shallowRef(false)

const {counter, pause, reset, resume, isActive} = useInterval(1000, {
  controls: true,
  immediate: false,
  callback(count) {
    if (count) {
      if (count === resetCounter)
        pause()
    }
  },
})
const getCode = async () => {
  codeLoading.value = true
  try {
    await formRef.value.validate(['mobile'])
    setTimeout(() => {
      reset()
      resume()
      codeLoading.value = false
    }, 3000)
  } catch (error) {
    // TODO
    codeLoading.value = false
  }
}

const submit = async () => {
  const res = await request.post('/user/login', userLoginModel)
  if (res.code === 0 && res.data) {
    userInfo.value.id = res.data.id
    userInfo.value.userName = res.data.userName
    userInfo.value.userAvatar = res.data.userAvatar
    userInfo.value.userProfile = res.data.userProfile
    userInfo.value.userRole = res.data.userRole

    Object.assign(useuserinfo, userInfo.value)
    // 跳转到主页
    // router.replace('/')
    window.location.href = "/"
    message.success('登录成功');
  } else {
    message.error(res.message);
  }
}

const register = async () => {
  const res = await request.post('/user/register', userRegisterModel)
  if (res.code === 0 && res.data) {
    message.success('注册成功');
    registerModel.type = 'account'
    userLoginModel.userAccount = userRegisterModel.userAccount
    userLoginModel.userPassword = userRegisterModel.userPassword
    userRegisterModel.userAccount = ""
    userRegisterModel.userPassword = ""
    userRegisterModel.checkPassword = ""
  } else {
    message.error(res.message);
  }
}


</script>

<template>
  <div class="login-container">
    <div class="login-lang" flex="~" items-center justify-end gap-2 px-24px>
      <span flex items-center justify-center cursor-pointer text-16px
            @click="appStore.toggleTheme(layoutSetting.theme === 'dark' ? 'light' : 'dark')">
        <!-- 亮色和暗黑模式切换按钮 -->
        <template v-if="layoutSetting.theme === 'light'">
          <carbon-moon/>
        </template>
        <template v-else>
          <carbon-sun/>
        </template>
      </span>
    </div>
    <div class="login-content">
      <div class="ant-pro-form-login-cotainer">
        <div class="ant-pro-form-login-top">
          <div class="ant-pro-form-login-header">
            <span class="ant-pro-form-login-logo">
              <img src="/favicon.ico">
            </span>
            <span class="ant-pro-form-login-title">
              迅联API
            </span>
          </div>
          <div class="ant-pro-form-login-desc">
            迅联 API 让你轻松的调用第三方接口
          </div>
        </div>
        <div class="ant-pro-form-login-main" w-335px>
          <a-form ref="formRef" :model="userRegisterModel">
            <a-tabs v-model:activeKey="registerModel.type" centered>
              <a-tab-pane key="account" tab="用户登录"/>
              <a-tab-pane key="register" tab="用户注册"/>
            </a-tabs>
            <template v-if="registerModel.type === 'account'">
              <a-form-item name="username">
                <a-input v-model:value="userLoginModel.userAccount" allow-clear placeholder="请输入账号" size="large"
                         @pressEnter="submit">
                  <template #prefix>
                    <UserOutlined/>
                  </template>
                </a-input>
              </a-form-item>
              <a-form-item name="password">
                <a-input-password v-model:value="userLoginModel.userPassword" allow-clear placeholder="请输入密码"
                                  size="large" @pressEnter="submit">
                  <template #prefix>
                    <LockOutlined/>
                  </template>
                </a-input-password>
              </a-form-item>
              <a-button type="primary" block :loading="submitLoading" size="large" @click="submit">
                登录
              </a-button>
            </template>
            <template v-if="registerModel.type === 'register'">
              <a-form-item name="userAccount" :rules="[{ required: true, message: '账号不能为空' }]">
                <a-input v-model:value="userRegisterModel.userAccount" allow-clear placeholder="请输入用户名！"
                         size="large">
                  <template #prefix>
                    <UserOutlined/>
                  </template>
                </a-input>
              </a-form-item>
              <a-form-item name="userPassword" :rules="[{ required: true, message: '密码不能为空' }]">
                <div flex items-center>
                  <a-input-password
                      v-model:value="userRegisterModel.userPassword"
                      style="flex: 1 1 0%; transition: width 0.3s ease 0s; margin-right: 8px;"
                      allow-clear placeholder="请输入密码！" size="large"
                  >
                    <template #prefix>
                      <LockOutlined/>
                    </template>
                  </a-input-password>
                </div>
              </a-form-item>
              <a-form-item name="checkPassword" :rules="[{ required: true, message: '密码不能为空' }]">
                <a-input-password v-model:value="userRegisterModel.checkPassword" allow-clear placeholder="请确认密码！"
                                  size="large">
                  <template #prefix>
                    <LockOutlined/>
                  </template>
                </a-input-password>
              </a-form-item>
              <a-button type="primary" block :loading="submitLoading" size="large" @click="register">
                注册
              </a-button>
            </template>
          </a-form>
          <div class="ant-pro-form-login-other" text-14px>
            其他登录方式:
            <AlipayCircleFilled class="icon"/>
            <TaobaoCircleFilled class="icon"/>
            <WeiboCircleFilled class="icon"/>
          </div>
        </div>
      </div>
    </div>
    <div py-24px px-50px :data-theme="layoutSetting.theme" text-14px>
      <GlobalLayoutFooter :copyright="layoutSetting.copyright">
        <template #renderFooterLinks>
          <footer-links/>
        </template>
      </GlobalLayoutFooter>
    </div>
  </div>
</template>

<style lang="less" scoped>
.login-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: auto;
  background: var(--bg-color-container);
}

.login-lang {
  width: 100%;
  height: 40px;
  line-height: 44px;
}

.login-content {
  flex: 1 1;
  padding: 32px 0
}

.ant-pro-form-login-cotainer {
  display: flex;
  flex: 1 1;
  flex-direction: column;
  height: 100%;
  padding: 32px 0;
  overflow: auto;
  background: inherit
}

.ant-pro-form-login-top {
  text-align: center
}

.ant-pro-form-login-header {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 44px;
  line-height: 44px
}

.ant-pro-form-login-header a {
  text-decoration: none
}

.ant-pro-form-login-title {
  position: relative;
  top: 2px;
  color: var(--text-color);
  font-weight: 600;
  font-size: 33px
}

.ant-pro-form-login-logo {
  width: 44px;
  height: 44px;
  margin-right: 16px;
  vertical-align: top
}

.ant-pro-form-login-logo img {
  width: 100%
}

.ant-pro-form-login-desc {
  margin-top: 12px;
  margin-bottom: 40px;
  color: var(--text-color-1);
  font-size: 14px
}

.ant-pro-form-login-main {
  min-width: 328px;
  max-width: 500px;
  margin: 0 auto
}

.ant-pro-form-login-main {
  .ant-tabs-nav-list {
    margin: 0 auto;
    font-size: 16px;
  }

  .ant-pro-form-login-other {
    margin-top: 24px;
    line-height: 22px;
    text-align: left
  }

  .icon {
    margin-left: 8px;
    color: var(--text-color-2);
    font-size: 24px;
    vertical-align: middle;
    cursor: pointer;
    transition: color .3s;

    &:hover {
      color: v-bind('antdToken.colorPrimary');
    }
  }
}

@media (min-width: 768px) {
  .login-container {
    background-image: url(https://gw.alipayobjects.com/zos/rmsportal/TVYTbAXWheQpRcWDaDMu.svg);
    background-repeat: no-repeat;
    background-position: center 110px;
    background-size: 100%;
  }

  .login-content {
    padding: 32px 0 24px;
  }

  .ant-pro-form-login-cotainer {
    padding: 32px 0 24px;
    background-repeat: no-repeat;
    background-position: center 110px;
    background-size: 100%
  }
}
</style>
