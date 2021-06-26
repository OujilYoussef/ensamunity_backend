package com.project.ensamunity.repository;

import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.User;
import com.project.ensamunity.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
