package com.ezen.valuefinder.service;

import com.ezen.valuefinder.entity.ItemImg;
import com.ezen.valuefinder.repository.ItemImgRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    ClassLoader classLoader = getClass().getClassLoader();
    URL url = classLoader.getResource("");
    //private String itemImgLocation =  url.getPath();
    @Value("${itemImgLocation}")
    private String itemImgLocation;

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
    
	public void updateItemImg(ItemImg itemImg, MultipartFile itemImgFile) 
			throws Exception {
		if(!itemImgFile.isEmpty()) { //첨부한 이미지 파일이 있으면
			
			//해당 이미지를 가져오고
			ItemImg savedItemImg = itemImgRepository.findById(itemImg.getItemImageNo()).orElseThrow();
			
			//기존 이미지 파일 C:/shop/item 폴더에서 삭제
			if(!StringUtils.isEmpty(savedItemImg.getItemImageName())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getItemImageName());
			}
			
			
			
			//수정된 이미지 파일 C:/shop/item 에 업로드
			String oriImgName = itemImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName
					, itemImgFile.getBytes());
			String imgUrl = "/images/item/" + imgName;
			
			//update쿼리문 실행
			/* ★★★ 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 
			그 이후에는 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을 쓰지 않고
			엔티티 데이터만 변경해주면 된다. */
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
		}
	}
    
    
    
}