package com.project.ensamunity.service;


import com.project.ensamunity.dto.DiscussionDto;
import com.project.ensamunity.exceptions.EnsamunityException;
import com.project.ensamunity.mapper.DiscussionMapper;
import com.project.ensamunity.model.Discussion;
import com.project.ensamunity.model.User;
import com.project.ensamunity.repository.DiscussionRepository;
import com.project.ensamunity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class DiscussionService {
    private final DiscussionRepository discussionRepository;
    private final DiscussionMapper discussionMapper;
    private final AuthService authService;
    @Transactional

    public DiscussionDto save(DiscussionDto discussionDto) {
        User currentUser=authService.getCurrentUser();
        Discussion discussion = discussionMapper.mapDtoToDiscussion(discussionDto,currentUser);
        Discussion saved = discussionRepository.save(discussion);
        discussionDto.setId(saved.getId());
        return discussionDto;
    }

    @Transactional(readOnly = true)
    public List<DiscussionDto> getAll() {
        return discussionRepository.findAll()
                .stream()
                .map(discussionMapper::mapDiscussionToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public DiscussionDto getDiscussion(Long id) {
        Discussion discussion=discussionRepository.findById(id).orElseThrow(()->new EnsamunityException("Discussion Not Found"));
        return discussionMapper.mapDiscussionToDto(discussion);
    }

}