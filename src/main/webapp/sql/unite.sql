select post.*, board.*, nvl(cnt,0) as cnt, img_path, img_original, img_uuid, img_type
					from post left outer join (select post_id, count(*) as cnt
											from post_comment
											group by post_id) pc
						on post.post_id = pc.post_id
					join board
						on board.board_id = post.board_id
					join emp
						on emp.emp_id = post.post_writer
					where board.dept_id IS NULL OR '241103' IN (0000, 1000, 1001)
					    OR ('241103' = 1100 AND board.dept_id between 1100 and 1199)
					    OR ('241103' = 1200 AND board.dept_id between 1200 and 1299)
					    OR ('241103' = 1300 AND board.dept_id between 1300 and 1399)
					    OR ('241103' = 1400 AND board.dept_id between 1400 and 1499)
					    OR (board.dept_id = '241103')
					order by post_date desc
					
SELECT p.project_name, t.task_date, t.task_update_date, t.emp_id, e.img_uuid, j.job_name
FROM task t
JOIN project p ON t.project_id = p.project_id
JOIN project_member pm ON p.project_id = pm.project_id
JOIN emp e on pm.member_id = e.emp_id
JOIN job j on e.job_id = j.job_id
WHERE p.project_finished = 0
  AND p.project_canceled = 0
  AND pm.member_id = '241103'
ORDER BY NVL(t.task_update_date, t.task_date) DESC;

SELECT p.project_name, t.task_date, t.task_update_date, e1.ename AS task_emp_name, e2.img_uuid AS pm_img_uuid, j.job_name
FROM task t JOIN project p ON t.project_id = p.project_id
JOIN project_member pm ON p.project_id = pm.project_id
JOIN emp e1 ON t.emp_id = e1.emp_id 
JOIN emp e2 ON pm.member_id = e2.emp_id 
JOIN job j ON e2.job_id = j.job_id
WHERE p.project_finished = 0 AND p.project_canceled = 0 AND pm.member_id = '241103'
ORDER BY NVL(t.task_update_date, t.task_date) DESC NULLS LAST;

SELECT p.project_name, 
       t.task_date, 
       t.task_update_date, 
       e1.ename AS task_emp_name, 
       e1.img_uuid AS pm_img_uuid, 
       j.job_name
FROM task t 
JOIN project p ON t.project_id = p.project_id
JOIN project_member pm ON p.project_id = pm.project_id	
JOIN emp e1 ON t.emp_id = e1.emp_id  -- 작업을 수행한 직원 (e1)
JOIN emp e2 ON pm.member_id = e2.emp_id  -- 프로젝트 멤버 (e2)
JOIN job j ON e2.job_id = j.job_id
WHERE p.project_finished = 0 
  AND p.project_canceled = 0 
  AND pm.member_id = '241103'  -- 특정 프로젝트 멤버
  AND t.emp_id != pm.member_id  -- 자기 자신은 제외
  AND e2.emp_id != t.emp_id    -- 작업을 수행한 직원의 emp_id와 프로젝트 멤버의 emp_id가 다르도록 추가
ORDER BY NVL(t.task_update_date, t.task_date) DESC NULLS LAST;

select img_uuid from emp where emp_id = '241103' or emp_id = '241105';

alter table DOC_VACATION modify(VACATION_FILE_TYPE varchar2(100))
--게시판 제약조건 수정
ALTER TABLE board MODIFY dept_id null;
ALTER TABLE board MODIFY dept_id default NULL;
select * from task where project_id = 7;
select * from emp;
select * from task_comment;
ALTER TABLE NOTICE ADD NOTICE_SUBJECT VARCHAR(150)  NOT NULL;
delete from post;
delete from post_file;
--컬럼 사이즈 및 타입 수정
ALTER TABLE POST MODIFY post_content CLOB;
ALTER TABLE POST MODIFY POST_WRITER VARCHAR2(15);
update
--게시판 데이터 임시
INSERT ALL
INTO BOARD(board_name1, board_name2) VALUES ('전사게시판', '공지사항')
INTO BOARD(board_name1, board_name2) VALUES ('전사게시판', '주간식단표')
INTO BOARD(board_name1, board_name2) VALUES ('전사게시판', 'FAQ')
INTO BOARD(board_name1, board_name2) VALUES ('일반게시판', '일반게시판')
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '경영기획본부', 1100)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '재무관리팀', 1110)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '인사관리팀', 1120)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', 'SI사업본부', 1200)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '신용평가팀', 1210)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '금융SI팀', 1220)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '비금융SI팀', 1230)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', 'SM팀', 1240)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '영업본부	', 1300)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '솔루션영업팀', 1310)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', 'SI영업팀', 1320)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', 'SM영업팀', 1330)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', 'R&B본부', 1400)
INTO BOARD(board_name1, board_name2, dept_id) VALUES ('부서게시판', '연구개발팀', 1410)
SELECT *
FROM DUAL;

select * from post;

ALTER TABLE POST ADD (new_post_content CLOB NOT NULL);
UPDATE POST SET new_post_content = post_content;
ALTER TABLE POST DROP COLUMN post_content;
ALTER TABLE POST RENAME COLUMN new_post_content TO post_content;



select post.*, board.*, nvl(cnt,0) as cnt
					from post left outer join (select post_id, count(*) as cnt
											from post_comment
											group by post_id) pc
						on post.post_id = pc.post_id
					join board
						on board.board_id = post.board_id
					order by post_re_ref desc, post_re_seq asc


select * from post;




















select * from board;
select * from task where emp_id = '241103'
SELECT t.*, e.ename FROM task t join emp e on t.emp_id = e.emp_id WHERE t.project_id = 7 AND t.emp_id = '241103' and t.task_id = 130

 SELECT *
	                FROM task t
	                WHERE t.project_id = 7 AND t.emp_id = '241103'
	                ORDER BY t.task_date DESC

SELECT * FROM (
	            SELECT rownum rnum, t.* FROM (
	                SELECT *
	                FROM task t
	                WHERE t.project_id = 7 AND t.emp_id = '241103'
	                ORDER BY t.task_date DESC
	            ) t
	            WHERE rownum <= 7
	        ) 
	        WHERE rnum >= 1 AND rnum <= 10

SELECT * FROM (
				    SELECT rownum rnum, t.*
				    FROM (
				        SELECT
				            p.project_id,
				            p.project_name,
				            p.project_start_date,
				            p.project_end_date,
				            p.project_file_original,
				            p.project_file_uuid,
				            p.project_file_type,
				            COUNT(pm.project_member_id) AS member_count,
				            SUM(pm.MEMBER_PROGRESS_RATE) / COUNT(pm.project_member_id) AS avg_progress,
				            (SELECT e.ename
				             FROM emp e
				             WHERE e.emp_id = (
				                 SELECT m.member_id
				                 FROM project_member m
				                 WHERE m.project_id = p.project_id
				                 AND m.member_role = 'MANAGER'
				                 AND ROWNUM = 1
				             )) AS emp_name,
				             (SELECT m.member_id
				             FROM project_member m
				             WHERE m.project_id = p.project_id
				             AND m.member_role = 'MANAGER') AS manager_id,
				            LISTAGG(CASE WHEN pm.member_role = 'PARTICIPANT' THEN e.ename END, ', ')
				            WITHIN GROUP (ORDER BY e.ename) AS participants,
				            LISTAGG(CASE WHEN pm.member_role = 'VIEWER' THEN e.ename END, ', ')
				            WITHIN GROUP (ORDER BY e.ename) AS viewers
				        FROM
				            project p
				        JOIN
				            project_member pm ON p.project_id = pm.project_id
				        LEFT JOIN
				            emp e ON pm.member_id = e.emp_id
				        WHERE
				            p.project_id IN (
				                SELECT project_id
				                FROM project_member
				                WHERE member_id = '241103'
				            )
				            AND p.project_canceled = 1  
				        GROUP BY
				            p.project_id, p.project_name, p.project_start_date, p.project_end_date, p.project_file_original, p.project_file_uuid, p.project_file_type
				        ORDER BY 
				            p.project_id
				    ) t
				    WHERE rownum <= 100
				)
				WHERE rnum >= 1 AND rnum <= 100
select * from project_member where project_id = 60;
select * from project_member where project_id = 63;
select * from project where manager_id = '241103';      
                    
                    
CREATE TABLE attend
(
  attend_id   NUMBER       NOT NULL,
  emp_id      VARCHAR2(10) NOT NULL,
  attend_date DATE         NOT NULL,
  attend_in   DATE        ,
  attend_out  DATE        ,
  attend_type VARCHAR2(15) NOT NULL,
  CONSTRAINT PK_attend PRIMARY KEY (attend_id)
);

CREATE SEQUENCE SEQ_attend
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE attend IS '근태';

COMMENT ON COLUMN attend.attend_id IS '근태 id';

COMMENT ON COLUMN attend.emp_id IS '사번';

COMMENT ON COLUMN attend.attend_date IS '날짜';

COMMENT ON COLUMN attend.attend_in IS '출근시각';

COMMENT ON COLUMN attend.attend_out IS '퇴근시각';

COMMENT ON COLUMN attend.attend_type IS '근무유형';

CREATE TABLE board
(
  board_id    NUMBER       NOT NULL,
  board_name1 VARCHAR2(30) NOT NULL,
  board_name2 VARCHAR2(30) NOT NULL,
  dept_id     NUMBER(4)    DEFAULT 9999 NOT NULL,
  CONSTRAINT PK_board PRIMARY KEY (board_id)
);

