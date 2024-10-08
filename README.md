# 이디핏 (Eadyfit) - 백엔드

![banner](/images/베너.jpg)

**구름톤 트레이닝 풀스택 7회차 파이널 프로젝트 구름방범대 팀의 이디핏 입니다.**

<br>

## 프로젝트 개요

이디핏(Eadyfit)은 운동을 처음 시작하는 사람들에게 유용한 웹사이트를 제공하기 위해 제작된 프로젝트입니다. 다양한 운동과 기구를 올바르게 사용하는 방법을 안내하고, 사용자들이 운동 기록과 식단 기록을 쉽게 관리할 수 있는 공간을 제공합니다. 이를 통해 사용자가 자신의 건강 목표를 효율적으로 달성할 수 있도록 돕습니다.

<br>

### 프로젝트 이름: 이디핏

### 프로젝트 진행 기간: 2024.06 ~ 2024.08

## 백엔드 멤버 소개

| 역할          | 이름       | 담당 기능                                                         |
| ------------- | ---------- | ---------------------------------------------------------------- |
| 팀원   | 김준영     | 운동 기록, 식단 기록                                                  |
| 팀원          | 이승현     | 회원가입, 게시판                             |
| 팀원          | 윤창기     | 실시간 채팅 기능                                                 |

<br>

## ERD
![erd](/images/ERD.jpg)

## 기술 스택
![기술 스택](/images/기술.jpg)

- **백엔드**:
  - Java, Spring Boot, Spring MVC
  - MariaDB
  - REST API
  - JPA (Java Persistence API)
  - OAuth2, JWT
  - AWS (Amazon Web Services) - EC2, RDS
  - OpenAPI (식품의약품안전처)

- **개발 도구**:
  - Git, GitHub
  - Docker

<br>

## 주요 기능

![주요 기능](/images/이디핏기능.jpg)

1. **운동 기록 관리 기능**
   - 사용자가 운동 기록을 추가, 수정, 삭제할 수 있도록 지원합니다.
   - METs(Metabolic Equivalent of Task)를 이용하여 사용자가 선택한 운동의 칼로리 소모량을 자동으로 계산합니다.

2. **식단 기록 관리 기능**
   - 사용자가 일일 식단을 기록할 수 있으며, 각 음식의 영양 정보와 칼로리 섭취량을 확인할 수 있습니다.
   - 식품의약품안전처의 Open API를 활용하여 음식 데이터를 DB에 저장하고, 사용자가 식단을 기록할 때 정확한 칼로리 섭취량을 계산할 수 있도록 지원합니다.

3. **사용자 인증 및 관리**
   - OAuth2와 JWT(Json Web Token)를 이용한 사용자 인증 및 권한 관리 기능을 구현하여 보안성을 강화하였습니다.

<br>

## 나의 역할

- 저는 백엔드 개발자로서 **운동 기록 관리**와 **식단 기록 관리** 기능을 개발했습니다.
- **METs(Metabolic Equivalent of Task)를 이용한 칼로리 소모량 계산 기능**을 구현하여 사용자가 선택한 운동의 칼로리 소모량을 자동으로 계산하도록 했습니다.
- **식단 관리 기능**에서 사용자가 기록하는 음식의 영양 정보를 제공하기 위해 **식품의약품안전처의 Open API**를 활용하여 DB에 저장하였고, 이를 통해 사용자가 정확한 칼로리 섭취량을 알 수 있도록 했습니다.

<br>

## 아쉬운 점, 개선 방안

### 아쉬운 점

- 데이터베이스 접근 코드나 서비스 레이어에서 반복적으로 나타나는 패턴들이 존재하여 코드의 가독성과 유지보수성에 문제가 있었습니다.
- 백엔드와 프론트엔드 간의 데이터 형식과 요구 사항을 맞추는 과정에서 일부 소통이 부족하여, 개발 초기의 API 인터페이스가 여러 번 변경되었습니다.

<br>

### 개선 방안

- **반복되는 코드를 모듈화**하고, 재사용 가능한 공통 메소드를 추출하여 코드의 중복을 줄이는 방향으로 개선했습니다.
- 서비스 계층의 기능들을 좀 더 명확히 구분하고, 각 기능의 책임을 명확히 함으로써 코드의 가독성을 높이고 유지보수성을 개선했습니다.
- 백엔드와 프론트엔드 팀원 간의 **정기적인 회의와 소통 채널**을 강화하여, API 인터페이스와 데이터 형식을 명확히 정의하고 개발 초기 단계에서 문제를 해결할 수 있도록 개선했습니다.

<br>

## 사용법

### 배포 URL

[이디핏 웹사이트](http://www.eadyfit.com/)

### 로컬에서 실행하려고 할 때

1. GitHub에서 코드를 다운로드합니다.
2. 명령 프롬프트나 쉘을 열어 `Finalproject/Backend` 디렉토리로 이동합니다.
3. 필요한 의존성을 설치합니다:
   ```bash
   ./gradlew build
