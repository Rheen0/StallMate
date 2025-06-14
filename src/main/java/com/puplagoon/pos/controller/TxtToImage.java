package src.main.java.com.puplagoon.pos.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class TxtToImage {
    public static void createImage(List<String> receiptLines, String fileName) throws Exception {
        // Font settings
        Font font = new Font("Monospaced", Font.PLAIN, 16); // Use a monospaced font for alignment
        int padding = 10; // Padding around the text

        // Create a temporary graphics object to calculate line height
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D tempGraphics = tempImage.createGraphics();
        tempGraphics.setFont(font);
        int lineHeight = 30;
        tempGraphics.dispose();

        // Calculate image dimensions
        int imageWidth = 1000; // Fixed width
        int imageHeight = (lineHeight * receiptLines.size()) + (2 * padding); // Dynamic height based on lines

        // Debugging logs
        System.out.println("Image Width: " + imageWidth);
        System.out.println("Image Height: " + imageHeight);
        System.out.println("Number of Lines: " + receiptLines.size());

        // Create the image
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Set background color
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);

        // Set text color and font
        g.setColor(Color.BLACK);
        g.setFont(font);

        // Draw each line of text
        int y = padding; // Start drawing text at the top padding
        for (String line : receiptLines) {
            g.drawString(line, padding, y);
            y += lineHeight; // Move to the next line
        }

        g.dispose();

        // Save the image to a file
        File outputFile = new File(fileName);
        ImageIO.write(image, "png", outputFile);

        System.out.println("Image saved as: " + fileName);
    }
}