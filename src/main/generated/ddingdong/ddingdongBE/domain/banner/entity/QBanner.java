package ddingdong.ddingdongBE.domain.banner.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = 1783101330L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBanner banner = new QBanner("banner");

    public final ddingdong.ddingdongBE.common.QBaseEntity _super = new ddingdong.ddingdongBE.common.QBaseEntity(this);

    public final StringPath colorCode = createString("colorCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath subTitle = createString("subTitle");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ddingdong.ddingdongBE.domain.user.entity.QUser user;

    public QBanner(String variable) {
        this(Banner.class, forVariable(variable), INITS);
    }

    public QBanner(Path<? extends Banner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBanner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBanner(PathMetadata metadata, PathInits inits) {
        this(Banner.class, metadata, inits);
    }

    public QBanner(Class<? extends Banner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new ddingdong.ddingdongBE.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

