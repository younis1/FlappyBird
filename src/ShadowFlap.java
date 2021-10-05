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

    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
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



    @Override
    public void update(Input input) {
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
                if (input.wasPressed(Keys.L)) {
                    Pipes.setSpeed(1);
                }
                if (input.wasPressed(Keys.K)) {
                    Pipes.setSpeed(-1);
                }


                // Drawing lives

                int i;
                for (i = 0; i < emptyLives; i++) {
                    noLife.drawFromTopLeft(LEFTMOST_LIFE_X + LIFE_SPACE * i, LEFTMOST_LIFE_Y);
                }
                for (i = emptyLives; i < LEVEL_0_LIVES; i++) {
                    fullLife.drawFromTopLeft(LEFTMOST_LIFE_X + LIFE_SPACE * i, LEFTMOST_LIFE_Y);
                }

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
                // and adding score
                collisionOrScoreDetector( pipesLevel_0,  birdieLevel_0, input);

                if (emptyLives == LEVEL_0_LIVES) {
                    gameEnded = true;
                }

                if (score == LEVEL_0_SCORE) {
                    level_0_completed = true;
                }
            }
            // LEVEL 0 IS COMPLETED

            //Level up screen
            else if (waitingFrames < WAITING_FRAMES_AFTER_LEVEL_O) {
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
                // Level 1
                backgroundImageLevel1.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
                font.drawString("PRESS SPACE TO START", (WINDOW_WIDTH - font.getWidth("PRESS SPACE TO START")) / 2,
                        WINDOW_HEIGHT / 2);
                font.drawString("Press S to Shoot",(WINDOW_WIDTH - font.getWidth("PRESS S TO SHOOT")) / 2,
                        WINDOW_HEIGHT / 2 + S_TO_SHOOT_SHIFT);
                if (input.wasPressed(Keys.SPACE)) {
                    this.gameStarted = true;
                }
            }
            else if (gameStarted && !level_1_completed) {
                // Lvl 1
                // Drawing lives
                backgroundImageLevel1.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
                frameCounter += Pipes.getChangePercent();  // to generate pipes every "100/frames*timescale" frames
                if (input.wasPressed(Keys.L)) {
                    Pipes.setSpeed(1);
                }
                if (input.wasPressed(Keys.K)) {
                    Pipes.setSpeed(-1);
                }


                int i;
                for (i = 0; i < emptyLives; i++) {
                    noLife.drawFromTopLeft(LEFTMOST_LIFE_X + LIFE_SPACE * i, LEFTMOST_LIFE_Y);
                }
                for (i = emptyLives; i < LEVEL_1_LIVES; i++) {
                    fullLife.drawFromTopLeft(LEFTMOST_LIFE_X + LIFE_SPACE * i, LEFTMOST_LIFE_Y);
                }

                // Drawing Bird
                birdieLevel_1.update(input, Pipes.getChangePercent());

                //Drawing Score
                font.drawString("SCORE: " + score, SCORE_INDENT, SCORE_INDENT);

                // Adding new pipes every 100 frames
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
                if (input.wasPressed(Keys.ESCAPE)){
                    System.exit(0);
                }
                backgroundImageLevel1.draw(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
                font.drawString("CONGRATULATIONS",(WINDOW_WIDTH - font.getWidth("CONGRATULATIONS"))/2,
                        WINDOW_HEIGHT/2);
            }
        }

        else{
            //GAMEOVER

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
            if (input.wasPressed(Keys.ESCAPE)){
                System.exit(0);
            }
        }
    }




    public void collisionOrScoreDetector(ArrayList<Pipes> pipesLevel, Birdie birdie, Input input){

        ArrayList<Integer> outOfBound = new ArrayList<Integer>();
        int i;
        int lastCollisionInScore; // if bird hits the edge, this can contribute to score
        for (i = 0; i < pipesLevel.size(); i++) {
            Pipes pipes = pipesLevel.get(i);
            lastCollisionInScore = 0;
            if (birdie.getRectangle().intersects(pipes.getLowerRectangle(false)) ||
                    birdie.getRectangle().intersects(pipes.getUpperRectangle(false))) {
                emptyLives += 1;
                pipes.destroy();
                lastCollisionInScore = 1;
            }

            // Delete pipes that are out of bound
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

        for (Integer integer : outOfBound) {
            pipesLevel.remove(integer);
        }

    }

    public double[] getCoordinatesOfLastPipe(ArrayList<Pipes> pipesLevel){
        // returns coordinates of last pipe to be considered when generating weapons
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

    public void weaponsLogic(ArrayList<Weapon> weapons, Birdie birdie, ArrayList<Pipes> pipes, Input input){
        for (Weapon weapon: weapons){
            if (birdie.getRectangle().intersects(weapon.getRectangle()) && !birdie.getWeapon()
                    && !weapon.getIsReleased()){
                weapon.hold();
                birdie.setWeaponary(true);
            }
            else if (weapon.getIsHeld()){
                if (input.wasPressed((Keys.S))){
                    weapon.release(birdie.getPosition().y);
                    birdie.setWeaponary(false);
                }
            }
            else if (weapon.getIsReleased()){
                boolean pipeAfterBird = false;
                Pipes firstPipe = pipes.get(0); // will get overwritten
                for (int i=0; i < pipes.size() && !pipeAfterBird;i++){
                    if (pipes.get(i).getPosition().x > birdie.getPosition().x){
                        pipeAfterBird = true;
                        firstPipe = pipes.get(i);
                    }
                }
                if (firstPipe.getLowerRectangle(true).intersects(weapon.getRectangle()) ||
                        firstPipe.getUpperRectangle(true).intersects(weapon.getRectangle()) && weapon.getIsReleased()){
                    weapon.colliding();
                    if (weapon.hasImpact(firstPipe.isItPlastic())){
                        firstPipe.destroy();
                        score += 1;
                    }
                }
            }
        }

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

}