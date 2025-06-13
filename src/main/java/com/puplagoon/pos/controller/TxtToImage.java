package src.main.java.com.puplagoon.pos.controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class TxtToImage {
    public static void createImage(List<String> lines, String outputPath) throws Exception {
        Font font = new Font("Monospaced", Font.PLAIN, 16);
        int width = 600;
        int lineHeight = 20;
        int height = lineHeight * lines.size() + 20;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);

        int y = 20;
        for (String line : lines) {
            g.drawString(line, 10, y);
            y += lineHeight;
        }
        g.dispose();

        ImageIO.write(image, "png", new File(outputPath));
    }
    
}