-- 기존 테이블 삭제 (존재할 경우)
DROP TABLE task_commnet CASCADE CONSTRAINTS;
DROP TABLE project_member CASCADE CONSTRAINTS;
DROP TABLE task CASCADE CONSTRAINTS;
DROP TABLE project CASCADE CONSTRAINTS;
DROP TABLE job CASCADE CONSTRAINTS;
DROP TABLE emp_info CASCADE CONSTRAINTS;
DROP TABLE emp CASCADE CONSTRAINTS;
DROP TABLE dept CASCADE CONSTRAINTS;

update project_member set member_progress_rate = 60 where member_id = 'e002';
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


SELECT 
                    p.project_id,
                    p.project_name,
                    (SELECT e.ename 
                     FROM project_member m 
                     JOIN emp e ON m.member_id = e.emp_id 
                     WHERE m.project_id = p.project_id 
                       AND m.member_role = 'MANAGER') AS manager_name,
                    m.member_role,
                    e.ename AS participant_name,
                    p.project_start_date,
                    p.project_end_date,
                    p.project_file_path
                FROM 
                    project p 
                JOIN 
                    project_member m ON p.project_id = m.project_id
                JOIN 
                    emp e ON m.member_id = e.emp_id
                WHERE 
                    p.project_finished = 1
                    and m.member_id = 'e002'
                ORDER BY 
                    p.project_id desc

update project_member set member_progress_rate = 30 where project_id = 9 and member_id = 'e002';
select * from task;
                    
CREATE SEQUENCE project_member_seq
START WITH 1
INCREMENT BY 1
NOCACHE;

CREATE TABLE task
(
  task_id            NUMBER        NOT NULL,
  emp_id             VARCHAR2(10)  NOT NULL,
  project_id         NUMBER        NOT NULL,
  task_subject       VARCHAR2(50),
  task_content       VARCHAR2(300),
  task_date          DATE         ,
  task_update_date   DATE         ,
  task_file_path     VARCHAR2(255),
  task_file_original VARCHAR2(255),
  task_file_uuid     VARCHAR2(36) ,
  task_file_type     VARCHAR2(50) ,
  CONSTRAINT PK_task PRIMARY KEY (task_id)
);
drop table task;
CREATE SEQUENCE SEQ_task
START WITH 1
INCREMENT BY 1;



-- 삽입된 데이터 확인-------------------------------
SELECT * FROM dept;
SELECT * FROM job;
SELECT * FROM emp;
select * from project;
select * from project_member;
select * from task;



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

INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('admin', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', 'admin', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);

delete from emp where emp_id = '홍길동'
delete from emp where emp_id = '김길동'
delete from emp where emp_id = '최길동'
delete from emp where emp_id = '한길동'
delete from emp where emp_id = '호길동'
delete from emp where emp_id = '히길동'
delete from emp where emp_id = '티길동'
delete from emp where emp_id = '도길동'
delete from emp where emp_id = '리길동'
delete from emp where emp_id = '지길동'
delete from emp where emp_id = '니길동'
delete from emp where emp_id = '코길동'

select * from emp;
select * from project_member;
select * from task;
select project_id, project_finished, project_canceled from project;
select p.project_id, member_id, member_role from project p join project_member m on p.project_id = m.project_id;
SELECT 
                    p.project_id, 
                    p.project_name, 
                    p.project_end_date, 
                    COUNT(pm.member_id) AS member_count, 
                    AVG(pm.member_progress_rate) AS average_progress_rate
                FROM 
                    project p
                JOIN 
                    project_member pm ON p.project_id = pm.project_id
                WHERE 
                    p.project_finished = 0 
                    AND p.project_canceled = 0
                    AND pm.member_id = 'e002'
                GROUP BY 
                    p.project_id, 
                    p.project_name, 
                    p.project_end_date
                ORDER BY p.project_id DESC



INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e001', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '홍길동', 1, 1, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e002', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '김길동', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e003', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '최길동', 3, 3, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e004', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '한길동', 1, 1, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e005', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '호길동', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e006', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '히길동', 3, 3, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e007', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '티길동', 1, 1, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e008', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '도길동', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e009', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '리길동', 3, 3, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e010', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '지길동', 1, 1, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e011', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '니길동', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('e012', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', '코길동', 3, 3, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);




































