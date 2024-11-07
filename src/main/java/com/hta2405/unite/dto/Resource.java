package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Resource {
    private Long resourceId;
    private String resourceType;
    private String resourceName;
    private String resourceInfo;
    private boolean resourceUsable;
}
