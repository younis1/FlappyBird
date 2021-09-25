import bagel.*;

import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @author: Younis Naser
 */
public class ShadowFlap extends AbstractGame {
    private int levelNumber = 0;
    private int score = 0;
    private int frameCounter = 0;
    private int emptyLives = 0;
    private int lastCollisionInScore = 0;
    private int currentLevel = 0;
    private int waitingFrames = 0;
    private static final double INITIAL_X = 200;
    private static final double INITIAL_Y = 350;
    private final int WAITING_FRAMES_AFTER_LEVEL_O = 20;
    private final int LEVEL_0_LIVES = 3;
    private final int LEVEL_1_LIVES = 6;
    private final int LEVEL_0_SCORE = 10;
    private final int WINNING_SCORE = 30;
    private final int WINDOW_WIDTH = 1024;
    private final int WINDOW_HEIGHT = 768;
    private final int LEFTMOST_LIFE_X = 100;
    private final int LEFTMOST_LIFE_Y = 15;
    private final int LIFE_SPACE = 50;
    private final int PIPES_FRAME_DIFF = 100;
    private final int SCORE_INDENT = 100;
    private final int FINAL_SCORE_SHIFT = 75;
    private final Font font = new Font("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\font\\slkscr.ttf", 48);
    private final Image backgroundImageLevel0 = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-0\\background.png");
    private final Image noLife = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level\\noLife.png");
    private final Image fullLife = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level\\fullLife.png");
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private boolean level_0_completed = false;
    private boolean level_1_completed = false;
    private Birdie birdieLevel_0 = new Birdie(0);
    private ArrayList<Pipes> pipesLevel_0 = new ArrayList<Pipes>();

    private ArrayList<Pipes> pipesLevel_1 = new ArrayList<Pipes>();
    private Birdie birdieLevel_1 = new Birdie(1);
    private final Image backgroundImageLevel1 = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-1\\background.png");
    private final int WAITING_FRAMES_BETWEEN_LEVELS = 20;

    public ShadowFlap() {
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */

    /**
     * else {
     * //GameOver Level 0
     * font.drawString("GAME OVER",(WINDOW_WIDTH - font.getWidth("GAME OVER"))/2,
     * WINDOW_HEIGHT/2);
     * font.drawString("FINAL SCORE: " + score,(WINDOW_WIDTH - font.getWidth("FINAL SCORE: "))/2,
     * WINDOW_HEIGHT/2 + FINAL_SCORE_SHIFT );
     * }
     */
    @Override
    public void update(Input input) {
        if (!gameEnded) {
            //Waiting Screen and Lvl 0

            if (!gameStarted) {
                backgroundImageLevel0.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
                font.drawString("PRESS SPACE TO START", (WINDOW_WIDTH - font.getWidth("PRESS SPACE TO START")) / 2,
                        WINDOW_HEIGHT / 2);
                if (input.isDown(Keys.SPACE)) {
                    this.gameStarted = true;
                }
            } 
        }
    }
}
