package com.project.ensamunity.controller;



import com.project.ensamunity.dto.PostRequest;
import com.project.ensamunity.dto.PostResponse;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostRequest postRequest) {
        return  status(HttpStatus.CREATED).body(postService.save(postRequest));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("/by-discussion/{id}")
    public ResponseEntity<List<PostResponse>> getPostsByDiscussion(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPostsByDiscussion(id));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }


}

