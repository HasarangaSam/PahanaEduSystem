<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Customer, model.Item" %>

<%
    if (session == null || !"cashier".equals(((model.User) session.getAttribute("user")).getRole())) {
        response.sendRedirect("../login.jsp?error=Unauthorized+access");
        return;
    }

    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
    List<Item> items = (List<Item>) request.getAttribute("items");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Billing - Pahana Edu</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

    <style>
        body {
            background: #f1f5f9;
        }

        .container {
            margin-top: 40px;
            max-width: 900px;
        }

        .card {
            padding: 25px;
        }

        .form-control[readonly] {
            background-color: #e2e8f0;
        }
    </style>
</head>
<body>
<%@ include file="../sidebars/cashier_sidebar.jsp" %>

<div class="container">
    <div class="card shadow">
        <h3 class="mb-4 text-center">🧾 Create Bill</h3>

        <form action="billing" method="post">
            <!-- Customer -->
            <div class="mb-3">
                <label class="form-label">Select Customer</label>
                <select name="customer_id" class="form-select" required>
                    <option disabled selected value="">-- Select Customer --</option>
                    <% for (Customer c : customers) { %>
                        <option value="<%= c.getCustomerId() %>">
                            <%= c.getFirstName() %> <%= c.getLastName() %>
                        </option>
                    <% } %>
                </select>
            </div>

            <!-- Items -->
            <div id="items-section">
                <h5 class="mt-4 mb-2">Add Items</h5>
                <div class="row g-2 item-row">
                    <div class="col-md-4">
                        <select name="item_id" class="form-select item-id" required>
                            <option disabled selected value="">-- Select Item --</option>
                            <% for (Item i : items) { %>
                                <option value="<%= i.getItemId() %>" data-price="<%= i.getUnitPrice() %>">
                                    <%= i.getName() %> - <%= i.getBrand() %> (Rs. <%= i.getUnitPrice() %>)
                                </option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <input type="number" name="quantity" class="form-control quantity" placeholder="Qty" min="1" required>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control unit-price" readonly placeholder="Unit Price">
                    </div>
                    <div class="col-md-3">
                        <button type="button" class="btn btn-danger remove-item">Remove</button>
                    </div>
                </div>
            </div>

            <div class="my-3">
                <button type="button" class="btn btn-secondary" id="add-item">➕ Add Another Item</button>
            </div>

            <hr>
            <div class="text-end">
                <button type="submit" class="btn btn-primary">🧾 Submit Bill</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const itemsSection = document.getElementById("items-section");
        const addItemBtn = document.getElementById("add-item");

        addItemBtn.addEventListener("click", () => {
            const newRow = itemsSection.querySelector(".item-row").cloneNode(true);
            newRow.querySelectorAll("input, select").forEach(input => input.value = "");
            itemsSection.appendChild(newRow);
        });

        itemsSection.addEventListener("click", (e) => {
            if (e.target.classList.contains("remove-item")) {
                const row = e.target.closest(".item-row");
                if (itemsSection.querySelectorAll(".item-row").length > 1) {
                    row.remove();
                }
            }
        });

        itemsSection.addEventListener("change", (e) => {
            if (e.target.classList.contains("item-id")) {
                const price = e.target.selectedOptions[0].getAttribute("data-price");
                const unitPriceField = e.target.closest(".item-row").querySelector(".unit-price");
                unitPriceField.value = price || "";
            }
        });
    });
</script>

</body>
</html>
