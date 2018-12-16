
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Bootleg Frogger.
 * 
 * @author tayloreyben0315
 */

public class Frog extends javax.swing.JFrame implements KeyListener {

    // Timers for the jumps and game
    public int jumpTimer, maxTimer = 10, timeLeft;
    // Window length and height
    public int winL = 460, winH = 492;
    // Offset for game window
    public int Xoffset = 8, Yoffset = 31;
    // Frog details - Xpos, Ypos, height and width
    public int frogX, frogY, frogW = 24, frogH = 24;
    // How many lives the player has left, the player's score, and the current state of the game (start, halfway, end)
    public int lives, state, score, finScore;
    // ArrayLists of cars and logs (for their X positions)
    public ArrayList<Integer> lane1 = new ArrayList(), lane2 = new ArrayList(), lane4 = new ArrayList(), lane5 = new ArrayList();
    public ArrayList<Integer> logs1 = new ArrayList(), logs2 = new ArrayList(), logs3 = new ArrayList(), logs4 = new ArrayList(), logs5 = new ArrayList();
    // Constant height of logs and car lanes
    public int lane1Y = 256, lane2Y = 288, lane3Y = 320, lane4Y = 352, lane5Y = 384;
    public int logs1Y = 64, logs2Y = 96, logs3Y = 128, logs4Y = 160, logs5Y = 192;
    // Log and car heights and widths
    public int lane1W = 40, lane2W = 60, lane3W = 75, lane4W = 60, lane5W = 40, lane3X = 0, carH = 28, logs1W = 65, logs2W = 80, logH = 26;
    // Speeds of the different car lanes and logs
    int lane1sp = 3, lane2sp = 2, lane3sp = 3, lane4sp = 1, lane5sp = 3, logs1sp = 1, logs2sp = 2;
    // If the frog is alive or not, the result of the game (win/lose), if the frog is safe on the water (goal/log), if time has been stopped, and if the middle car is facing left
    public boolean result, safe, zaWarudo = false, left = false, start = true, alive;
    // Defining images
    BufferedImage frog, lpad, win, lose, bi, car1, car2, car3, car3b, car4, car5, log1, log2;
    Graphics big;
    
    CarTimer carTimer = new CarTimer();
    
    public Frog() {
        
        initComponents();
        addKeyListener(this);
        setSize(winL, winH);
        
        bi = (BufferedImage)createImage(winL, winH);
        big = bi.createGraphics();
        
        File frogLoc = new File("./img/frog.png");
        File lpadLoc = new File("./img/lilypad.png");
        File winLoc = new File("./img/win.png");
        File loseLoc = new File("./img/lose.png");
        File car1Loc = new File("./img/car1.png");
        File car2Loc = new File("./img/car2.png");
        File car3Loc = new File("./img/car3.png");
        File car3bLoc = new File("./img/car3b.png");
        File car4Loc = new File("./img/car4.png");
        File car5Loc = new File("./img/car5.png");
        File log1Loc = new File("./img/log1.png");
        File log2Loc = new File("./img/log2.png");
        
        try {
            frog = ImageIO.read(frogLoc);
            lpad = ImageIO.read(lpadLoc);
            win = ImageIO.read(winLoc);
            lose = ImageIO.read(loseLoc);
            car1 = ImageIO.read(car1Loc);
            car2 = ImageIO.read(car2Loc);
            car3 = ImageIO.read(car3Loc);
            car3b = ImageIO.read(car3bLoc);
            car4 = ImageIO.read(car4Loc);
            car5 = ImageIO.read(car5Loc);
            log1 = ImageIO.read(log1Loc);
            log2 = ImageIO.read(log2Loc);
        } catch(Exception e) {
            System.out.println("Error loading image.");
        }
        
        jumpTimer = maxTimer;
        
        // Adds cars and logs at specified locations
        lane1.add(0);
        lane1.add(160);
        lane1.add(320);
        
        lane2.add(5);
        lane2.add(160);
        
        lane4.add(0);
        lane4.add(160);
        lane4.add(320);
        
        lane5.add(160);
        lane5.add(400);
        
        logs1.add(0);
        logs1.add(160);
        logs1.add(320);
        
        logs2.add(5);
        logs2.add(160);
        
        logs3.add(0);
        logs3.add(160);
        logs3.add(320);
        
        logs4.add(160);
        logs4.add(400);
        
        logs5.add(20);
        logs5.add(140);
        logs5.add(320);
        
        newGame();
        
        carTimer.start();
    }
    
