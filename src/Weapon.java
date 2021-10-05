import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import bagel.util.Rectangle;
import bagel.*;
import bagel.Image;

public class Weapon {
    private static final int UPPER_BOUND = 600;
    private static final int LOWER_BOUND = 100;
    private static final int INITIAL_SPEED = 5; // weapon speed
    private static final int HOLDING_SPACE = 30;
    private static final int MINIMUM_V_DISTANCE_TO_PIPE = 60;
    private static final int MINIMUM_H_DISTANCE_TO_PIPE = 100;
    private static final int DISTANCE_BETWEEN_PIPES = 300;

    private static final Image bombImage = new Image("ynaser-project-2\\res\\level-1\\bomb.png");
    private static final double BOMB_HEIGHT = bombImage.getHeight();
    private static final double BOMB_WIDTH = bombImage.getWidth();
    private static final double BOMB_RANGE = 50;

    private static final Image rockImage = new Image("ynaser-project-2\\res\\level-1\\rock.png");
    private static final double ROCK_HEIGHT = rockImage.getHeight();
    private static final double ROCK_WIDTH = rockImage.getWidth();
    private static final double ROCK_RANGE = 25;

    private boolean isHeld = false;
    private boolean isReleased = false;
    private boolean isBomb;  //type
    private boolean collided = false;
    private double x;
    private double y;
    private double releasedAtY = 0.0;
    private Image image;
    private double shootingRange;
    private boolean used = false;
    private int frameCounter = 0;

    public Weapon(double leftX, double rightX, double upperY, double lowerY) {
        this.y = ThreadLocalRandom.current().nextInt(LOWER_BOUND, UPPER_BOUND + 1); // 100 to 600

        if (!(y < upperY + MINIMUM_V_DISTANCE_TO_PIPE || y > lowerY - MINIMUM_V_DISTANCE_TO_PIPE)) {
            // Y coordinate same as pipe, choose x away from pipe
            this.x = ThreadLocalRandom.current().nextInt((int) leftX, (int) rightX + DISTANCE_BETWEEN_PIPES - MINIMUM_H_DISTANCE_TO_PIPE);
        } else {
            this.x = ThreadLocalRandom.current().nextInt((int) rightX + MINIMUM_H_DISTANCE_TO_PIPE, (int) rightX + DISTANCE_BETWEEN_PIPES - MINIMUM_H_DISTANCE_TO_PIPE);
        }

        if (y % 2 == 0) {
            // Bomb Type
            image = bombImage;
            isBomb = true;
            shootingRange = BOMB_RANGE;
        } else {
            // Rock type (50% chance)
            image = rockImage;
            isBomb = false;
            shootingRange = ROCK_RANGE;
        }

    }

    public void update(Input input, double speed, double birdieX, double birdieY, double percentChange) {
        if (!used) {
            if (!isHeld && !isReleased) {
                x -= speed;

            }
            else if (isHeld && !isReleased) {
                //sticking on top of bird
                this.x = birdieX;
                this.y = birdieY - HOLDING_SPACE;
            }

            else if (isReleased) {
                isHeld = false;
                frameCounter += 1;
                if (frameCounter < shootingRange && !collided) {
                    this.y = releasedAtY;
                    this.x += INITIAL_SPEED * percentChange;
                } else {
                    used = true;
                }

            }

            if (x < birdieX -  MINIMUM_H_DISTANCE_TO_PIPE){
                // out of bounds
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

    public void setUsed(){
        // in case of collisions with pipes
        used = true;
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

    public boolean isCollided(){ return collided;}

    public void colliding(){collided = true; }

    public Point getPosition(){
        return new Point((int)x, (int) y);
    }

    public double getWidth(){
        return image.getWidth();
    }

    public double getHeight(){
        return image.getHeight();
    }

    public boolean hasImpact(boolean isPlastic){
        // all collisions count except rocks on steel pipes
        if (!isPlastic && !isBomb){
            return false;
        }
        return true;
    }
}
