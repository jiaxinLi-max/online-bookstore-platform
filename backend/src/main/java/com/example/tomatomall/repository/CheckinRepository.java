package com.example.tomatomall.repository;

import com.example.tomatomall.po.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CheckinRepository extends JpaRepository<Checkin,Integer> {
    boolean existsByUserIdAndCheckinTime(Integer userId, LocalDate today);
    List<Checkin> findByUserId(Integer userId);
}
