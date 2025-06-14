package src.main.java.com.puplagoon.pos.model.dao;

import src.main.java.com.puplagoon.pos.model.dto.Order;
import src.main.java.com.puplagoon.pos.model.dto.OrderDetail;

import java.sql.*;

public class OrderDAO {
    private final Connection connection;

    public OrderDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean saveOrder(Order order) throws SQLException {
        connection.setAutoCommit(false);
        try {
            // Modified query to match your actual orders table structure
            String orderQuery = "INSERT INTO orders (product_id, quantity_in_stock) VALUES (?, ?)";
            int orderId;

            try (PreparedStatement stmt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                // Since your orders table only has product_id and quantity_in_stock,
                // we'll use the first product in the order for these values
                // This is problematic - see note below
                OrderDetail firstDetail = order.getDetails().get(0);
                stmt.setInt(1, firstDetail.getProduct().getProductId());
                stmt.setInt(2, firstDetail.getQuantity());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                        order.setOrderId(orderId);
                    } else {
                        throw new SQLException("Failed to get order ID");
                    }
                }
            }

            String detailQuery = "INSERT INTO order_detail (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(detailQuery)) {
                for (OrderDetail detail : order.getDetails()) {
                    stmt.setInt(1, orderId);
                    stmt.setInt(2, detail.getProduct().getProductId());
                    stmt.setInt(3, detail.getQuantity());
                    stmt.setDouble(4, detail.getUnitPrice());
                    stmt.setDouble(5, detail.getSubtotal());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
