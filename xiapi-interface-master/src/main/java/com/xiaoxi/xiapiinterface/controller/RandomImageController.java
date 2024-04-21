package com.xiaoxi.xiapiinterface.controller;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/random")
public class RandomImageController {

    private final  String IMAGE_DIRECTORY = "http://localhost:8123/api/";

    @PostMapping("/randomImages")
    public String getRandomImage() {
        List<String> images = getLocalImages();
        if (images.isEmpty()) {
            return "{\"error\": \"No images found.\"}";
        }

        Random random = new Random();
        String randomImageName = images.get(random.nextInt(images.size()));
        return IMAGE_DIRECTORY + randomImageName;
    }

    //获取本地图片列表
    private List<String> getLocalImages() {
        try {
            File imagesDirectory = ResourceUtils.getFile("classpath:static");
            File[] files = imagesDirectory.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$"));
            if (files != null) {
                List<String> imageNames = new ArrayList<>();
                for (File file : files) {
                    imageNames.add(file.getName());
                }
                return imageNames;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
