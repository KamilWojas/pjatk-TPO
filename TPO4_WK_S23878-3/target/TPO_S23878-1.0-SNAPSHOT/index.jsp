<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <style>
        body {
            line-height: 0.7;
        }
        .tile {
            display: inline-block;
            padding: 10px;
            background-color: #e0e0e0;
            margin-bottom: 10px;
            color: #000;
            text-decoration: none;
            border: 1px solid #000;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .tile:active {
            background-color: #ccc;
        }
    </style>
</head>
<body>
<h1><%= "Przeglądaj lub wyszukaj książkę" %></h1>
<br><br>
<a href="Books" class="tile">1. Wyświetl wszystkie książki</a><br><br>
<a href="SearchForm.jsp" class="tile">2. Wyszukaj książkę</a>
</body>
</html>