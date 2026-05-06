<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="kr.hnu.ice.productmvc.Product" %>
<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    if (products == null) {
        products = new java.util.ArrayList<Product>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>상품 목록</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #999; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        a { color: #0066cc; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h2>상품 목록</h2>
<table>
    <tr>
        <th>상품 코드</th>
        <th>상품명</th>
        <th>제조사</th>
        <th>가격</th>
        <th>등록일</th>
        <th>상세보기</th>
    </tr>
    <% for (Product product : products) { %>
    <tr>
        <td><%= product.getId() %></td>
        <td><%= product.getName() %></td>
        <td><%= product.getMaker() %></td>
        <td><%= String.format("%,d원", product.getPrice()) %></td>
        <td><%= product.getDate() %></td>
        <td><a href="<%= request.getContextPath() %>/product?action=info&id=<%= product.getId() %>">상세보기</a></td>
    </tr>
    <% } %>
</table>
</body>
</html>
