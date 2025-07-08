import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class Table extends JPanel implements ActionListener {
    private Color tableBorder = new Color(101, 65, 39);
    private Color table = new Color(62, 145, 115);
    private Color white = new Color(210, 227, 219);
    private Color lb = new Color(196, 164, 132);
    private BufferedImage backImage;
    private Font customFont;

    private Blackjack game;
    private JButton hitButton;
    private JButton standButton;
    private JButton playAgainButton;

    // Animation Variables
    private int flipWidth = 125;  
    private boolean flipping = false;
    private Card flippingCard;
    private int cardX, cardY;
    private int endX, endY;
    private boolean shrinking = true;
    private boolean expanding = false;
    private Timer timer;
	private boolean animationComplete = true;

    // Game state variables
    private boolean gameEnded = false; // Track if the game has ended
    private String resultMessage = ""; // Message to display when the game ends

    public Table() {
        setLayout(null);
        game = new Blackjack();

        try {
            backImage = ImageIO.read(new File("./cards/back.png"));
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Aloevera-OVoWO.ttf")).deriveFont(24f);
        } catch (IOException | FontFormatException e) {
            System.out.println("Error loading font or back image: " + e.getMessage());
            e.printStackTrace();
            backImage = null;
        }

        hitButton = new JButton("Hit");
        hitButton.setBounds(100, 555, 100, 30);
        add(hitButton);
        hitButton.addActionListener(this);

        standButton = new JButton("Stand");
        standButton.setBounds(425, 555, 100, 30);
        add(standButton);
        standButton.addActionListener(this);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(750, 555, 100, 30);
        add(playAgainButton);
        playAgainButton.addActionListener(this);
        playAgainButton.setEnabled(false); // Disable until the game ends

        // Initialize Timer for Animation
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flipping) {
                    repaint();
                }
            }
        });
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 600);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Table
        g.setColor(tableBorder);
        g.fillRect(0, 0, 1200, 600);
        g.setColor(table);
        g.fillRect(50, 50, 900, 500);
        g.setColor(white);

        boolean staggerUp = true;
        for (int i = 0; i < 7; i++) {
            int x = 75 + i * 115;
            int y = (staggerUp) ? 75 : 325;
            g.drawRect(x, y, 125, 200);
            staggerUp = !staggerUp;
        }

        // Displaying Card Total and Points
        g.setFont(customFont);
        g.setColor(Color.WHITE);
        g.drawString("Card Total: " + game.getTotalValue(), 262, 32);
        g.drawString("Points: " + game.getPoints(), 587, 32);

        // Drawing points board
        g.setColor(lb);
        g.fillRect(962, 50, 225, 250);
        g.setFont(customFont);
        g.setColor(Color.BLACK);
        String[] lines = {
            "21 - 5 points",
            "20 - 3 points",
            "19 - 2 points",
            "18 - 2 points",
            "17 - 1 point",
            "16 - 1 point",
            "<16 - 0 points"
        };

        int startY = 70 + (250 - (lines.length * 30)) / 2;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int textWidth = g.getFontMetrics().stringWidth(line);
            int x = 962 + (225 - textWidth) / 2;
            g.drawString(line, x, startY + (i * 30));
        }

        // Draw deck
        if (backImage != null) {
            g.drawImage(backImage, 1012, 350, 125, 200, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(1012, 350, 125, 200);
        }

        for (int i = 0; i < 2; i++) {
            game.drawCard(g, i);
        }

        // Call animation if active
        if (flipping) {
            cardAnimation(g);
        }

        for (int i = 2; i < game.getCardCount(); i++) {
            if (!flipping || i != game.getCardCount() - 1) {
                game.drawCard(g, i);
            }
        }

		if (game.getCardCount() == 7) {
			resultMessage = "You won by hitting 5 times!";
			gameEnded = true;
			playAgainButton.setEnabled(true);
		}

        // Display result message if the game has ended
        if (gameEnded && animationComplete) {
            g.setColor(lb);
            g.fillRect(400, 200, 400, 100); 
            g.setColor(Color.BLACK);
            g.setFont(customFont);
            g.drawString(resultMessage, 450, 250); 
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hitButton) {
            if (!gameEnded && animationComplete) {
                game.hit();
                Card drawnCard = game.getDrawnCard();
                if (drawnCard != null) {
                    // Start flipping animation
                    flipping = true;
                    animationComplete = false;
                    flippingCard = drawnCard;
                    cardX = 1012; // Starting position
                    cardY = 350;  
                    endX = game.getRectX(game.getCardCount() - 1); // Target position
                    endY = game.getRectY(game.getCardCount() - 1); // Target position
                    flipWidth = 125;
                    shrinking = true;
                    expanding = false;

                    timer.start();

                    // Check for bust
                    if (game.getTotalValue() > 21) {
                        resultMessage = "It's a bust, you lost!";
                        gameEnded = true;
                        playAgainButton.setEnabled(true);
                    }
                }
            }
        } else if (e.getSource() == standButton) {
            if (!gameEnded) {
                game.stand();
                int totalValue = game.getTotalValue();
                if (totalValue >= 16) {
                    resultMessage = "You won " + game.getWinnings() + " points!";
                } else if (totalValue < 16) {
                    resultMessage = "You did not win any points";
                } 
                gameEnded = true;
                playAgainButton.setEnabled(true);
				repaint();
            }
        } else if (e.getSource() == playAgainButton) {
            game.resetGame();
            resultMessage = "";
            gameEnded = false;	
            playAgainButton.setEnabled(false);
            animationComplete = true;
            repaint();
        }
    }

    public void cardAnimation(Graphics g) {
		int dx = (endX - cardX) / 40;  
		int dy = (endY - cardY) / 40;

		if (dx == 0 && cardX != endX) dx = (cardX < endX) ? 1 : -1;
		if (dy == 0 && cardY != endY) dy = (cardY < endY) ? 1 : -1;

		cardX += dx;
		cardY += dy;

		boolean reachedDestination = (Math.abs(endX - cardX) < 3 && Math.abs(endY - cardY) < 3);

		if (!reachedDestination) {  
			if (shrinking) {
				if (flipWidth > 1) {
					g.drawImage(backImage, cardX + (125 - flipWidth) / 2, cardY, flipWidth, 200, null);
					flipWidth -= 1;  
				} else {
					shrinking = false;
					expanding = true;
					flipWidth = 2; 
				}
			}

			if (expanding) {
				if (flipWidth < 125) {
					flippingCard.drawMe(g, cardX + (125 - flipWidth) / 2, cardY, flipWidth, 200);
					flipWidth += 1; 
				} else {
					expanding = false;
                    animationComplete = true; 
					flipping = false; 
					flippingCard = null;
				}
			}
		} else {
			flippingCard.drawMe(g, endX, endY, 125, 200);
            animationComplete = true;
			flipping = false; 
			flippingCard = null; 
		}

		if (flipping) {
			repaint();
		}
	}
}