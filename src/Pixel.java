import java.awt.Color;

public class Pixel {

    private int R;
    private int G;
    private int B;

    // Constructor to initialize a Pixel with specified RGB values
    public Pixel(int r, int g, int b) {
        this.R = r;
        this.G = g;
        this.B = b;
    }

    // Constructor to initialize a Pixel with a Color object
    public Pixel(Color c) {
        this.R = c.getRed();
        this.G = c.getGreen();
        this.B = c.getBlue();
    }

    // Set the RGB values of the Pixel
    public void setColor(int r, int g, int b) {
        this.R = r;
        this.G = g;
        this.B = b;
    }

    // Add the RGB values of another Color to the Pixel
    public void add(Color c) {
        R += c.getRed();
        G += c.getGreen();
        B += c.getBlue();
    }

    // Add the RGB values of an integer color to the Pixel
    public void add(int rgb) {
        add(new Color(rgb));
    }

    // Divide the RGB values of the Pixel by a specified number
    public void div(int n) {
        R /= n;
        G /= n;
        B /= n;
    }

    // Get the integer representation of the RGB values of the Pixel
    public int getRGB() {
        return new Color(R, G, B).getRGB();
    }
}
