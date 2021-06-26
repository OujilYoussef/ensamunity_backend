package com.project.ensamunity.mapper;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.project.ensamunity.dto.PostRequest;
import com.project.ensamunity.dto.PostResponse;
import com.project.ensamunity.model.Discussion;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import com.project.ensamunity.repository.CommentRepository;
import com.project.ensamunity.repository.VoteRepository;
import com.project.ensamunity.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel =  "spring")
public abstract class PostMapper {


    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;



    @Mapping(target = "id",source = "postId")
    @Mapping(target = "userName",source = "user.username")
    @Mapping(target = "discussionName",source = "discussion.name")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapPostToDto(Post post);
    @Mapping(target = "postId",ignore = true)
@Mapping(target = "voteCount",constant = "0")
@Mapping(target = "createdDate",expression ="java(java.time.Instant.now())" )
@Mapping(target = "user",source = "user")
@Mapping(target = "description",source = "postRequest.description")

    public abstract Post mapDtoToPost(PostRequest postRequest,Discussion discussion, User user);

    Integer commentCount(Post post) {
        return commentRepository.findAllByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
