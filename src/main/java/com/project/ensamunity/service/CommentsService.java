package com.project.ensamunity.service;


import com.project.ensamunity.dto.CommentsDto;
import com.project.ensamunity.exceptions.EnsamunityException;
import com.project.ensamunity.mapper.CommentMapper;
import com.project.ensamunity.model.Comment;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import com.project.ensamunity.repository.CommentRepository;
import com.project.ensamunity.repository.PostRepository;
import com.project.ensamunity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentsService {

private final AuthService authService;
private final CommentRepository commentRepository;
private final PostRepository postRepository;
private final UserRepository userRepository;
private final CommentMapper commentMapper;
    public Long save(CommentsDto commentsDto) {
        User currentUser=authService.getCurrentUser();
        Post currentPost=postRepository.findById(commentsDto.getPostId()).orElseThrow(()->new EnsamunityException(commentsDto.getPostId()+" not found"));
        Comment comment= commentMapper.mapDtoToComment(commentsDto,currentUser,currentPost);
        commentsDto.setId(comment.getId());
        return commentRepository.save(comment).getId();

    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post=postRepository.findById(postId).orElseThrow(()->new EnsamunityException(postId+" not found"));
        return commentRepository.findAllByPost(post).stream().map(commentMapper::mapCommentToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user=userRepository.findByUsername(userName).orElseThrow(()->new EnsamunityException(userName+" not found"));
        return commentRepository.findAllByUser(user).stream().map(commentMapper::mapCommentToDto).collect(toList());
    }
}
