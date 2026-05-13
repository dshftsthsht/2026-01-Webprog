# Week 11 - StudentJDBC 프로젝트

## 작성일
2026년 5월 13일

## 프로젝트 개요
클래스 다이어그램을 참고하여 StudentJDBC 기반 학생 정보 관리 웹 애플리케이션 구현

이번 주차에서는 학생 정보를 데이터베이스와 연동하여 등록, 조회, 수정, 삭제할 수 있는 JDBC 기반 웹 프로젝트를 작성하였다.  
Servlet, JSP, DAO 구조를 사용하여 학생 데이터 처리 흐름을 분리하고, MariaDB 데이터베이스와 연동하는 CRUD 기능을 구현하였다.

## 구현 내용

### 1. 모델 (Model)
- **Student.java**
  - 학생 정보를 저장하는 모델 클래스
  - 속성: id, name, univ, birthdate, email
  - Getter / Setter 메서드를 통해 데이터 접근 및 수정 가능
  - BeanUtils를 활용하기 위해 기본 생성자와 Setter 메서드 구성

### 2. 데이터 접근 객체 (DAO)
- **StudentDAO.java**
  - MariaDB 데이터베이스와 연결하여 학생 정보를 처리하는 클래스
  - `connect()`: 데이터베이스 연결
  - `close()`: 데이터베이스 연결 종료
  - `insert(student)`: 학생 정보 추가
  - `getAll()`: 전체 학생 목록 조회
  - `getStudent(id)`: 특정 학생 정보 조회
  - `update(student)`: 학생 정보 수정
  - `delete(id)`: 학생 정보 삭제

### 3. 컨트롤러 (Controller)
- **StudentController.java**
  - `HttpServlet` 확장
  - URL 매핑: `/student`
  - 요청 파라미터 `action` 값에 따라 기능 분기
  - 지원 액션:
    - `action=list`: 학생 목록 조회
    - `action=insert`: 학생 정보 등록
    - `action=edit&id=...`: 수정할 학생 정보 조회
    - `action=update`: 학생 정보 수정
    - `action=delete&id=...`: 학생 정보 삭제
  - `BeanUtils.populate()`를 사용하여 요청 파라미터를 Student 객체에 자동 매핑

### 4. 뷰 (View)
- **index.jsp**
  - `/student?action=list`로 자동 이동
  - 학생 정보 관리 화면으로 진입하는 시작 페이지 역할

- **studentInfo.jsp**
  - 학생 정보 입력 폼 제공
  - 학생 목록을 테이블 형식으로 출력
  - 학생 정보의 id, name, univ, birthdate, email 표시
  - 마지막 열에 비고 항목을 추가하여 수정 / 삭제 기능 제공
  - 수정 버튼 클릭 시 선택한 학생 정보를 상단 입력 폼에 표시
  - 삭제 버튼 클릭 시 해당 학생 정보를 삭제

### 5. 설정 파일
- **web.xml**
  - welcome-file로 index.jsp 설정
  - 웹 애플리케이션 기본 실행 페이지 지정

- **pom.xml**
  - Maven 기반 웹 프로젝트 설정
  - WAR 패키징 설정
  - Java 컴파일 버전 1.8 설정
  - 주요 의존성 추가:
    - `javax.servlet-api`
    - `mariadb-java-client`
    - `commons-beanutils`
    - `javaee-api`

## 데이터베이스 구성
사용 데이터베이스는 `test` 스키마이며, 학생 정보를 저장하기 위한 `student` 테이블을 사용한다.

### student 테이블 필드
- `id`: 학생 번호, 기본 키, 자동 증가
- `name`: 학생 이름
- `univ`: 소속 대학교
- `birthdate`: 생년월일
- `email`: 이메일 주소

### 테이블 생성 예시
```sql
CREATE TABLE student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    univ VARCHAR(100) NOT NULL,
    birthdate VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL
);