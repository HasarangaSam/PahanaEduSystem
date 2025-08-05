package test;

import dao.CategoryDAO;
import model.Category;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryDAOTest {

    private CategoryDAO categoryDAO;

    @Before
    public void setUp() {
        categoryDAO = new CategoryDAO();
    }

    @Test
    public void testAddAndGetCategoryById() {
        // Add category
        Category category = new Category();
        category.setCategoryName("JUnitTestCategory");
        categoryDAO.addCategory(category);

        // Fetch all to find the new one
        List<Category> categories = categoryDAO.getAllCategories();
        assertFalse(categories.isEmpty());

        Category last = categories.get(categories.size() - 1);
        assertEquals("JUnitTestCategory", last.getCategoryName());

        // Fetch by ID
        Category fetched = categoryDAO.getCategoryById(last.getCategoryId());
        assertNotNull(fetched);
        assertEquals("JUnitTestCategory", fetched.getCategoryName());
    }

    @Test
    public void testUpdateCategory() {
        // First insert
        Category category = new Category();
        category.setCategoryName("OldName");
        categoryDAO.addCategory(category);

        // Fetch inserted category
        List<Category> categories = categoryDAO.getAllCategories();
        Category toUpdate = categories.get(categories.size() - 1);

        // Update
        toUpdate.setCategoryName("UpdatedName");
        categoryDAO.updateCategory(toUpdate);

        // Fetch again
        Category updated = categoryDAO.getCategoryById(toUpdate.getCategoryId());
        assertEquals("UpdatedName", updated.getCategoryName());
    }

    @Test
    public void testDeleteCategory() {
        // Add a category
        Category category = new Category();
        category.setCategoryName("ToDelete");
        categoryDAO.addCategory(category);

        // Fetch the new category
        List<Category> categories = categoryDAO.getAllCategories();
        Category toDelete = categories.get(categories.size() - 1);

        // Delete
        categoryDAO.deleteCategory(toDelete.getCategoryId());

        // Should return null now
        Category deleted = categoryDAO.getCategoryById(toDelete.getCategoryId());
        assertNull(deleted);
    }

    @Test
    public void testGetAllCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() >= 0); // Should run without error
    }
}
