import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

import java.util.Random;
import java.lang.Math.*;

public class Pipes {
    private static final int optionsNumberForLevel0 = 3;
    private static double speed = 3.0;
    private static int speedMetre = 1;
    private static double changePercent = 1.0;
    private boolean displayLower = true;
    private boolean displayUpper = true;
    private boolean isPlastic = true;
    private int levelNumber;
    private Image upperPipeImage;
    private Image lowerPipeImage;
    private static final int WINDOW_WIDTH = 1024;  // window width
    private static final int WINDOW_HEIGHT = 768;  // window height
    private static final int PIPES_GAP = 168;
    private static final int HIGH_PIPE_Y = 100;
    private static final int PIPES_TYPES_DIF = 200;
    private static final int MAX_SPEED = 5;
    private static final int MIN_SPEED = 1;
    private static final int LOWEST_STEEL = 100;
    private static final int HIGHEST_STEEL = 500;
    private static double pipeImageHeight; //add final?
    private static double pipeImageWidth; // add final?
    private int upperPipeLowerY;
    private double x;
    Random rand = new Random();

    public Pipes(int levelNumber) {
        if (levelNumber == 1) {
            int HighGapY = ThreadLocalRandom.current().nextInt(LOWEST_STEEL, HIGHEST_STEEL + 1); // 100 to 500
            this.upperPipeLowerY = HighGapY;
            if (HighGapY % 2 == 0){
                // STEEL PIPE
                isPlastic = false;
                upperPipeImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-1\\steelPipe.png");
                lowerPipeImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-1\\steelPipeRotated.png");
            }
            else{
                // Plastic Pipe
                upperPipeImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level\\plasticPipe.png");
                lowerPipeImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level\\plasticPipeRotated.png");
            }
        } else {
            // levelNumber = 0
            int option = rand.nextInt(optionsNumberForLevel0); // 0, 1, or 2
            upperPipeImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level\\plasticPipe.png");
            lowerPipeImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level\\plasticPipeRotated.png");
            this.upperPipeLowerY = HIGH_PIPE_Y + option*PIPES_TYPES_DIF;

        }
        pipeImageHeight = upperPipeImage.getHeight();
        pipeImageWidth = upperPipeImage.getWidth();
        this.x = WINDOW_WIDTH;
    }


    //shift both pipes
    public void update(Input input){
        this.x = this.x - speed;
        if (this.displayLower){
            lowerPipeImage.drawFromTopLeft(this.x, upperPipeLowerY + PIPES_GAP);
        }
        if (this.displayUpper){
            upperPipeImage.drawFromTopLeft(this.x,upperPipeLowerY - pipeImageHeight);
        }
    }


    public static void setSpeed(int speedChange){
        if (speedChange<0){
            if (speedMetre != MIN_SPEED){
                changePercent = changePercent * 0.5;
                speedMetre -= 1;
                speed = speed*0.5; //decrease by 50%
            }
        }
        else{
            // increasing speed
            if (speedMetre != MAX_SPEED){
                speedMetre += 1;
                changePercent = changePercent * 1.5;
                speed = speed *1.5; //increase by 50%
            }
        }
    }

    public static double getSpeed(){
        return speed;
    }

    public static double getChangePercent(){
        return changePercent;
    }

    public static void resetSpeed(){
        speed = 3;
        speedMetre = 1;
        changePercent = 1.0;
    }
    // returns width of pipe image
    public double getWidth(){
        return pipeImageWidth;
    }
    // returns (x, height of lower part of upper pipe), where x is the left coordinate of the pipe
    public Point getPosition(){
        return new Point(this.x, this.upperPipeLowerY);
    }

    public Rectangle getUpperRectangle(){
        if (!displayUpper){
            return new Rectangle(-1, -1, 0,0); // impossible to intersect with bird
        }
        return new Rectangle(this.x,upperPipeLowerY - pipeImageHeight, pipeImageWidth, pipeImageHeight);
    }

    public Rectangle getLowerRectangle(){
        if (!displayLower){
            return new Rectangle(-1, -1, 0,0); // impossible to intersect with bird
        }
        return new Rectangle(this.x,upperPipeLowerY + PIPES_GAP, pipeImageWidth, pipeImageHeight);
    }

    public void hideLower(){
        displayLower = false;
    }

    public void hideUpper() {
        displayUpper = false;
    }
}

