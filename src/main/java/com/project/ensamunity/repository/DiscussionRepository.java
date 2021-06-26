package com.project.ensamunity.repository;

import com.project.ensamunity.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DiscussionRepository extends JpaRepository<Discussion,Long> {
    Optional<Discussion> findByName(String discussionName);
}
