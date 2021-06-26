package com.project.ensamunity.controller;


import com.project.ensamunity.dto.CommentsDto;
import com.project.ensamunity.model.Comment;
import com.project.ensamunity.service.CommentsService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;
    @PostMapping
    public ResponseEntity<Long> createComments(@RequestBody CommentsDto commentsDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentsService.save(commentsDto));
    }
    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(commentsService.getAllCommentsForPost(postId));
    }
    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName)
    {
        return ResponseEntity.status(HttpStatus.OK).body(commentsService.getAllCommentsForUser(userName));
    }


}
