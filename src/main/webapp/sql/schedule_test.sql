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

SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       -- 공유자들의 이름을 가져오는 부분
       LISTAGG(e.ename, ', ') WITHIN GROUP (ORDER BY e.ename) AS share_emp_names
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
-- schedule_share 테이블에서 share_emp를 emp 테이블의 emp_id와 조인하여 공유자의 이름을 가져옴
JOIN emp e ON ss.share_emp = e.emp_id
WHERE s.emp_id = '241001' OR ss.share_emp = '241103'
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay;
     
         
SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       (SELECT LISTAGG(ss.share_emp, ', ') WITHIN GROUP (ORDER BY ss.share_emp)
        FROM schedule_share ss
        WHERE ss.schedule_id = s.schedule_id) AS share_emp_names
FROM schedule s
WHERE s.schedule_id IN (
    SELECT schedule_id 
    FROM schedule_share 
    WHERE share_emp = '241103'
)




select * from schedule;

select * from schedule_share;

 
         
         
         
SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       (SELECT LISTAGG(ss.share_emp, ', ') WITHIN GROUP (ORDER BY ss.share_emp)
        FROM schedule_share ss
        WHERE ss.schedule_id = s.schedule_id) AS share_emp_names
FROM schedule s
WHERE s.schedule_id = ?;
 
         
         
         
         
         

         
SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       LISTAGG(e.ename, ', ') WITHIN GROUP (ORDER BY e.ename) AS share_emp_names
FROM schedule s
JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
JOIN emp e ON ss.share_emp = e.emp_id
WHERE s.emp_id = ? OR ss.share_emp = ?
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay
 

         
SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       ss.share_emp
FROM schedule s
JOIN schedule_share ss
  ON s.schedule_id = ss.schedule_share_id
WHERE s.emp_id = ss.share_emp
ORDER BY s.schedule_id;
      
         



SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       LISTAGG(ss.share_emp, ',') WITHIN GROUP (ORDER BY ss.share_emp) AS share_emp
FROM schedule s
LEFT JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = '241001' OR ss.share_emp IN ('241001', '241002', '241003')
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay
ORDER BY s.schedule_id;




SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       LISTAGG(ss.share_emp, ',') WITHIN GROUP (ORDER BY ss.share_emp) AS share_emp
FROM schedule s
LEFT JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = '241001'  -- 또는 로그인한 emp_id
   OR ss.share_emp IN ('241001', '241002', '241003')  -- 공유된 일정에 대한 조건
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay
ORDER BY s.schedule_id;




         

select * from schedule;

select * from schedule_share;


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




SELECT * from reservation;		

/* 나의 자원 예약목록 + 이름 불러오기 */
SELECT rs.resc_type, rs.resc_name, (select ename from emp where emp_id  = '241001') ename,
	   reservation.reservation_start, reservation.reservation_end, reservation.reservation_id
FROM reservation reservation
JOIN resc rs 
ON reservation.resource_id = rs.resc_id
WHERE reservation.emp_id = '241001';



/* 자원 예약 정보 팝업 + 이름 추가 */
SELECT 
    resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable, 
    (select ename from emp where emp_id  = '241001') ename,
    reservation.reservation_id,
    reservation.emp_id,
    reservation.reservation_start,
    reservation.reservation_end,
    reservation.reservation_info,
    reservation.reservation_allDay
FROM reservation
LEFT JOIN resc resc
ON reservation.resource_id = resc.resc_id
WHERE reservation.reservation_id = 5;




SELECT 
    resc.resc_id, 
    resc.resc_type, 
    resc.resc_name, 
    resc.resc_info, 
    resc.resc_usable, 
    CASE 
        WHEN reservation.emp_id = '241001' THEN (SELECT ename FROM emp WHERE emp_id = reservation.emp_id) 
        ELSE NULL 
    END AS ename,
    reservation.reservation_id,
    reservation.emp_id,
    reservation.reservation_start,
    reservation.reservation_end,
    reservation.reservation_info,
    reservation.reservation_allDay
FROM reservation
LEFT JOIN resc resc ON reservation.resource_id = resc.resc_id
WHERE reservation.reservation_id = 5;




select * from reservation;

TRUNCATE TABLE reservation;


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
	            WHERE reservation.reservation_id = ?
	            
	            
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
	            WHERE reservation.reservation_id = 13       
	            
	            

SELECT 
    resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable,
    (select ename from emp where emp_id  = '241001') ename, 
    reservation.reservation_id,
    reservation.emp_id,
    reservation.reservation_start,
    reservation.reservation_end,
    reservation.reservation_info,
    reservation.reservation_allDay
FROM reservation
LEFT JOIN resc resc
ON reservation.resource_id = resc.resc_id
WHERE reservation.reservation_id = 1



SELECT 
    emp.ename, 
    reservation.emp_id
FROM reservation
LEFT JOIN resc resc
    ON reservation.resource_id = resc.resc_id
LEFT JOIN emp emp
    ON reservation.emp_id = emp.emp_id
WHERE emp.emp_id = '241001';











SELECT rs.resc_type, rs.resc_name, (select ename from emp where emp_id  = '241001') ename,
	   reservation.reservation_start, reservation.reservation_end, 
	   reservation.reservation_info, reservation.reservation_id
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
WHERE reservation.reservation_id = 8 AND reservation.emp_id = '241001';


SELECT  rs.resc_type, rs.resc_name, rs.resc_info,
	    reservation.reservation_start, reservation.reservation_end, 
	    reservation.reservation_info, reservation.reservation_id
FROM reservation reservation
JOIN resc rs 
ON reservation.resource_id = rs.resc_id
WHERE reservation.emp_id = ?
ORDER BY reservation.reservation_id;


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