CREATE SEQUENCE SEQ_board
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE board IS '게시판';

COMMENT ON COLUMN board.board_id IS '게시판id';

COMMENT ON COLUMN board.board_name1 IS '게시판명(분류)';

COMMENT ON COLUMN board.board_name2 IS '게시판명(세부)';

COMMENT ON COLUMN board.dept_id IS '부서코드';

CREATE TABLE buy_list
(
  buy_list_id  NUMBER        NOT NULL,
  doc_buy_id   NUMBER        NOT NULL,
  product_name VARCHAR2(100) NOT NULL,
  standard     VARCHAR2(255),
  quantity     NUMBER        NOT NULL,
  price        NUMBER        NOT NULL,
  CONSTRAINT PK_buy_list PRIMARY KEY (buy_list_id)
);

CREATE SEQUENCE SEQ_buy_list
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE buy_list IS '구매목록';

COMMENT ON COLUMN buy_list.buy_list_id IS '구매목록id';

COMMENT ON COLUMN buy_list.doc_buy_id IS '구매신청서id';

COMMENT ON COLUMN buy_list.product_name IS '품명';

COMMENT ON COLUMN buy_list.standard IS '규격';

COMMENT ON COLUMN buy_list.quantity IS '수량';

COMMENT ON COLUMN buy_list.price IS '단가';

CREATE TABLE cert
(
  cert_id   NUMBER       NOT NULL,
  cert_name VARCHAR2(50) NOT NULL,
  emp_id    VARCHAR2(10) NOT NULL,
  CONSTRAINT PK_cert PRIMARY KEY (cert_id)
);

CREATE SEQUENCE SEQ_cert
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE cert IS '자격증';

COMMENT ON COLUMN cert.cert_id IS '자격증id';

COMMENT ON COLUMN cert.cert_name IS '자격증이름';

COMMENT ON COLUMN cert.emp_id IS '사번';

CREATE TABLE dept
(
  dept_id      NUMBER(4)    NOT NULL,
  dept_name    VARCHAR2(50) NOT NULL,
  dept_manager NUMBER      ,
  CONSTRAINT PK_dept PRIMARY KEY (dept_id)
);

COMMENT ON TABLE dept IS '부서';

COMMENT ON COLUMN dept.dept_id IS '부서id';

COMMENT ON COLUMN dept.dept_name IS '부서명';

COMMENT ON COLUMN dept.dept_manager IS '부서장사번';

CREATE TABLE doc
(
  doc_id          NUMBER         NOT NULL,
  doc_writer      VARCHAR2(10)   NOT NULL,
  doc_type        VARCHAR2(30)   NOT NULL,
  doc_title       VARCHAR2(100)  NOT NULL,
  doc_content     VARCHAR2(4000),
  doc_create_date DATE           NOT NULL,
  sign_finish     NUMBER(1)      DEFAULT 0 NOT NULL,
  CONSTRAINT PK_doc PRIMARY KEY (doc_id)
);

CREATE SEQUENCE SEQ_doc
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE doc IS '문서';

COMMENT ON COLUMN doc.doc_id IS '문서id';

COMMENT ON COLUMN doc.doc_writer IS '작성자사번';

COMMENT ON COLUMN doc.doc_type IS '문서종류';

COMMENT ON COLUMN doc.doc_title IS '문서제목';

COMMENT ON COLUMN doc.doc_content IS '문서내용';

COMMENT ON COLUMN doc.doc_create_date IS '문서작성일';

COMMENT ON COLUMN doc.sign_finish IS '결재완료여부';

CREATE TABLE doc_buy
(
  doc_buy_id NUMBER NOT NULL,
  doc_id     NUMBER NOT NULL,
  CONSTRAINT PK_doc_buy PRIMARY KEY (doc_buy_id)
);

CREATE SEQUENCE SEQ_doc_buy
START WITH 1
INCREMENT BY 1;

COMMENT ON TABLE doc_buy IS '구매신청서';

COMMENT ON COLUMN doc_buy.doc_buy_id IS '구매신청서id';

COMMENT ON COLUMN doc_buy.doc_id IS '문서id';

CREATE TABLE doc_trip
(
  doc_trip_id NUMBER        NOT NULL,
  doc_id      NUMBER        NOT NULL,
  trip_start  DATE          NOT NULL,
  trip_end    DATE          NOT NULL,
  trip_loc    VARCHAR2(120) NOT NULL,
  trip_phone  VARCHAR2(13) ,
  trip_info   VARCHAR2(255) NOT NULL,
  card_start  DATE         ,
  card_end    DATE         ,
  card_return DATE         ,
  CONSTRAINT PK_doc_trip PRIMARY KEY (doc_trip_id)
);

CREATE SEQUENCE SEQ_doc_trip
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE doc_trip IS '출장신청서';

COMMENT ON COLUMN doc_trip.doc_trip_id IS '출장신청서id';

COMMENT ON COLUMN doc_trip.doc_id IS '문서id';

COMMENT ON COLUMN doc_trip.trip_start IS '출장시작일';

COMMENT ON COLUMN doc_trip.trip_end IS '출장종료일';

COMMENT ON COLUMN doc_trip.trip_loc IS '출장목적지';

COMMENT ON COLUMN doc_trip.trip_phone IS '출장지연락처';

COMMENT ON COLUMN doc_trip.trip_info IS '출장목적';

COMMENT ON COLUMN doc_trip.card_start IS '카드시작일';

COMMENT ON COLUMN doc_trip.card_end IS '카드종료일';

COMMENT ON COLUMN doc_trip.card_return IS '카드반납일';

CREATE TABLE doc_vacation
(
  doc_vacation_id        NUMBER        NOT NULL,
  doc_id                 NUMBER        NOT NULL,
  vacation_apply         DATE          NOT NULL,
  vacation_start         DATE          NOT NULL,
  vacation_end           DATE          NOT NULL,
  vacation_file_path     VARCHAR2(255) NOT NULL,
  vacation_file_original VARCHAR2(255) NOT NULL,
  vacation_file_uuid     VARCHAR2(36)  NOT NULL,
  vacation_file_type     VARCHAR2(50)  NOT NULL,
  CONSTRAINT PK_doc_vacation PRIMARY KEY (doc_vacation_id)
);

CREATE SEQUENCE SEQ_doc_vacation
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE doc_vacation IS '휴가신청서';

COMMENT ON COLUMN doc_vacation.doc_vacation_id IS '휴가신청서id';

COMMENT ON COLUMN doc_vacation.doc_id IS '문서id';

COMMENT ON COLUMN doc_vacation.vacation_apply IS '휴가신청일';

COMMENT ON COLUMN doc_vacation.vacation_start IS '휴가시작일';

COMMENT ON COLUMN doc_vacation.vacation_end IS '휴가종료일';

COMMENT ON COLUMN doc_vacation.vacation_file_path IS '파일경로';

COMMENT ON COLUMN doc_vacation.vacation_file_original IS '파일원본명';

COMMENT ON COLUMN doc_vacation.vacation_file_uuid IS '파일UUID';

COMMENT ON COLUMN doc_vacation.vacation_file_type IS '파일MIME';

CREATE TABLE emp
(
  emp_id         VARCHAR2(10)  NOT NULL,
  password       VARCHAR2(200) NOT NULL,
  ename          VARCHAR2(15)  NOT NULL,
  dept_id        NUMBER(4)     NOT NULL,
  job_id         NUMBER        NOT NULL,
  gender         VARCHAR2(3)   NOT NULL,
  email          VARCHAR2(30)  NOT NULL,
  tel            VARCHAR2(15)  NOT NULL,
  mobile         VARCHAR2(13)  NOT NULL,
  mobile2        VARCHAR2(13)  NOT NULL,
  img_path       VARCHAR2(255) NOT NULL,
  img_original   VARCHAR2(255) NOT NULL,
  img_uuid       VARCHAR2(36)  NOT NULL,
  img_type       VARCHAR2(50)  NOT NULL,
  hiredate       DATE          NOT NULL,
  hiretype       VARCHAR2(20)  NOT NULL,
  birthday       DATE          NOT NULL,
  birthday_type  VARCHAR2(10)  NOT NULL,
  school         VARCHAR2(50)  NOT NULL,
  major          VARCHAR2(50) ,
  bank           VARCHAR2(30)  NOT NULL,
  account        VARCHAR2(30)  NOT NULL,
  address        VARCHAR2(255) NOT NULL,
  married        NUMBER(1)     NOT NULL,
  child          NUMBER(1)     NOT NULL,
  etype          VARCHAR2(20)  NOT NULL,
  vacation_count NUMBER        NOT NULL,
  hired          NUMBER        DEFAULT 1 NOT NULL,
  CONSTRAINT PK_emp PRIMARY KEY (emp_id)
);

COMMENT ON TABLE emp IS '직원';

COMMENT ON COLUMN emp.emp_id IS '사번';

COMMENT ON COLUMN emp.password IS '비밀번호';

COMMENT ON COLUMN emp.ename IS '이름';

COMMENT ON COLUMN emp.dept_id IS '부서id';

COMMENT ON COLUMN emp.job_id IS '직위id';

COMMENT ON COLUMN emp.gender IS '성별';

