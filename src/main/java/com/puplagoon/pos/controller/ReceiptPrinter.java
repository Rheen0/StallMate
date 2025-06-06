package src.main.java.com.puplagoon.pos.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import src.main.java.com.puplagoon.pos.model.dto.Order;
import src.main.java.com.puplagoon.pos.model.dto.OrderDetail;
import src.main.java.com.puplagoon.pos.model.dto.Product;
import src.main.java.com.puplagoon.pos.model.dto.User;

public class ReceiptPrinter {
     public void printTextFile(String filePath) {
        try {
            // Locate the default printer
            PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
            if (defaultPrinter == null) {
                System.err.println("No default printer found.");
                return;
            }

            // Create a print job
            DocPrintJob printJob = defaultPrinter.createPrintJob();

            // Load the text file
            FileInputStream fis = new FileInputStream(new File(filePath));
            Doc textDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);

            // Print the document
            printJob.print(textDoc, null);
            fis.close();

            System.out.println("File sent to printer: " + filePath);
        } catch (PrintException | IOException e) {
            System.err.println("Failed to print file: " + e.getMessage());
        }
    }
    public void printReceipt(Order order, User currentUser) {
        // Define the file name for the receipt
        String fileName = "receipt_" + System.currentTimeMillis() + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            // Write receipt header
            writer.write("=== POS Receipt ===\n");
            writer.write("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("Order ID: " + order.getOrderId() + "\n");
            writer.write("===================\n\n");

            for (Product product : order.getDetails().stream().map(OrderDetail::getProduct).toList()) {
                // Write product details

                writer.write("Category: " + product.getCategory() + "\n");
                writer.write("Size: " + product.getSize() + "\n");
                writer.write("Sugar Level: " + product.getSugarLevel() + "\n");
            }
            // Write order details
            for (OrderDetail detail : order.getDetails()) {
                writer.write("Quantity: " + detail.getQuantity() + "\n");
                writer.write("Unit Price: ₱" + String.format("%.2f", detail.getUnitPrice()) + "\n");
                writer.write("Subtotal: ₱" + String.format("%.2f", detail.getSubtotal()) + "\n\n");
            }
            // Write employee handler
            writer.write("Handled by: " + currentUser.getName() + "\n");

            // Write total amount
            writer.write("===================\n");
            writer.write("Total: ₱" + String.format("%.2f", order.getTotalAmount()) + "\n");
            writer.write("===================\n");

            // Confirm receipt generation
            System.out.println("Receipt saved to: " + fileName);
            // Print the receipt
            printTextFile(fileName);
        } catch (IOException e) {
            System.err.println("Failed to save receipt: " + e.getMessage());
        }
    }
}