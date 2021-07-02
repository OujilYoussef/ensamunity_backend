package com.project.ensamunity.service;


import com.project.ensamunity.dto.PostRequest;
import com.project.ensamunity.dto.PostResponse;
import com.project.ensamunity.exceptions.EnsamunityException;
import com.project.ensamunity.mapper.PostMapper;
import com.project.ensamunity.model.*;
import com.project.ensamunity.repository.CommentRepository;
import com.project.ensamunity.repository.DiscussionRepository;
import com.project.ensamunity.repository.PostRepository;
import com.project.ensamunity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
public class PostService {
    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
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

        recommendations();
        return postRepository.findAllByOrderByCreatedDateDesc()
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

    private void recommendations() {
        User currentUser=authService.getCurrentUser();

        List<String> lastInteraction = new ArrayList<>();

        List<Comment> comments = commentRepository.findFirst4ByUserOrderByCreatedDateDesc(currentUser);
        for (Comment c : comments){
            lastInteraction.add(c.getPost().getDiscussion().getName());
        }
        List<Post> posts = postRepository.findFirst4ByUserOrderByCreatedDateDesc(currentUser);
        for (Post p : posts){
            lastInteraction.add(p.getDiscussion().getName());
        }
        System.out.println(removeDuplication(lastInteraction));
        Recommends recommends = new Recommends();
        recommends.setInterest(removeDuplication(lastInteraction));


        RestTemplate restTemplate = new RestTemplate();

        final String uri = "http://127.0.0.1:5000";
        HttpEntity<Recommends> request = new HttpEntity<>(recommends);
        Recommends result  = restTemplate.postForObject(uri,request,Recommends.class);
        System.out.println(result);
        List<Post> RPosts = new ArrayList<>();
        for(String s: result.getInterest()){
            RPosts.addAll(postRepository.findAllByDiscussionName(s));
        }
        System.out.println(RPosts);
    }



    static List<String> removeDuplication(List<String> directCallList) {
        HashSet<String> set = new HashSet<>();
        List<String> returnList = new ArrayList<>();
        for(String builder : directCallList) {
            if(set.add(builder))
                returnList.add(builder);
        }
        return returnList;
    }

}
