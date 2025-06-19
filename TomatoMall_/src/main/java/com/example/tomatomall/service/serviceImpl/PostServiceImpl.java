package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Comment;
import com.example.tomatomall.po.Post;

import com.example.tomatomall.po.Product;
import com.example.tomatomall.repository.PostRepository;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.service.PostService;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.CommentVO;
import com.example.tomatomall.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    ProductService productService;



    @Override
    public List<PostVO> getAllPosts(){
        return postRepository.findAll().stream().map(Post::toVO).collect(Collectors.toList());

    }

    @Override
    public PostVO getPostInfo(Integer postId){
        Post post=postRepository.findById(postId).orElse(null);
        if(post==null){
            throw TomatoMallException.postNotExist();
        }
        return post.toVO();

    }

    @Override
    public String createPost(PostVO postVO){
        Set<Product> products = postVO.getProductIds().stream()
                .map(id -> productService.findById(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Post newPost=postVO.toPO(products);
        newPost.setLike(0);
        newPost.setDislike(0);
        newPost.setTime(LocalDateTime.now());
        postRepository.save(newPost);
        return "创建成功";

    }

    @Override
    public String deletePost(Integer postId){
        Post post=postRepository.findById(postId).orElse(null);
        if(post==null){
            throw TomatoMallException.postNotExist();

        }
        postRepository.deleteById(postId);
        return "删除成功";

    }


    @Override
    public String likePost(Integer postId,Integer userId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw TomatoMallException.postNotExist();
        }

        if (post.getLikedUserIds().contains(userId)) {
            post.setLike(post.getLike()-1);
            post.getLikedUserIds().remove(userId);
            postRepository.save(post);
            return "取消点赞";
        }else {
            post.setLike(post.getLike() + 1);
            post.getLikedUserIds().add(userId);
            postRepository.save(post);
            return "点赞成功";
        }

    }



    @Override
    public String dislikePost(Integer postId,Integer userId){
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw TomatoMallException.postNotExist();
        }
        if (post.getDislikedUserIds().contains(userId)) {
            post.setDislike(post.getDislike() - 1);
            post.getDislikedUserIds().remove(userId);
            postRepository.save(post);
            return "取消踩";
        }else {
            post.setDislike(post.getDislike() + 1);
            post.getDislikedUserIds().add(userId);
            postRepository.save(post);
            return "踩成功";
        }
    }


    @Override
    public List<Post>findAllByOrderByTimeDesc(){
        return postRepository.findAllByOrderByTimeDesc();
    }
    @Override
    public List<Post>findAllByOrderByLikeDesc(){
        return postRepository.findAllByOrderByLikeDesc();
    }
}
