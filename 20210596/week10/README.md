# Week 10 - Product MVC 프로젝트

## 작성일
2026년 5월 6일

## 프로젝트 개요
클래스 다이어그램에 따라 Product MVC 패턴 기반 웹 애플리케이션 구현

## 구현 내용

### 1. 모델 (Model)
- **Product.java**
  - 상품 정보를 저장하는 모델 클래스
  - 속성: id, name, maker, price, date
  - Getter 메서드로 데이터 접근

### 2. 서비스 (Service)
- **ProductService.java**
  - `Map<String, Product>` 기반 상품 저장소
  - `findAll()`: 모든 상품 목록 반환
  - `find(id)`: 특정 상품 검색
  - 초기 데이터 3개 상품 포함 (노트북, 스마트폰, 무선 이어폰)

### 3. 컨트롤러 (Controller)
- **ProductController.java**
  - `HttpServlet` 확장
  - URL 매핑: `/product`
  - `doGet()`: 요청 처리 및 JSP로 포워드
  - 지원 액션:
    - `action=list` (기본): 상품 목록 조회
    - `action=info&id=...`: 상품 상세 조회

### 4. 뷰 (View)
- **productList.jsp**
  - 모든 상품을 테이블 형식으로 표시
  - 상품 코드, 상품명, 제조사, 가격(쉼표 형식), 등록일 표시
  - 각 상품의 "상세보기" 링크로 상세 페이지 연결

- **productInfo.jsp**
  - 선택한 상품의 상세 정보 표시
  - 상품이 없을 경우 오류 메시지 출력
  - 목록으로 돌아가기 링크 제공

### 5. 설정 파일
- **web.xml**
  - ProductController 서블릿 등록
  - `/product` URL 패턴 매핑
  - welcome-file: index.jsp

- **index.jsp**
  - 홈 페이지로 "상품 목록 보기" 링크 제공

- **pom.xml**
  - Java 컴파일 버전 업데이트 (1.7 → 1.8)

## 빌드 결과
✅ Maven compile 성공

## 프로젝트 구조
```
productmvc/
├── src/main/
│   ├── java/kr/hnu/ice/productmvc/
│   │   ├── Product.java
│   │   ├── ProductService.java
│   │   └── ProductController.java
│   └── webapp/
│       ├── index.jsp
│       └── WEB-INF/
│           ├── web.xml
│           ├── productList.jsp
│           └── productInfo.jsp
├── pom.xml
└── target/
```

## 사용 방법
1. `mvn clean install` 실행
2. 톰캣에 WAR 배포
3. `http://localhost:8080/productmvc/` 접속
4. "상품 목록 보기" 클릭하여 상품 확인
5. 각 상품의 "상세보기"로 상세 정보 조회

## 주요 특징
- MVC 패턴 적용
- JSP를 이용한 동적 페이지 생성
- 서블릿 기반 요청 처리
- 한글 문자 인코딩 지원 (UTF-8)
- CSS를 통한 기본 스타일링
