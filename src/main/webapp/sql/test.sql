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





select * from project_member;





--테스트
SELECT p.project_id, p.project_name, m.member_id, e.ename, p.project_start_date, p.project_end_date, p.project_file_path FROM project p 
                     JOIN project_member m ON p.project_id = m.project_id
                     JOIN emp e ON m.member_id = e.ename
                     WHERE p.project_finished = 1;

SELECT p.project_id, p.project_name, m.member_role, e.ename, p.project_start_date, p.project_end_date, p.project_file_path FROM project p 
                JOIN project_member m ON p.project_id = m.project_id
                JOIN emp e ON m.member_id = e.ename
                WHERE p.project_canceled = 1;
select e.ename, m.member_designated, m.member_progress_rate
				from project p join project_member m on p.project_id = m.project_id
				join emp e on m.member_id = e.ename
				where p.project_id = 17
				--where p.project_id = ?
update project_member set member_progress_rate = 60 where project_id = 18 and member_id = '헤헤'; --진행률 테스트




