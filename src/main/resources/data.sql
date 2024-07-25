insert into users(name, password, role, userid)
values ('ddingdong', '$2a$12$9BIi3IGc79rU3Xgbnxq/X.T37Hlfrf/lSc2/g0HLeM1g7HmFXE8v.', 'ADMIN', 'ddingdong11'),
       ('cow', '$2a$12$9BIi3IGc79rU3Xgbnxq/X.T37Hlfrf/lSc2/g0HLeM1g7HmFXE8v.', 'CLUB', 'cow11');

insert into club(name, category, leader, tag, score, phone_number, location, user_id)
values ('카우', '학술', '김세빈', 'IT', 0, '010-0000-0000', 'S1350', 2);
