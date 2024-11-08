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
	private List<Lang> langList; // Lang 리스트 추가
	private List<Cert> certList; // Cert 리스트 추가
	private Dept dept;
	private Lang lang; // 단일 Lang 필드 
	private Cert cert; // 단일 Cert 필드
}
