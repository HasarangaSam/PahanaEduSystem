package test;

import dao.ItemDAO;
import model.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ItemDAOTest {

    private ItemDAO itemDAO;

    @Before
    public void setUp() {
        itemDAO = new ItemDAO();
    }

    @Test
    public void testAddAndGetItem() {
        Item item = new Item();
        item.setCategoryId(1);  
        item.setName("JUnit Item");
        item.setBrand("TestBrand");
        item.setUnitPrice(250.0);
        item.setStockQuantity(20);

        itemDAO.addItem(item);

        List<Item> results = itemDAO.searchItemsByName("JUnit Item");
        assertFalse(results.isEmpty());

        Item saved = results.get(0);
        assertEquals("JUnit Item", saved.getName());
        assertEquals("TestBrand", saved.getBrand());
        assertTrue(saved.getUnitPrice() >= 250.0);
    }

    @Test
    public void testUpdateItem() {
        List<Item> items = itemDAO.searchItemsByName("JUnit Item");
        if (!items.isEmpty()) {
            Item item = items.get(0);
            item.setUnitPrice(299.99);
            item.setStockQuantity(30);
            itemDAO.updateItem(item);

            Item updated = itemDAO.getItemById(item.getItemId());
            assertEquals(299.99, updated.getUnitPrice(), 0.01);
            assertEquals(30, updated.getStockQuantity());
        }
    }

    @Test
    public void testUpdateStock() {
        List<Item> items = itemDAO.searchItemsByName("JUnit Item");
        if (!items.isEmpty()) {
            Item item = items.get(0);
            itemDAO.updateStock(item.getItemId(), 55);

            Item updated = itemDAO.getItemById(item.getItemId());
            assertEquals(55, updated.getStockQuantity());
        }
    }

    @Test
    public void testDeleteItem() {
        List<Item> items = itemDAO.searchItemsByName("JUnit Item");
        if (!items.isEmpty()) {
            int id = items.get(0).getItemId();
            itemDAO.deleteItem(id);

            Item deleted = itemDAO.getItemById(id);
            assertNull(deleted);
        }
    }
}
