
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
<h2>Welcome to My Application</h2>
<p>This is the default welcome page.</p>
<form action="${pageContext.request.contextPath}/auth/login">
    <button type="submit">Login</button>
</form>
</body>
</html>
