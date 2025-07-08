import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;

public class Screen extends JPanel implements KeyListener, ActionListener {
    private Pong pong;
    private Menu menu;
    private Timer timer;
    private Set<Integer> pressedKeys;
    

    public Screen() {
        menu = new Menu();
        pong = new Pong();

        addKeyListener(this);
        setFocusable(true);

        // Initialize key state tracking
        pressedKeys = new HashSet<>();

        // Initialize and start the timer
        timer = new Timer(5, this);
        timer.start();

        // Mouse listener for the menu
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!menu.isGameStarted()) {
                    menu.handleMouseClick(e.getX(), e.getY());
                    if (menu.isGameStarted()) {
                        pong.setGameOver(false);
                        pong.start();
                    }
                }

                if (pong.gameOver) {
                    pong.handleMouseClick(menu, e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!menu.isGameStarted()) {
            menu.draw(g);
        } else if (pong.isGameOver()) {
            pong.drawGameOverScreen(g);
        } else {
            pong.paintComponent(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (menu.isGameStarted() && !pong.isGameOver()) {
            handleGameControls();
            pong.updateGame();
        }
        repaint();
    }

    private void handleGameControls() {
        // Player 1 controls (arrow keys)
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            pong.getPlayer1().moveUp();
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            pong.getPlayer1().moveDown();
        }

        // Player 2 controls (WASD)
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            pong.getPlayer2().moveUp();
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            pong.getPlayer2().moveDown();
        }

        // Cheat Key
        if (pressedKeys.contains(KeyEvent.VK_1)) { // To get to level 1
            pong.getTarget().setScore(0);
        }
        if (pressedKeys.contains(KeyEvent.VK_2)) { // To get to level 2
            pong.getTarget().setScore(6);
        }
        if (pressedKeys.contains(KeyEvent.VK_3)) { // To get to level 3
            pong.getTarget().setScore(16);
        }

        // CPU target tracking
        pong.getCpu().y = (int) pong.getTarget().y;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public Menu getMenu() {
        return menu;
    }
}
