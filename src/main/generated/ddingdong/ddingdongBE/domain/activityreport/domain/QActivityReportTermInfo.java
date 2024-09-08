package ddingdong.ddingdongBE.domain.activityreport.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QActivityReportTermInfo is a Querydsl query type for ActivityReportTermInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QActivityReportTermInfo extends EntityPathBase<ActivityReportTermInfo> {

    private static final long serialVersionUID = -149711239L;

    public static final QActivityReportTermInfo activityReportTermInfo = new QActivityReportTermInfo("activityReportTermInfo");

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final NumberPath<Integer> term = createNumber("term", Integer.class);

    public QActivityReportTermInfo(String variable) {
        super(ActivityReportTermInfo.class, forVariable(variable));
    }

    public QActivityReportTermInfo(Path<? extends ActivityReportTermInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QActivityReportTermInfo(PathMetadata metadata) {
        super(ActivityReportTermInfo.class, metadata);
    }

}
