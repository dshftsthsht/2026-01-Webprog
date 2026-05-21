# Week 12 - Listener & Filter 실습

## 작성일
2026년 5월 14일

## 프로젝트 개요

이번 주차에는 기존 뉴스 관리 웹 프로젝트를 기반으로 **서블릿 필터(Filter)** 기능을 추가하는 실습을 진행하였다.

기존 `news_project`에서는 `NewsController.java` 내부에서 직접 한글 인코딩을 처리하였다.

```java
request.setCharacterEncoding("UTF-8");
```

이번 실습에서는 위 코드를 주석 처리하고, 새로 만든 `NewsFilter.java`에서 한글 인코딩을 처리하도록 수정하였다.

즉, 컨트롤러가 직접 처리하던 공통 기능을 필터로 분리하여 서블릿 실행 전에 요청을 먼저 처리하는 구조를 구현하였다.

## 실습 목표

- 서블릿 필터의 개념 이해
- 필터가 요청을 가로채는 동작 구조 확인
- 한글 인코딩 처리를 필터로 분리
- 기존 뉴스 프로젝트를 `news_filter` 프로젝트로 복사 및 수정
- Maven 빌드 후 Tomcat에 배포하여 실행 확인

## 구현 내용

### 1. 기존 뉴스 프로젝트 복사

기존에 작성한 `news_project`를 복사하여 새로운 프로젝트인 `news_filter`를 생성하였다.

```text
week12
└── news_filter
```

기존 프로젝트의 기능은 다음과 같다.

- 뉴스 목록 조회
- 뉴스 상세 보기
- 뉴스 등록
- 뉴스 삭제
- 뉴스 수정
- 이미지 업로드
- MariaDB 연동

### 2. pom.xml 수정

복사한 프로젝트의 Maven 설정을 `news_filter`에 맞게 수정하였다.

수정한 주요 항목은 다음과 같다.

```xml
<artifactId>news_filter</artifactId>
<name>news_filter Maven WebApp</name>
<finalName>news_filter</finalName>
```

이를 통해 Maven 빌드 결과물이 `news_filter.war`로 생성되도록 설정하였다.

### 3. NewsController.java 수정

기존 `NewsController.java`의 `service()` 메서드에는 한글 인코딩 처리를 위한 코드가 있었다.

```java
request.setCharacterEncoding("UTF-8");
```

필터 실습을 위해 해당 코드를 주석 처리하였다.

```java
// request.setCharacterEncoding("UTF-8");
```

이렇게 수정하여 컨트롤러가 직접 한글 인코딩을 처리하지 않고, 필터가 먼저 요청을 가로채서 처리하도록 변경하였다.

### 4. NewsFilter.java 추가

새롭게 `NewsFilter.java` 파일을 생성하였다.

`NewsFilter`는 `javax.servlet.Filter` 인터페이스를 구현하며, `doFilter()` 메서드에서 POST 요청에 대해 UTF-8 인코딩을 설정한다.

```java
@WebFilter("/*")
public class NewsFilter implements Filter {
    ...
}
```

주요 코드는 다음과 같다.

```java
if (httpReq.getMethod().equalsIgnoreCase("POST")) {
    request.setCharacterEncoding("UTF-8");
}
```

이 코드를 통해 뉴스 등록이나 수정처럼 POST 방식으로 전달되는 요청에서 한글이 깨지지 않도록 처리하였다.

## 필터 동작 흐름

필터는 클라이언트 요청이 컨트롤러에 도달하기 전에 먼저 실행된다.

동작 순서는 다음과 같다.

```text
클라이언트 요청
→ NewsFilter 실행
→ UTF-8 인코딩 처리
→ NewsController 실행
→ NewsDAO를 통해 DB 처리
→ JSP 화면 출력
```

이번 실습에서는 `NewsController.java`에서 직접 인코딩을 처리하지 않아도 `NewsFilter.java`가 먼저 실행되어 한글 데이터를 정상적으로 처리하도록 구성하였다.

## 데이터베이스 설정

기존 뉴스 프로젝트에서 사용하던 MariaDB 데이터베이스를 그대로 사용하였다.

DB 이름:

```text
mywebdb
```

테이블 이름:

```text
news
```

테이블 구조:

```sql
CREATE TABLE news (
    aid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(1024) NOT NULL,
    img VARCHAR(512) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content VARCHAR(8192) NOT NULL
);
```

XAMPP MySQL 포트 충돌 문제로 인해 MySQL 포트를 `3307`로 변경하여 사용하였다.

`NewsDAO.java`의 DB 연결 주소는 다음과 같이 설정하였다.

```java
final String url = "jdbc:mariadb://localhost:3307/" + dbname;
```

## Maven 빌드

Maven package 명령을 실행하여 WAR 파일을 생성하였다.

```powershell
& "D:\xampp\apache-maven-3.9.9\bin\mvn.cmd" package -f "C:\Users\User\OneDrive\Desktop\20210596\2026-01-Webprog\20210596\week12\news_filter\pom.xml"
```

빌드 성공 시 다음과 같은 결과물이 생성된다.

```text
target/news_filter.war
```

## Tomcat 배포

생성된 `news_filter.war` 파일을 Tomcat의 `webapps` 폴더에 복사하여 배포하였다.

배포 위치:

```text
D:\xampp\tomcat\webapps
```

Tomcat 실행 후 다음 주소로 접속하였다.

```text
http://localhost:8080/news_filter/
```

또는

```text
http://localhost:8080/news_filter/news.ice?action=listNews
```

## 실행 확인

실행 후 다음 기능을 확인하였다.

- 뉴스 목록 조회
- 뉴스 등록
- 한글 제목 입력
- 한글 기사 내용 입력
- 이미지 업로드
- 뉴스 상세 보기
- 뉴스 수정
- 뉴스 삭제

특히 이번 실습의 핵심인 한글 인코딩 처리는 `NewsController.java`가 아니라 `NewsFilter.java`에서 처리하도록 변경하였다.

## 오늘 작업 요약

- 기존 `news_project`를 복사하여 `news_filter` 프로젝트를 생성함
- `pom.xml`에서 프로젝트 이름과 WAR 파일 이름을 `news_filter`로 변경함
- `NewsFilter.java`를 새로 생성함
- `Filter` 인터페이스를 구현함
- `doFilter()` 메서드에서 POST 요청에 대해 UTF-8 인코딩을 적용함
- `NewsController.java`의 `request.setCharacterEncoding("UTF-8")` 코드를 주석 처리함
- 필터가 컨트롤러보다 먼저 실행되어 한글 인코딩을 처리하도록 구성함
- Maven package를 통해 `news_filter.war` 파일을 생성함
- Tomcat `webapps` 폴더에 WAR 파일을 배포함
- 뉴스 등록, 조회, 수정, 삭제, 이미지 업로드 기능을 확인함

## 주요 특징

- Servlet Filter 적용
- 한글 인코딩 처리 분리
- MVC 구조 유지
- MariaDB JDBC 연동
- JSP와 JSTL을 활용한 화면 구성
- 이미지 업로드 기능 포함
- Maven 기반 WAR 빌드
- Tomcat 배포 및 실행 확인