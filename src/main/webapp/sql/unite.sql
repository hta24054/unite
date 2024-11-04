


CREATE TABLE attend
(
  attend_id   NUMBER       NOT NULL,
  emp_id      VARCHAR2(10) NOT NULL,
  attend_date DATE         NOT NULL,
  attend_in   DATE         NOT NULL,
  attend_out  DATE         NOT NULL,
  attend_type VARCHAR2(15) NOT NULL,
  CONSTRAINT PK_attend PRIMARY KEY (attend_id)
);

CREATE SEQUENCE SEQ_attend
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER SEQ_TRG_attend
BEFORE INSERT ON attend
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_attend.NEXTVAL
  INTO: NEW.attend_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_board
BEFORE INSERT ON board
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_board.NEXTVAL
  INTO: NEW.board_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_buy_list
BEFORE INSERT ON buy_list
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_buy_list.NEXTVAL
  INTO: NEW.buy_list_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_cert
BEFORE INSERT ON cert
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_cert.NEXTVAL
  INTO: NEW.cert_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_doc
BEFORE INSERT ON doc
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc.NEXTVAL
  INTO: NEW.doc_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_doc_buy
BEFORE INSERT ON doc_buy
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc_buy.NEXTVAL
  INTO: NEW.doc_buy_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_doc_trip
BEFORE INSERT ON doc_trip
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc_trip.NEXTVAL
  INTO: NEW.doc_trip_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_doc_vacation
BEFORE INSERT ON doc_vacation
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_doc_vacation.NEXTVAL
  INTO: NEW.doc_vacation_id
  FROM DUAL;
END;

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

COMMENT ON COLUMN emp.img_path IS '이미지경로';

COMMENT ON COLUMN emp.img_original IS '이미지원본명';

COMMENT ON COLUMN emp.img_uuid IS '이미지UUID';

COMMENT ON COLUMN emp.img_type IS '이미지MIME';

COMMENT ON COLUMN emp.hired IS '재직여부';

CREATE TABLE emp_info
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

COMMENT ON TABLE emp_info IS '인사정보';

COMMENT ON COLUMN emp_info.emp_id IS '사번';

COMMENT ON COLUMN emp_info.mobile2 IS '긴급연락처';

COMMENT ON COLUMN emp_info.hiredate IS '입사일';

COMMENT ON COLUMN emp_info.hiretype IS '채용구분';

COMMENT ON COLUMN emp_info.birthday IS '생년월일';

COMMENT ON COLUMN emp_info.school IS '최종학력';

COMMENT ON COLUMN emp_info.major IS '전공';

COMMENT ON COLUMN emp_info.bank IS '은행';

COMMENT ON COLUMN emp_info.account IS '계좌번호';

COMMENT ON COLUMN emp_info.address IS '주소';

COMMENT ON COLUMN emp_info.married IS '혼인여부';

COMMENT ON COLUMN emp_info.child IS '자녀유무';

COMMENT ON COLUMN emp_info.etype IS '직원구분';

COMMENT ON COLUMN emp_info.vacation_count IS '연차갯수';

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

CREATE OR REPLACE TRIGGER SEQ_TRG_holiday
BEFORE INSERT ON holiday
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_holiday.NEXTVAL
  INTO: NEW.holiday_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_job
BEFORE INSERT ON job
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_job.NEXTVAL
  INTO: NEW.job_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_lang
BEFORE INSERT ON lang
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_lang.NEXTVAL
  INTO: NEW.lang_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_notice
BEFORE INSERT ON notice
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_notice.NEXTVAL
  INTO: NEW.notice_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_post
BEFORE INSERT ON post
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_post.NEXTVAL
  INTO: NEW.post_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_post_comment
BEFORE INSERT ON post_comment
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_post_comment.NEXTVAL
  INTO: NEW.comment_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_post_file
BEFORE INSERT ON post_file
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_post_file.NEXTVAL
  INTO: NEW.post_file_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_project
BEFORE INSERT ON project
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_project.NEXTVAL
  INTO: NEW.project_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_project_member
BEFORE INSERT ON project_member
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_project_member.NEXTVAL
  INTO: NEW.project_member_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_resc
BEFORE INSERT ON resc
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_resc.NEXTVAL
  INTO: NEW.resc_id
  FROM DUAL;
END;

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
  CONSTRAINT PK_reservation PRIMARY KEY (reservation_id)
);

CREATE SEQUENCE SEQ_reservation
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER SEQ_TRG_reservation
BEFORE INSERT ON reservation
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_reservation.NEXTVAL
  INTO: NEW.reservation_id
  FROM DUAL;
END;

COMMENT ON TABLE reservation IS '자원예약';

