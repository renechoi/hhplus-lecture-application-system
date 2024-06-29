package io.hhpluslectureapplicationsystem.api.business.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLectureApplication is a Querydsl query type for LectureApplication
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLectureApplication extends EntityPathBase<LectureApplication> {

    private static final long serialVersionUID = 1862172041L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLectureApplication lectureApplication = new QLectureApplication("lectureApplication");

    public final BooleanPath applied = createBoolean("applied");

    public final DateTimePath<java.time.LocalDateTime> appliedAt = createDateTime("appliedAt", java.time.LocalDateTime.class);

    public final QLecture lecture;

    public final StringPath lectureApplicationExternalId = createString("lectureApplicationExternalId");

    public final StringPath lectureApplicationId = createString("lectureApplicationId");

    public final DateTimePath<java.time.LocalDateTime> requestAt = createDateTime("requestAt", java.time.LocalDateTime.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QLectureApplication(String variable) {
        this(LectureApplication.class, forVariable(variable), INITS);
    }

    public QLectureApplication(Path<? extends LectureApplication> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLectureApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLectureApplication(PathMetadata metadata, PathInits inits) {
        this(LectureApplication.class, metadata, inits);
    }

    public QLectureApplication(Class<? extends LectureApplication> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lecture = inits.isInitialized("lecture") ? new QLecture(forProperty("lecture")) : null;
    }

}

