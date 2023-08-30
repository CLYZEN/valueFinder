package com.ezen.valuefinder.service;

import com.ezen.valuefinder.entity.ItemImg;
import com.ezen.valuefinder.repository.ItemImgRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.net.URL;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    ClassLoader classLoader = getClass().getClassLoader();
    URL url = classLoader.getResource("");
    private String itemImgLocation =  url.getPath();
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename(); //파일이름 -> 이미지1.jpg
        String imgName = "";
        String imgUrl = "";


        if(!StringUtils.isEmpty(oriImgName)) {
            //oriImgName이 빈문자열이 아니라면 이미지 파일 업로드
            imgName = fileService.uploadFile(itemImgLocation,
                    oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;

        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
}