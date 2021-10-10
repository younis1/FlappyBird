import bagel.*;
import bagel.Font;
import bagel.Image;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @author: Younis Naser
 */
public class ShadowFlap extends AbstractGame {
    private int score = 0;
    private double frameCounter = 0;
    private int weapons_counter = 0;
    private int emptyLives = 0;
    private int waitingFrames = 0;
    private static final double INITIAL_X = 200;
    private static final double INITIAL_Y = 350;
    private final int WAITING_FRAMES_AFTER_LEVEL_O = 20;
    private final int LEVEL_0_LIVES = 3;
    private final int LEVEL_1_LIVES = 6;
    private final int LEVEL_0_SCORE = 10;
    private final int LEVEL_1_SCORE = 30;
    private final int WINDOW_WIDTH = 1024;
    private final int WINDOW_HEIGHT = 768;
    private final int LEFTMOST_LIFE_X = 100;
    private final int LEFTMOST_LIFE_Y = 15;
    private final int LIFE_SPACE = 50;
    private final int PIPES_FRAME_DIFF = 100;
    private final int SCORE_INDENT = 100;
    private final int FINAL_SCORE_SHIFT = 75;
    private final int S_TO_SHOOT_SHIFT = 68;
    private final int WEAPONS_INTERVAL_HUNDREDS = 5;
    private final Font font = new Font("ynaser-project-2\\res\\font\\slkscr.ttf", 48);
    private final Image backgroundImageLevel0 = new Image("ynaser-project-2\\res\\level-0\\background.png");
    private final Image noLife = new Image("ynaser-project-2\\res\\level\\noLife.png");
    private final Image fullLife = new Image("ynaser-project-2\\res\\level\\fullLife.png");
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private boolean level_0_completed = false;
    private boolean level_1_completed = false;

    private Birdie birdieLevel_0 = new Birdie(0);
    private ArrayList<Pipes> pipesLevel_0 = new ArrayList<Pipes>();

    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    private ArrayList<Pipes> pipesLevel_1 = new ArrayList<Pipes>();
    private Birdie birdieLevel_1 = new Birdie(1);
    private final Image backgroundImageLevel1 = new Image("ynaser-project-2\\res\\level-1\\background.png");
    private final int WAITING_FRAMES_BETWEEN_LEVELS = 20;
    public ShadowFlap() {
    }

