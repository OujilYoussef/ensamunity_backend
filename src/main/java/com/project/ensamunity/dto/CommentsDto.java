package com.project.ensamunity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
    private Long id;
    private String text;
    private String userName;
    private Long postId;
}
