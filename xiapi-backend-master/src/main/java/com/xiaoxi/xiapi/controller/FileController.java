package com.xiaoxi.xiapi.controller;

import cn.hutool.core.io.FileUtil;
import com.xiaoxi.common.common.BaseResponse;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.common.ResultUtils;
import com.xiaoxi.common.constant.FileConstant;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.xiapi.manager.CosManager;
import com.xiaoxi.xiapi.model.dto.file.UploadFileRequest;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.common.model.enums.FileUploadBizEnum;
import com.xiaoxi.xiapi.service.UserService;

import java.io.File;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    // 本地绝对路径
    private static final String LOCAL_FILE_PATH = "D:/projectDev/xi-api/xiapi-backend";

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/", fileUploadBizEnum.getValue(), loginUser.getId());

        File file = null;
        try {
            File newFile = new File(LOCAL_FILE_PATH + filepath, filename);
            // 检测是否存在该目录
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
            multipartFile.transferTo(newFile);
            // 返回可访问地址
            return ResultUtils.success(FileConstant.LOCAL_HOST + filepath + filename);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 10 * 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
