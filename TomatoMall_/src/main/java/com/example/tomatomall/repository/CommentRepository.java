package com.example.tomatomall.repository;

import com.example.tomatomall.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByProductId(Integer productId);
    List<Comment>findAllByOrderByTimeDesc();
    List<Comment>findAllByOrderByLikesDesc();
    List<Comment>findAllByProductIdOrderByTimeDesc(Integer productId);
    List<Comment>findAllByProductIdOrderByLikesDesc(Integer productId);
    List<Comment> findByParent_Id(Integer parentId);
    List<Comment> findByProductIdAndParentIsNull(Integer productId);
}
