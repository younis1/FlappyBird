import java.util.concurrent.ThreadLocalRandom;
import bagel.*;

public class Weapon {
    private static final int UPPERBOUND = 600;
    private static final int LOWERBOUND = 100;
    private static final int INITIAL_SPEED = 5;
    private static final Image bombImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-1\\bomb.png");
    private static final Image rockImage = new Image("C:\\Users\\youni\\Desktop\\UniMelb Sem2 2021\\OOP\\Assignment2FlappyBam\\project-2-skeleton\\res\\level-1\\rock.png");
    private static final double BOMB_HEIGHT = bombImage.getHeight();
    private static final double BOMB_WIDTH = bombImage.getWidth();
    private static final double ROCK_HEIGHT = rockImage.getHeight();
    private static final double ROCK_WIDTH = rockImage.getWidth();

    protected int shootingRange;
    private boolean isBomb;
    private boolean isCollected;
    private double x;
    private double y;
    private Image image;


    public void generateWeapon(double first_x, double ending_x){
         this.y = ThreadLocalRandom.current().nextInt(LOWERBOUND, UPPERBOUND + 1); // 100 to 600
        if (y % 2 == 0){
            isBomb = true;

        }
        else{
            isBomb = false;
        }

    }

    public void update(Input input){}
}
