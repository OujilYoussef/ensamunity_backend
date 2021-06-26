package com.project.ensamunity.mapper;


import com.project.ensamunity.dto.CommentsDto;
import com.project.ensamunity.model.Comment;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel =  "spring")
public interface CommentMapper {
@Mapping(target = "id",ignore = true)
    @Mapping(target = "createdDate",expression ="java(java.time.Instant.now())")
    @Mapping(target = "user",source = "user")
    @Mapping(target = "post",source = "post")
    Comment mapDtoToComment(CommentsDto commentsDto, User user, Post post);

    @Mapping(target = "userName",source = "user.username")
    @Mapping(target = "postId",source = "post.postId")
    CommentsDto mapCommentToDto(Comment comment);
}
