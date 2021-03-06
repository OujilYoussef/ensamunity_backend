package com.project.ensamunity.mapper;


import com.project.ensamunity.dto.DiscussionDto;
import com.project.ensamunity.model.Discussion;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel =  "spring")
public interface DiscussionMapper {
    @Mapping(target = "numberOfPosts",expression = "java(mapPosts(discussion.getPosts()))")

    DiscussionDto mapDiscussionToDto(Discussion discussion);

    default Integer mapPosts(List<Post> posts){return posts.size();}

    @InheritInverseConfiguration
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "posts",ignore = true)
    @Mapping(target = "createdDate",expression ="java(java.time.Instant.now())")
    @Mapping(target = "user",source = "currentUser")
    Discussion mapDtoToDiscussion(DiscussionDto discussionDto, User currentUser);

}


