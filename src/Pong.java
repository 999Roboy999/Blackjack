import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;


public class Pong extends JPanel {

    private Player p1;
    private Player p2;
    private Player cpu;
    private Player cpu2;
    private Target t1; // First ball
    private Target t2; // Second ball
    private Timer timer;
    public boolean gameOver = false;
    private Random random = new Random();
    private int score = 0; // Total score

    private int buttonX = 325; // X position of the button
    private int buttonY = 400; // Y position of the button
    private int buttonWidth = 150; // Width of the button
    private int buttonHeight = 50; // Height of the button

    private Font retroFont;

    private Color darkPastelGreen = new Color(154, 205, 50);
    private Color white = new Color(255, 255, 255);
    private Color pastelPink = new Color(255, 182, 193);
    private Color balloonBlue = new Color(11, 5, 138);
    private Color neonGreen = new Color(57, 255, 20);
    private Color black = new Color(0, 0, 0);

    private int[] flowerX = new int[16];
    private int[] fishX = new int[25];
    private int[] fishY = new int[25];

    public Pong() {
        p1 = new Player(220, 330, "1", new Color(255, 0, 0));
        p2 = new Player(20, 60, "2", new Color(0, 0, 255));
        cpu = new Player(780, 270, "C", new Color(0, 255, 0));
        cpu2 = new Player(780, 330, "C2", new Color(0, 255, 0)); // Initialize second CPU player
        t1 = new Target(387, 300); // First ball
        t2 = null; // Initially, the second ball is null
        initFlowers();
        initFish();
        loadCustomFont();

        // Timer for updating the game
        timer = new Timer(20, e -> {
            updateGame();
            repaint(); // Repaint the panel to update the balls and paddles
        });
    }

    public void start() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void stop() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    private void initFlowers() {
        for (int i = 0; i < flowerX.length; i++) {
            int x = random.nextInt(800); // Random x-coordinate between 0 and 800
            int y = 601 + random.nextInt(100);
            flowerX[i] = x;
        }
    }

    private void initFish() {
        for (int i = 0; i < fishX.length; i++) {
            int x = 100 + random.nextInt(600);
            int y = 10 + random.nextInt(550);
            fishX[i] = x;
            fishY[i] = y;
        }
    }

