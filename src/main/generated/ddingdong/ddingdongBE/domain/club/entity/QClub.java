package ddingdong.ddingdongBE.domain.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = -316467098L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final ddingdong.ddingdongBE.common.QBaseEntity _super = new ddingdong.ddingdongBE.common.QBaseEntity(this);

    public final StringPath activity = createString("activity");

    public final StringPath category = createString("category");

    public final ListPath<ClubMember, QClubMember> clubMembers = this.<ClubMember, QClubMember>createList("clubMembers", ClubMember.class, QClubMember.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endRecruitPeriod = createDateTime("endRecruitPeriod", java.time.LocalDateTime.class);

    public final StringPath formUrl = createString("formUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ideal = createString("ideal");

    public final StringPath introduction = createString("introduction");

    public final StringPath leader = createString("leader");

    public final QLocation location;

    public final StringPath name = createString("name");

    public final QPhoneNumber phoneNumber;

    public final StringPath regularMeeting = createString("regularMeeting");

    public final ddingdong.ddingdongBE.domain.scorehistory.entity.QScore score;

    public final DateTimePath<java.time.LocalDateTime> startRecruitPeriod = createDateTime("startRecruitPeriod", java.time.LocalDateTime.class);

    public final StringPath tag = createString("tag");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ddingdong.ddingdongBE.domain.user.entity.QUser user;

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location")) : null;
        this.phoneNumber = inits.isInitialized("phoneNumber") ? new QPhoneNumber(forProperty("phoneNumber")) : null;
        this.score = inits.isInitialized("score") ? new ddingdong.ddingdongBE.domain.scorehistory.entity.QScore(forProperty("score")) : null;
        this.user = inits.isInitialized("user") ? new ddingdong.ddingdongBE.domain.user.entity.QUser(forProperty("user")) : null;
    }

}
