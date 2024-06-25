Feature: 특강 신청 서비스 - 특강 신청

  Background:
    Given 다음과 같은 복수의 특강 정보가 주어지고 등록을 요청하면 성공 응답을 받는다
      | title   | description | applicationOpenTime | applicationCloseTime | lectureStartTime    | durationMinutes | capacity | location | instructor |
      | 항해 특강 1 | 항해 특강 1 입니다 | now                 | 2030-12-31T23:59:59  | 2025-04-25T13:00:00 | 120             | 30       | Online   | 김항해        |
      | 항해 특강 2 | 항해 특강 2 입니다 | now                 | 2030-12-31T23:59:59  | 2025-04-25T13:00:00 | 60              | 50       | Online   | 이학습        |
      | 항해 특강 3 | 항해 특강 3 입니다 | now                 | 2030-12-31T23:59:59  | 2025-04-25T13:00:00 | 60              | 2        | Online   | 박튜터        |

  Scenario: 새로운 특강 신청 - 기본 성공 케이스
    Given 특강 신청 페이지에 접속하여 현재 제공되는 특강 목록을 조회한다
    When "user1" 아이디로 "항해 특강 1" 특강 신청을 요청하고 성공 응답을 받는다
    And 특강 신청 완료 여부를 조회하면 신청 성공 응답을 받고 응답은 다음과 같은 내용을 포함한다.
      | applied |
      | true    |
    And "user1" 아이디의 "항해 특강 1" 특강 신청 이력이 생성되었는지 확인한다
    And 이력의 개수는 1개로 확인되고 다음과 같은 내용으로 확인되어야 한다
      | userId | success | lectureTitle |
      | user1  | true    | 항해 특강 1      |





  Scenario: 이미 신청한 사용자가 다시 신청 시도 - 실패
    Given 특강 신청 페이지에 접속하여 현재 제공되는 특강 목록을 조회한다
    And "user1" 아이디로 "항해 특강 1" 특강 신청을 요청하고 성공 응답을 받는다
    When "user1" 아이디로 "항해 특강 1" 특강 신청을 요청하고 실패 응답을 받는다
      | execptionMessage |
      | 이미 신청한 사용자입니다    |



  Scenario: 정원 초과로 인한 신청 실패
    Given 특강 신청 페이지에 접속하여 현재 제공되는 특강 목록을 조회한다
    And "user1" 아이디로 "항해 특강 3" 특강 신청을 요청하고 성공 응답을 받는다
    And "user2" 아이디로 "항해 특강 3" 특강 신청을 요청하고 성공 응답을 받는다
    When "user3" 아이디로 "항해 특강 3" 특강 신청을 요청하고 실패 응답을 받는다
      | execptionMessage |
      | 특강 신청이 마감되었습니다    |




  Scenario: 신청하지 않은 사용자의 특강 신청 완료 여부 조회
    Given 특강 신청 페이지에 접속하여 현재 제공되는 특강 목록을 조회한다
    Then "userId1" 아이디로 "항해 특강 1" 특강 신청 완료 여부를 조회하면 다음과 같이 확인된다
      | applied |
      | false   |