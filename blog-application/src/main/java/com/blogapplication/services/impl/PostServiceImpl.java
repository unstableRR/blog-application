package com.blogapplication.services.impl;

import com.blogapplication.entities.Category;
import com.blogapplication.entities.Post;
import com.blogapplication.entities.User;
import com.blogapplication.exceptions.ResourceNotFoundException;
import com.blogapplication.payloads.PostDto;
import com.blogapplication.payloads.PostResponse;
import com.blogapplication.repositories.CategoryRepo;
import com.blogapplication.repositories.PostRepo;
import com.blogapplication.repositories.UserRepo;
import com.blogapplication.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper mm;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo catRepo;

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer catId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id",userId));

        Category cat = this.catRepo.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id",catId));


        Post post = this.mm.map(postDto, Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setCategory(cat);
        post.setUser(user);

        Post newPost = this.postRepo.save(post);

        return this.mm.map(newPost, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer pid) {

        Post post = this.postRepo.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Post","post id", pid));

        post.setPostTitle(postDto.getPostTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());

        Post updatedPost = this.postRepo.save(post);

        return this.mm.map(updatedPost, PostDto.class);
    }

    @Override
    public void deletePost(Integer pid) {

        Post post = this.postRepo.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Post","post id",pid));

        this.postRepo.delete(post);
    }

    @Override
    public PostDto getPostById(Integer pid) {

        Post post = this.postRepo.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Post","post id",pid));

        return this.mm.map(post, PostDto.class);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNum, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
//        if(sortDir.equalsIgnoreCase("desc")){
//            sort = Sort.by(sortBy).descending();
//        }else{
//            sort = Sort.by(sortBy).ascending();
//        }

        Pageable p = PageRequest.of(pageNum,pageSize, sort);

        Page<Post> pagePost = this.postRepo.findAll(p);
        List<Post> allPosts = pagePost.getContent();

        List<PostDto> postDtos = allPosts.stream().map((post) -> this.mm.map(post, PostDto.class))
                  .collect(Collectors.toList());

        PostResponse pr = new PostResponse();

        pr.setPosts(postDtos);
        pr.setPageNumber(pagePost.getNumber());
        pr.setPageSize(pagePost.getSize());
        pr.setTotalElements(pagePost.getTotalElements());
        pr.setTotalPages(pagePost.getTotalPages());
        pr.setLastPage(pagePost.isLast());

        return pr;
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer catId){ //, Integer pageNum, Integer pageSize,String sortBy, String sortDir) {

       // Sort sort = sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();

       // Pageable p = PageRequest.of(pageNum, pageSize, sort);

        Category cat = this.catRepo.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", catId));
        List<Post> posts = this.postRepo.findByCategory(cat);

        List<PostDto> postDtos = posts.stream().map((post) -> this.mm.map(post, PostDto.class))
                .collect(Collectors.toList());

        return postDtos;
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {

        System.out.println("Here we go!");
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));

        List<Post> posts = this.postRepo.findByUser(user);

        List<PostDto> postDtos = posts.stream().map((post) -> this.mm.map(post, PostDto.class))
                .collect(Collectors.toList());

        return postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {

        List<Post> posts = this.postRepo.searchByPostTitle("%"+keyword+"%");

        List<PostDto> postDtos = posts.stream().map((post) -> this.mm.map(post, PostDto.class))
                .collect(Collectors.toList());

        return postDtos;
    }
}
