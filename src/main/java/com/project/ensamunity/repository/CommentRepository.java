package com.project.ensamunity.repository;

import com.project.ensamunity.model.Comment;
import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository  extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByUser(User currentUser);
}