COMMENT ON COLUMN emp.email IS '이메일';

COMMENT ON COLUMN emp.tel IS '내선번호';

COMMENT ON COLUMN emp.mobile IS '휴대폰번호';

COMMENT ON COLUMN emp.mobile2 IS '긴급연락처';

COMMENT ON COLUMN emp.img_path IS '이미지경로';

COMMENT ON COLUMN emp.img_original IS '이미지원본명';

COMMENT ON COLUMN emp.img_uuid IS '이미지UUID';

COMMENT ON COLUMN emp.img_type IS '이미지MIME';

COMMENT ON COLUMN emp.hiredate IS '입사일';

COMMENT ON COLUMN emp.hiretype IS '채용구분';

COMMENT ON COLUMN emp.birthday IS '생년월일';

COMMENT ON COLUMN emp.birthday_type IS '양력음력';

COMMENT ON COLUMN emp.school IS '최종학력';

COMMENT ON COLUMN emp.major IS '전공';

COMMENT ON COLUMN emp.bank IS '은행';

COMMENT ON COLUMN emp.account IS '계좌번호';

COMMENT ON COLUMN emp.address IS '주소';

COMMENT ON COLUMN emp.married IS '혼인여부';

COMMENT ON COLUMN emp.child IS '자녀유무';

COMMENT ON COLUMN emp.etype IS '직원구분';

COMMENT ON COLUMN emp.vacation_count IS '연차갯수';

COMMENT ON COLUMN emp.hired IS '재직여부';

CREATE TABLE holiday
(
  holiday_id   NUMBER       NOT NULL,
  holiday_date DATE         NOT NULL,
  holiday_name VARCHAR2(30) NOT NULL,
  CONSTRAINT PK_holiday PRIMARY KEY (holiday_id)
);

CREATE SEQUENCE SEQ_holiday
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE holiday IS '휴일';

COMMENT ON COLUMN holiday.holiday_id IS '휴일id';

COMMENT ON COLUMN holiday.holiday_date IS '휴일날짜';

COMMENT ON COLUMN holiday.holiday_name IS '휴일이름';

CREATE TABLE job
(
  job_id   NUMBER       NOT NULL,
  job_name VARCHAR2(50) NOT NULL,
  job_rank NUMBER       NOT NULL,
  CONSTRAINT PK_job PRIMARY KEY (job_id)
);

CREATE SEQUENCE SEQ_job
START WITH 1
INCREMENT BY 1;

COMMENT ON TABLE job IS '직위';

COMMENT ON COLUMN job.job_id IS '직위id';

COMMENT ON COLUMN job.job_name IS '직위명';

COMMENT ON COLUMN job.job_rank IS '직위순서';

CREATE TABLE lang
(
  lang_id   NUMBER       NOT NULL,
  lang_name VARCHAR2(50) NOT NULL,
  emp_id    VARCHAR2(10) NOT NULL,
  CONSTRAINT PK_lang PRIMARY KEY (lang_id)
);

CREATE SEQUENCE SEQ_lang
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE lang IS '외국어';

COMMENT ON COLUMN lang.lang_id IS '외국어id';

COMMENT ON COLUMN lang.lang_name IS '외국어이름';

COMMENT ON COLUMN lang.emp_id IS '사번';

CREATE TABLE notice
(
  notice_id       NUMBER         NOT NULL,
  notice_content  VARCHAR2(2000) NOT NULL,
  notice_end_date DATE           NOT NULL,
  CONSTRAINT PK_notice PRIMARY KEY (notice_id)
);

CREATE SEQUENCE SEQ_notice
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE notice IS '팝업공지사항';

COMMENT ON COLUMN notice.notice_id IS '팝업id';

COMMENT ON COLUMN notice.notice_content IS '팝업내용';

COMMENT ON COLUMN notice.notice_end_date IS '게시종료시각';

CREATE TABLE post
(
  post_id          NUMBER         NOT NULL,
  board_id         NUMBER         NOT NULL,
  post_writer      VARCHAR2(10)   NOT NULL,
  post_subject     VARCHAR2(250)  NOT NULL,
  post_content     VARCHAR2(4000) NOT NULL,
  post_date        DATE           NOT NULL,
  post_update_date DATE          ,
  post_re_ref      NUMBER         NOT NULL,
  post_re_lev      NUMBER         NOT NULL,
  post_re_seq      NUMBER         NOT NULL,
  post_view        NUMBER         DEFAULT 0 NOT NULL,
  CONSTRAINT PK_post PRIMARY KEY (post_id)
);

CREATE SEQUENCE SEQ_post
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE post IS '게시글';

COMMENT ON COLUMN post.post_id IS '게시글id';

COMMENT ON COLUMN post.board_id IS '게시판id';

COMMENT ON COLUMN post.post_writer IS '게시글작성자';

COMMENT ON COLUMN post.post_subject IS '제목';

COMMENT ON COLUMN post.post_content IS '내용';

COMMENT ON COLUMN post.post_date IS '게시글작성시각';

COMMENT ON COLUMN post.post_update_date IS '게시글최종수정시각';

COMMENT ON COLUMN post.post_re_ref IS '부모게시글';

COMMENT ON COLUMN post.post_re_lev IS '답글레벨';

COMMENT ON COLUMN post.post_re_seq IS '게시글순서';

COMMENT ON COLUMN post.post_view IS '조회수';

CREATE TABLE post_comment
(
  comment_id                 NUMBER        NOT NULL,
  post_id                    NUMBER        NOT NULL,
  post_comment_writer        VARCHAR2(10)  NOT NULL,
  post_comment_content       VARCHAR2(600) NOT NULL,
  post_comment_date          DATE          NOT NULL,
  post_comment_update_date   DATE         ,
  post_comment_file_path     VARCHAR2(255),
  post_comment_file_original VARCHAR2(255),
  post_comment_file_uuid     VARCHAR2(36) ,
  post_comment_file_type     VARCHAR2(50) ,
  post_comment_re_ref        NUMBER        NOT NULL,
  post_comment_re_lev        NUMBER        NOT NULL,
  post_comment_re_seq        NUMBER        NOT NULL,
  CONSTRAINT PK_post_comment PRIMARY KEY (comment_id)
);

CREATE SEQUENCE SEQ_post_comment
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE post_comment IS '댓글';

COMMENT ON COLUMN post_comment.comment_id IS '댓글id';

COMMENT ON COLUMN post_comment.post_id IS '게시글id';

COMMENT ON COLUMN post_comment.post_comment_writer IS '댓글작성자';

COMMENT ON COLUMN post_comment.post_comment_content IS '댓글내용';

COMMENT ON COLUMN post_comment.post_comment_date IS '댓글작성일';

COMMENT ON COLUMN post_comment.post_comment_update_date IS '댓글최종수정일';

COMMENT ON COLUMN post_comment.post_comment_file_path IS '댓글파일경로';

COMMENT ON COLUMN post_comment.post_comment_file_original IS '댓글파일원본명';

COMMENT ON COLUMN post_comment.post_comment_file_uuid IS '댓글파일UUID';

COMMENT ON COLUMN post_comment.post_comment_file_type IS '댓글파일MIME';

COMMENT ON COLUMN post_comment.post_comment_re_ref IS '부모댓글';

COMMENT ON COLUMN post_comment.post_comment_re_lev IS '답댓글레벨';

COMMENT ON COLUMN post_comment.post_comment_re_seq IS '댓글순서';

CREATE TABLE post_file
(
  post_file_id       NUMBER        NOT NULL,
  post_id            NUMBER        NOT NULL,
  post_file_path     VARCHAR2(255) NOT NULL,
  post_file_original VARCHAR2(255) NOT NULL,
  post_file_uuid     VARCHAR2(36)  NOT NULL,
  post_file_type     VARCHAR2(50)  NOT NULL,
  CONSTRAINT PK_post_file PRIMARY KEY (post_file_id)
);

CREATE SEQUENCE SEQ_post_file
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE post_file IS '게시글첨부파일';

COMMENT ON COLUMN post_file.post_file_id IS '파일ID';

COMMENT ON COLUMN post_file.post_id IS '게시글id';

COMMENT ON COLUMN post_file.post_file_path IS '파일경로';

COMMENT ON COLUMN post_file.post_file_original IS '파일원본명';

COMMENT ON COLUMN post_file.post_file_uuid IS '파일UUID';

COMMENT ON COLUMN post_file.post_file_type IS '파일MIME';

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

CREATE SEQUENCE SEQ_project
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE project IS '프로젝트';

COMMENT ON COLUMN project.project_id IS '프로젝트id';

COMMENT ON COLUMN project.manager_id IS '책임자사번';

COMMENT ON COLUMN project.project_name IS '프로젝트명';

COMMENT ON COLUMN project.project_start_date IS '시작일';

COMMENT ON COLUMN project.project_end_date IS '종료일';

COMMENT ON COLUMN project.project_content IS '프로젝트내용';

COMMENT ON COLUMN project.project_file_path IS '파일경로';

COMMENT ON COLUMN project.project_file_original IS '파일원본명';

COMMENT ON COLUMN project.project_file_uuid IS '파일UUID';

COMMENT ON COLUMN project.project_file_type IS '파일MIME';

COMMENT ON COLUMN project.project_finished IS '완료여부';

COMMENT ON COLUMN project.project_canceled IS '취소여부';

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

