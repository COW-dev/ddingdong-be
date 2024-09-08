package ddingdong.ddingdongBE.domain.activityreport.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActivityReport is a Querydsl query type for ActivityReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QActivityReport extends EntityPathBase<ActivityReport> {

    private static final long serialVersionUID = 1287698975L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActivityReport activityReport = new QActivityReport("activityReport");

    public final ddingdong.ddingdongBE.common.QBaseEntity _super = new ddingdong.ddingdongBE.common.QBaseEntity(this);

    public final ddingdong.ddingdongBE.domain.club.entity.QClub club;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Participant, QParticipant> participants = this.<Participant, QParticipant>createList("participants", Participant.class, QParticipant.class, PathInits.DIRECT2);

    public final StringPath place = createString("place");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath term = createString("term");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QActivityReport(String variable) {
        this(ActivityReport.class, forVariable(variable), INITS);
    }

    public QActivityReport(Path<? extends ActivityReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActivityReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActivityReport(PathMetadata metadata, PathInits inits) {
        this(ActivityReport.class, metadata, inits);
    }

    public QActivityReport(Class<? extends ActivityReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new ddingdong.ddingdongBE.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
    }

}

