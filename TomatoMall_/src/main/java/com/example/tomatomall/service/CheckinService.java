package com.example.tomatomall.service;

import com.example.tomatomall.vo.CheckinVO;

import java.util.List;

public interface CheckinService {
    List<CheckinVO> getCheckinInfo(Integer userId);
    String setCheckin(Integer userId);
}