    /**
     * The entry point for the program.
     * @param args
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }



    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input // input from keyboard
     */
    @Override
    public void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            System.exit(0);
        }


        if (!gameEnded) {

            //Waiting Screen and Lvl 0
            if (!gameStarted && !level_0_completed) {
                backgroundImageLevel0.draw(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0);
                font.drawString("PRESS SPACE TO START", (WINDOW_WIDTH - font.getWidth("PRESS SPACE TO START")) / 2.0,
                        WINDOW_HEIGHT / 2.0);

                if (input.wasPressed(Keys.SPACE)) {
                    this.gameStarted = true;
                }
            }

            else if(gameStarted && !level_0_completed) {
                // level 0 in progress
                // set speed
                backgroundImageLevel0.draw(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0);
                frameCounter += Pipes.getChangePercent();  // to generate pipes every "100/frames*timescale" frames
                detectSpeed(input);


                // Drawing lives
                drawHearts(emptyLives, 0);

                // Drawing Bird
                birdieLevel_0.update(input, Pipes.getChangePercent());

                //Drawing Score
                font.drawString("SCORE: " + score, SCORE_INDENT, SCORE_INDENT);

                // Adding new pipes every 100 frames, or shorter if faster timeframes
                if (frameCounter >= PIPES_FRAME_DIFF) {

                    frameCounter = 0;
                    pipesLevel_0.add(new Pipes(0));
                }

                // Detecting out of bound
                if (birdieLevel_0.getPosition().y > WINDOW_HEIGHT || birdieLevel_0.getPosition().y < 0) {
                    emptyLives += 1;
                    birdieLevel_0.setPosition(INITIAL_X, INITIAL_Y);
                }

                // Detecting Collision with pipes and deleting out-of-bound pipes, and drawing pipes
                // and adding score if pipes passed
                collisionOrScoreDetector( pipesLevel_0,  birdieLevel_0, input);

                if (emptyLives == LEVEL_0_LIVES) {
                    gameEnded = true;
                }

                if (score == LEVEL_0_SCORE) {
                    level_0_completed = true;
                }
            }
            // LEVEL 0 IS COMPLETED

            else if (waitingFrames < WAITING_FRAMES_AFTER_LEVEL_O) {
                //Level up screen
                backgroundImageLevel0.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
                waitingFrames += 1;
                font.drawString("LEVEL-UP!", (WINDOW_WIDTH - font.getWidth("LEVEL-UP!")) / 2,
                        WINDOW_HEIGHT / 2);

                // reset everything
                frameCounter = 0;
                emptyLives = 0;
                gameStarted = false;
                score = 0;
                Pipes.resetSpeed();

            } else if (!gameStarted && !level_1_completed){
                // Level 1 started
                backgroundImageLevel1.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
                font.drawString("PRESS SPACE TO START",
                        (WINDOW_WIDTH - font.getWidth("PRESS SPACE TO START")) / 2,
                        WINDOW_HEIGHT / 2);
                font.drawString("Press S to Shoot",(WINDOW_WIDTH - font.getWidth("PRESS S TO SHOOT")) /2,
                        WINDOW_HEIGHT / 2 + S_TO_SHOOT_SHIFT);
                if (input.wasPressed(Keys.SPACE)) {
                    this.gameStarted = true;
                }
            }
            else if (gameStarted && !level_1_completed) {
                // Lvl 1
                // Drawing lives
                backgroundImageLevel1.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);

                // to generate pipes every "100/frames*timescale" frames
                frameCounter += Pipes.getChangePercent();

                // set speed
                detectSpeed(input);

                // drawing Hearts
                drawHearts(emptyLives, 1);

                // Drawing Bird
                birdieLevel_1.update(input, Pipes.getChangePercent());

                //Drawing Score
                font.drawString("SCORE: " + score, SCORE_INDENT, SCORE_INDENT);

                // Adding new pipes every 100/timescale frames
                // Adding weapons every "WEAPONS_INTERVAL_HUNDREDS" * 100/timescale frames
                if (frameCounter >= PIPES_FRAME_DIFF) {
                    frameCounter = 0;
                    pipesLevel_1.add(new Pipes(1));
                    weapons_counter += 1;
                    if (weapons_counter % WEAPONS_INTERVAL_HUNDREDS ==0){
                        double[] coordinates = getCoordinatesOfLastPipe(pipesLevel_1);
                        weapons_counter = 0;
                        weapons.add(new Weapon(coordinates[0], coordinates[1], coordinates[2], coordinates[3]));
                    }
                }

                // implements everything about weapons
                weaponsLogic(weapons, birdieLevel_1, pipesLevel_1, input);


                // Detecting out of bound
                if (birdieLevel_1.getPosition().y > WINDOW_HEIGHT || birdieLevel_1.getPosition().y < 0) {
                    emptyLives += 1;
                    birdieLevel_1.setPosition(INITIAL_X, INITIAL_Y);
                }

                // Detecting Collision with pipes and deleting out-of-bound pipes, and drawing pipes
                // and adding score
                collisionOrScoreDetector( pipesLevel_1,  birdieLevel_1, input);
                if (emptyLives == LEVEL_1_LIVES) {
                    gameEnded = true;
                }
                if (score == LEVEL_1_SCORE) {
                    level_1_completed = true;
                }

            }
            else{
                // level 1 completed

                backgroundImageLevel1.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
                font.drawString("CONGRATULATIONS",(WINDOW_WIDTH - font.getWidth("CONGRATULATIONS"))/2,
                        WINDOW_HEIGHT/2);
            }
        }

        else{
            //GAME OVER

            if (level_0_completed){
                backgroundImageLevel1.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            }
            else{
                backgroundImageLevel0.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            }
            font.drawString("GAME OVER",(WINDOW_WIDTH - font.getWidth("GAME OVER"))/2,
                    WINDOW_HEIGHT/2);
            font.drawString("FINAL SCORE: " + score,(WINDOW_WIDTH - font.getWidth("FINAL SCORE: "))/2,
                    WINDOW_HEIGHT/2 + FINAL_SCORE_SHIFT );

        }
    }

    /**
     * Implements Pipes collision/pass algorithm, deletes old pipes (go out out of bound),
     * increments score if bird passed the pipe, or subtract a heart if collision happened
     *
     * @param pipesLevel
     * @param birdie
     * @param input
     */
    public void collisionOrScoreDetector(ArrayList<Pipes> pipesLevel, Birdie birdie, Input input){

        ArrayList<Integer> outOfBound = new ArrayList<Integer>();
        int i;
        int lastCollisionInScore; // if bird hits the edge, this can contribute to score

        // collision detection
        for (i = 0; i < pipesLevel.size(); i++) {
            Pipes pipes = pipesLevel.get(i);
            lastCollisionInScore = 0;
            if (birdie.getRectangle().intersects(pipes.getLowerRectangle(false)) ||
                    birdie.getRectangle().intersects(pipes.getUpperRectangle(false))) {
                emptyLives += 1;
                pipes.destroy();
                lastCollisionInScore = 1;
            }

            // store index of pipes that are out of bound to delete later
            if (pipes.getPosition().x < - pipes.getWidth()) {
                outOfBound.add((Integer) i);
            } else {
                pipes.update(input);
                // add one to score if birds centre passed pipes right (getPosition returns centre of bird,left edge of pipe)
                if ((pipes.getPosition().x + pipes.getWidth() - pipes.getSpeed() < birdie.getPosition().x &&
                        birdie.getPosition().x < pipes.getPosition().x + pipes.getWidth())&& !pipes.isDestroyed()){//
                    score += 1 - lastCollisionInScore;
                    lastCollisionInScore = 0;
                }
            }
        }

        // delete pipes that are out of bound
        for (Integer integer : outOfBound) {
            pipesLevel.remove(integer);
        }

    }

    /**
     * returns the coordinates of the last pipe to be generated (used to generate weapon coordinates)
     * @param pipesLevel
     * @return lastPipeCoordinates
     */
    public double[] getCoordinatesOfLastPipe(ArrayList<Pipes> pipesLevel){
        // Return coordinates of last pipe to be considered when generating weapons
        int lastIndex = pipesLevel.size() - 1;
        Pipes lastPipe = pipesLevel.get(lastIndex);
        double[] coordinates = new double[4];
        boolean isPlastic = lastPipe.isItPlastic();

        coordinates[0] = lastPipe.getPosition().x;
        coordinates[1] = lastPipe.getPosition().x + lastPipe.getWidth();

        if (!isPlastic){
            coordinates[2] =  lastPipe.getPosition().y + lastPipe.FLAME_HEIGHT;
            coordinates[3] = lastPipe.getPosition().y + lastPipe.PIPES_GAP - lastPipe.FLAME_HEIGHT;
        }

        else {
            coordinates[2] =  lastPipe.getPosition().y;
            coordinates[3] = lastPipe.getPosition().y + lastPipe.PIPES_GAP;
        }

        return coordinates;
    }

    /**
     * Implements everything to do with weapons, from detecting collisions, to shooting,to deleting weapons that
     * went out of bound
     * @param weapons
     * @param birdie
     * @param pipes
     * @param input
     */
    public void weaponsLogic(ArrayList<Weapon> weapons, Birdie birdie, ArrayList<Pipes> pipes, Input input){

        // goes through every weapon on screen
        for (Weapon weapon: weapons){

            // Picks up weapon, while not carrying a weapon
            if (birdie.getRectangle().intersects(weapon.getRectangle()) && !birdie.getWeapon()
                    && !weapon.getIsReleased()){
                weapon.hold();
                birdie.setWeaponary(true);
            }

            else if (weapon.getIsHeld()){
                // holding a weapon
                if (input.wasPressed((Keys.S))){
                    weapon.release(birdie.getPosition().y);
                    birdie.setWeaponary(false);
                }
            }
            else if (weapon.getIsReleased()){
                // weapon released
                Pipes currentPipe; // detect pipe and weapons collision
                for (int i=0; i < pipes.size() ;i++){
                    currentPipe = pipes.get(i);

                    // destroy pipe if collision with pipes, and increment score
                    if (currentPipe.getLowerRectangle(true).intersects(weapon.getRectangle()) ||
                            currentPipe.getUpperRectangle(true).intersects(weapon.getRectangle()) && weapon.getIsReleased()){
                        weapon.colliding();
                        if (weapon.hasImpact(currentPipe.isItPlastic())){
                            currentPipe.destroy();
                            score += 1;
                        }
                    }
                }
            }
        }

        // delete out of bound weapons, and update weapons that are on the screen
        for(int i=0; i< weapons.size(); i++){
            if (weapons.get(i).isUsed()){
                weapons.remove(i);
            }
            else{
                weapons.get(i).update(input, Pipes.getSpeed(), birdie.getPosition().x, birdie.getPosition().y,
                        Pipes.getChangePercent());
            }
        }
    }

    /**
     * draws the hearts for a given level
     * @param deadLives //number of lost hearts
     * @param levelNumber
     */
    public void drawHearts(int deadLives, int levelNumber){
        int i;
        int maxLives;

        if (levelNumber == 0){
            maxLives = 3;
        }
        else{
            maxLives = 6;
        }
        for (i = 0; i < deadLives; i++) {
            noLife.drawFromTopLeft(LEFTMOST_LIFE_X + LIFE_SPACE * i, LEFTMOST_LIFE_Y);
        }
        for (i = deadLives; i < maxLives; i++) {
            fullLife.drawFromTopLeft(LEFTMOST_LIFE_X + LIFE_SPACE * i, LEFTMOST_LIFE_Y);
        }
    }

    /**
     * detects if player wants to change the speed, and updates speed accordingly
     * @param input
     */
    public void detectSpeed(Input input){
        if (input.wasPressed(Keys.L)) {
            Pipes.setSpeed(1);
        }
        if (input.wasPressed(Keys.K)) {
            Pipes.setSpeed(-1);
        }
    }
}