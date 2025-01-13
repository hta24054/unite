show databases;

use unite;
show tables;

/*
CREATE TABLE schedule (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id VARCHAR(10) NOT NULL,
    schedule_name VARCHAR(50) NOT NULL,
    schedule_content VARCHAR(255),
    schedule_start DATETIME NOT NULL,
    schedule_end DATETIME NOT NULL,
    schedule_color VARCHAR(30) NOT NULL,
    schedule_allDay TINYINT DEFAULT 0 NOT NULL
);

CREATE TABLE schedule_share (
    schedule_share_id INT AUTO_INCREMENT PRIMARY KEY,
    share_emp VARCHAR(10) NOT NULL,
    schedule_id INT NOT NULL,
    CONSTRAINT FK_schedule FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id) ON DELETE CASCADE
);
*/


commit; /*INSERT 하고나서 항상 커밋!!!*/



select * from schedule;

CREATE TABLE schedule_share
(
    schedule_share_id bigINT AUTO_INCREMENT PRIMARY KEY,
    share_emp         VARCHAR(100) NOT NULL,
    schedule_id       bigINT       NOT NULL,
    dept_id           bigINT       NOT NULL,
    CONSTRAINT FK_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE
);


ALTER TABLE schedule_share
    MODIFY share_emp VARCHAR(255);

SELECT
    s.schedule_id,
    s.emp_id,
    s.schedule_name,
    s.schedule_content,
    s.schedule_start,
    s.schedule_end,
    s.schedule_color,
    s.schedule_allDay,
    e.dept_id,  -- empId에 해당하는 부서의 deptId
    d.dept_name,  -- 부서명
    GROUP_CONCAT(e2.emp_id) AS shared_emp_ids,  -- 같은 부서 직원들의 emp_id (일정 공유 대상)
    GROUP_CONCAT(e2.ename) AS shared_emp_names  -- 같은 부서 직원들의 이름 (일정 공유 대상)
FROM schedule s
         JOIN emp e ON s.emp_id = e.emp_id  -- 일정 등록자(emp_id)
         JOIN dept d ON e.dept_id = d.dept_id  -- 부서 정보
         JOIN emp e2 ON e2.dept_id = e.dept_id  -- 같은 부서의 다른 직원들
WHERE s.schedule_id = '103'  -- 특정 일정 정보
GROUP BY s.schedule_id;



ALTER TABLE schedule_share
ADD CONSTRAINT fk_deptId FOREIGN KEY (dept_id) REFERENCES dept(dept_id) ON DELETE CASCADE;



desc reservation;
select * from resc;



SELECT MIN(resc_id) AS resc_id, resc_type, resc_name, resc_usable
FROM resc
WHERE resc_type = '회의실' AND resc_usable = '1'
GROUP BY resc_type, resc_name, resc_usable
ORDER BY resc_id ASC;

SELECT MIN(resc_id) AS resc_id, resc_type, resc_name, resc_usable
FROM resc
WHERE resc_type = '차량' AND resc_usable = '1'
GROUP BY resc_type, resc_name, resc_usable
ORDER BY resc_id ASC;


desc resc;
select * from resc;

SELECT COUNT(*)
FROM reservation
WHERE resource_id = 1
  AND (
    (reservation_allDay = 1 AND
    reservation_start <= '2025-01-13 00:00' AND reservation_end >= '2025-01-13 00:00')
    OR
    (reservation_allDay = 0 AND
     '2025-01-13 00:00' < reservation_end AND '2025-01-13 00:00' > reservation_start)
    );

INSERT INTO reservation
(resource_id, emp_id, reservation_start, reservation_end, reservation_info, reservation_allDay)
VALUES
    (1,
     '241001',
     '2025-01-13 00:00',
     '2025-01-13 00:00',
     '회의 예약',
     1);

select * from reservation;

SELECT *
FROM resc
where resc_id = 1
AND resc_name = '회의실1';


select * from resc;

SELECT r.*, s.resc_name
FROM reservation r
         JOIN resc s ON r.resource_id = s.resc_id
WHERE r.resource_id = 1
  AND s.resc_name = '회의실1';

SHOW COLUMNS FROM resc;

SHOW COLUMNS FROM reservation;


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
WHERE reservation.reservation_id = 18;


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
    reservation.reservation_allDay,
    emp.ename  -- emp 테이블에서 ename 컬럼을 가져옵니다.
FROM reservation
         LEFT JOIN resc resc ON reservation.resource_id = resc.resc_id
         LEFT JOIN emp emp ON reservation.emp_id = emp.emp_id  -- emp 테이블과 JOIN
WHERE reservation.reservation_id = 18;







