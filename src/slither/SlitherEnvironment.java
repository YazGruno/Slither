/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slither;

import audio.AudioPlayer;
import environment.Environment;
import environment.GraphicsPalette;
import environment.LocationValidatorIntf;
import grid.Grid;
import images.ResourceTools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author YazGruno
 */
class SlitherEnvironment extends Environment implements GridDrawData, LocationValidatorIntf {

    private Grid grid;
    private Snake snake;

    private ArrayList<GridObject> gridObjects;
    
    private GameState gameState = GameState.PLAYING;
    

    public SlitherEnvironment() {
    }

    public Point getRandomPoint() {
        return new Point((int) (Math.random() * grid.getColumns()), (int) (Math.random() * grid.getRows()));
    }

    public Point getValidatedPoint(){
        Point location = getRandomPoint();
        return snake.getBody().contains(location) ? getRandomPoint() : location;
    }

    @Override
    public void initializeEnvironment() {
//        this.setBackground(ResourceTools.loadImageFromResource("resources/dark_bck.png").getScaledInstance(1000, 800, Image.SCALE_FAST));

        this.setBackground(Color.BLACK);

        grid = new Grid(13, 13, 35, 35, new Point(50, 50), Color.DARK_GRAY);
        grid.setColor(Color.GREEN);

        snake = new Snake();
        snake.setDirection(Direction.RIGHT);
        snake.setDrawData(this);
        snake.setLocationValidator(this);

        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(5, 1));
        snake.setGrowthCounter(10);
//        body.add(new Point(5, 2));
//        body.add(new Point(5, 3));
//        body.add(new Point(5, 4));
//        body.add(new Point(5, 5));
//        body.add(new Point(5, 6));
//        body.add(new Point(5, 7));
//        body.add(new Point(5, 8));
//        body.add(new Point(5, 9));
//        body.add(new Point(5, 10));
//        body.add(new Point(5, 11));

        snake.setBody(body);

        gridObjects = new ArrayList<>();
        gridObjects.add(new GridObject(GridObjectType.DIAMOND, getValidatedPoint()));

    }

    public final int SLOW_SPEED = 5;
    public final int MED_SPEED = 4;
    public final int HIGH_SPEED = 2;

    private int delayCounter = 0;
    private int delayLimit = MED_SPEED;

    @Override
    public void timerTaskHandler() {
        // if delayCounter >= delayLimit then
        //    - move the damn snake
        //    - and reset the counter to 0
        // else 
        //    - add 1 to the delayCounter

        if (delayCounter >= delayLimit) {
            if (snake != null) {
                snake.move();
            }
            delayCounter = 0;
        } else {
            delayCounter++;
        }
    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            grid.setShowCellCoordinates(!grid.getShowCellCoordinates());
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (snake != null) {
                snake.move();
            }
        } else if ((e.getKeyCode() == KeyEvent.VK_LEFT) && (snake.getNoMove() != Direction.LEFT)) {
            snake.setDirection(Direction.LEFT);
        } else if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && (snake.getNoMove() != Direction.RIGHT)) {
            snake.setDirection(Direction.RIGHT);
        } else if ((e.getKeyCode() == KeyEvent.VK_UP) && (snake.getNoMove() != Direction.UP)) {
            snake.setDirection(Direction.UP);
        } else if ((e.getKeyCode() == KeyEvent.VK_DOWN) && (snake.getNoMove() != Direction.DOWN)) {
            snake.setDirection(Direction.DOWN);
        }

        if (e.getKeyCode() == KeyEvent.VK_1) {
            delayLimit = SLOW_SPEED;
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            delayLimit = MED_SPEED;
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            delayLimit = HIGH_SPEED;
        }
        if (e.getKeyCode() == KeyEvent.VK_4) {
            if (delayLimit == SLOW_SPEED) {
                delayLimit = MED_SPEED;
            } else if (delayLimit == MED_SPEED) {
                delayLimit = HIGH_SPEED;
            } else {
                delayLimit = SLOW_SPEED;
            }
            //Ben wrote this, but I get it anyways.
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            snake.setPaused(!snake.isPaused());
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            snake.grow(2);
        }

        if (e.getKeyCode() == KeyEvent.VK_M) {
            AudioPlayer.play("/resources/Robot_blip.wav");
        }

    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {
    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {
    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        switch (gameState){
            case START:
                
                break;
                
            case PLAYING:
                        if (grid != null) {
            grid.paintComponent(graphics);
        }

        if ((snake != null) && (snake.isAlive())) {
            snake.draw(graphics);
        }

        if (gridObjects != null) {
            for (GridObject gridObject : gridObjects) {
                if (gridObject.getType() == GridObjectType.DIAMOND) {
                    GraphicsPalette.drawDiamond(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.YELLOW);

                }
            }
        }
                
                break;
                
            case OVER:
                
                break;
        
                
        }
        

    }

//<editor-fold defaultstate="collapsed" desc="GridDrawData Interface">
    @Override
    public int getCellHeight() {
        return grid.getCellHeight();
    }

    @Override
    public int getCellWidth() {
        return grid.getCellWidth();
    }

    @Override
    public Point getCellSystemCoordinate(Point cellCoordinate) {
        return grid.getCellSystemCoordinate(cellCoordinate);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="LocationValidatorIntf">
    @Override
    public Point validateLocation(Point point) {
        if (snake.selfHit()) {
//            System.out.println("Self hit");
            snake.setAlive(false);
            
            
        }

        if ((point.x < 0) || (point.x > grid.getColumns() - 1) || (point.y < 0) || (point.y > grid.getRows() - 1)) {
            snake.setAlive(false);
        }
//        if (point.x < 0){
//            point.x = grid.getColumns() -1;
//        }
//        if (point.x > grid.getColumns() -1) {
//            point.x = 0;
//        }
//        if (point.y < 0) {
//            point.y = grid.getRows() -1;
//        }
//        if (point.y > grid.getRows() -1) {
//            point.y = 0;
//        }

//        check if the snake hits a GridObject, then take appropriate action
//        - Apple - grow snake by 3
//        - Poison - make sound, kill snake
//        
//        look at all locations stored in gridObject ArrayList
//        for each, compare it to head location stored in the "point" parameter
        for (GridObject object : gridObjects) {
            if (object.getLocation().equals(point)) {
                System.out.println("HIT " + object.getType());

                if (object.getType() == GridObjectType.DIAMOND) {
//                move object on screen
//                snake grows
//                make noise
                    object.setLocation(this.getValidatedPoint());
                    snake.grow(3);
                    AudioPlayer.play("/resources/Beep8.wav");
                }
            }
        }

        return point;
    }
//</editor-fold>

}
