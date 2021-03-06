package com.project.ensamunity.repository;

import com.project.ensamunity.model.Discussion;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByCreatedDateDesc();
    List<Post> findAllByDiscussion(Discussion discussion);
    List<Post> findAllByDiscussionName(String name);
    List<Post> findFirst4ByUserOrderByCreatedDateDesc(User user);
    List<Post> findAllByUser(User user);
}
