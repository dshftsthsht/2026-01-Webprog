<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="kr.hnu.ice.Student" %>

<%
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
    Student editStudent = (Student) request.getAttribute("student");
    String mode = (String) request.getAttribute("mode");

    boolean isEdit = "edit".equals(mode) && editStudent != null;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student JDBC</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }

        h1 {
            margin-bottom: 20px;
        }

        form {
            margin-bottom: 30px;
            padding: 20px;
            border: 1px solid #ccc;
            width: 500px;
        }

        input {
            display: block;
            width: 95%;
            padding: 8px;
            margin: 8px 0;
        }

        button {
            padding: 8px 16px;
            cursor: pointer;
        }

        table {
            border-collapse: collapse;
            width: 900px;
        }

        th, td {
            border: 1px solid #aaa;
            padding: 8px;
            text-align: center;
        }

        th {
            background-color: #eee;
        }

        .btn {
            display: inline-block;
            padding: 5px 10px;
            margin: 2px;
            text-decoration: none;
            border: 1px solid #888;
            color: black;
            background-color: #f5f5f5;
        }

        .delete {
            background-color: #ffdddd;
        }

        .update {
            background-color: #ddffdd;
        }
    </style>

    <script>
        function confirmDelete(id) {
            if (confirm("정말 삭제하시겠습니까?")) {
                location.href = "student?action=delete&id=" + id;
            }
        }
    </script>
</head>
<body>
    <h1>Student JDBC</h1>

    <% if (isEdit) { %>
        <h2>학생 정보 수정</h2>

        <form action="student" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="<%= editStudent.getId() %>">

            <label>이름</label>
            <input type="text" name="name" value="<%= editStudent.getName() %>" required>

            <label>대학교</label>
            <input type="text" name="univ" value="<%= editStudent.getUniv() %>" required>

            <label>생년월일</label>
            <input type="text" name="birthdate" value="<%= editStudent.getBirthdate() %>" required>

            <label>이메일</label>
            <input type="email" name="email" value="<%= editStudent.getEmail() %>" required>

            <button type="submit">수정</button>
            <a href="student?action=list" class="btn">취소</a>
        </form>
    <% } else { %>
        <h2>학생 정보 입력</h2>

        <form action="student" method="post">
            <input type="hidden" name="action" value="insert">

            <label>이름</label>
            <input type="text" name="name" required>

            <label>대학교</label>
            <input type="text" name="univ" required>

            <label>생년월일</label>
            <input type="text" name="birthdate" placeholder="예: 2000-01-01" required>

            <label>이메일</label>
            <input type="email" name="email" required>

            <button type="submit">등록</button>
        </form>
    <% } %>

    <h2>학생 목록</h2>

    <table>
        <tr>
            <th>ID</th>
            <th>이름</th>
            <th>대학교</th>
            <th>생년월일</th>
            <th>이메일</th>
            <th>비고</th>
        </tr>

        <%
            if (studentList != null && !studentList.isEmpty()) {
                for (Student student : studentList) {
        %>
        <tr>
            <td><%= student.getId() %></td>
            <td><%= student.getName() %></td>
            <td><%= student.getUniv() %></td>
            <td><%= student.getBirthdate() %></td>
            <td><%= student.getEmail() %></td>
            <td>
                <a class="btn update" href="student?action=edit&id=<%= student.getId() %>">수정</a>
                <a class="btn delete" href="javascript:confirmDelete(<%= student.getId() %>)">삭제</a>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="6">등록된 학생 정보가 없습니다.</td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>