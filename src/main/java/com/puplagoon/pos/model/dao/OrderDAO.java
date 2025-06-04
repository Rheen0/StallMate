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
            String orderQuery = "INSERT INTO Orders (customer_id, total_amount) VALUES (?, ?)";
            int orderId;

            try (PreparedStatement stmt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setObject(1, null); // no customer for now
                stmt.setDouble(2, order.getTotalAmount());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                        order.setOrderId(orderId); // Set the generated ID back to the Order object
                    } else {
                        throw new SQLException("Failed to get order ID");
                    }
                }
            }

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
