package ddingdong.ddingdongBE.domain.fixzone.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFixZoneComment is a Querydsl query type for FixZoneComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFixZoneComment extends EntityPathBase<FixZoneComment> {

    private static final long serialVersionUID = 85888991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFixZoneComment fixZoneComment = new QFixZoneComment("fixZoneComment");

    public final ddingdong.ddingdongBE.common.QBaseEntity _super = new ddingdong.ddingdongBE.common.QBaseEntity(this);

    public final ddingdong.ddingdongBE.domain.club.entity.QClub club;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final QFixZone fixZone;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFixZoneComment(String variable) {
        this(FixZoneComment.class, forVariable(variable), INITS);
    }

    public QFixZoneComment(Path<? extends FixZoneComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFixZoneComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFixZoneComment(PathMetadata metadata, PathInits inits) {
        this(FixZoneComment.class, metadata, inits);
    }

    public QFixZoneComment(Class<? extends FixZoneComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new ddingdong.ddingdongBE.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.fixZone = inits.isInitialized("fixZone") ? new QFixZone(forProperty("fixZone"), inits.get("fixZone")) : null;
    }

}
