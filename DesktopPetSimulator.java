import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;


public class DesktopPetSimulator extends JFrame {
    private PetPanel petPanel;
    private JProgressBar happinessBar, hungerBar, energyBar;
    private JLabel moodLabel;
    private Timer animationTimer, moodTimer, particleTimer;
    private int happiness = 80, hunger = 30, energy = 70;
    private String mood = "happy";
    private String currentAnimation = "idle";
    private int bounceFrame = 0;
    private boolean eyesOpen = true;
    private ArrayList<Particle> particles = new ArrayList<>();
    private String notification = "";
    private int notificationTimer = 0;

    public DesktopPetSimulator() {
        setTitle("Desktop Pet Simulator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        startTimers();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 250));

        petPanel = new PetPanel();
        petPanel.setPreferredSize(new Dimension(400, 300));
        add(petPanel, BorderLayout.CENTER);

        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.NORTH);

        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBackground(new Color(240, 240, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        moodLabel = new JLabel("Mood: Happy", SwingConstants.CENTER);
        moodLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moodLabel.setForeground(new Color(255, 100, 100));
        panel.add(moodLabel);

        happinessBar = createStyledProgressBar(Color.GREEN);
        panel.add(createBarPanel("Happiness", happinessBar));

        hungerBar = createStyledProgressBar(Color.ORANGE);
        panel.add(createBarPanel("Fullness", hungerBar));

        energyBar = createStyledProgressBar(Color.CYAN);
        panel.add(createBarPanel("Energy", energyBar));

        updateBars();
        return panel;
    }

    private JProgressBar createStyledProgressBar(Color color) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setFont(new Font("Arial", Font.BOLD, 12));
        bar.setForeground(color);
        bar.setBorderPainted(true);
        return bar;
    }

    private JPanel createBarPanel(String label, JProgressBar bar) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(new Color(240, 240, 250));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(bar, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 250));

        JButton feedBtn = createStyledButton("Feed", new Color(255, 140, 100));
        feedBtn.addActionListener(e -> handleFeed());
        panel.add(feedBtn);

        JButton playBtn = createStyledButton("Play Game", new Color(100, 150, 255));
        playBtn.addActionListener(e -> handlePlay());
        panel.add(playBtn);

        JButton sleepBtn = createStyledButton("Sleep", new Color(180, 150, 255));
        sleepBtn.addActionListener(e -> handleSleep());
        panel.add(sleepBtn);
        
        JButton petBtn = createStyledButton("Pet", new Color(255, 150, 200));
        petBtn.addActionListener(e -> handlePet());
        panel.add(petBtn);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void handleFeed() {
        if (hunger < 20) {
            showNotification("I'm not hungry!");
            return;
        }
        hunger = Math.max(0, hunger - 40);
        happiness = Math.min(100, happiness + 15);
        showNotification("Yum yum! Thanks!");
        createParticles("PIZZA", 8);
        currentAnimation = "eating";
        Timer resetAnim = new Timer(1500, e -> currentAnimation = "idle");
        resetAnim.setRepeats(false);
        resetAnim.start();
        updateStats();
    }