    private void loadCustomFont() {
        try {
            retroFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/PressStart2P-Regular.ttf"));
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font. Default font will be used.");
            retroFont = new Font("Courier New", Font.PLAIN, 24);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (t1.score <= 5) {
            level1(g); // Call the current level background
        } else if (t1.score > 5 && t1.score <= 15) {
            level2(g);
        } else if (t1.score > 15) {
            level3(g);
        }

        drawScore(g); // Draw the score on top
        drawDashedLine(g); // Draw the dashed centerline
        p1.drawMe(g);
        p2.drawMe(g);  
        cpu.drawMe(g);
        if (t2 != null) {
            cpu2.drawMe(g);
            t2.drawMe(g); // Draw the second ball if it exists
        }
        t1.drawMe(g);

        if (gameOver) {
            drawGameOverScreen(g); // Draw game over screen if game is over
        }

    }

    private void level3(Graphics g) {
        // Create a Black to Neon Green gradient
        for (int y = 0; y < 600; y++) {
            // Calculate the color based on the y-coordinate
            float ratio = (float) y / 600;

            // Neon green RGB components
            int neonGreenR = 57;
            int neonGreenG = 255;
            int neonGreenB = 20;

            // Calculate the color components for the gradient
            int r = (int) (neonGreenR * ratio); // Red component increases to 57
            int gComponent = (int) (neonGreenG * ratio); // Green component increases to 255
            int blue = (int) (neonGreenB * ratio); // Blue component increases to 20

            // Create the color
            Color color = new Color(r, gComponent, blue);

            // Draw a horizontal line with the calculated color
            g.setColor(color);
            g.drawLine(0, y, 800, y);
        }
        t1.setSpeed(2.0);
        if (t2 != null) {
            t2.setSpeed(2.0);
        }
    }

    private void level2(Graphics g) {
        // Create a Blueish-purple-black gradient
        for (int y = 0; y < 600; y++) {
            // Calculate the color based on the y-coordinate
            float ratio = (float) y / 600;
            int r = (int) (Math.max(0, Math.min(255, 128 - ratio * 128))); // Red component
            int gComponent = (int) (Math.max(0, Math.min(255, 64 - ratio * 64))); // Green component
            int blue = (int) (Math.max(0, Math.min(255, 255 - ratio * 128))); // Blue component

            // Create the color
            Color color = new Color(r, gComponent, blue);

            // Draw a horizontal line with the calculated color
            g.setColor(color);
            g.drawLine(0, y, 800, y);
        }

        for (int i = 0; i < fishX.length; i++) {
            g.setColor(white);
            g.drawOval(fishX[i], fishY[i], 20, 10);
            g.drawLine(fishX[i] - 10, fishY[i], fishX[i], fishY[i] + 5);
            g.drawLine(fishX[i] - 10, fishY[i], fishX[i] - 10, fishY[i] + 10);
            g.drawLine(fishX[i] - 10, fishY[i] + 10, fishX[i], fishY[i] + 5);
            g.drawOval(fishX[i] + 16, fishY[i] + 3, 2, 2);
        }
        t1.setSpeed(2.0);
    }

    private void level1(Graphics g) {
        // Create a White-Purple gradient
        for (int y = 0; y < 600; y++) {
            // Calculate the color based on the y-coordinate
            float ratio = (float) y / 600;
            int r = (int) (Math.max(0, Math.min(255, 255 - ratio * 128))); // Red component
            int gComponent = (int) (Math.max(0, Math.min(255, 255 - ratio * 255))); // Green component
            int blue = (int) (Math.max(0, Math.min(255, 255 - ratio * 0))); // Blue component

            // Create the color
            Color color = new Color(r, gComponent, blue);

            // Draw a horizontal line with the calculated color
            g.setColor(color);
            g.drawLine(0, y, 800, y);
        }

        // Draw flowers on bottom of screen
        for (int i = 0; i < flowerX.length; i++) {
            // Draw the flower stem
            g.setColor(darkPastelGreen);
            g.drawLine(flowerX[i], 600 - 40, flowerX[i], 600); // Increased stem height


            // Draw the petals
            g.setColor(pastelPink); // Choose a color for the petals
            int numPetals = 8; // Increased number of petals for a fuller flower
            int petalLength = 24; // Increased length of each petal
            int petalWidth = 12; // Increased width of each petal
            int centerX = flowerX[i]; // X-coordinate of the flower center
            int centerY = 600 - 46; // Adjusted Y-coordinate of the flower center

            for (int j = 0; j < numPetals; j++) {
                // Calculate the angle for this petal
                double angle = Math.toRadians((360.0 / numPetals) * j);

                // Calculate the petal's position
                int petalX = (int) (centerX + Math.cos(angle) * (petalLength / 2));
                int petalY = (int) (centerY + Math.sin(angle) * (petalLength / 2));

                // Draw the petal as an oval
                g.fillOval(petalX - petalLength / 2, petalY - petalWidth / 2, petalLength, petalWidth);
            }

            // Draw the flower center
            g.setColor(new Color(253, 253, 150));
            g.fillOval(flowerX[i] - 6, 600 - 52, 12, 12); // Increased size of the center
        }

        t1.setSpeed(1.0);
    }

    private void drawScore(Graphics g) {
        // Outline
        g.setFont(retroFont.deriveFont(50f));
        g.setColor(Color.BLACK);
        String scoreText = String.valueOf(score); // Use the total score
        int textWidth = g.getFontMetrics().stringWidth(scoreText);
        int x = (800 - textWidth) / 2;
        int y = 60;

        // Draw outline
        g.drawString(scoreText, x - 1, y); // left
        g.drawString(scoreText, x + 1, y); // right
        g.drawString(scoreText, x, y - 1); // up
        g.drawString(scoreText, x, y + 1); // down

        // Score
        g.setColor(Color.WHITE);
        g.drawString(scoreText, x, y);
    }

    private void drawDashedLine(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

        // Set up the dashed stroke
        float[] dashPattern = {10, 10}; // Dash length, gap length
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0));

