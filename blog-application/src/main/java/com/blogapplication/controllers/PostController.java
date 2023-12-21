package com.blogapplication.controllers;

import com.blogapplication.config.AppConstants;
import com.blogapplication.entities.Post;
import com.blogapplication.payloads.ApiResponse;
import com.blogapplication.payloads.PostDto;
import com.blogapplication.payloads.PostResponse;
import com.blogapplication.services.FileService;
import com.blogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postSer;

    @Autowired
    private FileService fileSer;

    @Value("${project.image}")
    private String path;

    //POST - create a post
    @PostMapping("/user/{userId}/category/{catId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,
                                              @PathVariable Integer userId,
                                               @PathVariable Integer catId){

        PostDto createPost = this.postSer.createPost(postDto, userId, catId);

        return new ResponseEntity<>(createPost, HttpStatus.CREATED);
    }

    //GET - get post by its id
    @GetMapping("/posts/{pid}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer pid){

        PostDto postDto = this.postSer.getPostById(pid);
        return ResponseEntity.ok(postDto);
    }

    //GET - get all posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NO, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue= AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue= AppConstants.SORT_DIR,required = false) String sortDir
    ){
        PostResponse pr = this.postSer.getAllPosts (pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pr);
    }

    //GET - get posts by user
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId){

        List<PostDto> posts = this.postSer.getPostsByUser(userId);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //GET - get posts by category
    @GetMapping("/category/{catId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer catId){

        List<PostDto> posts = this.postSer.getPostsByCategory(catId);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //PUT - update post
    @PutMapping("/posts/{pId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer pId){

        PostDto updatedPostDto = this.postSer.updatePost(postDto, pId);
        return ResponseEntity.ok(updatedPostDto);
    }

    //DELETE - delete post
    @DeleteMapping("/posts/{pId}")
    public ApiResponse deletePost(@PathVariable Integer pId){

        this.postSer.deletePost(pId);
        return new ApiResponse("Post successfully deleted !", true);
    }

    //search
    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable String keyword){

        List<PostDto> postDtos = this.postSer.searchPosts(keyword);
        return ResponseEntity.ok(postDtos);
    }

    //post image upload
    @PostMapping("/post/image/upload/{postId}/")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId) throws IOException {

        PostDto postDto = this.postSer.getPostById(postId);
        String fileName = this.fileSer.uploadImage(path, image);

        postDto.setImageName(fileName);

        PostDto updatePost = this.postSer.updatePost(postDto, postId);
        return ResponseEntity.ok(updatePost);
    }

    @GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {

        InputStream resource = this.fileSer.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
