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

            // Write table header
        writer.write(String.format("%-15s %-10s %-10s %-10s\n", "Size", "Quantity", "Unit Price", "Subtotal"));
        writer.write("--------------------------------------------------\n");

        // Write product details in a tabular format
        for (OrderDetail detail : order.getDetails()) {
            writer.write(String.format(
                "%-15s %-10d ₱%-9.2f ₱%-10.2f\n",
                detail.getProduct().getSize(), // Size
                detail.getQuantity(),          // Quantity
                detail.getUnitPrice(),         // Unit Price
                detail.getSubtotal()           // Subtotal
            ));
        }

        // Write total items and total amount
        writer.write("\n");
        writer.write("Total Items: " + order.getDetails().stream().mapToInt(OrderDetail::getQuantity).sum() + "\n");
        writer.write("===================\n");
        writer.write("Total: ₱" + String.format("%.2f", order.getTotalAmount()) + "\n");
        writer.write("===================\n");

        // Write employee handler
        writer.write("Handled by: " + currentUser.getName() + "\n");

        // Confirm receipt generation
        System.out.println("Receipt saved to: " + fileName);
            // Print the receipt
            printTextFile(fileName);
        } catch (IOException e) {
            System.err.println("Failed to save receipt: " + e.getMessage());
        }
    }
}