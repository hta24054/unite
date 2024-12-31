show databases;

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

drop table schedule;

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

select * from schedule;

CREATE TABLE schedule_share (
    schedule_share_id INT AUTO_INCREMENT PRIMARY KEY,
    share_emp VARCHAR(10) NOT NULL,
    schedule_id INT NOT NULL,
    CONSTRAINT FK_schedule FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id) ON DELETE CASCADE
);

select * from schedule_share;



INSERT INTO schedule
(emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color, schedule_allDay)
VALUES ('EMP001', 'Meeting', 'Team meeting', '2024-12-31 10:00', '2024-12-31 11:00', '#FF5733', 1);


