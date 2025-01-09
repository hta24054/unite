show databases;

use unite;
show tables;

/*
use mysql;
alter user 'root'@'localhost' identified with mysql_native_password by '1234';
flush privileges;

create user unite@localhost identified by 'unite1234';
grant all privileges on *.* to unite@localhost with grant option;
flush privileges;
use mysql;
select user, host from user;
*/

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

select *
from schedule;

desc schedule;

select *
from schedule_share;

desc schedule_share;

select *
from schedule
where emp_id = '241001';


CREATE TABLE schedule_share
(
    schedule_share_id bigINT AUTO_INCREMENT PRIMARY KEY,
    share_emp         VARCHAR(100) NOT NULL,
    schedule_id       bigINT       NOT NULL,
    CONSTRAINT FK_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE
);



ALTER TABLE schedule_share
    MODIFY share_emp VARCHAR(255);

select share_emp from schedule_share;

SELECT
    s.schedule_id,
    s.emp_id,
    s.schedule_name,
    s.schedule_content,
    s.schedule_start,
    s.schedule_end,
    s.schedule_color,
    s.schedule_allDay,
    (select ename from emp where emp_id  = '241101') ename,
    GROUP_CONCAT(ss.share_emp) AS share_emp -- 공유 직원 목록
FROM schedule s
         JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = '241001'
   OR ss.share_emp LIKE CONCAT('%', '241101', '%') -- shareEmp를 LIKE로 처리
   GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
   s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay;



SELECT
    s.schedule_id,
    s.emp_id,
    s.schedule_name,
    s.schedule_content,
    s.schedule_start,
    s.schedule_end,
    s.schedule_color,
    s.schedule_allDay,
    (SELECT ename FROM emp WHERE emp_id = '241001') AS ename,
    GROUP_CONCAT(ss.share_emp) AS share_emp, -- 공유된 직원들의 emp_id
    GROUP_CONCAT(e.ename) AS share_emp_names -- 공유된 직원들의 이름
FROM schedule s
         JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
         LEFT JOIN emp e ON FIND_IN_SET(e.emp_id, ss.share_emp) > 0  -- share_emp와 일치하는 emp_id 이름
WHERE s.emp_id = '241001'
   OR ss.share_emp LIKE CONCAT('%', '241101,241102', '%')
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay;


SELECT
    s.schedule_id,
    s.emp_id,
    s.schedule_name,
    s.schedule_content,
    s.schedule_start,
    s.schedule_end,
    s.schedule_color,
    s.schedule_allDay,
    (SELECT ename FROM emp WHERE emp_id = '241001') AS ename,
    GROUP_CONCAT(ss.share_emp) AS share_emp, -- 공유된 직원들의 emp_id
    GROUP_CONCAT(e.ename) AS share_emp_names -- 공유된 직원들의 이름
FROM schedule s
         JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
         LEFT JOIN emp e ON ss.share_emp REGEXP CONCAT('(^|,)', e.emp_id, '(,|$)') -- share_emp와 일치하는 emp_id 이름
WHERE s.emp_id = '241001'
   OR ss.share_emp LIKE CONCAT('%', '241101,241102', '%')
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay;


SELECT
    s.schedule_id,
    s.emp_id,
    s.schedule_name,
    s.schedule_content,
    s.schedule_start,
    s.schedule_end,
    s.schedule_color,
    s.schedule_allDay,
    (SELECT ename FROM emp WHERE emp_id = '241001') AS empIdName, -- 로그인한 사용자의 이름을 가져옵니다.
                                 GROUP_CONCAT(ss.share_emp) AS share_emp, -- 공유된 직원들의 emp_id
            GROUP_CONCAT(e.ename) AS share_emp_names -- 공유된 직원들의 이름
     FROM schedule s
         JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
         LEFT JOIN emp e ON ss.share_emp REGEXP CONCAT('(^|,)', e.emp_id, '(,|$)') -- share_emp와 일치하는 emp_id 이름
     WHERE s.emp_id = '241001'
        OR ss.share_emp LIKE CONCAT('%', '241101,241102', '%')
         GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay;


select dept_manager from dept;


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
    d.dept_name  -- 부서명 가져오기
FROM schedule s
         JOIN emp e ON s.emp_id = e.emp_id  -- schedule 테이블과 emp 테이블을 empId로 조인
         JOIN dept d ON e.dept_id = d.dept_id  -- emp 테이블과 dept 테이블을 deptId로 조인
WHERE s.emp_id = '241001';  -- 특정 직원이 등록한 모든 일정에 대한 부서 정보




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



SELECT emp_id
FROM emp
WHERE dept_id = '1100';


desc schedule_share;
select * from schedule;
select * from schedule_share;

select * from dept;


ALTER TABLE schedule_share
ADD CONSTRAINT fk_deptId FOREIGN KEY (dept_id) REFERENCES dept(dept_id) ON DELETE CASCADE;


