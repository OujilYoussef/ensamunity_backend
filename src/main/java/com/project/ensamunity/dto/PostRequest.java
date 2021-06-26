package com.project.ensamunity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
private String discussionName;
private String postName;
private String url;
private String description;
}
