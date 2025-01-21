select * from reservation;
select * from resc;

desc reservation;
desc resc;


SELECT r.reservation_id, r.resource_id, r.emp_id, r.reservation_start, r.reservation_end,
       r.reservation_info, r.reservation_allDay, s.resc_type
FROM reservation r
         JOIN resc s ON r.resource_id = s.resc_id
WHERE r.reservation_id = 90;


SELECT r.reservation_id, r.resource_id, r.emp_id, r.reservation_start, r.reservation_end,
       r.reservation_info, r.reservation_allDay, s.resc_type
FROM reservation r
         JOIN resc s ON r.resource_id = s.resc_id
WHERE s.resc_type = '회의실'
ORDER BY r.reservation_id ASC;

SELECT r.reservation_id, r.resource_id, r.emp_id, r.reservation_start, r.reservation_end,
       r.reservation_info, r.reservation_allDay, s.resc_type
FROM reservation r
         JOIN resc s ON r.resource_id = s.resc_id
WHERE s.resc_type = '빔프로젝터'
ORDER BY r.reservation_id ASC;

