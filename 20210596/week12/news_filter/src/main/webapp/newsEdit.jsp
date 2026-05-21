<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>뉴스 수정</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container w-75 mt-5 mx-auto">
        <h2>뉴스 수정</h2>
        <hr>

        <form method="post" action="news.ice?action=updateNews" enctype="multipart/form-data">
            <input type="hidden" name="aid" value="${news.aid}">

            <label class="form-label">제목</label>
            <input type="text" name="title" class="form-control" value="${news.title}" required>

            <label class="form-label mt-2">현재 이미지</label>
            <div class="mb-2">
                <c:if test="${news.img != null && news.img != ''}">
                    <img src="${news.img}" class="img-fluid" style="max-width: 400px;" alt="현재 이미지">
                </c:if>

                <c:if test="${news.img == null || news.img == ''}">
                    <p class="text-muted">등록된 이미지가 없습니다.</p>
                </c:if>
            </div>

            <label class="form-label mt-2">새 이미지 선택</label>
            <input type="file" name="file" class="form-control">
            <div class="form-text">새 이미지를 선택하지 않으면 기존 이미지가 유지됩니다.</div>

            <label class="form-label mt-2">기사 내용</label>
            <textarea rows="8" name="content" class="form-control" required>${news.content}</textarea>

            <button type="submit" class="btn btn-success mt-3">수정 완료</button>
            <a href="news.ice?action=getNews&aid=${news.aid}" class="btn btn-secondary mt-3">취소</a>
        </form>
    </div>
</body>
</html>