    private void handlePlay() {
    if (energy < 20) {
        showNotification("I'm too tired...");
        return;
    }
    
    // Create custom dialog for game selection
    JDialog gameDialog = new JDialog(this, "Choose Your Game!", true);
    gameDialog.setSize(400, 300);
    gameDialog.setLocationRelativeTo(this);
    gameDialog.setLayout(new BorderLayout(10, 10));
    gameDialog.getContentPane().setBackground(new Color(240, 240, 250));
    
    // Title
    JLabel titleLabel = new JLabel("Pick a Game to Play!", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(new Color(100, 100, 255));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
    gameDialog.add(titleLabel, BorderLayout.NORTH);
    
    // Game buttons panel
    JPanel gamesPanel = new JPanel(new GridLayout(3, 1, 10, 10));
    gamesPanel.setBackground(new Color(240, 240, 250));
    gamesPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
    
    // Flappy Pet button
    JButton flappyBtn = createStyledButton("🐦 Flappy Pet - Dodge Obstacles", new Color(100, 150, 255));
    flappyBtn.addActionListener(e -> {
        gameDialog.dispose();
        new FlappyBirdGame(this);
    });
    
    // Maze Runner button
    JButton mazeBtn = createStyledButton("🏃 Maze Runner - Find the Exit", new Color(150, 100, 255));
    mazeBtn.addActionListener(e -> {
        gameDialog.dispose();
        new MazeRunnerGame(this);
    });
    
    // Catch Food button
    JButton catchBtn = createStyledButton("🍕 Catch Food - Don't Miss!", new Color(255, 140, 100));
    catchBtn.addActionListener(e -> {
        gameDialog.dispose();
        new CatchFoodGame(this);
    });
    
    gamesPanel.add(flappyBtn);
    gamesPanel.add(mazeBtn);
    gamesPanel.add(catchBtn);
    
    gameDialog.add(gamesPanel, BorderLayout.CENTER);
    
    // Cancel button
    JPanel bottomPanel = new JPanel();
    bottomPanel.setBackground(new Color(240, 240, 250));
    JButton cancelBtn = new JButton("Cancel");
    cancelBtn.setFont(new Font("Arial", Font.PLAIN, 12));
    cancelBtn.addActionListener(e -> gameDialog.dispose());
    bottomPanel.add(cancelBtn);
    gameDialog.add(bottomPanel, BorderLayout.SOUTH);
    
    gameDialog.setVisible(true);
}

    private void handleSleep() {
        if (energy > 80) {
            showNotification("I'm not sleepy!");
            return;
        }
        showNotification("Zzz...");
        currentAnimation = "sleeping";
        
        Timer sleepTimer = new Timer(500, null);
        final int[] tickCount = {0}; // Track number of ticks
        
        sleepTimer.addActionListener(e -> {
            tickCount[0]++;
            energy = Math.min(100, energy + 2);
            updateStats();
            
            if (energy >= 100) {
                sleepTimer.stop();
                // Start excited jump animation
                currentAnimation = "excited_jump";
                showNotification("All refreshed! ⭐");
                createParticles("STAR", 10);
                
                // Keep excited jump for 2 seconds
                Timer excitedTimer = new Timer(2000, evt -> {
                    currentAnimation = "idle";
                });
                excitedTimer.setRepeats(false);
                excitedTimer.start();
            }
        });
        sleepTimer.start();
    }
    
    private void handlePet() {
        happiness = Math.min(100, happiness + 10);
        showNotification("Hehe! That tickles!");
        createParticles("STAR", 5);
        currentAnimation = "giggle";
        Timer resetAnim = new Timer(1000, e -> currentAnimation = "idle");
        resetAnim.setRepeats(false);
        resetAnim.start();
        updateStats();
    }

    public void onGameComplete(int score) {
        happiness = Math.min(100, happiness + score * 3);
        hunger = Math.min(100, hunger + 15);
        energy = Math.max(0, energy - 20);
        showNotification("Great game! Score: " + score);
        createParticles("STAR", 15);
        updateStats();
    }

    private void createParticles(String type, int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(
                200 + rand.nextInt(100) - 50,
                200,
                type
            ));
        }
    }

    private void showNotification(String msg) {
        notification = msg;
        notificationTimer = 60;
    }

    private void updateStats() {
        updateBars();
        updateMood();
    }

    private void updateBars() {
        happinessBar.setValue(happiness);
        hungerBar.setValue(100 - hunger);
        energyBar.setValue(energy);
        
        happinessBar.setForeground(getBarColor(happiness));
        hungerBar.setForeground(getBarColor(100 - hunger));
        energyBar.setForeground(getBarColor(energy));
    }

    private Color getBarColor(int value) {
        if (value > 60) return new Color(100, 200, 100);
        if (value > 30) return new Color(255, 200, 0);
        return new Color(255, 100, 100);
    }

    private void updateMood() {
        if (hunger > 70) mood = "hungry";
        else if (energy < 30) mood = "sleepy";
        else if (happiness > 70) mood = "happy";
        else if (happiness < 30) mood = "sad";
        else mood = "neutral";

        moodLabel.setText("Mood: " + mood.substring(0, 1).toUpperCase() + mood.substring(1));
        moodLabel.setForeground(getMoodColor());
    }

    private Color getMoodColor() {
        return switch(mood) {
            case "happy" -> new Color(255, 215, 0);
            case "sad" -> new Color(168, 218, 220);
            case "hungry" -> new Color(255, 181, 167);
            case "sleepy" -> new Color(195, 177, 225);
            default -> new Color(149, 225, 211);
        };
    }

    private void startTimers() {
        animationTimer = new Timer(33, e -> {
            bounceFrame++;
            if (bounceFrame % 100 == 0 && currentAnimation.equals("idle")) {
                eyesOpen = false;
                Timer blinkTimer = new Timer(150, evt -> {
                    eyesOpen = true;
                    petPanel.repaint();
                });
                blinkTimer.setRepeats(false);
                blinkTimer.start();
            }
            petPanel.repaint();
        });
        animationTimer.start();

        moodTimer = new Timer(10000, e -> {
            hunger = Math.min(100, hunger + 5);
            energy = Math.max(0, energy - 3);
            happiness = Math.max(0, happiness - 2);
            updateStats();
        });
        moodTimer.start();

        particleTimer = new Timer(33, e -> {
            particles.removeIf(p -> {
                p.update();
                return p.opacity <= 0;
            });
            if (notificationTimer > 0) {
                notificationTimer--;
                if (notificationTimer == 0) notification = "";
            }
        });
        particleTimer.start();
    }

    class PetPanel extends JPanel {
        private Image backgroundImage;
        public PetPanel() {
            setBackground(new Color(255, 250, 250));
            try {
                backgroundImage = new ImageIcon("Bg.png").getImage();
            } catch (Exception e) {
                System.out.println("Background image not found: " + e.getMessage());
            }
            
            // Add mouse click listener for interaction
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int centerX = getWidth() / 2;
                    int centerY = getHeight() / 2;
                    int petY = (int)(centerY + Math.sin(bounceFrame / 10.0) * 8);
                    
                    // Check if click is within pet's body
                    int dx = e.getX() - centerX;
                    int dy = e.getY() - petY;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    
                    if (distance < 70) { // Within pet's radius
                        // Alternate between giggle and jump
                        if (Math.random() < 0.5) {
                            currentAnimation = "giggle";
                            showNotification("Hehe!");
                            createParticles("STAR", 3);
                        } else {
                            currentAnimation = "jump";
                            showNotification("Wheee!");
                        }
                        happiness = Math.min(100, happiness + 5);
                        updateStats();
                        
                        Timer resetAnim = new Timer(800, evt -> currentAnimation = "idle");
                        resetAnim.setRepeats(false);
                        resetAnim.start();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Draw background image if loaded
            if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            double bounce = Math.sin(bounceFrame / 10.0) * 8;

            // Draw particles
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            for (Particle p : particles) {
                g2.setColor(new Color(0, 0, 0, (int)(p.opacity * 255)));
                if (p.type.equals("PIZZA")) {
                    drawPizza(g2, p.x, p.y, 20);
                } else if (p.type.equals("STAR")) {
                    drawStar(g2, p.x, p.y, 15);
                }
            }

            int petY = (int)(centerY + bounce);
            Color petColor = getMoodColor();
            
            // Apply animation-specific offsets
            int animationOffsetY = 0;
            double scale = 1.0;
            
            if (currentAnimation.equals("jump")) {
                // Bigger jump for click interaction
                animationOffsetY = (int)(-50 - 20 * Math.abs(Math.sin(bounceFrame / 2.0)));
            } else if (currentAnimation.equals("excited_jump")) {
                // Even bigger jump after sleeping
                animationOffsetY = (int)(-70 - 30 * Math.abs(Math.sin(bounceFrame / 2.5)));
            } else if (currentAnimation.equals("giggle")) {
                // Wobble left and right
                int wobble = (int)(10 * Math.sin(bounceFrame / 1.5));
                animationOffsetY = wobble;
                scale = 1.0 + 0.08 * Math.abs(Math.sin(bounceFrame / 1.5));
            }
            
            petY += animationOffsetY;
            
            // Draw cute bow/accessory on top
            if (mood.equals("happy") || mood.equals("neutral")) {
                drawBow(g2, centerX, petY - 80, 30);
            }
            
            // Save original transform
            java.awt.geom.AffineTransform originalTransform = g2.getTransform();
            
            // Apply scale transformation for giggle
            if (scale != 1.0) {
                g2.translate(centerX, petY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -petY);
            }
            
            GradientPaint gradient = new GradientPaint(
                centerX - 70, petY - 70, petColor.brighter(),
                centerX + 70, petY + 70, petColor
            );
            g2.setPaint(gradient);
            
            Ellipse2D body = new Ellipse2D.Double(centerX - 70, petY - 70, 140, 140);
            g2.fill(body);

            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillOval(centerX - 60, petY + 80, 120, 20);

            // Draw eyes based on animation
            g2.setColor(Color.BLACK);
            if (currentAnimation.equals("sleeping")) {
                // Sleeping eyes (closed with Z's)
                g2.setStroke(new BasicStroke(3));
                g2.drawArc(centerX - 35, petY - 20, 20, 10, 0, -180);
                g2.drawArc(centerX + 15, petY - 20, 20, 10, 0, -180);
                
                // Draw Z's floating
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                int zOffset = (int)(Math.sin(bounceFrame / 5.0) * 5);
                g2.drawString("Z", centerX + 60, petY - 40 + zOffset);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString("z", centerX + 75, petY - 50 + zOffset);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.drawString("z", centerX + 85, petY - 60 + zOffset);
            } else if (currentAnimation.equals("giggle")) {
                // Happy closed eyes (^_^)
                g2.setStroke(new BasicStroke(3));
                g2.drawArc(centerX - 38, petY - 22, 25, 15, 0, 180);
                g2.drawArc(centerX + 13, petY - 22, 25, 15, 0, 180);
            } else if (eyesOpen) {
                // Bigger, cuter eyes with sparkle
                g2.fillOval(centerX - 38, petY - 25, 25, 25);
                g2.fillOval(centerX + 13, petY - 25, 25, 25);
                
                // White shine spots (bigger)
                g2.setColor(Color.WHITE);
                g2.fillOval(centerX - 32, petY - 19, 10, 10);
                g2.fillOval(centerX + 19, petY - 19, 10, 10);
                
                // Tiny sparkle
                g2.fillOval(centerX - 25, petY - 28, 4, 4);
                g2.fillOval(centerX + 26, petY - 28, 4, 4);
            } else {
                g2.setStroke(new BasicStroke(3));
                g2.drawArc(centerX - 35, petY - 15, 20, 10, 0, -180);
                g2.drawArc(centerX + 15, petY - 15, 20, 10, 0, -180);
            }

            // Draw mouth
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            if (currentAnimation.equals("eating")) {
                // Open mouth for eating
                g2.fillOval(centerX - 15, petY + 10, 30, 30);
                // Tongue
                g2.setColor(new Color(255, 150, 150));
                g2.fillOval(centerX - 8, petY + 25, 16, 10);
            } else {
                // Draw mouth
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                
                // Special expression for giggle/petting
                if (currentAnimation.equals("giggle")) {
                    // Big happy smile for giggling
                    g2.drawArc(centerX - 35, petY + 8, 70, 40, 0, -180);
                } else {
                    switch(mood) {
                        case "happy" -> {
                            // Wider, cuter smile
                            g2.drawArc(centerX - 30, petY + 10, 60, 35, 0, -180);
                        }
                        case "sad" -> g2.drawArc(centerX - 25, petY + 15, 50, 30, 0, 180);
                        case "hungry" -> {
                            g2.drawOval(centerX - 12, petY + 15, 24, 24);
                        }
                        default -> g2.drawLine(centerX - 20, petY + 25, centerX + 20, petY + 25);
                    }
                }
            }
            
            // Reset transformation if scaled
            g2.setTransform(originalTransform);
            
            // Add cute heart when very happy
            if (happiness > 90 && mood.equals("happy")) {
                drawHeart(g2, centerX - 90, petY - 60, 15, new Color(255, 100, 150));
                drawHeart(g2, centerX + 75, petY - 60, 15, new Color(255, 100, 150));
            }

            // Draw notification
            if (!notification.isEmpty() && notificationTimer > 0) {
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(centerX - 100, 40, 200, 50, 20, 20);
                g2.setColor(new Color(255, 215, 0));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(centerX - 100, 40, 200, 50, 20, 20);
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(notification);
                g2.drawString(notification, centerX - textWidth/2, 70);
            }
        }

        private void drawPizza(Graphics2D g2, int x, int y, int size) {
            g2.setColor(new Color(255, 200, 100));
            int[] xPoints = {x, x + size, x + size/2};
            int[] yPoints = {y, y, y - size};
            g2.fillPolygon(xPoints, yPoints, 3);
            
            g2.setColor(new Color(200, 50, 50));
            g2.fillOval(x + 5, y - 10, 5, 5);
            g2.fillOval(x + 12, y - 8, 5, 5);
        }

        private void drawStar(Graphics2D g2, int x, int y, int size) {
            g2.setColor(new Color(255, 215, 0));
            int[] xPoints = new int[10];
            int[] yPoints = new int[10];
            for (int i = 0; i < 10; i++) {
                double angle = Math.PI / 2 + (2 * Math.PI * i / 10);
                double radius = (i % 2 == 0) ? size : size / 2.5;
                xPoints[i] = (int)(x + radius * Math.cos(angle));
                yPoints[i] = (int)(y - radius * Math.sin(angle));
            }
            g2.fillPolygon(xPoints, yPoints, 10);
        }
        
        private void drawBow(Graphics2D g2, int x, int y, int size) {
            // Cute bow accessory on top of head
            g2.setColor(new Color(255, 100, 150));
            
            // Left loop
            g2.fillOval(x - size, y, size, size);
            // Right loop
            g2.fillOval(x, y, size, size);
            // Center knot
            g2.fillOval(x - size/3, y + size/4, size*2/3, size/2);
            
            // Ribbon tails
            int[] xPoints = {x - size/2, x - size - 5, x - size};
            int[] yPoints = {y + size, y + size + 15, y + size + 5};
            g2.fillPolygon(xPoints, yPoints, 3);
            
            int[] xPoints2 = {x + size/2, x + size + 5, x + size};
            int[] yPoints2 = {y + size, y + size + 15, y + size + 5};
            g2.fillPolygon(xPoints2, yPoints2, 3);
            
            // Shine effect
            g2.setColor(new Color(255, 200, 220, 150));
            g2.fillOval(x - size + 5, y + 5, size/3, size/3);
        }
        private void drawHeart(Graphics2D g2, int x, int y, int size, Color color) {
            g2.setColor(color);
            
            // Two circles for top of heart
            g2.fillOval(x, y, size/2, size/2);
            g2.fillOval(x + size/2, y, size/2, size/2);
            
            // Triangle for bottom of heart
            int[] xPoints = {x, x + size, x + size/2};
            int[] yPoints = {y + size/3, y + size/3, y + size};
            g2.fillPolygon(xPoints, yPoints, 3);
        }
    }

    class Particle {
        int x, y;
        String type;
        double opacity = 1.0;

        Particle(int x, int y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        void update() {
            y -= 2;
            opacity -= 0.02;
        }
    }

    // Flappy Bird Mini-Game
    class FlappyBirdGame extends JDialog {
        private GamePanel gamePanel;
        private Timer gameTimer;
        private int petY = 200;
        private int petVelocity = 0;
        private ArrayList<Obstacle> obstacles = new ArrayList<>();
        private int score = 0;
        private boolean gameOver = false;
        private int frameCount = 0;
        private DesktopPetSimulator parent;

        public FlappyBirdGame(DesktopPetSimulator parent) {
            super(parent, "Flappy Pet Game", true);
            this.parent = parent;
            setSize(400, 500);
            setLocationRelativeTo(parent);
            setResizable(false);

            gamePanel = new GamePanel();
            add(gamePanel);

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (!gameOver) {
                        petVelocity = -12;
                    } else {
                        endGame();
                    }
                }
            });

            addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                        petVelocity = -12;
                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
                        endGame();
                    }
                }
            });

            gameTimer = new Timer(20, e -> updateGame());
            gameTimer.start();

            setVisible(true);
        }

        private void endGame() {
            gameTimer.stop();
            parent.onGameComplete(score);
            dispose();
        }

        private void updateGame() {
            if (gameOver) return;

            frameCount++;
            petVelocity += 1; // gravity
            petY += petVelocity;

            // Add obstacles
            if (frameCount % 90 == 0) {
                int gap = 150;
                int height = 100 + new Random().nextInt(150);
                obstacles.add(new Obstacle(400, 0, height));
                obstacles.add(new Obstacle(400, height + gap, 500 - height - gap));
            }

            // Move obstacles and check scoring
            for (Obstacle obs : obstacles) {
                obs.x -= 3;
                // Score when the obstacle completely passes the pet (pet is at x=50)
                if (!obs.scored && obs.x + obs.width < 50) {
                    score++;
                    obs.scored = true;
                }
            }
            obstacles.removeIf(obs -> obs.x < -obs.width);

            // Check collision
            for (Obstacle obs : obstacles) {
                if (50 + 30 > obs.x && 50 < obs.x + obs.width) {
                    if (petY < obs.y + obs.height && petY + 30 > obs.y) {
                        gameOver = true;
                    }
                }
            }

            if (petY > 470 || petY < 0) {
                gameOver = true;
            }

            if (gameOver) {
                gameTimer.stop();
            }

            gamePanel.repaint();
        }
        

        class GamePanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2.setColor(new Color(135, 206, 235));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Obstacles
                g2.setColor(new Color(34, 139, 34));
                for (Obstacle obs : obstacles) {
                    g2.fillRect(obs.x, obs.y, obs.width, obs.height);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(obs.x, obs.y, obs.width, obs.height);
                    g2.setColor(new Color(34, 139, 34));
                }

                // Pet
                Color petColor = getMoodColor();
                g2.setColor(petColor);
                g2.fillOval(50, petY, 30, 30);
                g2.setColor(Color.BLACK);
                g2.fillOval(55, petY + 8, 5, 5);
                g2.fillOval(65, petY + 8, 5, 5);

                // Score
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.setColor(Color.WHITE);
                g2.drawString("Score: " + score, 20, 40);

                // Game over
                if (gameOver) {
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 40));
                    g2.drawString("Game Over!", 100, 200);
                    g2.setFont(new Font("Arial", Font.PLAIN, 20));
                    g2.drawString("Final Score: " + score, 130, 250);
                    g2.drawString("Click/Space to close", 90, 300);
                }
            }
        }

        class Obstacle {
            int x, y, width = 60, height;
            boolean scored = false;

            Obstacle(int x, int y, int height) {
                this.x = x;
                this.y = y;
                this.height = height;
            }
        }
    }

