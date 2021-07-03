package com.project.ensamunity.mapper;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.project.ensamunity.dto.PostRequest;
import com.project.ensamunity.dto.PostResponse;
import com.project.ensamunity.model.*;
import com.project.ensamunity.repository.CommentRepository;
import com.project.ensamunity.repository.VoteRepository;
import com.project.ensamunity.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.project.ensamunity.model.VoteType.DOWNVOTE;
import static com.project.ensamunity.model.VoteType.UPVOTE;

@Mapper(componentModel =  "spring")
public abstract class PostMapper {


    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;




    @Mapping(target = "id",source = "postId")
    @Mapping(target = "userName",source = "user.username")
    @Mapping(target = "discussionName",source = "discussion.name")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
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

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}
