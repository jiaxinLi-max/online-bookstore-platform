package com.example.tomatomall.service;

import com.example.tomatomall.vo.AdvertisementVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdvertisementService {
    List<AdvertisementVO> getAllAdvertisements();

    AdvertisementVO updateAdvertisement(AdvertisementVO advertisementVO);

    AdvertisementVO createAdvertisement(AdvertisementVO advertisementVO);

    String deleteAdvertisement(String id);
}