    public void newGame() {
        // Resets lives and score to zero
        lives = 3;
        score = 0;
        
        // Defaults to lose, makes the player alive, turns on timer, and sets the state to 0 (start)
        result = false;
        zaWarudo = false;
        state = 0;
        alive = true;
        
        // Initial starting of timer, position of frog...
        posReset();
        resetTimer();
    }
    
    public void posReset() {
        // Sets the frog's X position to the middle, and the Y position depending on the progress (0 = start, 1 = halfway)
        frogX = 223 - frogW/2 + Xoffset + (32 - frogW)/2;
        if (state == 0) frogY = 384 + Yoffset + (32 - frogH)/2;
        else frogY = Yoffset + (32 - frogH)/2;
        // Delay the players movement on respawn to prevent accidental player death
        jumpTimer = 15;
    }
    
    // Resets the timer back to its initial value
    public void resetTimer() {
        timeLeft = 2500;
    }
    
    public void paint(Graphics g) {
        
        if (state != -1) finScore = score;
        
        if (lives == 0) {
            state = -1;
            zaWarudo = true;
            alive = false;
        }
        safe = false;
        
        big.clearRect(0, 0, winL, winH);
        
        try {
            File frogLoc = new File("./img/frog.png");
            File frogdLoc = new File("./img/frogd.png");
            if (state == 1) {
                frog = ImageIO.read(frogdLoc);
            } else {
                frog = ImageIO.read(frogLoc);
            }
        } catch(Exception e) { }
        
        // Setting the background
        Color green = new Color(0x27c451);
        Color blue = new Color(0x0791d8);
        Color brown = new Color(0x663300);
        
        // Goal Line
        big.setColor(Color.black);
        big.fillRect(0, 0, 999, 999);
        
        // Water
        big.setColor(blue);
        big.fillRect(Xoffset, 0, winL, 224+Yoffset);
        
        // Grass (Safe zone)
        big.setColor(green);
        big.fillRect(Xoffset, 192 + Yoffset, winL, 224);
        
        // Road
        big.setColor(Color.gray);
        big.fillRect(Xoffset, 224 + Yoffset, winL, 160);
        
        // Road Lines
        big.setColor(Color.yellow);
        for (int i = 0; i < 32; i++) {
            int xPosStart = 32*i + Xoffset;
            for (int b = 1; b < 5; b++) {
                int yPosStart = 32*b + Yoffset + 224;
                big.drawLine(xPosStart + 3, yPosStart, xPosStart + 20, yPosStart);
            }
        }
        
        // Goals
        for (int i = 0; i < 5; i++) {
            big.drawImage(lpad, 28 + i*96, 32, this);
            
            if (collision(frogX, frogY, frogW, frogH, 24 + i*96, 32, 32, 32) == true) {
                safe = true;
                if (state != 1) {
                    state = 1;
                    score += 50;
                    score += (timeLeft/2);
                }
            }
        }
        
        big.setColor(brown);
        // Logs 1
        for (int i = 0; i < logs1.size(); i++) {
            big.drawImage(log1, logs1.get(i), logs1Y, this);
            
            if (collision(frogX, frogY, frogW, frogH, logs1.get(i), logs1Y, logs1W, logH) == true) {
                safe = true;
                if (!zaWarudo) moveX(-logs1sp);
            }
        }
        
        // Logs 2
        for (int i = 0; i < logs2.size(); i++) {
            big.drawImage(log2, logs2.get(i), logs2Y, this);
            
            if (collision(frogX, frogY, frogW, frogH, logs2.get(i), logs2Y, logs2W, logH) == true) {
                safe = true;
                if (!zaWarudo) moveX(logs2sp);
            }
        }
        
        // Logs 3
        for (int i = 0; i < logs3.size(); i++) {
            big.drawImage(log2, logs3.get(i), logs3Y, this);
            
            if (collision(frogX, frogY, frogW, frogH, logs3.get(i), logs3Y, logs2W, logH) == true) {
                safe = true;
                if (!zaWarudo) moveX(logs1sp);
            }
        }
        
        // Logs 4
        for (int i = 0; i < logs4.size(); i++) {
            big.drawImage(log1, logs4.get(i), logs4Y, this);
            
            if (collision(frogX, frogY, frogW, frogH, logs4.get(i), logs4Y, logs1W, logH) == true) {
                safe = true;
                if (!zaWarudo) moveX(-logs1sp);
            }
        }
        
        // Logs 5
        for (int i = 0; i < logs5.size(); i++) {
            big.drawImage(log2, logs5.get(i), logs5Y, this);
            
            if (collision(frogX, frogY, frogW, frogH, logs5.get(i), logs5Y, logs2W, logH) == true) {
                safe = true;
                if (!zaWarudo) moveX(logs2sp);
            }
        }
        
        // Frog
        big.setColor(Color.green);
        if (state != -1) big.drawImage(frog, frogX+1, frogY+1, this);
        
        // Lane 1
        big.setColor(Color.blue);
        
        for (int i = 0; i < lane1.size(); i++) {
            big.drawImage(car1, lane1.get(i), lane1Y, this);
            if (collision(frogX, frogY, frogW, frogH, lane1.get(i), lane1Y, lane1W, carH) == true) {
                lives--;
                posReset();
            }
        }
        
        // Lane 2
        for (int i = 0; i < lane2.size(); i++) {
            big.drawImage(car2, lane2.get(i), lane2Y, this);
            if (collision(frogX, frogY, frogW, frogH, lane2.get(i), lane2Y, lane2W, carH) == true) {
                lives--;
                posReset();
            }
        }
        
        // Lane 3
        big.setColor(Color.red);
        if (left)  {
            big.drawImage(car3, lane3X, lane3Y, this);
        } else {
            big.drawImage(car3b, lane3X, lane3Y, this);
        }
        
        if (collision(frogX, frogY, frogW, frogH, lane3X, lane3Y, lane3W, carH) == true) {
            lives--;
            posReset();
        }
        
        // Lane 4
        big.setColor(Color.green);
        for (int i = 0; i < lane4.size(); i++) {
            big.drawImage(car4, lane4.get(i), lane4Y, this);
            if (collision(frogX, frogY, frogW, frogH, lane4.get(i), lane4Y, lane4W, carH) == true) {
                lives--;
                posReset();
            }
        }
        
        // Lane 5
        for (int i = 0; i < lane5.size(); i++) {
            big.drawImage(car5, lane5.get(i), lane5Y, this);
            if (collision(frogX, frogY, frogW, frogH, lane5.get(i), lane5Y, lane5W, carH) == true) {
                lives--;
                posReset();
            }
        }
        
        // If the state is 1 (halfway point), check if the frog is at the finish line (the starting line)
        if (state == 1) {
            if (collision(frogX, frogY, frogW, frogH, 0, 416, winL, 32)) {
                score += lives*50;
                score += timeLeft/2;
                state = -1;
                result = true;
            }
        }
        
        // If the frog is NOT safe on water (not on log or goal), then check if the frog is on water
        if (!safe) {
            if (collision(frogX, frogY, frogW, frogH, 0, 0, winL, 224) == true) {
                lives--;
                posReset();
            }
        }

        // If timer runs out, put frog back to start, reset the timer, and take 100 away from score
        if (timeLeft <= 0) {
            state = 0;
            lives--;
            score-=100;
            posReset();
            resetTimer();
        }
        
        // If the frog goes offscreen (via log), then kill the frog.
        if (frogX + frogW <= 0) {
            lives--;
            posReset();
        } else if (frogX >= winL) {
            lives--;
            posReset();
        }
        
        // Info at the bottom of the screen that displays life counter, score, and remaining time
        big.setColor(Color.white);
        big.drawString("LIVES: " + lives, 12, 470);
        big.drawString("SCORE: " + (state != -1 ? (score <= 9999 ? score : 9999) : finScore), 122, 470);
        big.drawString("TIME", 396, 470);
        // Timer that displays remaining time.
        big.setColor(Color.green);
        big.fillRect(240, 454, (int)timeLeft/10/2, 24);
        
        // If the game's state is -1 (game end)
        if (state == -1 || !alive) {
            alive = false;
            zaWarudo = true;
            if (result) {
                big.drawImage(win, 0, 0, this);
            } else {
                big.drawImage(lose, 10, 10, this);
            }
        }
        
        g.drawImage(bi, 0, 0, this);
    }
    
