package com.project.ensamunity.controller;

import com.project.ensamunity.dto.DiscussionDto;
import com.project.ensamunity.service.DiscussionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Discussions")
@AllArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;
     @PostMapping
    public ResponseEntity<DiscussionDto> createDiscussion(@RequestBody DiscussionDto discussionDto){
        return ResponseEntity.status(HttpStatus.CREATED)
         .body(discussionService.save(discussionDto));
     }

     @GetMapping
    public ResponseEntity<List<DiscussionDto>> getAllDiscussions(){
        return ResponseEntity.status(HttpStatus.OK).body(discussionService.getAll());
     }

     @GetMapping("/{id}")
    public ResponseEntity<DiscussionDto> getDiscussion(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(discussionService.getDiscussion(id));
     }
}
