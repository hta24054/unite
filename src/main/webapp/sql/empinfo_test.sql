-- 기존 테이블 삭제 (존재할 경우)
DROP TABLE project_member CASCADE CONSTRAINTS;
DROP TABLE task_commnet CASCADE CONSTRAINTS;
DROP TABLE task CASCADE CONSTRAINTS;
DROP TABLE project CASCADE CONSTRAINTS;
DROP TABLE job CASCADE CONSTRAINTS;
DROP TABLE emp_info CASCADE CONSTRAINTS;
DROP TABLE emp CASCADE CONSTRAINTS;
DROP TABLE dept CASCADE CONSTRAINTS;

-- 테이블 생성
CREATE TABLE dept
(
  dept_id      NUMBER(4)    NOT NULL,
  dept_name    VARCHAR2(50) NOT NULL,
  dept_manager NUMBER      ,
  CONSTRAINT PK_dept PRIMARY KEY (dept_id)
);

CREATE TABLE job
(
  job_id   NUMBER       NOT NULL,
  job_name VARCHAR2(50) NOT NULL,
  job_rank NUMBER       NOT NULL,
  CONSTRAINT PK_job PRIMARY KEY (job_id)
);

CREATE TABLE emp
(
  emp_id       VARCHAR2(10)  NOT NULL,
  password     VARCHAR2(200) NOT NULL,
  ename        VARCHAR2(15)  NOT NULL,
  dept_id      NUMBER(4)     NOT NULL,
  job_id       NUMBER        NOT NULL,
  gender       VARCHAR2(3)   NOT NULL,
  email        VARCHAR2(30)  NOT NULL,
  tel          VARCHAR2(15)  NOT NULL,
  mobile       VARCHAR2(13)  NOT NULL,
  img_path     VARCHAR2(255) NOT NULL,
  img_original VARCHAR2(255) NOT NULL,
  img_uuid     VARCHAR2(36)  NOT NULL,
  img_type     VARCHAR2(50)  NOT NULL,
  hired        NUMBER        DEFAULT 1 NOT NULL,
  CONSTRAINT PK_emp PRIMARY KEY (emp_id)
);

-- 나머지 테이블도 여기에 추가 생성...
-- 부서 데이터 삽입
INSERT INTO dept (dept_id, dept_name, dept_manager) VALUES (1, '인사부', NULL);
INSERT INTO dept (dept_id, dept_name, dept_manager) VALUES (2, '영업부', NULL);
INSERT INTO dept (dept_id, dept_name, dept_manager) VALUES (3, '개발부', NULL);

update dept set dept_name = '인사부' where dept_id = 1;
update dept set dept_name = '재무부' where dept_id = 2;
update dept set dept_name = '평가부' where dept_id = 3;

-- 직무 데이터 삽입
INSERT INTO job (job_id, job_name, job_rank) VALUES (1, '사원', 1);
INSERT INTO job (job_id, job_name, job_rank) VALUES (2, '대리', 2);
INSERT INTO job (job_id, job_name, job_rank) VALUES (3, '팀장', 3);


-- 직원 데이터 삽입
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E001', 'password123', '홍길동', 1, 1, 'M', 'hong@example.com', '1234', '010-1234-5678', 'path/to/img', 'original.jpg', 'uuid-value', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E003', 'password123', '초히길동', 1, 1, 'M', 'hong@example.com', '1234', '010-1234-5678', 'path/to/img', 'original.jpg', 'uuid-value', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E002', 'password123', '김철수', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E004', 'password123', '하하', 1, 1, 'M', 'hong@example.com', '1234', '010-1234-5678', 'path/to/img', 'original.jpg', 'uuid-value', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E005', 'password123', '호호', 3, 3, 'M', 'hong@example.com', '1234', '010-1234-5678', 'path/to/img', 'original.jpg', 'uuid-value', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E006', 'password123', '히히', 1, 1, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E007', 'password123', '키키', 2, 2, 'M', 'hong@example.com', '1234', '010-1234-5678', 'path/to/img', 'original.jpg', 'uuid-value', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E008', 'password123', '헤헤', 3, 3, 'M', 'hong@example.com', '1234', '010-1234-5678', 'path/to/img', 'original.jpg', 'uuid-value', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E009', 'password123', '후후', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);


