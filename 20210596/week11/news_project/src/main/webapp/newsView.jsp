<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>뉴스 보기</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container w-75 mt-5 mx-auto">
        <h2>${news.title}</h2>
        <hr>

        <div class="card">
            <div class="card-body">
                <c:if test="${news.img != null && news.img != ''}">
                    <img src="${news.img}" class="img-fluid mb-3" alt="뉴스 이미지">
                </c:if>

                <h6 class="text-muted">Date: ${news.date}</h6>

                <p class="mt-3" style="white-space: pre-line;">
                    ${news.content}
                </p>
            </div>
        </div>

        <a href="news.ice?action=listNews" class="btn btn-primary mt-3">목록</a>
        <a href="news.ice?action=editNews&aid=${news.aid}" class="btn btn-warning mt-3">수정</a>
    </div>
</body>
</html>