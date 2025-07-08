import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Player {
    private int x;
    public int y;
    private int width;
    private int height;
    private Color paddleColor;
    private boolean visible = true;
    private Color black;

    // Speed of movement
    private int speed = 2;

    // Player number and its color
    private String playerNumber;
    private Color numberColor;

    public Player(int x, int y, String playerNumber, Color numberColor) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 70;
        this.paddleColor = new Color(255, 255, 255); // Default white paddle
        this.black = new Color(0, 0, 0);
        this.playerNumber = playerNumber;
        this.numberColor = numberColor;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public void drawMe(Graphics g) {
        if (visible) {
            // Draw the paddle
            g.setColor(paddleColor);
            g.fillRect(x, y, width, height);
            g.setColor(black);
            g.drawRect(x, y, width, height);

            // Draw the player number in the center of the paddle
            g.setColor(numberColor);
            g.setFont(new Font("Arial", Font.BOLD, 16)); // Font size and style for the number
            int textWidth = g.getFontMetrics().stringWidth(playerNumber);
            int textHeight = g.getFontMetrics().getHeight();
            g.drawString(playerNumber, x + (width - textWidth) / 2, y + (height + textHeight / 2) / 2);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void moveUp() {
        y -= speed; // Move up by speed
        if (y < -70) { // Prevent moving beyond the top
            y = 670;
        }
    }

    public void moveDown() {
        y += speed; // Move down by speed
        if (y + height > 670) { // Prevent moving beyond the bottom
            y = -70;
        }
    }

    public boolean checkCollision(Target t) {
        int pX = x;
        int pY = y;
        int pWidth = width;
        int pHeight = height;
        double tX = t.getX();
        double tY = t.getY();
        int tWidth = t.getWidth();
        int tHeight = t.getHeight();

        return (pX + pWidth >= tX && pX <= tX + tWidth &&
                pY + pHeight >= tY && pY <= tY + tHeight);
    }
}
