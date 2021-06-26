package com.project.ensamunity.service;


import com.project.ensamunity.dto.PostRequest;
import com.project.ensamunity.dto.PostResponse;
import com.project.ensamunity.exceptions.EnsamunityException;
import com.project.ensamunity.mapper.PostMapper;
import com.project.ensamunity.model.Discussion;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import com.project.ensamunity.repository.DiscussionRepository;
import com.project.ensamunity.repository.PostRepository;
import com.project.ensamunity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
public class PostService {
    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Transactional
    public Long save(PostRequest postRequest) {
        Discussion currentDiscussion=discussionRepository.findByName(postRequest.getDiscussionName()).orElseThrow(()->new EnsamunityException(postRequest.getDiscussionName() + " not found"));
        User currentUser=authService.getCurrentUser();
        Post post=postMapper.mapDtoToPost(postRequest,currentDiscussion,currentUser);
        return postRepository.save(post).getPostId();

    }
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapPostToDto)
                .collect(toList());

    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post=postRepository.findById(id).orElseThrow(()->new EnsamunityException("post not found"));
        return postMapper.mapPostToDto(post);
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByDiscussion(Long id) {
        Discussion discussion=discussionRepository.findById(id).orElseThrow(()->new EnsamunityException("discussion not found"));
        List<Post> posts=postRepository.findAllByDiscussion(discussion);
        return posts.stream().map(postMapper::mapPostToDto).collect(toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user=userRepository.findByUsername(username).orElseThrow(()->new EnsamunityException(username +" not found"));
        List<Post> posts=postRepository.findAllByUser(user);
        return posts.stream().map(postMapper::mapPostToDto).collect(toList());    }
}
