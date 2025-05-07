package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Post;

import com.example.tomatomall.repository.PostRepository;
import com.example.tomatomall.service.PostService;
import com.example.tomatomall.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;


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
        Post newPost=postVO.toPO();
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
    public String likePost(Integer postId){
        Post post=postRepository.findById(postId).orElse(null);
        if(post==null){
            throw TomatoMallException.postNotExist();

        }
        Integer like=post.getLike();
        post.setLike(like+1);
        postRepository.save(post);
        return "点赞成功";
    }

    @Override
    public String dislikePost(Integer postId){
        Post post=postRepository.findById(postId).orElse(null);
        if(post==null){
            throw TomatoMallException.postNotExist();
        }
        Integer dislike=post.getDislike();
        post.setLike(dislike-1);
        postRepository.save(post);
        return "踩成功";
    }
}
