import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Target {
    private double x;
    public double y;
	private Random r = new Random();
    private int width;
    private int height;
    public int score = 0; // Track the score
    public boolean running = true;
    private Color black;

    private Color white;

    // Ball speed and velocity
    private double speed = 1;
    private double velocityX = speed;
    private double velocityY = speed;
	private double newVy;
	private double newVx;
	private double max_angle = 89; // Degrees
	private double sign;
	private double theta;

    public Target(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 25;
        this.height = 25;
        this.white = new Color(255, 255, 255);
        this.black = new Color(0, 0, 0);
    }

    public void move() {
        // Update position based on velocity
        x += velocityX;
        y += velocityY;

    }

    public void bounceOffWalls(int panelWidth, int panelHeight) {
		if (y < 1) {
			velocityY *= -1;
		} else if (y > 575) {
			velocityY *= -1;
		}     
	}



    public void checkPaddleCollision(Player p1, Player p2, Player cpu) {
        // Get the positions and sizes of paddles and the ball
        int p1X = p1.getX();
        int p1Y = p1.getY();
        int p1Width = p1.getWidth();
        int p1Height = p1.getHeight();

        int p2X = p2.getX();
        int p2Y = p2.getY();
        int p2Width = p2.getWidth();
        int p2Height = p2.getHeight();

        int cpuX = cpu.getX();
        int cpuY = cpu.getY();
        int cpuWidth = cpu.getWidth();
        int cpuHeight = cpu.getHeight();

        double tX = this.x;  // Ball X position 
        double tY = this.y;  // Ball Y position
        int tWidth = this.width;  // Ball width
        int tHeight = this.height;  // Ball height

        // Check for collision with player 1's paddle
        if (velocityX <= 0 && p1X + p1Width == (int)tX && p1Y - 2 < tY && p1Y + p1Height + 2 > tY) {
			theta = calculateAngle(p1);
            newVx = Math.abs((Math.cos(theta)) * speed);
			newVy = (-Math.sin(theta)) * speed;
            score += 1;

			//sign = Math.signum(velocityX);
			velocityX = newVx; //* (-1.0 * sign);
			velocityY = newVy;
            
        }

        // Check for collision with player 2's paddle
        if (p2X + p2Width == (int)tX && p2Y - 2 < tY && p2Y + p1Height + 2 > tY) {
            theta = calculateAngle(p2);
			newVx = Math.abs((Math.cos(theta)) * speed);
			newVy = (-Math.sin(theta)) * speed;
            score += 1;

			//sign = Math.signum(velocityX);
			velocityX = newVx;
			velocityY = newVy;
            
        }

        // Check for collision with CPU paddle
        if (cpuX + cpuWidth >= tX && cpuX <= tX + tWidth &&
            cpuY + cpuHeight >= tY && cpuY <= tY + tHeight) {
			theta = calculateAngle(cpu);
			newVx = Math.abs((Math.cos(theta)) * speed);
			newVy = (-Math.sin(theta)) * speed;

			velocityX = newVx * -1.0;
			velocityY = newVy;
        }
    }
    

    private double calculateAngle(Player paddle) {
    	double p_cy = (paddle.getY() + (paddle.getHeight() / 2)) - (y + (height / 2));
		double i_n = p_cy/(paddle.getHeight() / 2);
		theta = i_n * max_angle;

		return Math.toRadians(theta);
    }

    public void drawMe(Graphics g) {
        g.setColor(white);
        g.fillOval((int)x, (int)y, width, height); 
        g.setColor(black);
        g.drawOval((int)x, (int)y, width, height);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean checkLoss() {
        if (x < -25) {
            return true;
        }
        return false;
    }

    // Getters for x, y, width, height
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
