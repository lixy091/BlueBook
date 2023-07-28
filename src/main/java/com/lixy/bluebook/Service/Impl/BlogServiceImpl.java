package com.lixy.bluebook.Service.Impl;

import cn.hutool.core.util.StrUtil;
import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Dao.BlogMapper;
import com.lixy.bluebook.Entity.Blog;
import com.lixy.bluebook.Service.BlogService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ProjectConstant;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author lixy
 */
@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper blogMapper;

    @Override
    public ResponseData uploadImage(MultipartFile image) {
        String filename = createNewFileName(image.getOriginalFilename());
        try {
            image.transferTo(new File(ProjectConstant.IMAGE_DIR , filename));
        } catch (IOException e) {
            log.error("图片保存失败" ,e);
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(filename);
    }

    @Override
    public ResponseData saveBlog(Blog blog) {
        UserDTO user = UserLocal.getUserDTO();
        blog.setUser(user);
        return blogMapper.saveBlog(blog) != 0
                ? ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData("保存成功")
                : ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"保存失败");
    }

    @Override
    public ResponseData getBlog(long blogId) {

        return null;
    }

    private String createNewFileName(String originalFilename) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        int hash = name.hashCode();
        int d1 = hash & 0xF;
        int d2 = (hash >> 4) & 0xF;
        // 判断目录是否存在
        File dir = new File(ProjectConstant.IMAGE_DIR, StrUtil.format("/blogs/{}/{}", d1, d2));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return StrUtil.format("/blogs/{}/{}/{}.{}", d1, d2, name, suffix);
    }
}
