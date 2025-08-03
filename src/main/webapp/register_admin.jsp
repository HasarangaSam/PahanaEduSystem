<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5 col-md-6">
    <h3 class="mb-4">Temporary Admin Registration</h3>

    <form action="register-admin" method="post">
        <!-- Username input -->
        <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input type="text" class="form-control" name="username" required>
        </div>

        <!-- Password input -->
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" name="password" required>
        </div>

        <!-- Register button -->
        <button type="submit" class="btn btn-primary">Register Admin</button>
    </form>

    <%
        String success = (String) request.getAttribute("success");
        String error = (String) request.getAttribute("error");
        if (success != null) {
    %>
        <div class="alert alert-success mt-3"><%= success %></div>
    <% } else if (error != null) { %>
        <div class="alert alert-danger mt-3"><%= error %></div>
    <% } %>
</div>
</body>
</html>
