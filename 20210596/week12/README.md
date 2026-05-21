# Week 12 - Listener & Filter 실습

## 작성일
2026년 5월 14일

## 프로젝트 개요

이번 주차에는 **서블릿 필터(Filter)** 와 **리스너(Listener)** 실습을 진행하였다.

1차시에는 기존 뉴스 관리 웹 프로젝트를 기반으로 `NewsFilter.java`를 추가하여 한글 인코딩 처리를 필터로 분리하였다.

2차시에는 `SessionListener` 프로젝트를 새로 생성하여 `ServletContextListener`, `ServletContextAttributeListener`, `HttpSessionListener`, `HttpSessionAttributeListener`를 구현하고, Tomcat 로그를 통해 리스너의 동작 결과를 확인하는 실습을 진행하였다.

---

# 1차시 - News Filter 실습

## 실습 개요

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

---

# 2차시 - SessionListener 실습

## 실습 개요

2차시에는 `SessionListener` 프로젝트를 새로 생성하여 리스너 실습을 진행하였다.

리스너는 웹 애플리케이션에서 특정 이벤트가 발생했을 때 자동으로 실행되는 객체이다. 이번 실습에서는 서블릿 컨텍스트와 세션의 생성, 종료, 속성 추가, 속성 변경, 속성 삭제 이벤트를 감지하도록 구현하였다.

실행 결과는 브라우저 화면이 아니라 Tomcat 로그 파일에서 확인하였다.

## 실습 목표

- 리스너의 개념 이해
- `@WebListener` 애너테이션 사용
- `ServletContextListener` 구현
- `ServletContextAttributeListener` 구현
- `HttpSessionListener` 구현
- `HttpSessionAttributeListener` 구현
- 서블릿 실행 후 Tomcat 로그에서 리스너 동작 결과 확인

## 프로젝트 구조

```text
SessionListener/
├── src/main/
│   ├── java/kr/hnu/ice/
│   │   ├── ListenerExam.java
│   │   └── ListenerTestServlet.java
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml
│       └── index.jsp
├── pom.xml
└── README.md
```

## 구현 내용

### 1. SessionListener 프로젝트 생성

`week12` 폴더 안에 새로운 Maven WebApp 프로젝트인 `SessionListener`를 생성하였다.

```text
week12
├── news_filter
└── SessionListener
```

### 2. ListenerExam.java 생성

`ListenerExam.java` 파일을 생성하고, 여러 리스너 인터페이스를 구현하였다.

구현한 인터페이스는 다음과 같다.

```java
ServletContextListener
ServletContextAttributeListener
HttpSessionListener
HttpSessionAttributeListener
```

`@WebListener` 애너테이션을 사용하여 별도의 `web.xml` 등록 없이 리스너가 자동으로 동작하도록 설정하였다.

```java
@WebListener
public class ListenerExam implements ServletContextListener,
        ServletContextAttributeListener,
        HttpSessionListener,
        HttpSessionAttributeListener {
    ...
}
```

### 3. ServletContextListener 구현

웹 애플리케이션이 시작되거나 종료될 때 동작하도록 구현하였다.

```java
@Override
public void contextInitialized(ServletContextEvent sce) {
    sce.getServletContext().log("[ServletContextListener] contextInitialized 호출");
    System.out.println("[ServletContextListener] contextInitialized 호출");
}

@Override
public void contextDestroyed(ServletContextEvent sce) {
    sce.getServletContext().log("[ServletContextListener] contextDestroyed 호출");
    System.out.println("[ServletContextListener] contextDestroyed 호출");
}
```

이를 통해 Tomcat이 웹 애플리케이션을 시작하거나 종료할 때 로그가 출력되도록 하였다.

### 4. ServletContextAttributeListener 구현

서블릿 컨텍스트에 속성이 추가, 삭제, 변경될 때 동작하도록 구현하였다.

```java
@Override
public void attributeAdded(ServletContextAttributeEvent event) {
    event.getServletContext().log(
        "[ServletContextAttributeListener] attributeAdded 호출 - name="
        + event.getName() + ", value=" + event.getValue()
    );
}
```

확인한 이벤트는 다음과 같다.

- context attribute 추가
- context attribute 삭제
- context attribute 변경

### 5. HttpSessionListener 구현

세션이 생성되거나 종료될 때 동작하도록 구현하였다.

```java
@Override
public void sessionCreated(HttpSessionEvent se) {
    se.getSession().getServletContext().log(
        "[SessionListener] Session 생성 - " + se.getSession().getId()
    );
}
```

확인한 이벤트는 다음과 같다.

- Session 생성
- Session 종료

### 6. HttpSessionAttributeListener 구현

세션 속성이 추가, 삭제, 변경될 때 동작하도록 구현하였다.

```java
@Override
public void attributeAdded(HttpSessionBindingEvent event) {
    event.getSession().getServletContext().log(
        "[SessionAttributeListener] Session 속성 추가 - name="
        + event.getName() + ", value=" + event.getValue()
    );
}
```

확인한 이벤트는 다음과 같다.

- Session 속성 추가
- Session 속성 삭제
- Session 속성 변경

