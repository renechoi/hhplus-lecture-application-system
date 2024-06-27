package io.hhpluslectureapplicationsystem.api.business.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLecture is a Querydsl query type for Lecture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLecture extends EntityPathBase<Lecture> {

    private static final long serialVersionUID = 1196289351L;

    public static final QLecture lecture = new QLecture("lecture");

    public final DateTimePath<java.time.LocalDateTime> applicationCloseTime = createDateTime("applicationCloseTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> applicationOpenTime = createDateTime("applicationOpenTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> capacity = createNumber("capacity", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> durationMinutes = createNumber("durationMinutes", Integer.class);

    public final StringPath instructor = createString("instructor");

    public final StringPath lectureExternalId = createString("lectureExternalId");

    public final StringPath lectureId = createString("lectureId");

    public final DateTimePath<java.time.LocalDateTime> lectureStartTime = createDateTime("lectureStartTime", java.time.LocalDateTime.class);

    public final StringPath location = createString("location");

    public final NumberPath<Integer> registeredCount = createNumber("registeredCount", Integer.class);

    public final EnumPath<LectureStatus> status = createEnum("status", LectureStatus.class);

    public final StringPath title = createString("title");

    public QLecture(String variable) {
        super(Lecture.class, forVariable(variable));
    }

    public QLecture(Path<? extends Lecture> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLecture(PathMetadata metadata) {
        super(Lecture.class, metadata);
    }

}

