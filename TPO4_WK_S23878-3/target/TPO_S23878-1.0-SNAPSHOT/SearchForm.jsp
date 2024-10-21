<%--
  Created by IntelliJ IDEA.
  User: kamilwojas
  Date: 23/05/2023
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        form {
            line-height: 0.7;
        }
        .button {
            display: inline-block;
            padding: 5px 10px;
            background-color: #e0e0e0;
            color: #000;
            text-decoration: none;
            border: 1px solid #000;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .button:active {
            background-color: #ccc;
        }
    </style>
</head>
<body>
<form action="BookSearch" name="SearchForm" method="GET">
    Tytuł: <input type="text" name="Tytuł"/> <br><br><br>
    Autor: <input type="text" name="Autor"/> <br><br><br>
    <input type="submit" value="Szukaj" class="button">
</form>
<a href="index.jsp" class="button">Wstecz</a>
</body>
</html>
