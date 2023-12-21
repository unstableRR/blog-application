package com.blogapplication.controllers;

import com.blogapplication.entities.User;
import com.blogapplication.payloads.ApiResponse;
import com.blogapplication.payloads.CommentDto;
import com.blogapplication.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class CommentController {

    @Autowired
    private CommentService comSer;

    //POST - create comment
    @PostMapping("/user/{userId}/post/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto commentDto,
            @PathVariable Integer userId,
            @PathVariable Integer postId){

        CommentDto comment = this.comSer.createComment(commentDto, postId, userId);

        return new ResponseEntity(comment, HttpStatus.CREATED);
    }

    //DELETE - delete comment
    @DeleteMapping("/comment/{comId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer comId){

        this.comSer.deleteComment(comId);
        return ResponseEntity.ok(new ApiResponse("Comment deleted successfully!", true));

    }
}
