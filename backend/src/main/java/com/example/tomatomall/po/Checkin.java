package com.example.tomatomall.po;

import com.example.tomatomall.vo.CheckinVO;
import com.example.tomatomall.vo.CommentVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Checkin {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Basic
    @Column(name = "userId",nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "checkin_time",nullable = false)
    private LocalDate checkinTime;

    @Basic
    @Column(name = "real_time",nullable = false)
    private LocalDateTime realTime;
    public CheckinVO toVO(){
       CheckinVO checkinVO=new CheckinVO();
       checkinVO.setId(this.id);
       checkinVO.setCheckinTime(checkinTime);
       checkinVO.setRealTime(this.realTime);
       checkinVO.setUserId(this.userId);
       return checkinVO;
    }

}