-- 삽입된 데이터 확인
SELECT * FROM dept;
SELECT * FROM job;
SELECT * FROM emp;
select * from project;
select * from project_member;


-----------------------
--프로젝트
CREATE TABLE project
(
  project_id            NUMBER        NOT NULL,
  manager_id            VARCHAR2(10)  NOT NULL,
  project_name          VARCHAR2(50)  NOT NULL,
  project_start_date    DATE          NOT NULL,
  project_end_date      DATE          NOT NULL,
  project_content       VARCHAR2(255) NOT NULL,
  project_file_path     VARCHAR2(255),
  project_file_original VARCHAR2(255),
  project_file_uuid     VARCHAR2(36) ,
  project_file_type     VARCHAR2(50) ,
  project_finished      NUMBER(1)     DEFAULT 0 NOT NULL,
  project_canceled      NUMBER(1)     DEFAULT 0 NOT NULL,
  CONSTRAINT PK_project PRIMARY KEY (project_id)
);

CREATE TABLE project_member
(
  project_member_id    NUMBER       NOT NULL,
  member_id            VARCHAR2(10) NOT NULL,
  project_id           NUMBER       NOT NULL,
  member_role          VARCHAR2(20) NOT NULL,
  member_designated    VARCHAR2(50),
  member_progress_rate NUMBER(3)    DEFAULT 0 NOT NULL,
  member_date          DATE         NOT NULL,
  member_update_date   DATE        ,
  CONSTRAINT PK_project_member PRIMARY KEY (project_member_id)
);

CREATE SEQUENCE project_seq START WITH 1 INCREMENT BY 1;



CREATE SEQUENCE project_member_seq
START WITH 1
INCREMENT BY 1
NOCACHE;

select * from emp_info;
DROP TABLE emp_info CASCADE CONSTRAINTS;

create table emp_info
(
emp_id         VARCHAR2(10)  NOT NULL,
  mobile2        VARCHAR2(13)  NOT NULL,
  hiredate       DATE          NOT NULL,
  hiretype       VARCHAR2(20)  NOT NULL,
  birthday       DATE          NOT NULL,
  school         VARCHAR2(50)  NOT NULL,
  major          VARCHAR2(50) ,
  bank           VARCHAR2(30)  NOT NULL,
  account        VARCHAR2(30)  NOT NULL,
  address        VARCHAR2(255) NOT NULL,
  married        NUMBER(1)     NOT NULL,
  child          NUMBER(1)     NOT NULL,
  etype          VARCHAR2(20)  NOT NULL,
  vacation_count NUMBER        NOT NULL,
  CONSTRAINT PK_emp_info PRIMARY KEY (emp_id)
);

select * from emp;
select * from emp_info;

INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E001', '010-1234-5679', TO_DATE('2020-01-15', 'YYYY-MM-DD'), 'Full-time', TO_DATE('1985-06-20', 'YYYY-MM-DD'), 'Harvard', 'Computer Science', 'KB Bank', '1234-5678-9012', '123 Main St', 1, 2, 'Employee', 15);
INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E002', '010-5678-9012', TO_DATE('2019-03-20', 'YYYY-MM-DD'), 'Part-time', TO_DATE('1990-04-25', 'YYYY-MM-DD'), 'Stanford', 'Business', 'Shinhan Bank', '2345-6789-0123', '456 Elm St', 0, 0, 'Contractor', 10);
INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E003', '010-9876-5432', TO_DATE('2018-07-10', 'YYYY-MM-DD'), 'Full-time', TO_DATE('1988-11-30', 'YYYY-MM-DD'), 'MIT', 'Engineering', 'NH Bank', '3456-7890-1234', '789 Pine St', 1, 1, 'Employee', 12);
INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E004', '010-6543-2109', TO_DATE('2017-09-01', 'YYYY-MM-DD'), 'Full-time', TO_DATE('1982-02-18', 'YYYY-MM-DD'), 'Yale', 'Law', 'Hana Bank', '4567-8901-2345', '123 Oak St', 1, 3, 'Employee', 20);
INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E005', '010-3210-9876', TO_DATE('2016-12-11', 'YYYY-MM-DD'), 'Full-time', TO_DATE('1992-08-05', 'YYYY-MM-DD'), 'Princeton', 'Medicine', 'Woori Bank', '5678-9012-3456', '456 Maple St', 0, 0, 'Employee', 18);
INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E006', '010-6789-0123', TO_DATE('2021-05-22', 'YYYY-MM-DD'), 'Intern', TO_DATE('1995-09-15', 'YYYY-MM-DD'), 'Columbia', 'Art', 'KB Bank', '6789-0123-4567', '789 Birch St', 0, 0, 'Intern', 5);
INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E007', '010-0987-6543', TO_DATE('2015-04-19', 'YYYY-MM-DD'), 'Full-time', TO_DATE('1987-12-10', 'YYYY-MM-DD'), 'UCLA', 'Music', 'Shinhan Bank', '7890-1234-5678', '123 Cedar St', 1, 2, 'Employee', 25);
INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E008', '010-5432-1098', TO_DATE('2014-06-30', 'YYYY-MM-DD'), 'Part-time', TO_DATE('1993-03-25', 'YYYY-MM-DD'), 'NYU', 'Journalism', 'NH Bank', '8901-2345-6789', '456 Spruce St', 0, 0, 'Contractor', 10);


INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E008', '010-2345-6789', TO_DATE('1990-01-01', 'YYYY-MM-DD'), '정규직', TO_DATE('1970-01-01', 'YYYY-MM-DD'), '서울대학교', '컴퓨터공학', 'KB국민은행', '123-456-7890', '서울특별시 강남구', 1, 2, '재직', 15);

INSERT INTO emp_info (emp_id, mobile2, hiredate, hiretype, birthday, school, major, bank, account, address, married, child, etype, vacation_count)
VALUES ('E009', '010-2345-6789', TO_DATE('1990-01-01', 'YYYY-MM-DD'), '정규직', TO_DATE('1970-01-01', 'YYYY-MM-DD'), '서울대학교', '컴퓨터공학', 'KB국민은행', '123-456-7890', '서울특별시 강남구', 1, 2, '재직', 

select * from emp_info;
select * from emp;



SELECT * FROM emp_info WHERE emp_id = 'E001';
















select * from lang where emp_id='admin';
select * from cert where emp_id='admin';
select * from emp 
where emp_id='admin';
select * from job;
select * from dept;

INSERT INTO cert (cert_id, cert_name, emp_id) VALUES (1, 'Java Certification', 'admin');
INSERT INTO cert (cert_id, cert_name, emp_id) VALUES (2, 'AWS Certified Developer', 'admin');
INSERT INTO cert (cert_id, cert_name, emp_id) VALUES (3, 'PMP Certification', '241101');

INSERT INTO lang (lang_id, lang_name, emp_id) VALUES (1, 'English', 'admin');
INSERT INTO lang (lang_id, lang_name, emp_id) VALUES (2, 'Japanese', 'admin');
INSERT INTO lang (lang_id, lang_name, emp_id) VALUES (3, 'Spanish', '241101');


UPDATE EMP 
SET img_path='image/Test1.png'
WHERE emp_id='admin'; 
SELECT * FROM emp WHERE dept_id = (SELECT dept_id FROM emp WHERE emp_id = '241001');
SELECT * FROM emp WHERE dept_id = (
    SELECT dept_id FROM emp WHERE emp_id = '241001' AND dept_id IS NOT NULL
);
SELECT emp_id, ename, dept_id, job_id, email, tel	
				FROM emp 
				WHERE dept_id = (SELECT dept_id 
								 FROM emp 
								 WHERE emp_id = 'admin');