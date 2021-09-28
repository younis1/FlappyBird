public class GameDetails {
    private static final int MAX_TIMESCALE = 5;
    private static final int MIN_TIMESCALE = 1;
    private static final double MIN_CHANGE_PERCENT = 0.5; // after experimenting, anything slower is unplayable
    private static final double TIMESCALE_INCREASE = 0.5;
    private static final double TIMESCALE_DECREASE = 0.5;
    private static final double INITIAL_SPEED = 3.0;
    private int score;
    private int levelNumber;
    private int timescale = MIN_TIMESCALE;
    private int remaining_lives;
    private int numberOfLives;
    private double changePercent = 1.0;
    private double speed = INITIAL_SPEED; // initial speed
    private int maximumScore;


    public GameDetails(int score, int levelNumber, int remaining_lives, int maximum_score){
        this.score = score;
        this.levelNumber = levelNumber;
        this.remaining_lives = remaining_lives;
        this.maximumScore = maximum_score;
    }

    public boolean isAlive(){
        if (remaining_lives == 0){
            return false;
        }
        return true;
    }

    public void setSpeed(int speedChange){
        if (speedChange<0){
            if (timescale != MIN_TIMESCALE && changePercent > MIN_CHANGE_PERCENT){
                changePercent = changePercent * (1- TIMESCALE_DECREASE);
                timescale -= 1;
                speed = speed * TIMESCALE_DECREASE; //decrease by 50%
            }
        }
        else{
            // increasing speed
            if (timescale != MAX_TIMESCALE){
                timescale += 1;
                changePercent = changePercent * (1+TIMESCALE_INCREASE);
                speed = speed* (1+TIMESCALE_INCREASE); //increase by 50%
            }
        }
    }
    public double getSpeed(){
        return speed;
    }

    public double getChangePercent(){
        return changePercent;
    }

    public void resetSpeed(){
        speed = INITIAL_SPEED;
        timescale = MIN_TIMESCALE;
        changePercent = 1.0;
    }

    public void addScore(){
        score += 1;
    }

    public int getRemaining_lives() {
        return remaining_lives;
    }

    public void loseHeart(){
        remaining_lives -= 1;
    }
    public int getNumberOfLives() {
        return numberOfLives;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public boolean isLevelCompleted(){
        if (score >= maximumScore){
            return true;
        }
        return false;
    }

}
