package com.project.ensamunity.service;

import com.project.ensamunity.dto.VoteDto;
import com.project.ensamunity.exceptions.EnsamunityException;

import com.project.ensamunity.model.Post;
import com.project.ensamunity.model.Vote;
import com.project.ensamunity.repository.PostRepository;
import com.project.ensamunity.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.project.ensamunity.model.VoteType.*;

@AllArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new EnsamunityException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            if(UPVOTE.equals(voteDto.getVoteType())) {
                post.setVoteCount(post.getVoteCount() - 1);
                voteDto.setVoteType(NOVOTE);
            }
            else
            {
                post.setVoteCount(post.getVoteCount()  + 1);
                voteDto.setVoteType(NOVOTE);
            }


        }
        else if(voteByPostAndUser.isPresent() &&
                !voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())
                && (voteByPostAndUser.get().getVoteType().equals(UPVOTE) || voteByPostAndUser.get().getVoteType().equals(DOWNVOTE))
        )
        {
            if(UPVOTE.equals(voteDto.getVoteType())) {
                post.setVoteCount(post.getVoteCount() + 2);
            }
            else
            {
                post.setVoteCount(post.getVoteCount()  -2);
            }

        }
        else {
            if (UPVOTE.equals(voteDto.getVoteType())) {
                post.setVoteCount(post.getVoteCount() + 1);
            } else {
                post.setVoteCount(post.getVoteCount() - 1);
            }
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
