package com.blogapplication.services.impl;

import com.blogapplication.entities.Comment;
import com.blogapplication.entities.Post;
import com.blogapplication.entities.User;
import com.blogapplication.exceptions.ResourceNotFoundException;
import com.blogapplication.payloads.CommentDto;
import com.blogapplication.repositories.CommentRepo;
import com.blogapplication.repositories.PostRepo;
import com.blogapplication.repositories.UserRepo;
import com.blogapplication.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper mm;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","post id", postId));

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","user id", userId));

        Comment comment = this.mm.map(commentDto, Comment.class);
        comment.setPost(post);
        comment.setUser(user);

        System.out.println("line 1");

        Comment savedComment = this.commentRepo.save(comment);

        System.out.println("line 2");

        return this.mm.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer comId) {

        Comment comment = this.commentRepo.findById(comId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment","comment id", comId));

        this.commentRepo.delete(comment);

    }
}
