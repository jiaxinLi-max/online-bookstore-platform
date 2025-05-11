package com.example.tomatomall.repository;

import com.example.tomatomall.po.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
