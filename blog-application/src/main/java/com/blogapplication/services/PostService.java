package com.blogapplication.services;

import com.blogapplication.entities.Post;
import com.blogapplication.payloads.PostDto;
import com.blogapplication.payloads.PostResponse;

import java.util.List;

public interface PostService {

    //create
    PostDto createPost(PostDto postDto, Integer userId, Integer catId);

    //update
    PostDto updatePost(PostDto postDto, Integer pid);

    //delete
    void deletePost(Integer pid);

    //get
    PostDto getPostById(Integer pid);

    //get all
    PostResponse getAllPosts(Integer pageNum, Integer pageSize, String sortBy, String sortDir);

    //get all post by category
    List<PostDto> getPostsByCategory(Integer catId );//, Integer pageNum, Integer pageSize, String sortBy, String sortDir);

    //get all post by user
    List<PostDto> getPostsByUser(Integer userId); //, Integer pageNum, Integer pageSize, String sortBy, String sortDir);

    //search posts
    List<PostDto> searchPosts(String keyword);
}
