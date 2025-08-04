<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Item" %>

<%
    // Block unauthorized access - only storekeeper role allowed
    if (session == null || !"storekeeper".equals(((model.User) session.getAttribute("user")).getRole())) {
        response.sendRedirect("../login.jsp?error=Unauthorized+access");
        return;
    }
%>
<%@ include file="../sidebars/storekeeper_sidebar.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Manage Items - Pahana Edu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>

<div class="main-content p-4">
    <h2>📦 Manage Items</h2>

    <!-- Search + Add New Button Section -->
    <div class="row mb-3 align-items-center">
        <div class="col-md-8">
            <form action="items" method="get" class="d-flex gap-2">
                <input type="text" name="search" class="form-control" placeholder="Search by item name"
                       value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>" />
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="items" class="btn btn-secondary">Clear</a>
            </form>
        </div>
        <div class="col-md-4 text-end mt-2 mt-md-0">
            <a href="add_item" class="btn btn-success">➕ Add New Item</a>
        </div>
    </div>

    <!-- Optional: show search keyword -->
    <%
        String searchTerm = request.getParameter("search");
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
    %>
        <p>Showing results for: "<strong><%= searchTerm %></strong>"</p>
    <%
        }
    %>

    <!-- Items Table -->
    <table class="table table-striped table-bordered align-middle">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Category</th>
                <th>Name</th>
                <th>Brand</th>
                <th>Unit Price (Rs.)</th>
                <th>Stock Quantity</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<model.Item> items = (List<model.Item>) request.getAttribute("items");
                if (items != null && !items.isEmpty()) {
                    for (model.Item item : items) {
            %>
            <tr>
                <td><%= item.getItemId() %></td>
                <td><%= item.getCategoryName() != null ? item.getCategoryName() : "N/A" %></td>
                <td><%= item.getName() %></td>
                <td><%= item.getBrand() %></td>
                <td><%= String.format("%.2f", item.getUnitPrice()) %></td>
                <td><%= item.getStockQuantity() %></td>
                <td>
                    <a href="edit_item?id=<%= item.getItemId() %>" class="btn btn-sm btn-warning">✏️ Edit</a>
                    <a href="update_stock?id=<%= item.getItemId() %>" class="btn btn-sm btn-info">🔄 Update Stock</a>
                    <a href="delete_item?id=<%= item.getItemId() %>" class="btn btn-sm btn-danger"
                       onclick="return confirm('Are you sure you want to delete this item?');">🗑️ Delete</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="7" class="text-center">No items found.</td>
            </tr>
            <% } %>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>
