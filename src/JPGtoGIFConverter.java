import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JPGtoGIFConverter {

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 5) {
            System.out.println("Usage: JPGtoGIFConverter <inputFolder> <outputFile.gif> [delayInMilliseconds] [width] [height]");
            return;
        }

        String inputFolder = args[0];
        String outputFile = args[1];
        int delay = args.length >= 3 ? Integer.parseInt(args[2]) : 100; // Default delay of 100 milliseconds
        int width = args.length >= 4 ? Integer.parseInt(args[3]) : -1; // Default -1 means keep original width
        int height = args.length == 5 ? Integer.parseInt(args[4]) : -1; // Default -1 means keep original height

        List<BufferedImage> images = loadImagesFromFolder(inputFolder);

        if (images.isEmpty()) {
            System.out.println("No JPG images found in the input folder.");
            return;
        }

        try {
            saveAsGIF(images, outputFile, delay, width, height);
            System.out.println("GIF file saved successfully: " + outputFile);
        } catch (IOException e) {
            System.out.println("Error while saving GIF: " + e.getMessage());
        }
    }

    private static List<BufferedImage> loadImagesFromFolder(String folderPath) {
        List<BufferedImage> images = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        if (files != null) {
            for (File file : files) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        images.add(img);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + file.getName());
                }
            }
        }

        return images;
    }

    private static void saveAsGIF(List<BufferedImage> images, String outputFile, int delay, int width, int height) throws IOException {
        if (images.isEmpty()) {
            throw new IllegalArgumentException("No images provided to create GIF.");
        }

        BufferedImage firstImage = images.get(0);
        int gifWidth = width > 0 ? width : firstImage.getWidth();
        int gifHeight = height > 0 ? height : firstImage.getHeight();

        GifSequenceWriter writer = new GifSequenceWriter(ImageIO.createImageOutputStream(new File(outputFile)),
                firstImage.getType(), delay, true);

        for (BufferedImage image : images) {
            BufferedImage resizedImage = resizeImage(image, gifWidth, gifHeight);
            writer.writeToSequence(resizedImage);
        }

        writer.close();
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        if (width <= 0 || height <= 0) {
            return originalImage;
        }
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
