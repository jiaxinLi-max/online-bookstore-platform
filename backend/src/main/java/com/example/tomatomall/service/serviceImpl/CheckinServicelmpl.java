package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Checkin;
import com.example.tomatomall.repository.CheckinRepository;
import com.example.tomatomall.service.CheckinService;
import com.example.tomatomall.vo.CheckinVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckinServicelmpl implements CheckinService {
    @Autowired
    CheckinRepository checkinRepository;

    @Override
    public List<CheckinVO> getCheckinInfo(Integer userId){
        List<Checkin> checkin = checkinRepository.findByUserId(userId);
        // 假设你有一个工具方法或构造器能把 Comment 转成 CommentVO
        return checkin.stream().map(Checkin::toVO).collect(Collectors.toList());
    }

    @Override
    public String setCheckin(Integer userId){
        LocalDate today = LocalDate.now();
        if (checkinRepository.existsByUserIdAndCheckinTime(userId, today)) {
            throw TomatoMallException.checkinExist();
        }
        Checkin checkin = new Checkin();
        checkin.setUserId(userId);
        checkin.setCheckinTime(today);
        checkin.setRealTime(LocalDateTime.now());
        checkinRepository.save(checkin);
        return "签到成功";
    }

    @Override
    public Boolean IScheckin(Integer userId){
        return checkinRepository.existsByUserIdAndCheckinTime(userId,LocalDate.now());
    }

}
