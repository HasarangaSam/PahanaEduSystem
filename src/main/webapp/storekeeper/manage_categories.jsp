<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Category" %>
<%
    // Restrict access to storekeeper role
    if (session == null || !"storekeeper".equals(((model.User) session.getAttribute("user")).getRole())) {
        response.sendRedirect("../login.jsp?error=Unauthorized+access");
        return;
    }

    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Categories - Pahana Edu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<%@ include file="../sidebars/storekeeper_sidebar.jsp" %>

<div class="main-content" style="margin-left: 240px; padding: 2rem;">
    <div class="container">
        <h3 class="mb-4">Manage Categories</h3>

        <a href="add_category.jsp" class="btn btn-success mb-3">➕ Add New Category</a>

        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Category Name</th>
                    <th style="width: 150px;">Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (categories != null && !categories.isEmpty()) {
                        for (Category c : categories) {
                %>
                    <tr>
                        <td><%= c.getCategoryId() %></td>
                        <td><%= c.getCategoryName() %></td>
                        <td>
                            <a href="edit_category?id=<%= c.getCategoryId() %>" class="btn btn-sm btn-warning">Edit</a>
                            <a href="delete_category?id=<%= c.getCategoryId() %>" class="btn btn-sm btn-danger"
                               onclick="return confirm('Are you sure you want to delete this category?');">Delete</a>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="3" class="text-center">No categories found.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>
