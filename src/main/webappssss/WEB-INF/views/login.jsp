<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<h2>Login</h2>
<form action="${pageContext.request.contextPath}/auth/login" method="post">
    <div>
        <label for="email">Email:</label>
        <input type="text" id="email" name="email" required>
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
    </div>
    <button type="submit">Login</button>
</form>
<div>
    <p style="color:red">${error}</p>
</div>
</body>
</html>
