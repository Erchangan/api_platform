<script setup lang="ts">
import {ref, h} from 'vue';
import {useUserInfo} from "~/stores/userstore.ts";
import {EditOutlined} from "@ant-design/icons-vue";

const useuserinfo = useUserInfo()

const open = ref<boolean>(false);

const showModal = () => {
  open.value = true;
};

const handleOk = () => {
  updateMyUser()
};

const handleCancel = () => {
  open.value = false;
};

// 头像上传
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import type { UploadChangeParam, UploadProps } from 'ant-design-vue';
import request from "~/utils/request.ts";

const fileList = ref([]);
const loading = ref<boolean>(false);
const imageUrl = ref<string>('');

const handleChange = (info: UploadChangeParam) => {
  if (info.file.status !== 'uploading') {
    imageUrl.value = info.fileList[0].response.data
    userForm.value.userAvatar = info.fileList[0].response.data
  }
  if (info.file.status === 'done') {
    message.success(`${info.file.name} 上传成功`);
  } else if (info.file.status === 'error') {
    message.error(`${info.file.name} 上传失败`);
  }
};

  const beforeUpload = (file: UploadProps['fileList'][number]) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('只可以上传JPG文件!');
    }
    const isLt2M = file.size / 1024 / 1024 < 10;
    if (!isLt2M) {
      message.error('文件大小不能超过10MB!');
    }
    return isJpgOrPng && isLt2M;
  };
// 输入框
interface User {
  userAvatar: string,
  userName: string,
  userProfile: string,
}

const userForm = ref<User>({
  userAvatar: useuserinfo.userAvatar,
  userName: useuserinfo.userName,
  userProfile: useuserinfo.userProfile,
})
const updateMyUser = async () => {
  const res = await request.post('user/update/my', userForm.value)
  if (res.code === 0){
    message.success("修改成功")
    useuserinfo.userAvatar = userForm.value.userAvatar
    useuserinfo.userName = userForm.value.userName
    useuserinfo.userProfile = userForm.value.userProfile
    open.value = false;
  }else {
    message.error(res.message)
  }
}

</script>

<template>
  <div>
    <a-space direction="vertical">
      <a-card title="个人信息">
        <a-space direction="vertical">
          <a-avatar
              :size="{ xs: 24, sm: 32, md: 40, lg: 64, xl: 80, xxl: 100 }"
              :src="useuserinfo.userAvatar"/>
          <a-descriptions
              :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
            <a-descriptions-item label="用户名">{{ useuserinfo.userName }}</a-descriptions-item>
            <a-descriptions-item label="身份">
              <template v-if="useuserinfo.userRole === 'admin'">
                <a-tag color="success">管理员</a-tag>
              </template>
              <template v-else>
                <a-tag color="default">用户</a-tag>
              </template>
            </a-descriptions-item>
            <a-descriptions-item label="个人简介">{{ useuserinfo.userProfile }}</a-descriptions-item>
          </a-descriptions>
        </a-space>
      </a-card>
      <a-card title="信息设置">
        <a-button @click="showModal" type="primary" :icon="h(EditOutlined)" ghost>修改信息</a-button>
      </a-card>
    </a-space>
    <a-modal v-model:open="open" title="修改个人信息" @ok="handleOk">
      <template #footer>
        <a-button key="back" @click="handleCancel">取消</a-button>
        <a-button key="submit" type="primary" @click="handleOk">修改</a-button>
      </template>
      <div>
        <a-upload
            v-model:file-list="fileList"
            name="file"
            list-type="picture-card"
            class="avatar-uploader"
            :show-upload-list="false"
            action="http://127.0.0.1:8101/api/file/upload?biz=user_avatar"
            :before-upload="beforeUpload"
            :with-credentials='true'
            @change="handleChange"
        >
          <img v-if="imageUrl" :src="imageUrl" alt="avatar"/>
          <div v-else>
            <loading-outlined v-if="loading"></loading-outlined>
            <plus-outlined v-else></plus-outlined>
            <div class="ant-upload-text">Upload</div>
          </div>
        </a-upload>
      </div>
      <div>
        <a-form v-model="userForm">
          <a-form-item label="用户名">
            <a-input v-model:value="userForm.userName" />
          </a-form-item>
          <a-form-item label="个人简介">
            <a-textarea v-model:value="userForm.userProfile" />
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>
<style scoped lang="less">
img {
  width: 110px;
  height: 110px;
  border-radius: 20px;
}

.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
