import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Main {
    // Declare variables
    static Display ImageDisplay;
    static BufferedImage image;
    static int squareSize;

    public static void main(String[] args) {
        try {
            // Read image file and set square size from command line arguments
            image = ImageIO.read(new File(args[0]));
            squareSize = Integer.parseInt(args[1]);

            // Initialize ImageDisplay
            ImageDisplay = new Display(image, squareSize);
            ImageDisplay.setVisible(true);
            ImageDisplay.setImage(image);
            Thread.sleep(200);

            // Choose single or multi-threaded processing based on command line argument
            if (args[2].equals("S") || args[2].equals("s")) {
                singleThreaded();
            } else {
                multiThreaded();
            }

            // Write the processed image to a file
            writeImage(image, "result.jpg");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Multi-threaded pixelization using a thread pool
    static void multiThreaded() {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(processors);

        int width = image.getWidth();
        int height = image.getHeight();

        List<PoolingThread> threads = new ArrayList<>();

        // Horizontal pixelization threads
        for (int i = 0; i < processors; i++) {
            PoolingThread thread = new PoolingThread(i) {
                public Boolean call() {
                    int row = (width / processors + 1) * index;
                    int len = Math.min(width, (width / processors + 1) * (index + 1));

                    for (; row < len; row++) {
                        for (int col = 0; col < height; col += squareSize) {
                            image = HorizontalPixelization(image, row, col);
                        }
                        if(row % squareSize == 0)
                            ImageDisplay.setImage(image);
                    }
                    ImageDisplay.panel.repaint();
                    return true;
                }
            };

            threads.add(thread);
        }

        // Vertical pixelization threads
        for (int i = 0; i < processors; i++) {
            PoolingThread thread = new PoolingThread(i) {
                public Boolean call() {
                    int col = (height / processors + 1) * index;
                    int len = Math.min(height, (height / processors + 1) * (index + 1));

                    for (; col < len; col++) {
                        for (int row = 0; row < width; row += squareSize) {
                            image = VerticalPooling(image, row, col);
                        }
                        if(col % squareSize == 0)
                            ImageDisplay.setImage(image);
                    }
                    ImageDisplay.panel.repaint();
                    return true;
                }
            };
            threads.add(thread);
        }

        // Execute threads and clear the thread list
        try {
            threadPool.invokeAll(threads);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            ImageDisplay.panel.repaint();
            threads.clear();
        }
    }

    // Single-threaded pixelization
    static void singleThreaded()  throws InterruptedException {
        int width = image.getWidth();
        int height = image.getHeight();

        // Horizontal pixelization
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col += squareSize){
                image = HorizontalPixelization(image, row, col);
            }
            if(row % squareSize == 0)
                ImageDisplay.setImage(image);
        }

        // Vertical pixelization
        for (int col = 0; col < height; col++) {
            for (int row = 0; row < width; row += squareSize){
                image = VerticalPooling(image, row, col);
            }
            if(col % squareSize == 0)
                ImageDisplay.setImage(image);
        }
    }

    // Abstract class for pixelization threads
    static abstract class PoolingThread implements Callable<Boolean> {
        int index;

        public PoolingThread(int index) {
            this.index = index;
        }
    }
    
    // Method for vertical pixelization of a given image at specified coordinates
    static BufferedImage VerticalPooling(BufferedImage image, int x, int y) {
        Pixel averageColor = new Pixel(0, 0, 0);

        int len = Math.min(image.getWidth(), x + squareSize);
        for (int i = x; i < len; i++) {
            averageColor.add(image.getRGB(i, y));
        }

        int divisor = Math.min(squareSize, image.getWidth() - x);
        averageColor.div(divisor);

        for (int i = x; i < len; i++) {
            image.setRGB(i, y, averageColor.getRGB());
        }
        return image;
    }

    // Method for horizontal pixelization of a given image at specified coordinates
    static BufferedImage HorizontalPixelization(BufferedImage image, int x, int y) {
        Pixel averageColor = new Pixel(0, 0, 0);

        int len = Math.min(image.getHeight(), y + squareSize);
        for (int i = y; i < len; i++) {
            averageColor.add(image.getRGB(x, i));
        }

        int divisor = Math.min(squareSize, image.getHeight() - y);
        averageColor.div(divisor);

        for (int i = y; i < len; i++) {
            image.setRGB(x, i, averageColor.getRGB());
        }

        return image;
    }

    // Method to write the processed image to a file
    public static void writeImage(BufferedImage image, String out) throws IOException {
        File file = new File("./" + out);
        ImageIO.write(image, "jpg", file);
    }
}