### 7. ListenerTestServlet.java 생성

리스너 이벤트를 발생시키기 위해 `ListenerTestServlet.java` 파일을 생성하였다.

이 서블릿은 실행 시 `ServletContext`와 `HttpSession`에 속성을 추가, 변경, 삭제하여 리스너가 동작하도록 한다.

```java
@WebServlet("/ListenerTestServlet")
public class ListenerTestServlet extends HttpServlet {
    ...
}
```

서블릿의 `init()` 메서드에서는 `ServletContext` 속성을 처리하였다.

```java
ServletContext sc = getServletContext();

sc.setAttribute("scName", "홍길동");
sc.setAttribute("scName", "김철수");
sc.removeAttribute("scName");
sc.setAttribute("scName", "홍길동");
```

`doGet()` 메서드에서는 세션 속성을 처리하였다.

```java
HttpSession session = request.getSession();

session.setAttribute("sName", "홍길동");
session.setAttribute("sName", "김철수");
session.removeAttribute("sName");
session.setAttribute("sName", "홍길동");
```

이를 통해 세션 생성과 세션 속성 변화 로그를 확인할 수 있도록 하였다.

### 8. web.xml 생성

`src/main/webapp/WEB-INF/web.xml` 파일을 생성하였다.

기본 welcome 파일을 `index.jsp`로 설정하였다.

```xml
<welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

### 9. index.jsp 생성

`index.jsp` 파일을 생성하여 `ListenerTestServlet`으로 이동할 수 있는 링크를 작성하였다.

```jsp
<a href="ListenerTestServlet">ListenerTestServlet 실행</a>
```

## Maven 빌드

Maven package 명령을 실행하여 WAR 파일을 생성하였다.

```powershell
& "D:\xampp\apache-maven-3.9.9\bin\mvn.cmd" package -f "C:\Users\User\OneDrive\Desktop\20210596\2026-01-Webprog\20210596\week12\SessionListener\pom.xml"
```

빌드 성공 시 다음 결과물이 생성된다.

```text
target/SessionListener.war
```

## Tomcat 배포

생성된 `SessionListener.war` 파일을 Tomcat의 `webapps` 폴더에 복사하여 배포하였다.

배포 위치:

```text
D:\xampp\tomcat\webapps
```

Tomcat 실행 후 다음 주소로 접속하였다.

```text
http://localhost:8080/SessionListener/
```

또는

```text
http://localhost:8080/SessionListener/ListenerTestServlet
```

## 실행 결과 확인

브라우저에서 `ListenerTestServlet`을 실행한 뒤 Tomcat 로그 파일을 확인하였다.

로그 파일 위치:

```text
D:\xampp\tomcat\logs
```

확인한 로그 파일 예시:

```text
localhost.2026-05-14.log
```

로그 파일에서 다음과 같은 리스너 실행 결과를 확인하였다.

```text
[ServletContextListener] contextInitialized 호출
[ServletContextAttributeListener] attributeAdded 호출
[ServletContextAttributeListener] attributeReplaced 호출
[ServletContextAttributeListener] attributeRemoved 호출
[SessionListener] Session 생성
[SessionAttributeListener] Session 속성 추가
[SessionAttributeListener] Session 속성 변경
[SessionAttributeListener] Session 속성 삭제
```

## 2차시 작업 요약

- `SessionListener` Maven WebApp 프로젝트를 새로 생성함
- `ListenerExam.java` 파일을 생성함
- `@WebListener` 애너테이션을 적용함
- `ServletContextListener`를 구현하여 웹 애플리케이션 시작과 종료 이벤트를 확인함
- `ServletContextAttributeListener`를 구현하여 컨텍스트 속성 추가, 삭제, 변경 이벤트를 확인함
- `HttpSessionListener`를 구현하여 세션 생성과 종료 이벤트를 확인함
- `HttpSessionAttributeListener`를 구현하여 세션 속성 추가, 삭제, 변경 이벤트를 확인함
- `ListenerTestServlet.java`를 생성하여 리스너 이벤트를 강제로 발생시킴
- `index.jsp`에서 `ListenerTestServlet` 실행 링크를 제공함
- Tomcat 로그 파일에서 리스너 실행 결과를 확인함

---

# 전체 작업 요약

## 1차시

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

## 2차시

- `SessionListener` 프로젝트를 새로 생성함
- `ListenerExam.java`와 `ListenerTestServlet.java`를 작성함
- 서블릿 컨텍스트와 세션의 생명 주기 이벤트를 확인함
- 컨텍스트 속성과 세션 속성의 추가, 삭제, 변경 이벤트를 확인함
- Tomcat 로그 파일에서 리스너 실행 결과를 확인함

## 주요 특징

- Servlet Filter 적용
- Servlet Listener 적용
- 한글 인코딩 처리 분리
- `@WebFilter` 사용
- `@WebListener` 사용
- MVC 구조 유지
- MariaDB JDBC 연동
- JSP와 JSTL을 활용한 화면 구성
- 이미지 업로드 기능 포함
- Maven 기반 WAR 빌드
- Tomcat 배포 및 실행 확인
- Tomcat 로그 파일을 통한 리스너 동작 확인