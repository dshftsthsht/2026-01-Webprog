<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kr.hnu.ice.productmvc.Product" %>
<%
    Product product = (Product) request.getAttribute("product");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>상품 정보</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { border-collapse: collapse; width: 60%; margin-top: 16px; }
        th, td { border: 1px solid #999; padding: 8px; }
        th { background-color: #f2f2f2; text-align: left; width: 30%; }
        a { color: #0066cc; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h2>상품 정보</h2>
<% if (product == null) { %>
    <p>요청하신 상품을 찾을 수 없습니다.</p>
<% } else { %>
    <table>
        <tr><th>상품 코드</th><td><%= product.getId() %></td></tr>
        <tr><th>상품명</th><td><%= product.getName() %></td></tr>
        <tr><th>제조사</th><td><%= product.getMaker() %></td></tr>
        <tr><th>가격</th><td><%= String.format("%,d원", product.getPrice()) %></td></tr>
        <tr><th>등록일</th><td><%= product.getDate() %></td></tr>
    </table>
<% } %>
<p><a href="<%= request.getContextPath() %>/product">상품 목록으로 돌아가기</a></p>
</body>
</html>
