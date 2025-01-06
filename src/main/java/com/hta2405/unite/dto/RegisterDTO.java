package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.domain.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class RegisterDTO {
    List<Dept> deptList;
    List<Job> jobList;
}
