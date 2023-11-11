import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Display extends JFrame {

    public JPanel panel; // Panel to hold the image

    private JLabel label; // Label to display the image

    public final Dimension shape; // Resolution of the display

    // Constructor for the Display class
    public Display(BufferedImage image, int blockSize) {
        // Get the screen resolution and scale the image accordingly
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenRes.width * 0.8), h = (int) (screenRes.height * 0.8);
        screenRes = new Dimension(w, h);
        image = scale(image, screenRes); // Use Engine class to scale the image
        shape = new Dimension(image.getWidth(), image.getHeight());

        // Set up the JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, shape.width + 25, shape.height + 50);
        setLocationRelativeTo(null);

        // Create a JPanel for the canvas
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout(0, 0));
        setContentPane(panel);

        // Create a JLabel for displaying the image
        label = new JLabel("");
        label.setIcon(new ImageIcon(image));

        // Add the image label to the canvas panel
        panel.add(label, BorderLayout.CENTER);
    }

    // Method to set and display a new image on the label
    public void setImage(BufferedImage image) {
        image = resize(image, shape); // Use Engine class to resize the image
        label.setIcon(new ImageIcon(image));
    }

        // Method to resize an image to a specified dimension
    public static BufferedImage resize(BufferedImage image, Dimension newSize) {
        Image tmp = image.getScaledInstance(newSize.width, newSize.height, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newSize.width, newSize.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return newImage;
    }

        // Method to scale an image based on a given screen resolution
    public static BufferedImage scale(BufferedImage image, Dimension screenRes) {
        double widthRatio = screenRes.getWidth() / image.getWidth();
        double heightRatio = screenRes.getHeight() / image.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        Dimension newDimension = new Dimension((int) (image.getWidth() * ratio), (int) (image.getHeight() * ratio));
        return resize(image, newDimension);
    }

}
