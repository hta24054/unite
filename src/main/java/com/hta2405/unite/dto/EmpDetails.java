package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmpDetails {
	private Emp emp;
	private List<Emp> empList;
	private Job job;
	private List<Lang> langList; 
	private List<Cert> certList; 
	private Dept dept;
	private Lang lang;  
	private Cert cert; 
}
