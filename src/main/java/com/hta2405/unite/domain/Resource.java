package com.hta2405.unite.domain;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Resource {
    private Long resourceId;
    private String resourceType;
    private String resourceName;
    private String resourceInfo;
    private boolean resourceUsable;
}
