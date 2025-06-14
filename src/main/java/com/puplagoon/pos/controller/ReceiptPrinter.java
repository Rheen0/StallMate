package src.main.java.com.puplagoon.pos.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
        List<String> receiptLines = new ArrayList<>();
    
        // Generate a unique receipt ID
        String receiptId = "REC-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID().toString().substring(0, 8);
    
        // Build receipt in memory
        String header = "=== POS Receipt ===";
        receiptLines.add(header);
    
        String receiptIdLine = "Receipt ID: " + receiptId; // Add receipt ID to the receipt
        receiptLines.add(receiptIdLine);
    
        String dateLine = "Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        receiptLines.add(dateLine);
    
        String orderIdLine = "Order ID: " + order.getOrderId();
        receiptLines.add(orderIdLine);
    
        receiptLines.add("===================");
        receiptLines.add("");
    
        String tableHeader = String.format("%-25s %-15s %-15s %-15s %-15s", "Category", "Size", "Quantity", "Unit Price", "Subtotal");
        receiptLines.add(tableHeader);
        receiptLines.add("----------------------------------------------------------------------------------------------------");
    
        for (OrderDetail detail : order.getDetails()) {
            String detailLine = String.format(
                "%-25s %-15s %-15s %-15s %-15s",
                detail.getProduct().getCategory(),
                detail.getProduct().getSize(),
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getSubtotal()
            );
            receiptLines.add(detailLine);
        }
    
        receiptLines.add("");
    
        String totalItemsLine = "Total Items: " + order.getDetails().stream().mapToInt(OrderDetail::getQuantity).sum();
        receiptLines.add(totalItemsLine);
        receiptLines.add("===================");
    
        String totalLine = "Total: ₱" + String.format("%.2f", order.getTotalAmount());
        receiptLines.add(totalLine);
    
        receiptLines.add("===================");
    
        String handlerLine = "Handled by: " + currentUser.getName();
        receiptLines.add(handlerLine);
    
        // Create the image from the receipt lines using the receipt ID as the file name
        try {
            String fileName = receiptId + ".png"; // Use receipt ID as the file name
            TxtToImage.createImage(receiptLines, fileName);
            System.out.println("Receipt image created with name: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}