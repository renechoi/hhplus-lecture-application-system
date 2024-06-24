Feature: 특강 신청 서비스 - 특강 등록

  Scenario: 새로운 특강 객체를 등록한다 - 기본 시나리오
    Given 다음과 같은 특강 정보가 주어지고 등록을 요청하면 성공 응답을 받는다
      | title   | description | applicationOpenTime | applicationCloseTime | lectureStartTime    | durationMinutes | capacity | location | instructor |
      | 항해 특강 1 | 항해 특강 1 입니다 | 2024-04-20T13:00:00 | 2024-04-20T23:59:59  | 2024-04-25T13:00:00 | 120             | 30       | Online   | 김항해        |
    And 등록된 특강의 정보를 조회하면 아래와 같은 정보가 확인되어야 한다
      | title   | description | applicationOpenTime | applicationCloseTime | lectureStartTime | durationMinutes | capacity | location | instructor |
      | 항해 특강 1 | 항해 특강 1 입니다 | notNull             | notNull              | notNull          | 120             | 30       | Online   | 김항해        |

  Scenario: 특강 등록 시 필수 필드가 누락된 경우 예외가 발생한다
    Given 다음과 같은 필수 필드가 누락된 특강 정보가 주어지고 등록을 요청하면 실패 응답을 받는다
      | title | description | applicationOpenTime | applicationCloseTime | lectureStartTime    | durationMinutes | capacity | location | instructor |
      |       | 항해 특강 1 입니다 | 2024-04-20T13:00:00 | 2024-04-20T23:59:59  | 2024-04-25T13:00:00 | 120             | 30       | Online   | 김항해        |
