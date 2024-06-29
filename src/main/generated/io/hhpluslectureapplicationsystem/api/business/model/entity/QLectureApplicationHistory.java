package io.hhpluslectureapplicationsystem.api.business.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLectureApplicationHistory is a Querydsl query type for LectureApplicationHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLectureApplicationHistory extends EntityPathBase<LectureApplicationHistory> {

    private static final long serialVersionUID = 2082759371L;

    public static final QLectureApplicationHistory lectureApplicationHistory = new QLectureApplicationHistory("lectureApplicationHistory");

    public final DateTimePath<java.time.LocalDateTime> appliedAt = createDateTime("appliedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lectureApplicationId = createString("lectureApplicationId");

    public final StringPath lectureExternalId = createString("lectureExternalId");

    public final DateTimePath<java.time.LocalDateTime> requestAt = createDateTime("requestAt", java.time.LocalDateTime.class);

    public final BooleanPath success = createBoolean("success");

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QLectureApplicationHistory(String variable) {
        super(LectureApplicationHistory.class, forVariable(variable));
    }

    public QLectureApplicationHistory(Path<? extends LectureApplicationHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLectureApplicationHistory(PathMetadata metadata) {
        super(LectureApplicationHistory.class, metadata);
    }

}