COMMENT ON COLUMN reservation.reservation_id IS '자원예약id';

COMMENT ON COLUMN reservation.resource_id IS '자원id';

COMMENT ON COLUMN reservation.emp_id IS '예약자';

COMMENT ON COLUMN reservation.reservation_start IS '예약시작시각';

COMMENT ON COLUMN reservation.reservation_end IS '예약종료시각';

COMMENT ON COLUMN reservation.reservation_info IS '예약내용';

CREATE TABLE schedule
(
  schedule_id      NUMBER       NOT NULL,
  emp_id           VARCHAR2(10) NOT NULL,
  schedule_name    VARCHAR2(50) NOT NULL,
  schedule_content VARCHAR(255),
  schedule_start   DATE         NOT NULL,
  schedule_end     DATE         NOT NULL,
  schedule_color   VARCHAR2(30) NOT NULL,
  CONSTRAINT PK_schedule PRIMARY KEY (schedule_id)
);

CREATE SEQUENCE SEQ_schedule
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER SEQ_TRG_schedule
BEFORE INSERT ON schedule
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_schedule.NEXTVAL
  INTO: NEW.schedule_id
  FROM DUAL;
END;

COMMENT ON TABLE schedule IS '일정';

COMMENT ON COLUMN schedule.schedule_id IS '일정코드';

COMMENT ON COLUMN schedule.emp_id IS '일정등록자';

COMMENT ON COLUMN schedule.schedule_name IS '일정명';

COMMENT ON COLUMN schedule.schedule_content IS '일정내용';

COMMENT ON COLUMN schedule.schedule_start IS '시작일시';

COMMENT ON COLUMN schedule.schedule_end IS '종료일시';

COMMENT ON COLUMN schedule.schedule_color IS '일정색상';

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

CREATE OR REPLACE TRIGGER SEQ_TRG_schedule_share
BEFORE INSERT ON schedule_share
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_schedule_share.NEXTVAL
  INTO: NEW.schedule_share_id
  FROM DUAL;
END;

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

CREATE OR REPLACE TRIGGER SEQ_TRG_sign
BEFORE INSERT ON sign
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_sign.NEXTVAL
  INTO: NEW.sign_id
  FROM DUAL;
END;

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
  task_subject       VARCHAR2(50)  NOT NULL,
  task_content       VARCHAR2(300),
  task_date          DATE          NOT NULL,
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

CREATE OR REPLACE TRIGGER SEQ_TRG_task
BEFORE INSERT ON task
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_task.NEXTVAL
  INTO: NEW.task_id
  FROM DUAL;
END;

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

CREATE TABLE task_commnet
(
  task_commnet_id          NUMBER        NOT NULL,
  task_comment_writer      VARCHAR2(10)  NOT NULL,
  task_id                  NUMBER        NOT NULL,
  task_commnet_ref         NUMBER       ,
  task_commnet_content     VARCHAR2(300) NOT NULL,
  task_commnet_date        DATE          NOT NULL,
  task_commnet_update_date DATE         ,
  task_comment_re_ref      NUMBER        NOT NULL,
  task_comment_re_lev      NUMBER        NOT NULL,
  task_comment_re_seq      NUMBER        NOT NULL,
  CONSTRAINT PK_task_commnet PRIMARY KEY (task_commnet_id)
);

CREATE SEQUENCE SEQ_task_commnet
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER SEQ_TRG_task_commnet
BEFORE INSERT ON task_commnet
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
  SELECT SEQ_task_commnet.NEXTVAL
  INTO: NEW.task_commnet_id
  FROM DUAL;
END;

COMMENT ON TABLE task_commnet IS '과제댓글';

COMMENT ON COLUMN task_commnet.task_commnet_id IS '댓글id';

COMMENT ON COLUMN task_commnet.task_comment_writer IS '과제댓글작성자';

COMMENT ON COLUMN task_commnet.task_id IS '과제id';

COMMENT ON COLUMN task_commnet.task_commnet_ref IS '부모댓글id';

COMMENT ON COLUMN task_commnet.task_commnet_content IS '댓글내용';

COMMENT ON COLUMN task_commnet.task_commnet_date IS '최초등록일자';

COMMENT ON COLUMN task_commnet.task_commnet_update_date IS '최종변경일자';

COMMENT ON COLUMN task_commnet.task_comment_re_ref IS '부모댓글';

COMMENT ON COLUMN task_commnet.task_comment_re_lev IS '답댓글레벨';

COMMENT ON COLUMN task_commnet.task_comment_re_seq IS '댓글순서';

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

ALTER TABLE emp_info
  ADD CONSTRAINT FK_emp_TO_emp_info
    FOREIGN KEY (emp_id)
    REFERENCES emp (emp_id);

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