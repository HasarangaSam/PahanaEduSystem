package controller;

import dao.CategoryDAO;
import model.Category;
import util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
    "/storekeeper/categories",
    "/storekeeper/add_category",
    "/storekeeper/edit_category",
    "/storekeeper/delete_category"
})
public class CategoryServlet extends HttpServlet {

    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/storekeeper/categories".equals(path)) {
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/storekeeper/manage_categories.jsp").forward(request, response);
        }

        else if ("/storekeeper/edit_category".equals(path)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Category category = categoryDAO.getCategoryById(id);

                if (category != null) {
                    request.setAttribute("category", category);
                    request.getRequestDispatcher("/storekeeper/edit_category.jsp").forward(request, response);
                } else {
                    response.sendRedirect("categories?error=Category+not+found");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("categories?error=Invalid+ID");
            }
        }

        else if ("/storekeeper/delete_category".equals(path)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                categoryDAO.deleteCategory(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect("categories");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/storekeeper/add_category".equals(path)) {
            String name = request.getParameter("category_name");

            if (ValidationUtil.isValidName(name)) {
                Category category = new Category();
                category.setCategoryName(name.trim());

                categoryDAO.addCategory(category);
                response.sendRedirect("categories");
            } else {
                response.sendRedirect("add_category.jsp?error=Invalid+category+name");
            }
        }

        else if ("/storekeeper/edit_category".equals(path)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("category_name");

                if (ValidationUtil.isValidName(name)) {
                    Category category = new Category();
                    category.setCategoryId(id);
                    category.setCategoryName(name.trim());

                    categoryDAO.updateCategory(category);
                    response.sendRedirect("categories");
                } else {
                    response.sendRedirect("edit_category.jsp?id=" + id + "&error=Invalid+category+name");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("categories?error=Update+failed");
            }
        }
    }
}
