package ddingdong.ddingdongBE.domain.fileinformation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileInformation is a Querydsl query type for FileInformation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileInformation extends EntityPathBase<FileInformation> {

    private static final long serialVersionUID = 1337656640L;

    public static final QFileInformation fileInformation = new QFileInformation("fileInformation");

    public final ddingdong.ddingdongBE.common.QBaseEntity _super = new ddingdong.ddingdongBE.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final EnumPath<FileDomainCategory> fileDomainCategory = createEnum("fileDomainCategory", FileDomainCategory.class);

    public final EnumPath<FileTypeCategory> fileTypeCategory = createEnum("fileTypeCategory", FileTypeCategory.class);

    public final StringPath findParam = createString("findParam");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath storedName = createString("storedName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath uploadName = createString("uploadName");

    public QFileInformation(String variable) {
        super(FileInformation.class, forVariable(variable));
    }

    public QFileInformation(Path<? extends FileInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileInformation(PathMetadata metadata) {
        super(FileInformation.class, metadata);
    }

}

