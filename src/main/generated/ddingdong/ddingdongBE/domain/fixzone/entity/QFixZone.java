package ddingdong.ddingdongBE.domain.fixzone.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFixZone is a Querydsl query type for FixZone
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFixZone extends EntityPathBase<FixZone> {

    private static final long serialVersionUID = 306285152L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFixZone fixZone = new QFixZone("fixZone");

    public final ddingdong.ddingdongBE.common.QBaseEntity _super = new ddingdong.ddingdongBE.common.QBaseEntity(this);

    public final ddingdong.ddingdongBE.domain.club.entity.QClub club;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final ListPath<FixZoneComment, QFixZoneComment> fixZoneComments = this.<FixZoneComment, QFixZoneComment>createList("fixZoneComments", FixZoneComment.class, QFixZoneComment.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFixZone(String variable) {
        this(FixZone.class, forVariable(variable), INITS);
    }

    public QFixZone(Path<? extends FixZone> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFixZone(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFixZone(PathMetadata metadata, PathInits inits) {
        this(FixZone.class, metadata, inits);
    }

    public QFixZone(Class<? extends FixZone> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new ddingdong.ddingdongBE.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
    }

}
