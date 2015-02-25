/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slither;

import audio.AudioPlayer;
import environment.LocationValidatorIntf;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author YazGruno
 */
public class Snake {

    private ArrayList<Point> body = new ArrayList<>();
    private Direction direction = Direction.RIGHT;
    private Direction noMove = null;
    private GridDrawData drawData;
    private LocationValidatorIntf locationValidator;
    private boolean paused;
    private int growthCounter;
    private boolean alive = true;

//    grow
//    eat
//    move
//    die
//    draw
    private Color SNAKE_BODY_COLOR = new Color(51, 102, 0);
    private Color SNAKE_WHITE = Color.LIGHT_GRAY;
    private int MAX_OPACITY = 255;
    private int MIN_OPACITY = 100;
    public boolean selfHit;
    
    public void draw(Graphics graphics) {
        int opacity = getMAX_OPACITY();
        double opacityStepSize = (getMAX_OPACITY() - getMIN_OPACITY()) / getBody().size();

        for (Point bodySegmentLocation : getStableBody()) {
//            System.out.println("Location = " + bodySegmentLocation);
//            System.out.println("System Loc = " + drawData.getCellSystemCoordinate(bodySegmentLocation));

            Point topLeft = getDrawData().getCellSystemCoordinate(bodySegmentLocation);

//          graphics.setColor(new Color(SNAKE_BODY_COLOR.getRed(),SNAKE_BODY_COLOR.getGreen(), SNAKE_BODY_COLOR.getBlue(), (int) opacity));
            graphics.setColor(new Color(getSNAKE_WHITE().getRed(), getSNAKE_WHITE().getGreen(), getSNAKE_WHITE().getBlue(), (int) opacity));
            graphics.fillRect(topLeft.x, topLeft.y, getDrawData().getCellWidth(), getDrawData().getCellHeight());

            opacity -= opacityStepSize;
        }

    }

    private ArrayList<Point> getStableBody() {
        ArrayList<Point> aBody;
        aBody = getBody();
        return aBody;
    }

    /**
     * @return the body
     */
    public ArrayList<Point> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return the drawData
     */
    public GridDrawData getDrawData() {
        return drawData;
    }

    /**
     * @param drawData the drawData to set
     */
    public void setDrawData(GridDrawData drawData) {
        this.drawData = drawData;
    }

    public void move() {
        if (!paused) {

            Point newHead = (Point) getHead().clone();

            if (getDirection() == Direction.RIGHT) {
                newHead.x++;
                setNoMove(Direction.LEFT);
                
            } else if (getDirection() == Direction.LEFT) {
                newHead.x--;
                setNoMove(Direction.RIGHT);
                
            } else if (getDirection() == Direction.UP) {
                newHead.y--;
                setNoMove(Direction.DOWN);
                
            } else if (getDirection() == Direction.DOWN) {
                newHead.y++;
                setNoMove(Direction.UP);
            }

            if (locationValidator != null) {
                body.add(HEAD_POSITION, locationValidator.validateLocation(newHead));
            }
            
            if (growthCounter <= 0) {
                body.remove(body.size() -1);
            } else {
                growthCounter--;
            }

        }
    }

    private int HEAD_POSITION = 0;

    public Point getHead() {
        return getBody().get(getHEAD_POSITION());
    }

    /**
     * @return the locationValidator
     */
    public LocationValidatorIntf getLocationValidator() {
        return locationValidator;
    }

    /**
     * @param locationValidator the locationValidator to set
     */
    public void setLocationValidator(LocationValidatorIntf locationValidator) {
        this.locationValidator = locationValidator;
    }

    /**
     * @return the SNAKE_BODY_COLOR
     */
    public Color getSNAKE_BODY_COLOR() {
        return SNAKE_BODY_COLOR;
    }

    /**
     * @param SNAKE_BODY_COLOR the SNAKE_BODY_COLOR to set
     */
    public void setSNAKE_BODY_COLOR(Color SNAKE_BODY_COLOR) {
        this.SNAKE_BODY_COLOR = SNAKE_BODY_COLOR;
    }

    /**
     * @return the SNAKE_WHITE
     */
    public Color getSNAKE_WHITE() {
        return SNAKE_WHITE;
    }

    /**
     * @param SNAKE_WHITE the SNAKE_WHITE to set
     */
    public void setSNAKE_WHITE(Color SNAKE_WHITE) {
        this.SNAKE_WHITE = SNAKE_WHITE;
    }

    /**
     * @return the MAX_OPACITY
     */
    public int getMAX_OPACITY() {
        return MAX_OPACITY;
    }

    /**
     * @param MAX_OPACITY the MAX_OPACITY to set
     */
    public void setMAX_OPACITY(int MAX_OPACITY) {
        this.MAX_OPACITY = MAX_OPACITY;
    }

    /**
     * @return the MIN_OPACITY
     */
    public int getMIN_OPACITY() {
        return MIN_OPACITY;
    }

    /**
     * @param MIN_OPACITY the MIN_OPACITY to set
     */
    public void setMIN_OPACITY(int MIN_OPACITY) {
        this.MIN_OPACITY = MIN_OPACITY;
    }

    /**
     * @return the HEAD_POSITION
     */
    public int getHEAD_POSITION() {
        return HEAD_POSITION;
    }

    /**
     * @param HEAD_POSITION the HEAD_POSITION to set
     */
    public void setHEAD_POSITION(int HEAD_POSITION) {
        this.HEAD_POSITION = HEAD_POSITION;
    }

    /**
     * @return the pause
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @param pause the pause to set
     */
    public void setPaused(boolean pause) {
        this.paused = pause;
    }
    
    public void togglePaused(){
        paused = !paused;
    }

    /**
     * @return the growthCounter
     */
    public int getGrowthCounter() {
        return growthCounter;
    }

    /**
     * @param growthCounter the growthCounter to set
     */
    public void setGrowthCounter(int growthCounter) {
        this.growthCounter = growthCounter;
    }

    public void grow(int length) {
        growthCounter += length;
    }

    /**
     * @return the alive
     */
    public boolean isDead() {
        return !alive;
    }

    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @param alive the alive to set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public boolean selfHit(){
        for (int i = 1; i < body.size(); i++) {
            if (getHead().equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the noMove
     */
    public Direction getNoMove() {
        return noMove;
    }

    /**
     * @param noMove the noMove to set
     */
    public void setNoMove(Direction noMove) {
        this.noMove = noMove;
    }
    
}
