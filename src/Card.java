import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Card {
    public String name;
    public int value;
    public String suit;
    private BufferedImage suitImage;

    public Card(String name, int value, String suit) {
        this.name = name;
        this.value = value;
        this.suit = suit;

        try {
            String filePath = "./cards/" + name + "_of_" + suit + ".png";
            suitImage = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println("Error loading image for " + name + " of " + suit + ": " + e.getMessage());
            e.printStackTrace();
            suitImage = null;
        }
    }

    public void drawMe(Graphics g, int x, int y, int width, int height) {
       if (suitImage != null) {
            g.drawImage(suitImage, x, y, width, height, null);
        } else {
            // Draw a red rectangle if the image is not loaded
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }

    public int getValue() {
        return value;
    }

}
