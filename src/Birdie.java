import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Birdie{
    private static final double ACCELERATION = 0.4;
    private static final double INITIAL_X = 200;
    private static final double INITIAL_Y = 350;
    private static final double FLYING_STEP = 6.0;
    private static final int MAX_ACCELERATION = 10;
    private static final int FLAP_FRAMES = 10;
    private static final int JUMPING_FRAMES = 6;
    private static  double x = INITIAL_X;  // Does not change
    private double y = INITIAL_Y;
    private double speed = 0;
    private int frameCounter = 0;
    private boolean hasWeapon = false;
    int levelNumber;
    private Image birdWingUp;
    private Image birdWingDown;
    private Rectangle rectangle;

    public Birdie(int levelNumber) {
        this.levelNumber = levelNumber;

        if (levelNumber == 1) {
            birdWingDown = new Image("project-2-skeleton\\res\\level-1\\birdWingDown.png");
            birdWingUp = new Image("project-2-skeleton\\res\\level-1\\birdWingUp.png");
        } else {
            // levelNumber is 0
            birdWingDown = new Image("project-2-skeleton\\res\\level-0\\birdWingDown.png");
            birdWingUp = new Image("project-2-skeleton\\res\\level-0\\birdWingUp.png");
        }
    }
    public void update(Input input, double timescale){
        // Drawing wing flap

        if (frameCounter % FLAP_FRAMES == 0){
            birdWingUp.draw(this.x, this.y);
            frameCounter = 0;
        }
        else {
            birdWingDown.draw(this.x, this.y);
        }

        // Calculating Speed
        if (speed < MAX_ACCELERATION * timescale){
            speed = speed + (ACCELERATION * timescale);
        }
        if (input.wasPressed(Keys.SPACE)){
            speed = -JUMPING_FRAMES * timescale;
        }
        this.y += speed;
        frameCounter += 1;
    }

    public Point getPosition(){
        return new Point(this.x, this.y);
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    // returns top right point
    public Point getBounds(){
        return new Point(this.x + birdWingUp.getWidth()/2, this.y + birdWingUp.getHeight()/2);
    }

    // returns width of birdImage
    public double getWidth(){
        return (double)birdWingUp.getWidth();
    }

    // returns height of birdImage
    public double getHeight(){
        return (double)birdWingUp.getHeight();
    }

    // returns rectangle of Birdie
    public Rectangle getRectangle() {
        return new Rectangle(this.getBounds().x - getWidth(),
                this.getBounds().y - this.getHeight(), this.getWidth(), this.getHeight());
    }

    public boolean getWeapon() {
        return hasWeapon;
    }

    public void setWeaponary(boolean value){
        hasWeapon = value;
    }
}