// Maze Runner Mini-Game
class MazeRunnerGame extends JDialog {
    private MazePanel mazePanel;
    private int playerX = 1, playerY = 1;
    private int goalX = 13, goalY = 13;
    private int score = 0;
    private int moves = 0;
    private boolean gameWon = false;
    private DesktopPetSimulator parent;
    
    // Simple maze (0 = wall, 1 = path)
    private int[][] maze;
    private void generateMaze() {
    int size = 15; // Bigger maze (was 15)
    maze = new int[size][size];
    
    // Fill with walls
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            maze[i][j] = 0;
        }
    }
    
    // Recursive backtracking maze generation
    Random rand = new Random();
    carvePath(1, 1, rand);
    
    // Ensure start and goal are paths
    maze[1][1] = 1;
    maze[size-2][size-2] = 1;
    
    // Update goal position
    goalX = size - 2;
    goalY = size - 2;
}

private void carvePath(int x, int y, Random rand) {
    maze[y][x] = 1;
    
    // Directions: right, down, left, up
    int[][] directions = {{2, 0}, {0, 2}, {-2, 0}, {0, -2}};
    
    // Shuffle directions
    for (int i = directions.length - 1; i > 0; i--) {
        int j = rand.nextInt(i + 1);
        int[] temp = directions[i];
        directions[i] = directions[j];
        directions[j] = temp;
    }
    
    // Try each direction
    for (int[] dir : directions) {
        int nx = x + dir[0];
        int ny = y + dir[1];
        
        if (nx > 0 && ny > 0 && nx < maze[0].length - 1 && ny < maze.length - 1 && maze[ny][nx] == 0) {
            maze[y + dir[1]/2][x + dir[0]/2] = 1; // Carve path between cells
            carvePath(nx, ny, rand);
        }
    }
}

    public MazeRunnerGame(DesktopPetSimulator parent) {
        super(parent, "Maze Runner", true);
        this.parent = parent;
        generateMaze(); // Generate random maze
        setSize(450, 500);
        setLocationRelativeTo(parent);
        setResizable(false);

        mazePanel = new MazePanel();
        add(mazePanel);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (gameWon) {
                    endGame();
                    return;
                }
                
                int newX = playerX;
                int newY = playerY;
                
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP, KeyEvent.VK_W -> newY--;
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S -> newY++;
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> newX--;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> newX++;
                }
                
                // Check if move is valid
                if (maze[newY][newX] == 1) {
                    playerX = newX;
                    playerY = newY;
                    moves++;
                    
                    // Check if reached goal
                    if (playerX == goalX && playerY == goalY) {
                        gameWon = true;
                        score = Math.max(0, 100 - moves);
                    }
                    
                    mazePanel.repaint();
                }
            }
        });

        setVisible(true);
    }

    private void endGame() {
        parent.onGameComplete(score);
        dispose();
    }

    class MazePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cellSize = 30;
            
            // Draw maze
            for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[y].length; x++) {
                    if (maze[y][x] == 0) {
                        g2.setColor(new Color(60, 60, 60));
                        g2.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    } else {
                        g2.setColor(new Color(240, 240, 250));
                        g2.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
            
            // Draw goal
            g2.setColor(new Color(255, 215, 0));
            g2.fillOval(goalX * cellSize + 5, goalY * cellSize + 5, cellSize - 10, cellSize - 10);
            g2.setColor(new Color(255, 180, 0));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(goalX * cellSize + 5, goalY * cellSize + 5, cellSize - 10, cellSize - 10);
            
            // Draw player (pet)
            Color petColor = getMoodColor();
            g2.setColor(petColor);
            g2.fillOval(playerX * cellSize + 3, playerY * cellSize + 3, cellSize - 6, cellSize - 6);
            
            // Eyes
            g2.setColor(Color.BLACK);
            g2.fillOval(playerX * cellSize + 8, playerY * cellSize + 10, 4, 4);
            g2.fillOval(playerX * cellSize + 16, playerY * cellSize + 10, 4, 4);

            // Instructions and stats
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Moves: " + moves, 10, 470);
            g2.drawString("Use WASD or Arrow Keys", 150, 470);
            
            // Game won
            if (gameWon) {
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 40));
                g2.drawString("You Win!", 140, 200);
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.drawString("Score: " + score, 170, 250);
                g2.drawString("Press any key to close", 100, 300);
            }
        }
    }
}

    // Catch the Falling Food Mini-Game
