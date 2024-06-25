```meraid
sequenceDiagram
    participant User as User
    participant LectureApplyService as LectureApplyService
    participant ApplicationEventPublisher as ApplicationEventPublisher
    participant LectureApplyHistoryFactory as LectureApplyHistoryFactory

    User->>+LectureApplyService: applyForLecture(command)
    LectureApplyService-->>+ApplicationEventPublisher: publishEvent(LectureApplyTryEvent)
    Note right of LectureApplyService: 시도 이력 발행 (비동기)

    LectureApplyService->>LectureApplyService: validate and save lecture application
    LectureApplyService->>LectureApplyService: publish success event
    LectureApplyService->>+ApplicationEventPublisher: publishEvent(LectureApplySuccessEvent)
    Note right of LectureApplyService: 성공 이력 발행 (트랜잭션-동기)

    Note right of LectureApplyHistoryFactory: 성공 이력이 먼저 도착한 경우
    ApplicationEventPublisher->>-LectureApplyHistoryFactory: upsertSuccessEvent(event)
    LectureApplyHistoryFactory->>LectureApplyHistoryFactory: upsertSuccessEvent(event)
    
    Note right of LectureApplyHistoryFactory: 시도 이력이 나중에 도착한 경우
    ApplicationEventPublisher-->>-LectureApplyHistoryFactory: saveTryHistory(event)
    LectureApplyHistoryFactory->>LectureApplyHistoryFactory: saveTryHistory(event)
```