CREATE SEQUENCE SEQ_project_member
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE project_member IS '프로젝트 멤버';

COMMENT ON COLUMN project_member.project_member_id IS '번호';

COMMENT ON COLUMN project_member.member_id IS '사번';

COMMENT ON COLUMN project_member.project_id IS '프로젝트id';

COMMENT ON COLUMN project_member.member_role IS '역할';

COMMENT ON COLUMN project_member.member_designated IS '담당업무';

COMMENT ON COLUMN project_member.member_progress_rate IS '진행률';

COMMENT ON COLUMN project_member.member_date IS '최초등록일자';

COMMENT ON COLUMN project_member.member_update_date IS '최종변경일자';

CREATE TABLE resc
(
  resc_id     NUMBER        NOT NULL,
  resc_type   VARCHAR2(50)  NOT NULL,
  resc_name   VARCHAR2(50)  NOT NULL,
  resc_info   VARCHAR2(100),
  resc_usable NUMBER(1)     DEFAULT 1 NOT NULL,
  CONSTRAINT PK_resc PRIMARY KEY (resc_id)
);

CREATE SEQUENCE SEQ_resc
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE resc IS '자원';

COMMENT ON COLUMN resc.resc_id IS '자원id';

COMMENT ON COLUMN resc.resc_type IS '자원종류';

COMMENT ON COLUMN resc.resc_name IS '자원이름';

COMMENT ON COLUMN resc.resc_info IS '자원정보';

COMMENT ON COLUMN resc.resc_usable IS '가용여부';

CREATE TABLE reservation
(
  reservation_id    NUMBER        NOT NULL,
  resource_id       NUMBER        NOT NULL,
  emp_id            VARCHAR2(10)  NOT NULL,
  reservation_start DATE          NOT NULL,
  reservation_end   DATE          NOT NULL,
  reservation_info  VARCHAR2(100),
  reservation_allDay NUMBER        DEFAULT 0 NOT NULL,
  CONSTRAINT PK_reservation PRIMARY KEY (reservation_id)
);

CREATE SEQUENCE SEQ_reservation
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE reservation IS '자원예약';

COMMENT ON COLUMN reservation.reservation_id IS '자원예약id';

COMMENT ON COLUMN reservation.resource_id IS '자원id';

COMMENT ON COLUMN reservation.emp_id IS '예약자';

COMMENT ON COLUMN reservation.reservation_start IS '예약시작시각';

COMMENT ON COLUMN reservation.reservation_end IS '예약종료시각';

COMMENT ON COLUMN reservation.reservation_info IS '예약내용';

COMMENT ON COLUMN reservation.reservation_allDay IS '종일';

CREATE TABLE schedule
(
  schedule_id      NUMBER       NOT NULL,
  emp_id           VARCHAR2(10) NOT NULL,
  schedule_name    VARCHAR2(50) NOT NULL,
  schedule_content VARCHAR(255),
  schedule_start   DATE         NOT NULL,
  schedule_end     DATE         NOT NULL,
  schedule_color   VARCHAR2(30) NOT NULL,
  schedule_allDay  NUMBER       DEFAULT 0 NOT NULL,
  CONSTRAINT PK_schedule PRIMARY KEY (schedule_id)
);

CREATE SEQUENCE SEQ_schedule
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE schedule IS '일정';

COMMENT ON COLUMN schedule.schedule_id IS '일정코드';

COMMENT ON COLUMN schedule.emp_id IS '일정등록자';

COMMENT ON COLUMN schedule.schedule_name IS '일정명';

COMMENT ON COLUMN schedule.schedule_content IS '일정내용';

COMMENT ON COLUMN schedule.schedule_start IS '시작일시';

COMMENT ON COLUMN schedule.schedule_end IS '종료일시';

COMMENT ON COLUMN schedule.schedule_color IS '일정색상';

COMMENT ON COLUMN schedule.schedule_allDay IS '종일';

CREATE TABLE schedule_share
(
  schedule_share_id NUMBER       NOT NULL,
  share_emp         VARCHAR2(10) NOT NULL,
  schedule_id       NUMBER       NOT NULL,
  CONSTRAINT PK_schedule_share PRIMARY KEY (schedule_share_id)
);

CREATE SEQUENCE SEQ_schedule_share
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE schedule_share IS '일정공유';

COMMENT ON COLUMN schedule_share.schedule_share_id IS '일정공유 ID';

COMMENT ON COLUMN schedule_share.share_emp IS '공유직원';

COMMENT ON COLUMN schedule_share.schedule_id IS '일정코드';

CREATE TABLE sign
(
  sign_id    NUMBER       NOT NULL,
  emp_id     VARCHAR2(10) NOT NULL,
  doc_id     NUMBER       NOT NULL,
  sign_order NUMBER       NOT NULL,
  CONSTRAINT PK_sign PRIMARY KEY (sign_id)
);

CREATE SEQUENCE SEQ_sign
START WITH 1
INCREMENT BY 1;

COMMENT ON TABLE sign IS '결재';

COMMENT ON COLUMN sign.sign_id IS '결재id';

COMMENT ON COLUMN sign.emp_id IS '결재자';

COMMENT ON COLUMN sign.doc_id IS '문서번호';

COMMENT ON COLUMN sign.sign_order IS '결재순번';

CREATE TABLE task
(
  task_id            NUMBER        NOT NULL,
  emp_id             VARCHAR2(10)  NOT NULL,
  project_id         NUMBER        NOT NULL,
  task_subject       VARCHAR2(50),
  task_content       VARCHAR2(300),
  task_date          DATE        ,
  task_update_date   DATE         ,
  task_file_path     VARCHAR2(255),
  task_file_original VARCHAR2(255),
  task_file_uuid     VARCHAR2(36) ,
  task_file_type     VARCHAR2(50) ,
  CONSTRAINT PK_task PRIMARY KEY (task_id)
);

CREATE SEQUENCE SEQ_task
START WITH 1
INCREMENT BY 1;


COMMENT ON TABLE task IS '과제';

COMMENT ON COLUMN task.task_id IS '과제id';

COMMENT ON COLUMN task.emp_id IS '과제담당자';

COMMENT ON COLUMN task.project_id IS '프로젝트id';

COMMENT ON COLUMN task.task_subject IS '과제제목';

COMMENT ON COLUMN task.task_content IS '과제내용';

COMMENT ON COLUMN task.task_date IS '과제등록일';

COMMENT ON COLUMN task.task_update_date IS '과제최종변경일';

COMMENT ON COLUMN task.task_file_path IS '과제파일경로';

COMMENT ON COLUMN task.task_file_original IS '과제파일원본명';

COMMENT ON COLUMN task.task_file_uuid IS '과제파일UUID';

COMMENT ON COLUMN task.task_file_type IS '과제파일MIME';

drop table task_comment;
CREATE TABLE task_comment
(
  task_comment_id          NUMBER        NOT NULL,
  task_comment_writer      VARCHAR2(10)  NOT NULL,
  task_id                  NUMBER     references task(task_id) on delete cascade  NOT NULL,
  task_comment_content     VARCHAR2(300) NOT NULL,
  task_comment_date        DATE          NOT NULL,
  task_comment_re_ref      NUMBER        NOT NULL,
  task_comment_re_lev      NUMBER        NOT NULL,
  task_comment_re_seq      NUMBER        NOT NULL,
  CONSTRAINT PK_task_comment PRIMARY KEY (task_comment_id)
);
select * from task;
select * from task_comment
select task_comment_id from task_comment where task_comment_content = 'ab'
select * from task_comment;


drop sequence seq_task_comment

CREATE SEQUENCE SEQ_task_comment
START WITH 1
INCREMENT BY 1;



COMMENT ON TABLE task_comment IS '과제댓글';

COMMENT ON COLUMN task_comment.task_comment_id IS '댓글id';

COMMENT ON COLUMN task_comment.task_comment_writer IS '과제댓글작성자';

COMMENT ON COLUMN task_comment.task_id IS '과제id';

COMMENT ON COLUMN task_comment.task_comment_ref IS '부모댓글id';

COMMENT ON COLUMN task_comment.task_comment_content IS '댓글내용';

COMMENT ON COLUMN task_comment.task_comment_date IS '최초등록일자';

COMMENT ON COLUMN task_comment.task_comment_update_date IS '최종변경일자';

COMMENT ON COLUMN task_comment.task_comment_re_ref IS '부모댓글';

COMMENT ON COLUMN task_comment.task_comment_re_lev IS '답댓글레벨';

COMMENT ON COLUMN task_comment.task_comment_re_seq IS '댓글순서';

ALTER TABLE attend
  ADD CONSTRAINT FK_emp_TO_attend
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

ALTER TABLE emp
  ADD CONSTRAINT FK_dept_TO_emp
    FOREIGN KEY (dept_id)
    REFERENCES dept (dept_id);

ALTER TABLE board
  ADD CONSTRAINT FK_dept_TO_board
    FOREIGN KEY (dept_id)
    REFERENCES dept (dept_id);

ALTER TABLE reservation
  ADD CONSTRAINT FK_emp_TO_reservation
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

ALTER TABLE schedule_share
  ADD CONSTRAINT FK_schedule_TO_schedule_share
    FOREIGN KEY (schedule_id)
    REFERENCES schedule (schedule_id);

ALTER TABLE sign
  ADD CONSTRAINT FK_doc_TO_sign
    FOREIGN KEY (doc_id)
    REFERENCES doc (doc_id);