-- 나머지 테이블도 여기에 추가 생성...


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

INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('admin', '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60', 'admin', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);
INSERT INTO emp (emp_id, password, ename, dept_id, job_id, gender, email, tel, mobile, img_path, img_original, img_uuid, img_type, hired)
VALUES ('E010', 'password123', '시시시', 2, 2, 'M', 'kim@example.com', '5678', '010-5678-1234', 'path/to/img2', 'original2.jpg', 'uuid-value2', 'image/jpeg', 1);




select * from project where project_canceled = 1;
select * from project_member;
select * from project p join project_member m on p.project_id = m.project_id;


select * from project_member where project_id = 19;

insert into task values('1','E001','2');
-----------------------
--완료, 취소프로젝트 테스트
SELECT p.project_id, p.project_name, m.member_id, e.ename, p.project_start_date, p.project_end_date, p.project_file_path FROM project p 
                     JOIN project_member m ON p.project_id = m.project_id
                     JOIN emp e ON m.member_id = e.ename
                     WHERE p.project_finished = 1;
SELECT 
    p.project_id,
    p.project_name,
    (SELECT e.ename 
     FROM project_member m 
     JOIN emp e ON m.member_id = e.emp_id 
     WHERE m.project_id = p.project_id 
       AND m.member_role = 'MANAGER') AS manager_name,
    e.ename AS participant_name,
    p.project_start_date,
    p.project_end_date
FROM 
    project p 
JOIN 
    project_member m ON p.project_id = m.project_id
JOIN 
    emp e ON m.member_id = e.emp_id
WHERE 
    p.project_canceled = 1
order by 
	p.project_id desc;
    
    

SELECT p.project_id, p.project_name, m.member_role, e.ename, p.project_start_date, p.project_end_date, p.project_file_path FROM project p 
                JOIN project_member m ON p.project_id = m.project_id
                JOIN emp e ON m.member_id = e.ename
                WHERE p.project_canceled = 1;
--진행률 테스트
select e.ename, m.member_designated, m.member_progress_rate
				from project p join project_member m on p.project_id = m.project_id
				join emp e on m.member_id = e.ename
				where p.project_id = 17
				--where p.project_id = ?
update project_member set member_progress_rate = 60 where project_id = 18 and member_id = '헤헤'; 
update project_member set member_progress_rate = 40 where project_id = 16 and member_id = '헤헤'; 
update project_member set member_progress_rate = 80 where project_id = 16 and member_id = '김철수';
update project_member set member_progress_rate = 60 where project_id = 14 and member_id = '김철수';
update project_member set member_progress_rate = 80 where project_id = 14 and member_id = '홍길동';
update project_member set member_progress_rate = 70 where project_id = 14 and member_id = '하하'; 
update project_member set member_progress_rate = 90 where project_id = 13 and member_id = '김철수'; 
update project_member set member_progress_rate = 95 where project_id = 13 and member_id = '키키';

--진행과정 테스트
SELECT e.ename, t.task_subject, NVL(t.task_date, t.task_update_date) AS task_date
FROM project p
JOIN project_member m ON p.project_id = m.project_id
JOIN emp e ON m.member_id = e.ename
JOIN task t ON p.project_id = t.project_id
WHERE p.project_id = 18

--task 테스트
SELECT e.ename, t.task_subject, NVL(t.task_date, t.task_update_date) AS task_date
FROM project p
JOIN project_member m ON p.project_id = m.project_id
JOIN emp e ON m.member_id = e.emp_id
JOIN task t ON p.project_id = t.project_id
WHERE p.project_id = 55
GROUP BY e.ename, t.task_subject, NVL(t.task_date, t.task_update_date);

SELECT e.ename, t.task_subject, NVL(t.task_date, t.task_update_date) AS task_date
				FROM project p
				JOIN project_member m ON p.project_id = m.project_id
				JOIN emp e ON m.member_id = e.emp_id
				JOIN task t ON p.project_id = t.project_id
				WHERE p.project_id = ?



select * from project p join project_member m on p.project_id = m.project_id;
select * from emp e join project_member m on e.ename = m.member_id;


