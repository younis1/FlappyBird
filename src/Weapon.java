import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import bagel.util.Rectangle;
import bagel.*;
import bagel.Image;

public class Weapon {
    private final int WINDOW_WIDTH = 1024;
    private static final int UPPERBOUND = 600;
    private static final int LOWERBOUND = 100;
    private static final int INITIAL_SPEED = 5;
    private static final int HOLDING_SPACE = 30;
    private static final int MINIMUM_V_DISTANCE_TO_PIPE = 60;
    private static final int MINIMUM_H_DISTANCE_TO_PIPE = 100;
    private static final int DISTANCE_BETWEEN_PIPES = 300;
    private static final Image bombImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-1\\bomb.png");
    private static final Image rockImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-1\\rock.png");
    private static final double BOMB_HEIGHT = bombImage.getHeight();
    private static final double BOMB_WIDTH = bombImage.getWidth();
    private static final double ROCK_HEIGHT = rockImage.getHeight();
    private static final double ROCK_WIDTH = rockImage.getWidth();

    private boolean isHeld = false;
    private boolean isReleased = false;
    protected int shootingRange =100;
    private boolean isBomb;
    private boolean isCollected;
    private double x;
    private double y;
    private double range = 0;
    private double releasedAtY = 0.0;
    private Image image;
    private boolean used = false;

    public Weapon(double leftX, double rightX, double upperY, double lowerY) {
        this.y = ThreadLocalRandom.current().nextInt(LOWERBOUND, UPPERBOUND + 1); // 100 to 600

        if (!(y < upperY + MINIMUM_V_DISTANCE_TO_PIPE || y > lowerY - MINIMUM_V_DISTANCE_TO_PIPE)) {
            // Y coordinate same as pipe, choose x away from pipe
            this.x = ThreadLocalRandom.current().nextInt((int) leftX, (int) rightX + DISTANCE_BETWEEN_PIPES - MINIMUM_H_DISTANCE_TO_PIPE);
        } else {
            this.x = ThreadLocalRandom.current().nextInt((int) rightX + MINIMUM_H_DISTANCE_TO_PIPE, (int) rightX + DISTANCE_BETWEEN_PIPES - MINIMUM_H_DISTANCE_TO_PIPE);
        }
        if (y % 2 == 0) {
            image = bombImage;
            isBomb = true;
        } else {
            image = rockImage;
            isBomb = false;
        }

    }

    public void update(Input input, double speed, double birdieX, double birdieY, double percentChange) {
        if (!used) {
            if (!isHeld && !isReleased) {
                x -= speed;


            }
            else if (isHeld && !isReleased) {
                this.x = birdieX;
                this.y = birdieY - HOLDING_SPACE;
            }

            else if (isReleased) {
                isHeld = false;
                if (range < 100) {
                    this.y = releasedAtY;
                    this.x += INITIAL_SPEED * percentChange;
                    range += INITIAL_SPEED * percentChange;
                } else {
                    used = true;
                }

            }

            if (x < birdieX -  MINIMUM_H_DISTANCE_TO_PIPE){
                used = true;
            }
            image.draw(x, y);
        }

    }

    public void hold() {
        isHeld = true;
    }

    public void release(double releasedAtY) {
        if (isHeld) {
            isReleased = true;
            isHeld = false;
            this.releasedAtY = releasedAtY;
        }
    }


    public boolean isUsed(){
        return used;
    }

    public bagel.util.Rectangle getRectangle(){
        return new Rectangle((int)x - image.getWidth()/2 , (int)y - image.getHeight()/2, (int)image.getWidth(), (int)image.getHeight());
    }

    public boolean getIsHeld() {
        return isHeld;
    }

    public boolean getIsReleased() {
        return isReleased;
    }

    public Point getPosition(){
        return new Point((int)x, (int) y);
    }

    public double getWidth(){
        return image.getWidth();
    }

    public double getHeight(){
        return image.getHeight();
    }
}