ALTER TABLE post_comment
  ADD CONSTRAINT FK_emp_TO_post_comment
    FOREIGN KEY (post_comment_writer)
    REFERENCES emp (emp_id);

ALTER TABLE project_member
  ADD CONSTRAINT FK_project_TO_project_member
    FOREIGN KEY (project_id)
    REFERENCES project (project_id);

ALTER TABLE task
  ADD CONSTRAINT FK_project_TO_task
    FOREIGN KEY (project_id)
    REFERENCES project (project_id);

ALTER TABLE task
  ADD CONSTRAINT FK_emp_TO_task
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

ALTER TABLE lang
  ADD CONSTRAINT FK_emp_TO_lang
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

ALTER TABLE cert
  ADD CONSTRAINT FK_emp_TO_cert
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

ALTER TABLE post
  ADD CONSTRAINT FK_emp_TO_post
    FOREIGN KEY (post_writer)
    REFERENCES emp (emp_id);

ALTER TABLE post_file
  ADD CONSTRAINT FK_post_TO_post_file
    FOREIGN KEY (post_id)
    REFERENCES post (post_id);

ALTER TABLE reservation
  ADD CONSTRAINT FK_resc_TO_reservation
    FOREIGN KEY (resource_id)
    REFERENCES resc (resc_id);

ALTER TABLE task_commnet
  ADD CONSTRAINT FK_task_TO_task_commnet
    FOREIGN KEY (task_id)
    REFERENCES task (task_id);

ALTER TABLE task_commnet
  ADD CONSTRAINT FK_emp_TO_task_commnet
    FOREIGN KEY (task_comment_writer)
    REFERENCES emp (emp_id);

ALTER TABLE project_member
  ADD CONSTRAINT FK_emp_TO_project_member
    FOREIGN KEY (member_id)
    REFERENCES emp (emp_id);

ALTER TABLE schedule
  ADD CONSTRAINT FK_emp_TO_schedule
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

ALTER TABLE schedule_share
  ADD CONSTRAINT FK_emp_TO_schedule_share
    FOREIGN KEY (share_emp)
    REFERENCES emp (emp_id);

ALTER TABLE sign
  ADD CONSTRAINT FK_emp_TO_sign
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

ALTER TABLE doc
  ADD CONSTRAINT FK_emp_TO_doc
    FOREIGN KEY (doc_writer)
    REFERENCES emp (emp_id);

ALTER TABLE post_comment
  ADD CONSTRAINT FK_post_TO_post_comment
    FOREIGN KEY (post_id)
    REFERENCES post (post_id);

ALTER TABLE emp
  ADD CONSTRAINT FK_job_TO_emp
    FOREIGN KEY (job_id)
    REFERENCES job (job_id);

ALTER TABLE doc_vacation
  ADD CONSTRAINT FK_doc_TO_doc_vacation
    FOREIGN KEY (doc_id)
    REFERENCES doc (doc_id);

ALTER TABLE doc_trip
  ADD CONSTRAINT FK_doc_TO_doc_trip
    FOREIGN KEY (doc_id)
    REFERENCES doc (doc_id);

ALTER TABLE project
  ADD CONSTRAINT FK_emp_TO_project
    FOREIGN KEY (manager_id)
    REFERENCES emp (emp_id);

ALTER TABLE post
  ADD CONSTRAINT FK_board_TO_post
    FOREIGN KEY (board_id)
    REFERENCES board (board_id);

ALTER TABLE doc_buy
  ADD CONSTRAINT FK_doc_TO_doc_buy
    FOREIGN KEY (doc_id)
    REFERENCES doc (doc_id);

ALTER TABLE buy_list
  ADD CONSTRAINT FK_doc_buy_TO_buy_list
    FOREIGN KEY (doc_buy_id)
    REFERENCES doc_buy (doc_buy_id);

CREATE OR REPLACE TRIGGER SEQ_TRG_attend
BEFORE INSERT ON attend
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_attend.NEXTVAL
  INTO :NEW.attend_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_board
BEFORE INSERT ON board
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_board.NEXTVAL
  INTO :NEW.board_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_buy_list
BEFORE INSERT ON buy_list
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_BUY_ITEM.NEXTVAL
  INTO :NEW.buy_list_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_cert
BEFORE INSERT ON cert
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_cert.NEXTVAL
  INTO :NEW.cert_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_doc
BEFORE INSERT ON doc
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc.NEXTVAL
  INTO :NEW.doc_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_doc_buy
BEFORE INSERT ON doc_buy
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc_buy.NEXTVAL
  INTO :NEW.doc_buy_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_doc_trip
BEFORE INSERT ON doc_trip
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc_trip.NEXTVAL
  INTO :NEW.doc_trip_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_doc_vacation
BEFORE INSERT ON doc_vacation
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc_vacation.NEXTVAL
  INTO :NEW.doc_vacation_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_holiday
BEFORE INSERT ON holiday
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_holiday.NEXTVAL
  INTO :NEW.holiday_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_job
BEFORE INSERT ON job
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_job.NEXTVAL
  INTO :NEW.job_id
  FROM DUAL;
END;


CREATE OR REPLACE TRIGGER SEQ_TRG_lang
BEFORE INSERT ON lang
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_lang.NEXTVAL
  INTO :NEW.lang_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_notice
BEFORE INSERT ON notice
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_notice.NEXTVAL
  INTO :NEW.notice_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_post
BEFORE INSERT ON post
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_post.NEXTVAL
  INTO :NEW.post_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_post_comment
BEFORE INSERT ON post_comment
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_post_comment.NEXTVAL
  INTO :NEW.comment_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_post_file
BEFORE INSERT ON post_file
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_post_file.NEXTVAL
  INTO :NEW.post_file_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_project
BEFORE INSERT ON project
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_project.NEXTVAL
  INTO :NEW.project_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_project_member
BEFORE INSERT ON project_member
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_project_member.NEXTVAL
  INTO :NEW.project_member_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_resc
BEFORE INSERT ON resc
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_resc.NEXTVAL
  INTO :NEW.resc_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_reservation
BEFORE INSERT ON reservation
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_reservation.NEXTVAL
  INTO :NEW.reservation_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_schedule
BEFORE INSERT ON schedule
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_schedule.NEXTVAL
  INTO :NEW.schedule_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_schedule_share
BEFORE INSERT ON schedule_share
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_schedule_share.NEXTVAL
  INTO :NEW.schedule_share_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_sign
BEFORE INSERT ON sign
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_sign.NEXTVAL
  INTO :NEW.sign_id
  FROM DUAL;
END;

CREATE OR REPLACE TRIGGER SEQ_TRG_task
BEFORE INSERT ON task
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_task.NEXTVAL
  INTO :NEW.task_id
  FROM DUAL;
END;


UPDATE POST SET POST_WRITER = EMP_ID;
ALTER TABLE POST DROP COLUMN EMP_ID;
ALTER TABLE post
  ADD CONSTRAINT FK_emp_TO_post
    FOREIGN KEY (post_writer)
    REFERENCES emp (emp_id);



select * from task_comment;
-------------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER SEQ_TRG_task_comment
BEFORE INSERT ON task_comment
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_task_comment.NEXTVAL
  INTO :NEW.task_comment_id
  FROM DUAL;
END;

DROP TRIGGER SEQ_TRG_task_comment

CREATE OR REPLACE TRIGGER SEQ_TRG_task_comment
BEFORE INSERT ON task_comment
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  -- task_comment_id와 task_comment_re_seq가 동일하게 설정되도록
  SELECT SEQ_task_comment.NEXTVAL
  INTO :NEW.task_comment_id
  FROM DUAL;

  -- task_comment_re_seq도 동일한 값으로 설정
  :NEW.task_comment_re_seq := :NEW.task_comment_id;
END;
select * from task_comment;


CREATE OR REPLACE TRIGGER SEQ_TRG_task_comment
BEFORE INSERT ON task_comment
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  -- task_comment_id는 시퀀스를 이용해 자동으로 생성
  SELECT SEQ_task_comment.NEXTVAL
  INTO :NEW.task_comment_id
  FROM DUAL;

  -- 부모 댓글이 있는 경우 대댓글 처리
  IF :NEW.task_comment_re_ref IS NOT NULL THEN
    -- 대댓글의 경우 부모 댓글의 task_comment_re_seq를 가져와서 +1을 함
    DECLARE
      v_parent_seq INT;
    BEGIN
      -- 부모 댓글의 task_comment_re_seq를 가져옴
      SELECT task_comment_re_seq
      INTO v_parent_seq
      FROM task_comment
      WHERE task_comment_id = :NEW.task_comment_re_ref;

      -- 대댓글의 task_comment_re_seq를 부모 댓글의 task_comment_re_seq + 1로 설정
      :NEW.task_comment_re_seq := v_parent_seq + 1;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        -- 부모 댓글이 없는 경우
        :NEW.task_comment_re_seq := :NEW.task_comment_id; -- 기본적으로 task_comment_id 값으로 설정
    END;
  ELSE
    -- 댓글인 경우 task_comment_re_seq는 task_comment_id와 동일하게 설정
    :NEW.task_comment_re_seq := :NEW.task_comment_id;
  END IF;

  -- task_comment_re_ref는 댓글의 원본글 번호로 설정 (대댓글인 경우 부모 댓글의 task_comment_id)
  IF :NEW.task_comment_re_ref IS NOT NULL THEN
    :NEW.task_comment_re_ref := :NEW.task_comment_re_ref;
  ELSE
    :NEW.task_comment_re_ref := :NEW.task_comment_id; -- 댓글인 경우 자신을 참조
  END IF;

