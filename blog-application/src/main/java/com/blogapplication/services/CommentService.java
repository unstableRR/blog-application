package com.blogapplication.services;

import com.blogapplication.payloads.CommentDto;

public interface CommentService {

    //create comment
    CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);

    //delete comment
    void deleteComment(Integer comId);
}
