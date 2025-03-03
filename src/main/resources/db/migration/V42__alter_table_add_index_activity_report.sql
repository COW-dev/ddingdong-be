CREATE INDEX activity_report_term_index
    ON ddingdong_dev.activity_report (term)
    COMMENT '활동 보고서 회차(term) 인덱스';

CREATE INDEX activity_report_created_at_index
    ON ddingdong_dev.activity_report (created_at)
    COMMENT '활동보고서 생성 일자(created_at) 인덱스';

CREATE INDEX activity_report_start_date_index
    ON ddingdong_dev.activity_report (start_date)
    COMMENT '활동보고서 시작 일자(start_date) 인덱스';