        // Draw the dashed line
        int scoreHeight = 60; // Adjusted for padding below the score
        g2d.drawLine(400, scoreHeight + 10, 400, 600); // Start below the score
        g2d.setStroke(new BasicStroke(2));
    }

    public void updateGame() {
        if (!gameOver) {
            t1.move();
            t1.bounceOffWalls(getWidth(), getHeight());
            t1.checkPaddleCollision(p1, p2, cpu);  // Check collision with paddles
            if (t1.checkLoss()) {
                t1.setRunning(false);
                gameOver = true; // Set game over state
            }

            // Check if the score exceeds 15 to spawn the second ball
            if (score > 15 && t2 == null) {
                t2 = new Target(400, 300); // Instantiate the second ball
            } else if (score < 15 && t2 != null) {
                t2 = null;
            }

            // Update the second ball if it exists
            if (t2 != null) {
                t2.move();
                t2.bounceOffWalls(getWidth(), getHeight());
                t2.checkPaddleCollision(p1, p2, cpu2);
                if (t2.checkLoss()) {
                    t2.setRunning(false);
                    gameOver = true; // Set game over state if the second ball loses
                }
            }

            score = t1.score + (t2 != null ? t2.score : 0);

            if (t2 != null) {
                cpu2.y = (int) t2.y; // Make cpu2 follow t2
            }
        }
    }

    public void resetGame() {
        // Reset the score
        score = 0;
        t1.score = 0; // Reset the first ball's score
        t1.running = true;

        // Reset player positions
        p1.setPosition(220, 330);
        p2.setPosition(20, 60);
        cpu.setPosition(780, 270);
        cpu2.setPosition(780, 330);

        // Reset ball positions
        t1 = new Target(387, 300); // Reset the first ball
        t2 = null; // Reset the second ball

        // Stop the timer
        stop();
    }

    public void drawGameOverScreen(Graphics g) {
        // Set background color
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);

        // Set font and color for "Game Over"v
        g.setColor(Color.RED);
        g.setFont(retroFont.deriveFont(72f));
        String gameOverText = "Game Over";
        int gameOverTextWidth = g.getFontMetrics().stringWidth(gameOverText);
        int gameOverTextX = (800 - gameOverTextWidth) / 2;
        int gameOverTextY = 250;
        g.drawString(gameOverText, gameOverTextX, gameOverTextY);

        // Set font and color for "Your Score"
        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(36f));
        String scoreText = "Your Score: " + t1.score;
        int scoreTextWidth = g.getFontMetrics().stringWidth(scoreText);
        int scoreTextX = (800 - scoreTextWidth) / 2;
        int scoreTextY = 350;
        g.drawString(scoreText, scoreTextX, scoreTextY);

        // Set font and calculate dimensions for "Back to Menu" button
        g.setFont(retroFont.deriveFont(24f));
        String buttonText = "Back to Menu";
        int buttonTextWidth = g.getFontMetrics().stringWidth(buttonText);
        int buttonTextHeight = g.getFontMetrics().getHeight();

        // Calculate the button's rectangle dimensions
        int padding = 20; // Padding around the text
        buttonX = (800 - buttonTextWidth - 2 * padding) / 2; // Centered horizontally
        buttonY = 400; // Fixed vertical position
        buttonWidth = buttonTextWidth + 2 * padding;
        buttonHeight = buttonTextHeight + padding;

        // Draw the button rectangle
        g.setColor(Color.WHITE);
        g.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);

        // Draw the button text
        g.drawString(buttonText, buttonX + padding, buttonY + buttonHeight - padding / 2);
    }


    public void handleMouseClick(Menu menu, int mouseX, int mouseY) {
        if (isButtonClicked(mouseX, mouseY)) {
            menu.reset(); // Reset the menu state
            resetGame(); // Reset the game logic
        }
    }

    private boolean isButtonClicked(int mouseX, int mouseY) {
        return mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
               mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
    }

    public Player getPlayer1() {
        return p1;
    }

    public Player getPlayer2() {
        return p2;
    }

    public Player getCpu() {
        return cpu;
    }

    public Target getTarget() {
        return t1;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}