END;

select * from task_comment;

-------------------------------------------------------------------------------0
INSERT INTO task_commnet (task_comment_writer, task_id, task_commnet_content, task_commnet_date, task_comment_re_lev, task_comment_re_seq)
VALUES ('241103', 213, '댓글 내용', SYSDATE, 0, 1);
SELECT * 
FROM USER_TRIGGERS
WHERE TRIGGER_NAME = 'SEQ_TRG_TASK_COMMNET';

SELECT *
FROM USER_ERRORS
WHERE NAME = 'SEQ_TRG_TASK_COMMNET'
AND TYPE = 'TRIGGER';


--JOB 데이터 입력
INSERT ALL
INTO JOB (JOB_NAME, JOB_RANK) VALUES ('대표이사', 1)
INTO JOB (JOB_NAME, JOB_RANK) VALUES ('부사장', 2)
INTO JOB (JOB_NAME, JOB_RANK) VALUES ('수석', 3)
INTO JOB (JOB_NAME, JOB_RANK) VALUES ('책임', 4)
INTO JOB (JOB_NAME, JOB_RANK) VALUES ('선임', 5)
INTO JOB (JOB_NAME, JOB_RANK) VALUES ('사원', 6)
INTO JOB (JOB_NAME, JOB_RANK) VALUES ('관리자', 99)
SELECT * FROM DUAL;

--부서 정보 데이터 입력
INSERT ALL
INTO DEPT VALUES (0000, '관리자', '')
INTO DEPT VALUES (1000, '대표이사', '')
INTO DEPT VALUES (1001, '부사장', '')
INTO DEPT VALUES (1100, '경영기획본부', '')
INTO DEPT VALUES (1110, '재무관리팀', '')
INTO DEPT VALUES (1120, '인사관리팀', '')
INTO DEPT VALUES (1200, 'SI사업본부', '')
INTO DEPT VALUES (1210, '신용평가팀', '')
INTO DEPT VALUES (1220, '금융SI팀', '')
INTO DEPT VALUES (1230, '비금융SI팀', '')
INTO DEPT VALUES (1240, 'SM팀', '')
INTO DEPT VALUES (1300, '영업본부', '')
INTO DEPT VALUES (1310, '솔루션영업팀', '')
INTO DEPT VALUES (1320, 'SI영업팀', '')
INTO DEPT VALUES (1330, 'SM영업팀', '')
INTO DEPT VALUES (1400, 'R&D본부', '')
INTO DEPT VALUES (1410, '연구개발팀', '')
SELECT *
FROM DUAL;

