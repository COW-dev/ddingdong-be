package ddingdong.ddingdongBE.domain.activityreport.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QParticipant is a Querydsl query type for Participant
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QParticipant extends BeanPath<Participant> {

    private static final long serialVersionUID = 670376887L;

    public static final QParticipant participant = new QParticipant("participant");

    public final StringPath department = createString("department");

    public final StringPath name = createString("name");

    public final StringPath studentId = createString("studentId");

    public QParticipant(String variable) {
        super(Participant.class, forVariable(variable));
    }

    public QParticipant(Path<? extends Participant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QParticipant(PathMetadata metadata) {
        super(Participant.class, metadata);
    }

}
