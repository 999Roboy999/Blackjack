import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

public class Menu {
    public boolean gameStarted = false;
    private boolean visible = true;

    private int playButtonX = 300; // X position of the play button rectangle
    private int playButtonY = 250; // Y position of the play button rectangle (moved up slightly)
    private int playButtonWidth = 200; // Width of the play button rectangle
    private int playButtonHeight = 60; // Height of the play button rectangle

    private Font retroFont; // Custom retro font

    public Menu() {
        loadCustomFont(); // Load the custom font
    }

    private void loadCustomFont() {
        try {
            // Load the font from file
            retroFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/PressStart2P-Regular.ttf"));
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading custom font. Using default font.");
            retroFont = new Font("Courier New", Font.PLAIN, 48); // Fallback font
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted; 
    }

    public void reset() {
        gameStarted = false;
        visible = true;
    }

    public void handleMouseClick(int mouseX, int mouseY) {
        if (isPlayButtonClicked(mouseX, mouseY)) {
            gameStarted = true;
            visible = false;
        }
    }

    private boolean isPlayButtonClicked(int mouseX, int mouseY) {
        return mouseX >= playButtonX && mouseX <= playButtonX + playButtonWidth &&
               mouseY >= playButtonY && mouseY <= playButtonY + playButtonHeight;
    }

    public void draw(Graphics g) {
        if (visible) {
            // Draw menu background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 600);

            // Draw title at the top
            g.setColor(Color.WHITE);
            g.setFont(retroFont.deriveFont(64f)); // Larger font for the title
            String title = "Pong";
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, (800 - titleWidth) / 2, 80); // Title at the top of the screen

            // Draw play button rectangle
            g.setColor(Color.WHITE);
            g.drawRect(playButtonX, playButtonY, playButtonWidth, playButtonHeight);

            // Draw play button text
            g.setFont(retroFont.deriveFont(24f)); // Smaller font for the button
            String playText = "Play";
            int playTextWidth = g.getFontMetrics().stringWidth(playText);
            g.drawString(playText, playButtonX + (playButtonWidth - playTextWidth) / 2, playButtonY + 40);

            // Draw instructions centered on the screen
            g.setFont(retroFont.deriveFont(16f)); // Smaller font for instructions
            String instructions1 = "1. Player 1: Arrow Keys | Player 2: WASD";
            String instructions2 = "2. Don't let the ball get past the paddle";
            String instructions3 = "3. Difficulty increases as score increases";

            int instructionsYStart = 400; // Vertical position to start the instructions
            int instructionsCenterX = 800 / 2; // Center of the screen horizontally

            int instructions1Width = g.getFontMetrics().stringWidth(instructions1);
            int instructions2Width = g.getFontMetrics().stringWidth(instructions2);
            int instructions3Width = g.getFontMetrics().stringWidth(instructions3);

            g.drawString(instructions1, instructionsCenterX - (instructions1Width / 2), instructionsYStart);
            g.drawString(instructions2, instructionsCenterX - (instructions2Width / 2), instructionsYStart + 30);
            g.drawString(instructions3, instructionsCenterX - (instructions3Width / 2), instructionsYStart + 60);
        }
    }

    public Font getRetroFont() {
        return retroFont;
    }
}