--사원 정보 데이터 입력
INSERT INTO emp VALUES ('admin', '1234', 'admin', 0, 7, '남', 'admin@gmail.com', '02-1234-0000', '010-1234-0000', '010-1234-0000', 'testPath-admin', 'testPath-admin', 'testUUID-admin', 'testype-admin', '1990-01-01', '신입', '1960-01-01', '음력', '서울대학교', '전공1', '국민은행', '241001-1234', '주소1', 1, 1, '정규직', 25, 1);
INSERT INTO emp VALUES ('241001', '1234', '테스트01', 1000, 1, '남', '1001@gmail.com', '02-1234-1001', '010-1234-1001', '010-1234-1001', 'testPath-241001', 'testPath-241001', 'testUUID-241001', 'testype-241001', '1991-01-01', '신입', '1961-01-01', '음력', '연세대학교', '전공2', '신한은행', '241002-1234', '주소2', 1, 1, '정규직', 23, 1);
INSERT INTO emp VALUES ('241002', '1234', '테스트02', 1001, 2, '남', '1002@gmail.com', '02-1234-1002', '010-1234-1002', '010-1234-1002', 'testPath-241002', 'testPath-241002', 'testUUID-241002', 'testype-241002', '1992-01-01', '경력', '1962-01-01', '음력', '고려대학교', '전공3', '카카오뱅크', '241101-1234', '주소3', 1, 1, '정규직', 21, 1);
INSERT INTO emp VALUES ('241101', '1234', '테스트03', 1100, 3, '남', '1103@gmail.com', '02-1234-1103', '010-1234-1103', '010-1234-1103', 'testPath-241101', 'testPath-241101', 'testUUID-241101', 'testype-241101', '1993-01-01', '신입', '1963-01-01', '음력', '서강대학교', '전공4', '하나은행', '241102-1234', '주소4', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241102', '1234', '테스트04', 1100, 5, '남', '1104@gmail.com', '02-1234-1104', '010-1234-1104', '010-1234-1104', 'testPath-241102', 'testPath-241102', 'testUUID-241102', 'testype-241102', '1994-01-01', '신입', '1964-01-01', '음력', '성균관대학교', '전공5', '우리은행', '241103-1234', '주소5', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241103', '1234', '테스트05', 1110, 4, '남', '1105@gmail.com', '02-1234-1105', '010-1234-1105', '010-1234-1105', 'testPath-241103', 'testPath-241103', 'testUUID-241103', 'testype-241103', '1995-01-01', '신입', '1965-01-01', '음력', '한양대학교', '전공6', '국민은행', '241104-1234', '주소6', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241104', '1234', '테스트06', 1110, 5, '남', '1106@gmail.com', '02-1234-1106', '010-1234-1106', '010-1234-1106', 'testPath-241104', 'testPath-241104', 'testUUID-241104', 'testype-241104', '1996-01-01', '신입', '1966-01-01', '음력', '중앙대학교', '전공7', '신한은행', '241105-1234', '주소7', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241105', '1234', '테스트07', 1110, 5, '남', '1107@gmail.com', '02-1234-1107', '010-1234-1107', '010-1234-1107', 'testPath-241105', 'testPath-241105', 'testUUID-241105', 'testype-241105', '1997-01-01', '신입', '1967-01-01', '양력', '경희대학교', '전공8', '카카오뱅크', '241106-1234', '주소8', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241106', '1234', '테스트08', 1110, 6, '남', '1108@gmail.com', '02-1234-1108', '010-1234-1108', '010-1234-1108', 'testPath-241106', 'testPath-241106', 'testUUID-241106', 'testype-241106', '1998-01-01', '경력', '1968-01-01', '양력', '한국외국어대학교', '전공9', '하나은행', '241107-1234', '주소9', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241107', '1234', '테스트09', 1110, 6, '남', '1109@gmail.com', '02-1234-1109', '010-1234-1109', '010-1234-1109', 'testPath-241107', 'testPath-241107', 'testUUID-241107', 'testype-241107', '1999-01-01', '신입', '1969-01-01', '양력', '서울시립대학교', '전공10', '우리은행', '241108-1234', '주소10', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241108', '1234', '테스트10', 1120, 4, '남', '1110@gmail.com', '02-1234-1110', '010-1234-1110', '010-1234-1110', 'testPath-241108', 'testPath-241108', 'testUUID-241108', 'testype-241108', '2000-01-01', '신입', '1970-01-01', '양력', '건국대학교', '전공11', '국민은행', '241109-1234', '주소11', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241109', '1234', '테스트11', 1120, 5, '남', '1111@gmail.com', '02-1234-1111', '010-1234-1111', '010-1234-1111', 'testPath-241109', 'testPath-241109', 'testUUID-241109', 'testype-241109', '2001-01-01', '신입', '1971-01-01', '양력', '동국대학교', '전공12', '신한은행', '241110-1234', '주소12', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241110', '1234', '테스트12', 1120, 5, '남', '1112@gmail.com', '02-1234-1112', '010-1234-1112', '010-1234-1112', 'testPath-241110', 'testPath-241110', 'testUUID-241110', 'testype-241110', '2002-01-01', '신입', '1972-01-01', '양력', '홍익대학교', '전공13', '카카오뱅크', '241111-1234', '주소13', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241111', '1234', '테스트13', 1120, 6, '남', '1113@gmail.com', '02-1234-1113', '010-1234-1113', '010-1234-1113', 'testPath-241111', 'testPath-241111', 'testUUID-241111', 'testype-241111', '2003-01-01', '경력', '1973-01-01', '양력', '서울대학교', '전공14', '하나은행', '241112-1234', '주소14', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241112', '1234', '테스트14', 1120, 6, '남', '1114@gmail.com', '02-1234-1114', '010-1234-1114', '010-1234-1114', 'testPath-241112', 'testPath-241112', 'testUUID-241112', 'testype-241112', '2004-01-01', '신입', '1974-01-01', '양력', '연세대학교', '전공15', '우리은행', '241201-1234', '주소15', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241201', '1234', '테스트15', 1200, 3, '남', '1215@gmail.com', '02-1234-1215', '010-1234-1215', '010-1234-1215', 'testPath-241201', 'testPath-241201', 'testUUID-241201', 'testype-241201', '2005-01-01', '신입', '1975-01-01', '양력', '고려대학교', '전공16', '국민은행', '241202-1234', '주소16', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241202', '1234', '테스트16', 1200, 5, '남', '1216@gmail.com', '02-1234-1216', '010-1234-1216', '010-1234-1216', 'testPath-241202', 'testPath-241202', 'testUUID-241202', 'testype-241202', '2006-01-01', '신입', '1976-01-01', '양력', '서강대학교', '전공17', '신한은행', '241203-1234', '주소17', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241203', '1234', '테스트17', 1210, 4, '남', '1217@gmail.com', '02-1234-1217', '010-1234-1217', '010-1234-1217', 'testPath-241203', 'testPath-241203', 'testUUID-241203', 'testype-241203', '2007-01-01', '신입', '1977-01-01', '양력', '성균관대학교', '전공18', '카카오뱅크', '241204-1234', '주소18', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241204', '1234', '테스트18', 1210, 5, '남', '1218@gmail.com', '02-1234-1218', '010-1234-1218', '010-1234-1218', 'testPath-241204', 'testPath-241204', 'testUUID-241204', 'testype-241204', '2008-01-01', '경력', '1978-01-01', '양력', '한양대학교', '전공19', '하나은행', '241205-1234', '주소19', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241205', '1234', '테스트19', 1210, 5, '남', '1219@gmail.com', '02-1234-1219', '010-1234-1219', '010-1234-1219', 'testPath-241205', 'testPath-241205', 'testUUID-241205', 'testype-241205', '2009-01-01', '신입', '1979-01-01', '양력', '중앙대학교', '전공20', '우리은행', '241206-1234', '주소20', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241206', '1234', '테스트20', 1210, 6, '남', '1220@gmail.com', '02-1234-1220', '010-1234-1220', '010-1234-1220', 'testPath-241206', 'testPath-241206', 'testUUID-241206', 'testype-241206', '2010-01-01', '신입', '1980-01-01', '양력', '경희대학교', '전공21', '국민은행', '241207-1234', '주소21', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241207', '1234', '테스트21', 1210, 6, '남', '1221@gmail.com', '02-1234-1221', '010-1234-1221', '010-1234-1221', 'testPath-241207', 'testPath-241207', 'testUUID-241207', 'testype-241207', '2011-01-01', '신입', '1981-01-01', '양력', '한국외국어대학교', '전공22', '신한은행', '241208-1234', '주소22', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241208', '1234', '테스트22', 1220, 4, '남', '1222@gmail.com', '02-1234-1222', '010-1234-1222', '010-1234-1222', 'testPath-241208', 'testPath-241208', 'testUUID-241208', 'testype-241208', '2012-01-01', '신입', '1982-01-01', '양력', '서울시립대학교', '전공23', '카카오뱅크', '241209-1234', '주소23', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241209', '1234', '테스트23', 1220, 5, '남', '1223@gmail.com', '02-1234-1223', '010-1234-1223', '010-1234-1223', 'testPath-241209', 'testPath-241209', 'testUUID-241209', 'testype-241209', '2013-01-01', '경력', '1983-01-01', '양력', '건국대학교', '전공24', '하나은행', '241210-1234', '주소24', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241210', '1234', '테스트24', 1220, 5, '남', '1224@gmail.com', '02-1234-1224', '010-1234-1224', '010-1234-1224', 'testPath-241210', 'testPath-241210', 'testUUID-241210', 'testype-241210', '2014-01-01', '신입', '1984-01-01', '양력', '동국대학교', '전공25', '우리은행', '241211-1234', '주소25', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241211', '1234', '테스트25', 1220, 6, '남', '1225@gmail.com', '02-1234-1225', '010-1234-1225', '010-1234-1225', 'testPath-241211', 'testPath-241211', 'testUUID-241211', 'testype-241211', '2015-01-01', '신입', '1985-01-01', '양력', '홍익대학교', '전공26', '국민은행', '241212-1234', '주소26', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241212', '1234', '테스트26', 1220, 6, '남', '1226@gmail.com', '02-1234-1226', '010-1234-1226', '010-1234-1226', 'testPath-241212', 'testPath-241212', 'testUUID-241212', 'testype-241212', '2016-01-01', '신입', '1986-01-01', '양력', '서울대학교', '전공27', '신한은행', '241213-1234', '주소27', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241213', '1234', '테스트27', 1230, 4, '남', '1227@gmail.com', '02-1234-1227', '010-1234-1227', '010-1234-1227', 'testPath-241213', 'testPath-241213', 'testUUID-241213', 'testype-241213', '2017-01-01', '신입', '1987-01-01', '양력', '연세대학교', '전공28', '카카오뱅크', '241214-1234', '주소28', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241214', '1234', '테스트28', 1230, 5, '남', '1228@gmail.com', '02-1234-1228', '010-1234-1228', '010-1234-1228', 'testPath-241214', 'testPath-241214', 'testUUID-241214', 'testype-241214', '2018-01-01', '경력', '1988-01-01', '양력', '고려대학교', '전공29', '하나은행', '241215-1234', '주소29', 1, 1, '계약직', 20, 1);
INSERT INTO emp VALUES ('241215', '1234', '테스트29', 1230, 5, '남', '1229@gmail.com', '02-1234-1229', '010-1234-1229', '010-1234-1229', 'testPath-241215', 'testPath-241215', 'testUUID-241215', 'testype-241215', '2019-01-01', '신입', '1989-01-01', '양력', '서강대학교', '전공30', '우리은행', '241216-1234', '주소30', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241216', '1234', '테스트30', 1230, 6, '남', '1230@gmail.com', '02-1234-1230', '010-1234-1230', '010-1234-1230', 'testPath-241216', 'testPath-241216', 'testUUID-241216', 'testype-241216', '2020-01-01', '신입', '1990-01-01', '양력', '성균관대학교', '전공31', '국민은행', '241217-1234', '주소31', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241217', '1234', '테스트31', 1230, 6, '남', '1231@gmail.com', '02-1234-1231', '010-1234-1231', '010-1234-1231', 'testPath-241217', 'testPath-241217', 'testUUID-241217', 'testype-241217', '2021-01-01', '신입', '1991-01-01', '양력', '한양대학교', '전공32', '신한은행', '241218-1234', '주소32', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241218', '1234', '테스트32', 1240, 4, '남', '1232@gmail.com', '02-1234-1232', '010-1234-1232', '010-1234-1232', 'testPath-241218', 'testPath-241218', 'testUUID-241218', 'testype-241218', '2022-01-01', '신입', '1992-01-01', '양력', '중앙대학교', '전공33', '카카오뱅크', '241219-1234', '주소33', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241219', '1234', '테스트33', 1240, 5, '남', '1233@gmail.com', '02-1234-1233', '010-1234-1233', '010-1234-1233', 'testPath-241219', 'testPath-241219', 'testUUID-241219', 'testype-241219', '2023-01-01', '경력', '1993-01-01', '양력', '경희대학교', '전공34', '하나은행', '241220-1234', '주소34', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241220', '1234', '테스트34', 1240, 5, '남', '1234@gmail.com', '02-1234-1234', '010-1234-1234', '010-1234-1234', 'testPath-241220', 'testPath-241220', 'testUUID-241220', 'testype-241220', '2024-01-01', '신입', '1994-01-01', '양력', '한국외국어대학교', '전공35', '우리은행', '241221-1234', '주소35', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241221', '1234', '테스트35', 1240, 6, '남', '1235@gmail.com', '02-1234-1235', '010-1234-1235', '010-1234-1235', 'testPath-241221', 'testPath-241221', 'testUUID-241221', 'testype-241221', '2025-01-01', '신입', '1995-01-01', '양력', '서울시립대학교', '전공36', '국민은행', '241222-1234', '주소36', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241222', '1234', '테스트36', 1240, 6, '남', '1236@gmail.com', '02-1234-1236', '010-1234-1236', '010-1234-1236', 'testPath-241222', 'testPath-241222', 'testUUID-241222', 'testype-241222', '2026-01-01', '신입', '1996-01-01', '양력', '건국대학교', '전공37', '신한은행', '241301-1234', '주소37', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241301', '1234', '테스트37', 1300, 3, '남', '1337@gmail.com', '02-1234-1337', '010-1234-1337', '010-1234-1337', 'testPath-241301', 'testPath-241301', 'testUUID-241301', 'testype-241301', '2027-01-01', '신입', '1997-01-01', '양력', '동국대학교', '전공38', '카카오뱅크', '241302-1234', '주소38', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241302', '1234', '테스트38', 1300, 5, '남', '1338@gmail.com', '02-1234-1338', '010-1234-1338', '010-1234-1338', 'testPath-241302', 'testPath-241302', 'testUUID-241302', 'testype-241302', '2028-01-01', '경력', '1998-01-01', '양력', '홍익대학교', '전공39', '하나은행', '241303-1234', '주소39', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241303', '1234', '테스트39', 1310, 4, '남', '1339@gmail.com', '02-1234-1339', '010-1234-1339', '010-1234-1339', 'testPath-241303', 'testPath-241303', 'testUUID-241303', 'testype-241303', '2029-01-01', '신입', '1999-01-01', '양력', '서울대학교', '전공40', '우리은행', '241304-1234', '주소40', 1, 1, '계약직', 20, 1);
INSERT INTO emp VALUES ('241304', '1234', '테스트40', 1310, 5, '남', '1340@gmail.com', '02-1234-1340', '010-1234-1340', '010-1234-1340', 'testPath-241304', 'testPath-241304', 'testUUID-241304', 'testype-241304', '2030-01-01', '신입', '2000-01-01', '양력', '연세대학교', '전공41', '국민은행', '241305-1234', '주소41', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241305', '1234', '테스트41', 1310, 5, '남', '1341@gmail.com', '02-1234-1341', '010-1234-1341', '010-1234-1341', 'testPath-241305', 'testPath-241305', 'testUUID-241305', 'testype-241305', '2031-01-01', '신입', '2001-01-01', '양력', '고려대학교', '전공42', '신한은행', '241306-1234', '주소42', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241306', '1234', '테스트42', 1310, 6, '남', '1342@gmail.com', '02-1234-1342', '010-1234-1342', '010-1234-1342', 'testPath-241306', 'testPath-241306', 'testUUID-241306', 'testype-241306', '2032-01-01', '신입', '2002-01-01', '양력', '서강대학교', '전공43', '카카오뱅크', '241307-1234', '주소43', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241307', '1234', '테스트43', 1310, 6, '남', '1343@gmail.com', '02-1234-1343', '010-1234-1343', '010-1234-1343', 'testPath-241307', 'testPath-241307', 'testUUID-241307', 'testype-241307', '2033-01-01', '경력', '2003-01-01', '양력', '성균관대학교', '전공44', '하나은행', '241308-1234', '주소44', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241308', '1234', '테스트44', 1320, 4, '남', '1344@gmail.com', '02-1234-1344', '010-1234-1344', '010-1234-1344', 'testPath-241308', 'testPath-241308', 'testUUID-241308', 'testype-241308', '2034-01-01', '신입', '2004-01-01', '양력', '한양대학교', '전공45', '우리은행', '241309-1234', '주소45', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241309', '1234', '테스트45', 1320, 5, '남', '1345@gmail.com', '02-1234-1345', '010-1234-1345', '010-1234-1345', 'testPath-241309', 'testPath-241309', 'testUUID-241309', 'testype-241309', '2035-01-01', '신입', '2005-01-01', '양력', '중앙대학교', '전공46', '국민은행', '241310-1234', '주소46', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241310', '1234', '테스트46', 1320, 5, '남', '1346@gmail.com', '02-1234-1346', '010-1234-1346', '010-1234-1346', 'testPath-241310', 'testPath-241310', 'testUUID-241310', 'testype-241310', '2036-01-01', '신입', '2006-01-01', '양력', '경희대학교', '전공47', '신한은행', '241311-1234', '주소47', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241311', '1234', '테스트47', 1320, 6, '남', '1347@gmail.com', '02-1234-1347', '010-1234-1347', '010-1234-1347', 'testPath-241311', 'testPath-241311', 'testUUID-241311', 'testype-241311', '2037-01-01', '신입', '2007-01-01', '양력', '한국외국어대학교', '전공48', '카카오뱅크', '241312-1234', '주소48', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241312', '1234', '테스트48', 1320, 6, '남', '1348@gmail.com', '02-1234-1348', '010-1234-1348', '010-1234-1348', 'testPath-241312', 'testPath-241312', 'testUUID-241312', 'testype-241312', '2038-01-01', '경력', '2008-01-01', '양력', '서울시립대학교', '전공49', '하나은행', '241313-1234', '주소49', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241313', '1234', '테스트49', 1330, 4, '남', '1349@gmail.com', '02-1234-1349', '010-1234-1349', '010-1234-1349', 'testPath-241313', 'testPath-241313', 'testUUID-241313', 'testype-241313', '2039-01-01', '신입', '2009-01-01', '양력', '건국대학교', '전공50', '우리은행', '241314-1234', '주소50', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241314', '1234', '테스트50', 1330, 5, '남', '1350@gmail.com', '02-1234-1350', '010-1234-1350', '010-1234-1350', 'testPath-241314', 'testPath-241314', 'testUUID-241314', 'testype-241314', '2040-01-01', '신입', '2010-01-01', '양력', '동국대학교', '전공51', '국민은행', '241315-1234', '주소51', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241315', '1234', '테스트51', 1330, 5, '남', '1351@gmail.com', '02-1234-1351', '010-1234-1351', '010-1234-1351', 'testPath-241315', 'testPath-241315', 'testUUID-241315', 'testype-241315', '2041-01-01', '신입', '2011-01-01', '양력', '홍익대학교', '전공52', '신한은행', '241316-1234', '주소52', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241316', '1234', '테스트52', 1330, 6, '남', '1352@gmail.com', '02-1234-1352', '010-1234-1352', '010-1234-1352', 'testPath-241316', 'testPath-241316', 'testUUID-241316', 'testype-241316', '2042-01-01', '신입', '2012-01-01', '양력', '서울대학교', '전공53', '카카오뱅크', '241317-1234', '주소53', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241317', '1234', '테스트53', 1330, 6, '남', '1353@gmail.com', '02-1234-1353', '010-1234-1353', '010-1234-1353', 'testPath-241317', 'testPath-241317', 'testUUID-241317', 'testype-241317', '2043-01-01', '경력', '2013-01-01', '양력', '연세대학교', '전공54', '하나은행', '241401-1234', '주소54', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241401', '1234', '테스트54', 1400, 3, '남', '1454@gmail.com', '02-1234-1454', '010-1234-1454', '010-1234-1454', 'testPath-241401', 'testPath-241401', 'testUUID-241401', 'testype-241401', '2044-01-01', '신입', '2014-01-01', '양력', '고려대학교', '전공55', '우리은행', '241402-1234', '주소55', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241402', '1234', '테스트55', 1400, 5, '남', '1455@gmail.com', '02-1234-1455', '010-1234-1455', '010-1234-1455', 'testPath-241402', 'testPath-241402', 'testUUID-241402', 'testype-241402', '2045-01-01', '신입', '2015-01-01', '양력', '서강대학교', '전공56', '국민은행', '241403-1234', '주소56', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241403', '1234', '테스트56', 1410, 4, '남', '1456@gmail.com', '02-1234-1456', '010-1234-1456', '010-1234-1456', 'testPath-241403', 'testPath-241403', 'testUUID-241403', 'testype-241403', '2046-01-01', '신입', '2016-01-01', '양력', '성균관대학교', '전공57', '신한은행', '241404-1234', '주소57', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241404', '1234', '테스트57', 1410, 5, '남', '1457@gmail.com', '02-1234-1457', '010-1234-1457', '010-1234-1457', 'testPath-241404', 'testPath-241404', 'testUUID-241404', 'testype-241404', '2047-01-01', '신입', '2017-01-01', '양력', '한양대학교', '전공58', '카카오뱅크', '241405-1234', '주소58', 0, 0, '정규직', 20, 1);
INSERT INTO emp VALUES ('241405', '1234', '테스트58', 1410, 5, '남', '1458@gmail.com', '02-1234-1458', '010-1234-1458', '010-1234-1458', 'testPath-241405', 'testPath-241405', 'testUUID-241405', 'testype-241405', '2048-01-01', '경력', '2018-01-01', '양력', '중앙대학교', '전공59', '하나은행', '241406-1234', '주소59', 1, 1, '정규직', 20, 1);
INSERT INTO emp VALUES ('241406', '1234', '테스트59', 1410, 6, '남', '1459@gmail.com', '02-1234-1459', '010-1234-1459', '010-1234-1459', 'testPath-241406', 'testPath-241406', 'testUUID-241406', 'testype-241406', '2049-01-01', '신입', '2019-01-01', '양력', '경희대학교', '전공60', '우리은행', '-', '주소60', 1, 1, '정규직', 20, 1);

--부서장 설정
UPDATE DEPT SET DEPT_MANAGER = '241001'
WHERE DEPT_ID = 1000;
UPDATE DEPT SET DEPT_MANAGER = '241002'
WHERE DEPT_ID = 1001;
UPDATE DEPT SET DEPT_MANAGER = '241101'
WHERE DEPT_ID = 1100;
UPDATE DEPT SET DEPT_MANAGER = '241103'
WHERE DEPT_ID = 1110;
UPDATE DEPT SET DEPT_MANAGER = '241108'
WHERE DEPT_ID = 1120;
UPDATE DEPT SET DEPT_MANAGER = '241201'
WHERE DEPT_ID = 1200;
UPDATE DEPT SET DEPT_MANAGER = '241203'
WHERE DEPT_ID = 1210;
UPDATE DEPT SET DEPT_MANAGER = '241208'
WHERE DEPT_ID = 1220;
UPDATE DEPT SET DEPT_MANAGER = '241213'
WHERE DEPT_ID = 1230;
UPDATE DEPT SET DEPT_MANAGER = '241218'
WHERE DEPT_ID = 1240;
UPDATE DEPT SET DEPT_MANAGER = '241301'
WHERE DEPT_ID = 1300;
UPDATE DEPT SET DEPT_MANAGER = '241303'
WHERE DEPT_ID = 1310;
UPDATE DEPT SET DEPT_MANAGER = '241308'
WHERE DEPT_ID = 1320;
UPDATE DEPT SET DEPT_MANAGER = '241313'
WHERE DEPT_ID = 1330;
UPDATE DEPT SET DEPT_MANAGER = '241401'
WHERE DEPT_ID = 1400;
UPDATE DEPT SET DEPT_MANAGER = '241403'
WHERE DEPT_ID = 1410;

--비밀번호 1234로 통일

UPDATE EMP SET PASSWORD = '0e326151934fbaf780f2e3860cde8ff2c5c3fd51befc56243118c5aac52b71c2edc60'
WHERE 1=1;


