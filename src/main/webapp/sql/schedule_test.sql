select * from schedule;

select * from schedule_share;

/*
 * emp_id가 241001인 특정 사용자가 등록한 일정 중에서, 
 * share_emp가 241101 또는 241102에게 공유된 일정만 조회
 * */
/*
 * 조회할 때 배열값 필요없음
 * 
select * 
from schedule_share a 
join SCHEDULE s 
on a.schedule_id = s.schedule_id;

SELECT *
FROM schedule a
JOIN schedule_share ss ON a.schedule_id = ss.schedule_id
WHERE a.emp_id = 241001 AND ss.share_emp IN (241101, 241102);
*/

SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = 241001 OR ss.share_emp = 241101;



SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       MIN(ss.share_emp) AS share_emp -- 중복 제거
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = 241001 OR ss.share_emp = 241101
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay;


SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = 241001 OR ss.share_emp = 241102;

SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = 241001;


SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE ss.share_emp = 241101;

SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE ss.share_emp = 241102;

SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = 241101 OR ss.share_emp = 241101;


SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE ss.share_emp = 241102;

SELECT *
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = 241102 OR ss.share_emp = 241102;



TRUNCATE TABLE schedule;
TRUNCATE TABLE schedule_share;


select * from emp;


/* 자원  */
SELECT * 
FROM resc;
         
SELECT resc_type
FROM resc;

SELECT resc_name
FROM resc 
WHERE resc_type = '회의실' AND resc_usable = '1';

SELECT resc_name
FROM resc 
WHERE resc_type = '차량' AND resc_usable = '1';


SELECT *
FROM resc 
WHERE resc_type = '차량' AND resc_usable = '1';


SELECT resc_type, resc_name
FROM resc
WHERE resc_type = '회의실' AND resc_usable = '1'
GROUP BY resc_name, resc_type;

SELECT resc_type, resc_name
FROM resc
WHERE resc_type = '차량' AND resc_usable = '1'
GROUP BY resc_name, resc_type;

SELECT MIN(resc_id) AS resc_id, resc_type, resc_name, resc_usable
FROM resc
WHERE resc_type = '차량' AND resc_usable = '1'
GROUP BY resc_type, resc_name, resc_usable;

/* resc_name에 해당하는 resc_id 값을 가져오기  */
SELECT MIN(resc_id) AS resc_id, resc_name
FROM resc
WHERE resc_type = '차량' AND resc_usable = '1'
GROUP BY resc_name;


SELECT resc_name 
FROM resc 
WHERE resc_id = 1;

ALTER TABLE reservation
ADD resc_name VARCHAR2(50);

INSERT INTO reservation
(reservation_id, resource_id, emp_id, reservation_start, reservation_end, reservation_info, reservation_allDay, resc_name)
VALUES (SEQ_reservation.NEXTVAL, 1, '241001', TO_DATE('2024-11-18 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
TO_DATE('2024-11-18 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), '회의실 예약', 0, 
(SELECT resc_name 
 FROM resc 
 WHERE resc_id = 1));



SELECT * 
FROM reservation;

TRUNCATE TABLE reservation;

/*
ALTER TABLE reservation
ADD resc_name VARCHAR2(50);

INSERT INTO reservation
(reservation_id, resource_id, emp_id, reservation_start, reservation_end, reservation_info, reservation_allDay, resc_name)
VALUES (SEQ_reservation.NEXTVAL, ?, ?, ?, ?, ?, ?, 
(SELECT resc_name FROM resc WHERE resc_id = ?));

*/


/*
INSERT INTO reservation
(reservation_id, emp_id, reservation_start, reservation_end, reservation_info, reservation_allDay)
VALUES (SEQ_reservation.NEXTVAL, ?, ?, ?, ?, ?);
*/


/*
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
*/


/*
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

CREATE TABLE schedule_share
(
  schedule_share_id NUMBER       NOT NULL,
  share_emp         VARCHAR2(10) NOT NULL,
  schedule_id       NUMBER       NOT NULL,
  CONSTRAINT PK_schedule_share PRIMARY KEY (schedule_share_id)
);
*/