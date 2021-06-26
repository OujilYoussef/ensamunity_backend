package com.project.ensamunity.dto;


import com.project.ensamunity.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteDto {
    private Long postId;
    private VoteType voteType;

}
