package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public final class Board extends JPanel implements ActionListener {

    private Image apple;
    private Image dot;
    private Image head;

    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 29;

    private int appleX;
    private int appleY;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame = true;

    private int dots;
    private int score;
    private Timer timer;


    public Board() {
        addKeyListener(new TAdapter());

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 300));
        setFocusable(true);

        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon appleIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
        apple = appleIcon.getImage();

        ImageIcon dotIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
        dot = dotIcon.getImage();

        ImageIcon headIcon = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
        head = headIcon.getImage();
    }

    private void initGame() {
        dots = 3;
        score = 0;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();
        timer = new Timer(140, this);
        timer.start();
    }

    private void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        appleX = r * DOT_SIZE;

        r = (int) (Math.random() * RANDOM_POSITION);
        appleY = r * DOT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            
            drawScore(g);
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }
    
    private void drawScore(Graphics g) {
        String scoreMsg = "Score: " + score;
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(scoreMsg, 10,20);
    }
    

    private void gameOver(Graphics g) {
        String msg = "Game Over!";
        String scoreMsg = "Score: " + score;
        String newMsg = "Press N to start new game";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (300 - metrics.stringWidth(msg)) / 2, 300 / 2 - 10);
        g.drawString(scoreMsg, (300 - metrics.stringWidth(scoreMsg)) / 2, 300 / 2 + 10);
        g.drawString(newMsg, (300 - metrics.stringWidth(newMsg)) / 2, 300 / 2 + 30);
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            dots++;
            score++;
            locateApple();
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }
        if (y[0] >= 300 || x[0] >= 300 || y[0] < 0 || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
            repaint();
        }
    }


    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if ((key == KeyEvent.VK_N) && (!inGame)) {
                initGame();
                inGame = true;
                timer.start();
            }
        }
    }
}
