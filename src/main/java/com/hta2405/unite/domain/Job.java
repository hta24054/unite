package com.hta2405.unite.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Job {
    private int job_id;
    private String job_name;
    private int job_rank;

}
