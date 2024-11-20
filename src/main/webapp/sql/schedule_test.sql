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




SELECT reservation_start, reservation_end
FROM reservation
WHERE emp_id = '241001';

SELECT resc_type, resc_name
FROM resc
WHERE resc_id = 1;



SELECT r.reservation_start, r.reservation_end, 
       rs.resc_type, rs.resc_name
FROM reservation r
JOIN resc rs ON r.resource_id = rs.resc_id
WHERE r.emp_id = '241001' AND rs.resc_id = 1;

/* 나의 예약목록 */
SELECT reservation.reservation_start, reservation.reservation_end, 
       rs.resc_type, rs.resc_name
FROM reservation reservation
JOIN resc rs 
ON reservation.resource_id = rs.resc_id
WHERE reservation.emp_id = '241001' 
AND reservation.reservation_id = 50;

SELECT reservation.reservation_id
FROM reservation
LEFT JOIN resc
ON reservation.resource_id = resc.resc_id;




SELECT reservation.reservation_id
				FROM reservation
				INNER JOIN resc
				ON reservation.resource_id = resc.resc_id
				WHERE reservation.reservation_id = 50

				
SELECT rs.resc_type, rs.resc_name, 
	   reservation.reservation_start, reservation.reservation_end
FROM reservation reservation
JOIN resc rs 
ON reservation.resource_id = rs.resc_id
WHERE reservation.emp_id = '241001'
AND reservation.reservation_id = 50;
				






SELECT * from reservation;		

/* 나의 자원 예약목록 + 이름 불러오기 */
SELECT rs.resc_type, rs.resc_name, (select ename from emp where emp_id  = '241001') ename,
	   reservation.reservation_start, reservation.reservation_end
FROM reservation reservation
JOIN resc rs 
ON reservation.resource_id = rs.resc_id
WHERE reservation.emp_id = '241001';
				


SELECT 
    resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable, 
    reservation.reservation_id,
    reservation.emp_id,
    reservation.reservation_start,
    reservation.reservation_end,
    reservation.reservation_info,
    reservation.reservation_allDay
FROM reservation
LEFT JOIN resc resc
ON reservation.resource_id = resc.resc_id
WHERE reservation.reservation_id = 51 AND reservation.emp_id = '241001';



SELECT 
    resc.resc_id, 
    resc.resc_type, 
    resc.resc_name, 
    resc.resc_info, 
    resc.resc_usable,
    reservation.reservation_id,
    reservation.emp_id,
    reservation.reservation_start,
    reservation.reservation_end,
    reservation.reservation_info,
    reservation.reservation_allDay
FROM reservation
LEFT JOIN resc resc
ON reservation.resource_id = resc.resc_id
WHERE reservation.reservation_id = 1;

SELECT e.ename
FROM emp e
JOIN reservation r ON e.emp_id = r.emp_id;

SELECT 
    resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable,
    reservation.reservation_id, reservation.emp_id, reservation.reservation_start,
    reservation.reservation_end, reservation.reservation_info, reservation.reservation_allDay,
    emp.ename AS emp_name
FROM reservation
LEFT JOIN resc resc ON reservation.resource_id = resc.resc_id
LEFT JOIN emp emp ON reservation.emp_id = emp.emp_id
WHERE reservation.reservation_id = 36;


SELECT 
    resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable, 
    emp.ename AS emp_name
FROM reservation
LEFT JOIN resc resc
ON reservation.resource_id = resc.resc_id
LEFT JOIN emp emp ON reservation.emp_id = emp.emp_id
WHERE reservation.reservation_id = ?;







TRUNCATE TABLE reservation;



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
