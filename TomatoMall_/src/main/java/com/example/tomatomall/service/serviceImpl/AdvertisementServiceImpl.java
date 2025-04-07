package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Advertisement;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.repository.AdvertisementRepository;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.service.AdvertisementService;
import com.example.tomatomall.vo.AdvertisementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<AdvertisementVO> getAllAdvertisements(){
        return advertisementRepository.findAll().stream().map(Advertisement::toVO).collect(Collectors.toList());
    }

    @Override
    public AdvertisementVO updateAdvertisement(AdvertisementVO advertisementVO){
        Product product=productRepository.findById(advertisementVO.getProductId()).orElse(null);
        Advertisement advertisement=advertisementRepository.findById(advertisementVO.getId()).orElse(null);
        if(advertisement==null){
            throw TomatoMallException.advertisementNotExist();
        }
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        if(advertisementVO.getContent()!=null) {
            advertisement.setContent(advertisementVO.getContent());
        }
        if(advertisementVO.getImgUrl()!=null) {
            advertisement.setImgUrl(advertisementVO.getImgUrl());
        }
        if(advertisementVO.getTitle()!=null){
            advertisement.setTitle(advertisementVO.getTitle());
        }

        advertisement.setProduct(product);

        advertisementRepository.save(advertisement);
        return advertisement.toVO();
    }

    @Override
    public AdvertisementVO createAdvertisement(AdvertisementVO advertisementVO){
        Product product=productRepository.findById(advertisementVO.getProductId()).orElse(null);
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        Advertisement advertisement=advertisementVO.toPO();
        advertisement.setProduct(product);
        advertisementRepository.save(advertisement);
        return advertisement.toVO();
    }

    @Override
    public String deleteAdvertisement(String id){
        try {
            Integer advertisementId = Integer.valueOf(id);  // 转换为 Integer 类型
            Advertisement advertisement=advertisementRepository.findById(advertisementId).orElse(null);
            if (advertisement == null) {
                throw TomatoMallException.advertisementNotExist();
            }
            advertisementRepository.deleteById(advertisementId);
            return "删除成功";
        } catch (NumberFormatException e) {
            throw TomatoMallException.invalidProductId();  // 处理无效的ID格式
        }
    }
}
