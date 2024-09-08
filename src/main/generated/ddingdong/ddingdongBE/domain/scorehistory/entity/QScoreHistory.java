package ddingdong.ddingdongBE.domain.scorehistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScoreHistory is a Querydsl query type for ScoreHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScoreHistory extends EntityPathBase<ScoreHistory> {

    private static final long serialVersionUID = 275105630L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScoreHistory scoreHistory = new QScoreHistory("scoreHistory");

    public final ddingdong.ddingdongBE.common.QBaseEntity _super = new ddingdong.ddingdongBE.common.QBaseEntity(this);

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final ddingdong.ddingdongBE.domain.club.entity.QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reason = createString("reason");

    public final EnumPath<ScoreCategory> scoreCategory = createEnum("scoreCategory", ScoreCategory.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QScoreHistory(String variable) {
        this(ScoreHistory.class, forVariable(variable), INITS);
    }

    public QScoreHistory(Path<? extends ScoreHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScoreHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScoreHistory(PathMetadata metadata, PathInits inits) {
        this(ScoreHistory.class, metadata, inits);
    }

    public QScoreHistory(Class<? extends ScoreHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new ddingdong.ddingdongBE.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
    }

}
