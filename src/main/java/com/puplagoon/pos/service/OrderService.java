package src.main.java.com.puplagoon.pos.service;

import java.sql.SQLException;
import java.util.List;
import src.main.java.com.puplagoon.pos.model.dao.OrderDAO;
import src.main.java.com.puplagoon.pos.model.dao.ProductDAO;
import src.main.java.com.puplagoon.pos.model.dto.Order;
import src.main.java.com.puplagoon.pos.model.dto.Product;

public class OrderService {
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    public List<Product> findAllProducts() {
        try {
            return productDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    public int getMostRecentOrderId() {
        return orderDAO.getMostRecentOrderId();
    }

    public boolean processOrder(Order order) {
        try {
            return orderDAO.saveOrder(order);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
