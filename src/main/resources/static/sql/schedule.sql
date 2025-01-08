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

SELECT s.schedule_id,
       s.emp_id,
       s.schedule_name,
       s.schedule_content,
       s.schedule_start,
       s.schedule_end,
       s.schedule_color,
       s.schedule_allDay,
       GROUP_CONCAT(ss.share_emp) AS share_emp -- 공유 직원 목록
FROM schedule s
         JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = '241001'
   OR ss.share_emp = '241101, 241102'
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
       GROUP_CONCAT(ss.share_emp) AS share_emp -- 공유 직원 목록
FROM schedule s
         JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
WHERE s.emp_id = '241101'
   OR ss.share_emp = '241101, 241102'
GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
         s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay;

