-- activity_report_participants 테이블
ALTER TABLE activity_report_participants
    DROP FOREIGN KEY FKbine0e4wrv5vp15ifuivm7ysv,
    ADD CONSTRAINT FK_activity_report_participants_activity_report_id
    FOREIGN KEY (activity_report_id) REFERENCES activity_report (id);

-- banner 테이블
ALTER TABLE banner
    DROP FOREIGN KEY FKk5t5cl4qmev8n93gi9whm44p5,
    ADD CONSTRAINT FK_banner_users_id
    FOREIGN KEY (user_id) REFERENCES users (id);

-- club 테이블
ALTER TABLE club
    DROP FOREIGN KEY FK7xnudv3706b3xpjrhutva13rd,
    ADD CONSTRAINT FK_club_users_user_id
    FOREIGN KEY (user_id) REFERENCES users (id);

-- activity_report 테이블
ALTER TABLE activity_report
    DROP FOREIGN KEY FKol6ps7dpml9l2kuokcciyv1kk,
    ADD CONSTRAINT FK_activity_report_club_id
    FOREIGN KEY (club_id) REFERENCES club (id);

-- club_member 테이블
ALTER TABLE club_member
    DROP FOREIGN KEY FKf6tl19ih8acrmheidn4xos2tx,
    ADD CONSTRAINT FK_club_member_club_id
    FOREIGN KEY (club_id) REFERENCES club (id);

-- document 테이블
ALTER TABLE document
    DROP FOREIGN KEY FKm19xjdnh3l6aueyrpm1705t52,
    ADD CONSTRAINT FK_document_users_id
    FOREIGN KEY (user_id) REFERENCES users (id);

-- fix_zone 테이블
ALTER TABLE fix_zone
    DROP FOREIGN KEY FKrv7031dafqlk0k5nm8x69p85g,
    ADD CONSTRAINT FK_fix_zone_club_id
    FOREIGN KEY (club_id) REFERENCES club (id);

-- fix_zone_comment 테이블
ALTER TABLE fix_zone_comment
    DROP FOREIGN KEY FKc2d91n1o7eindrcl3io79hlj7,
    ADD CONSTRAINT FK_fix_zone_comment_club_id
    FOREIGN KEY (club_id) REFERENCES club (id);

ALTER TABLE fix_zone_comment
    DROP FOREIGN KEY FKfbkrutdl1qo2f1hnva40cixj7,
    ADD CONSTRAINT FK_fix_zone_comment_fix_zone_id
    FOREIGN KEY (fix_zone_id) REFERENCES fix_zone (id);

-- notice 테이블
ALTER TABLE notice
    DROP FOREIGN KEY FK6hu3mfrsmpbqjk44w2fq5t5us,
    ADD CONSTRAINT FK_notice_users_id
    FOREIGN KEY (user_id) REFERENCES users (id);

-- question 테이블
ALTER TABLE question
    DROP FOREIGN KEY FK7rnpup7eaonh2ubt922ormoij,
    ADD CONSTRAINT FK_question_users_id
    FOREIGN KEY (user_id) REFERENCES users (id);

-- score_history 테이블
ALTER TABLE score_history
    DROP FOREIGN KEY FK1otphxnm3ngxfkbw3lmbwqt78,
    ADD CONSTRAINT FK_score_history_club_id
    FOREIGN KEY (club_id) REFERENCES club (id);