class CatchFoodGame extends JDialog {
    private CatchPanel catchPanel;
    private Timer gameTimer;
    private int basketX = 200;
    private ArrayList<FallingFood> foods = new ArrayList<>();
    private int score = 0;
    private int missed = 0;
    private boolean gameOver = false;
    private int frameCount = 0;
    private DesktopPetSimulator parent;

    public CatchFoodGame(DesktopPetSimulator parent) {
        super(parent, "Catch the Food", true);
        this.parent = parent;
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setResizable(false);

        catchPanel = new CatchPanel();
        add(catchPanel);

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                basketX = Math.max(30, Math.min(e.getX(), 370));
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (gameOver) {
                    endGame();
                } else {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_LEFT, KeyEvent.VK_A -> basketX = Math.max(30, basketX - 20);
                        case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> basketX = Math.min(370, basketX + 20);
                    }
                }
            }
        });

        gameTimer = new Timer(20, e -> updateGame());
        gameTimer.start();

        setVisible(true);
    }

    private void endGame() {
        gameTimer.stop();
        parent.onGameComplete(score);
        dispose();
    }

    private void updateGame() {
        if (gameOver) return;

        frameCount++;
        
        // Spawn food
        if (frameCount % 50 == 0) {
            foods.add(new FallingFood(new Random().nextInt(350) + 25));
        }

        // Update foods
        for (FallingFood food : foods) {
            food.y += 3;
            
            // Check catch
            if (food.y > 420 && food.y < 450 && 
                Math.abs(food.x - basketX) < 40 && !food.caught) {
                food.caught = true;
                score++;
            }
            
            // Check missed
            if (food.y > 500 && !food.caught && !food.missed) {
                food.missed = true;
                missed++;
            }
        }
        
        foods.removeIf(f -> f.y > 520);

        // Game over after 5 misses
        if (missed >= 5) {
            gameOver = true;
        }

        catchPanel.repaint();
    }

    class CatchPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(new Color(135, 206, 235));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw falling foods
            for (FallingFood food : foods) {
                if (!food.caught) {
                    // Pizza slice
                    g2.setColor(new Color(255, 200, 100));
                    int[] xPoints = {food.x - 15, food.x + 15, food.x};
                    int[] yPoints = {food.y, food.y, food.y - 20};
                    g2.fillPolygon(xPoints, yPoints, 3);
                    
                    g2.setColor(new Color(200, 50, 50));
                    g2.fillOval(food.x - 5, food.y - 10, 6, 6);
                    g2.fillOval(food.x + 3, food.y - 8, 6, 6);
                }
            }

            // Draw basket (pet's face)
            Color petColor = getMoodColor();
            g2.setColor(petColor);
            g2.fillOval(basketX - 30, 420, 60, 60);
            
            // Eyes
            g2.setColor(Color.BLACK);
            g2.fillOval(basketX - 18, 435, 10, 10);
            g2.fillOval(basketX + 8, 435, 10, 10);
            
            // Mouth
            g2.setStroke(new BasicStroke(2));
            g2.drawArc(basketX - 15, 445, 30, 20, 0, -180);

            // Score display
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.WHITE);
            g2.drawString("Score: " + score, 20, 30);
            g2.drawString("Missed: " + missed + "/5", 280, 30);

            // Instructions
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("Move mouse or A/D keys", 110, 55);

            // Game over
            if (gameOver) {
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 40));
                g2.drawString("Game Over!", 100, 200);
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.drawString("Final Score: " + score, 130, 250);
                g2.drawString("Press any key to close", 90, 300);
            }
        }
    }

    class FallingFood {
        int x, y = 0;
        boolean caught = false;
        boolean missed = false;

        FallingFood(int x) {
            this.x = x;
        }
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DesktopPetSimulator simulator = new DesktopPetSimulator();
            simulator.setVisible(true);
        });
    }
}