package io.hhpluslectureapplicationsystem.common.mapper;

import io.hhpluslectureapplicationsystem.api.business.model.dto.LectureRegisterCommand;
import io.hhpluslectureapplicationsystem.api.business.model.entity.Lecture;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-29T10:08:13+0900",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.11 (Homebrew)"
)
public class LectureEntityMapperImpl implements LectureEntityMapper {

    @Override
    public Lecture toEntity(LectureRegisterCommand command, String id) {
        if ( command == null && id == null ) {
            return null;
        }

        Lecture.LectureBuilder lecture = Lecture.builder();

        if ( command != null ) {
            lecture.title( command.title() );
            lecture.description( command.description() );
            lecture.applicationOpenTime( command.applicationOpenTime() );
            lecture.applicationCloseTime( command.applicationCloseTime() );
            lecture.lectureStartTime( command.lectureStartTime() );
            lecture.durationMinutes( command.durationMinutes() );
            lecture.capacity( command.capacity() );
            lecture.location( command.location() );
            lecture.instructor( command.instructor() );
        }
        lecture.lectureId( id );

        return lecture.build();
    }
}
