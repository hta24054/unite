package com.hta2405.unite.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Project {
	private int project_id;
	private String manager_id;
	private String project_name;
	private Date project_start_date;
	private Date project_end_date;
	private String project_content;
	private String project_file_path;
	private String project_file_original;
	private String project_file_uuid;
	private String project_file_type;
	private int project_finished;
	private int project_canceled;
}
