package src.main.java.com.puplagoon.pos.model.dao;

import java.sql.*;
import src.main.java.com.puplagoon.pos.model.dto.Order;
import src.main.java.com.puplagoon.pos.model.dto.OrderDetail;

public class OrderDAO {
    private final Connection connection;

    public OrderDAO() {
        this.connection = DBConnection.getConnection();
    }

    public int getMostRecentOrderId() {
        String query = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("order_id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // Return -1 if no orders are found or an error occurs
    }

    public boolean saveOrder(Order order) throws SQLException {
        connection.setAutoCommit(false);
        try {
            // 1) Insert into Orders
            String orderQuery = "INSERT INTO Orders (customer_id, total_amount) VALUES (?, ?)";
            int orderId;
            try (PreparedStatement stmt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setObject(1, null); // no customer for now
                stmt.setDouble(2, order.getTotalAmount());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get order ID");
                    }
                }
            }

            // 2) Insert order details in batch
            String detailQuery = """
                    INSERT INTO Order_Detail
                      (order_id, product_id, quantity, unit_price, subtotal)
                    VALUES (?, ?, ?, ?, ?)
                    """;
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
