package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