    public void keyPressed(KeyEvent ke) {
        if (jumpTimer != 0) return;
        
        int leapLength = 32;
        
        if (state != -1 || alive) {
            switch(ke.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (frogY-leapLength <= 5) return;
                    if (state == 0) score += 10;
                    else score -= 10;
                    moveY(-leapLength);
                    break;
                case KeyEvent.VK_DOWN:
                    if (frogY+frogH+leapLength >= 446) return;
                    if (state == 0) score -= 10;
                    else score += 10;
                    moveY(leapLength);
                    break;
                case KeyEvent.VK_RIGHT:
                    if (frogX+frogW+leapLength >= Xoffset+winL) return;
                    moveX(leapLength);
                    break;
                case KeyEvent.VK_LEFT:
                    if (frogX-leapLength <= Xoffset) return;
                    moveX(-leapLength);
                    break;
                case KeyEvent.VK_SPACE:
                    if (!zaWarudo) zaWarudo = true;
                    else zaWarudo = false;
                    break;
            }
        } else {
            switch(ke.getKeyCode()) {
                case KeyEvent.VK_Y:
                    if (state == -1) newGame();
                    break;
                case KeyEvent.VK_N:
                    if (state == -1) System.exit(0);
                    break;
                }
        }
        
        jumpTimer = maxTimer;
        
        repaint();
    }
    
