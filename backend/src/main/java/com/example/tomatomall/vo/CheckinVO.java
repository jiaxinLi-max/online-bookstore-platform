package com.example.tomatomall.vo;

import com.example.tomatomall.po.Checkin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
public class CheckinVO {

    private Integer id;


    private Integer userId;


    private LocalDate checkinTime;

    private LocalDateTime realTime;
    public Checkin toPO(){
        Checkin checkin=new Checkin();
        checkin.setId(this.id);
        checkin.setUserId(this.userId);
        checkin.setCheckinTime(this.checkinTime);
        checkin.setRealTime(this.realTime);
        return checkin;
    }
}
