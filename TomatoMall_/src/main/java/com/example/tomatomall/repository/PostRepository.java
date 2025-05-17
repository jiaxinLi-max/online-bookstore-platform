package com.example.tomatomall.repository;

import com.example.tomatomall.po.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post>findAllByOrderByTimeDesc();
    List<Post>findAllByOrderByLikeDesc();
}