    public void keyTyped(KeyEvent ke) { }
    
    public void keyReleased(KeyEvent ke) { }
    
    class CarTimer extends Thread {
        public void run() {
            
            while (true) {
                try {
                    if (zaWarudo == false) Thread.sleep(10);
                    
                    // Jumping delay
                    if (jumpTimer != 0) jumpTimer -= 1;
                    
                    if (zaWarudo == false) {
                        // Timer
                        timeLeft -= 1;
                        
                        // Lane 1
                        for (int i = 0; i < lane1.size(); i++) {
                            lane1.set(i, lane1.get(i) - lane1sp);
                            if (lane1.get(i) + lane1W <= 0) lane1.set(i, winL);
                        }
                        
                        // Lane 2
                        for (int i = 0; i < lane2.size(); i++) {
                            lane2.set(i, lane2.get(i) - lane2sp);
                            if (lane2.get(i) + lane2W <= 0) lane2.set(i, winL);
                        }
                        
                        // Lane 3
                        if (left) {
                            lane3X-=lane3sp;
                            if (lane3X+lane3W <= 0) left = false;
                        } else {
                            lane3X+=lane3sp;
                            if (lane3X >= winL) left = true;
                        }
                        
                        // Lane 4
                        for (int i = 0; i < lane4.size(); i++) {
                            lane4.set(i, lane4.get(i) + lane4sp);
                            if (lane4.get(i) >= winL) lane4.set(i, -lane4W);
                        }
                        
                        // Lane 5
                        for (int i = 0; i < lane5.size(); i++) {
                            lane5.set(i, lane5.get(i) + lane5sp);
                            if (lane5.get(i) >= winL) lane5.set(i, -lane5W);
                        }
                        
                        // Logs
                        // Logs 1
                        
                        for (int i = 0; i < logs1.size(); i++) {
                            logs1.set(i, logs1.get(i) - logs1sp);
                            if (logs1.get(i) + logs1W <= 0) logs1.set(i, winL);
                        }
                        
                        // Logs 2
                        for (int i = 0; i < logs2.size(); i++) {
                            logs2.set(i, logs2.get(i) + logs2sp);
                            if (logs2.get(i) >= winL) logs2.set(i, -logs2W);
                        }
                        
                        // Logs 3
                        for (int i = 0; i < logs3.size(); i++) {
                            logs3.set(i, logs3.get(i) + logs1sp);
                            if (logs3.get(i) >= winL) logs3.set(i, -logs2W);
                        }
                        
                        // Logs 4
                        for (int i = 0; i < logs4.size(); i++) {
                            logs4.set(i, logs4.get(i) - logs1sp);
                            if (logs4.get(i) + logs1W <= 0) logs4.set(i, winL);
                        }
                        
                        // Logs 5
                        for (int i = 0; i < logs5.size(); i++) {
                            logs5.set(i, logs5.get(i) + logs2sp);
                            if (logs5.get(i) >= winL) logs5.set(i, -logs2W);
                        }
                    }
                    
                    repaint();
                } catch (Exception e) {
                    System.out.println("Thread error!");
                }
            }
        }
    }
    
    public void moveX(int i) {
        frogX += i;
    }
    public void moveY(int i) {
        frogY += i;
    }
    
    public boolean collision(int aX, int aY, int aW, int aH, int bX, int bY, int bW, int bH) {
        
        aW/=2;
        aH/=2;
        
        if (aX + aW >= bX) {
            if (aX + aW <= bX + bW) {
                if (aY + aH >= bY) {
                    if (aY + aH <= bY + bH) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    //<editor-fold defaultstate="collapsed" desc="Default stuff">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(460, 480));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}