select * from reservation;
select * from resc;

desc reservation;
desc resc;


SELECT *
FROM resc
WHERE resc_type ='회의실' AND resc_usable = 1
GROUP BY resc_type, resc_name, resc_usable
ORDER BY resc_id;


SELECT *
FROM resc
WHERE resc_type ='차량' AND resc_usable = 1
GROUP BY resc_type, resc_name, resc_usable
ORDER BY resc_id;


SELECT r.reservation_id, r.resource_id, r.emp_id, r.reservation_start, r.reservation_end,
       r.reservation_info, r.reservation_allDay, s.resc_type, s.resc_name
FROM reservation r
         JOIN resc s ON r.resource_id = s.resc_id
WHERE s.resc_type = '회의실'
  AND s.resc_usable = 1
  AND r.resource_id IN (
    SELECT reservation.resource_id
    FROM reservation reservation
             JOIN resc resource ON reservation.resource_id = resource.resc_id
    WHERE resource.resc_type = '회의실'
)
ORDER BY r.reservation_id;