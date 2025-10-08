insert into users(name, password, role, auth_id)
values ('ddingdong', '$2a$12$9BIi3IGc79rU3Xgbnxq/X.T37Hlfrf/lSc2/g0HLeM1g7HmFXE8v.', 'ADMIN', 'ddingdong11'),
       ('cow', '$2a$12$9BIi3IGc79rU3Xgbnxq/X.T37Hlfrf/lSc2/g0HLeM1g7HmFXE8v.', 'CLUB', 'cow11');
-- PhoneNumber, Location, Score가 각각 단일 컬럼으로 매핑됨
INSERT INTO club (
    user_id,
    name,
    category,
    tag,
    leader,
    phone_number,           -- PhoneNumber.number
    location,               -- Location.value
    regular_meeting,
    introduction,
    activity,
    ideal,
    score,                  -- Score.value
    created_at,
    updated_at,
    deleted_at
) VALUES (
             2,                      -- user_id
             '테스트 동아리',         -- name
             '문화예술',              -- category
             '음악,공연',             -- tag
             '홍길동',                -- leader
             '010-1234-5678',        -- phone_number (형식: \d{2,3}-\d{3,4}-\d{4})
             'S1234',                -- location (형식: S[0-9]{4,5})
             '매주 수요일 18:00',     -- regular_meeting
             '음악을 사랑하는 모임',   -- introduction
             '정기 공연, 버스킹',     -- activity
             '음악으로 하나되는 캠퍼스', -- ideal
             0.00,                   -- score (BigDecimal)
             NOW(),                  -- created_at
             NOW(),                  -- updated_at
             NULL                    -- deleted_at
         );
