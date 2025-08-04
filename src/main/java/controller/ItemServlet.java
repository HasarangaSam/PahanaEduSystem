package controller;

import dao.ItemDAO;
import dao.CategoryDAO;
import model.Item;
import model.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
    "/storekeeper/items",          // View/search items
    "/storekeeper/add_item",       // Add item (GET form, POST add)
    "/storekeeper/edit_item",      // Edit item (GET form, POST update)
    "/storekeeper/delete_item",    // Delete item (GET)
    "/storekeeper/update_stock",    // Update stock (GET form, POST update)
    "/admin/view_items"          // Admin view items
})
public class ItemServlet extends HttpServlet {

    private ItemDAO itemDAO;

    @Override
    public void init() {
        itemDAO = new ItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // View/Search items
        if ("/storekeeper/items".equals(path)) {
            String search = request.getParameter("search");

            List<Item> itemList = (search != null && !search.trim().isEmpty())
                ? itemDAO.searchItemsByName(search)
                : itemDAO.getAllItems();

            request.setAttribute("items", itemList);
            request.getRequestDispatcher("/storekeeper/manage_items.jsp").forward(request, response);
        }

        // Show Add Item Form (load categories)
        else if ("/storekeeper/add_item".equals(path)) {
            try {
                List<Category> categories = new CategoryDAO().getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/storekeeper/add_item.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("items?error=Unable+to+load+add+item+form");
            }
        }

        // Show Edit Item Form
        else if ("/storekeeper/edit_item".equals(path)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Item item = itemDAO.getItemById(id);
                if (item != null) {
                    List<Category> categories = new CategoryDAO().getAllCategories();
                    request.setAttribute("item", item);
                    request.setAttribute("categories", categories);
                    request.getRequestDispatcher("/storekeeper/edit_item.jsp").forward(request, response);
                } else {
                    response.sendRedirect("items?error=Item+not+found");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("items?error=Invalid+item+ID");
            }
        }

        // Delete Item
        else if ("/storekeeper/delete_item".equals(path)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                itemDAO.deleteItem(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect("items");
        }

        // Show Update Stock Form
        else if ("/storekeeper/update_stock".equals(path)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Item item = itemDAO.getItemById(id);
                if (item != null) {
                    request.setAttribute("item", item);
                    request.getRequestDispatcher("/storekeeper/update_stock.jsp").forward(request, response);
                } else {
                    response.sendRedirect("items?error=Item+not+found");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("items?error=Invalid+item+ID");
            }
        }
        else if ("/admin/view_items".equals(path)) {
            List<Item> itemList = itemDAO.getAllItems();
            request.setAttribute("items", itemList);
            request.getRequestDispatcher("/admin/view_items.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Add New Item
        if ("/storekeeper/add_item".equals(path)) {
            Item item = extractItemFromRequest(request, false);

            if (item != null) {
                itemDAO.addItem(item);
                response.sendRedirect("items");
            } else {
                response.sendRedirect("add_item.jsp?error=Invalid+input");
            }
        }

        // Update Item
        else if ("/storekeeper/edit_item".equals(path)) {
            Item item = extractItemFromRequest(request, true);

            if (item != null) {
                itemDAO.updateItem(item);
                response.sendRedirect("items");
            } else {
                response.sendRedirect("edit_item?id=" + request.getParameter("id") + "&error=Invalid+input");
            }
        }

        // Update Stock Only
        else if ("/storekeeper/update_stock".equals(path)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                int newStock = Integer.parseInt(request.getParameter("stock_quantity"));

                if (newStock < 0) {
                    response.sendRedirect("update_stock?id=" + id + "&error=Stock+cannot+be+negative");
                    return;
                }

                itemDAO.updateStock(id, newStock);
                response.sendRedirect("items");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("items?error=Invalid+input");
            }
        }
    }

    // Extract item data from form
    private Item extractItemFromRequest(HttpServletRequest request, boolean includeId) {
        try {
            int categoryId = Integer.parseInt(request.getParameter("category_id"));
            String name = request.getParameter("name");
            String brand = request.getParameter("brand");
            double unitPrice = Double.parseDouble(request.getParameter("unit_price"));
            int stockQuantity = Integer.parseInt(request.getParameter("stock_quantity"));

            if (name == null || name.trim().isEmpty() || brand == null || brand.trim().isEmpty()
                    || unitPrice < 0 || stockQuantity < 0) {
                return null;
            }

            Item item = new Item();
            item.setCategoryId(categoryId);
            item.setName(name.trim());
            item.setBrand(brand.trim());
            item.setUnitPrice(unitPrice);
            item.setStockQuantity(stockQuantity);

            if (includeId) {
                int id = Integer.parseInt(request.getParameter("id"));
                item.setItemId(id);
            }

            return item;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
