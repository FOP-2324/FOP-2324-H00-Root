package h00;

import fopbot.Robot;
import fopbot.World;

import static fopbot.Direction.RIGHT;
import static fopbot.Direction.LEFT;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {
        // variable representing width/size of world
        final int worldSize = 5;

        // setting world size symmetrical, meaning height = width
        World.setSize(worldSize, worldSize);

        // speed of how fast the world gets refreshed (e.g. how fast the robot(s) act) 
        // the lower the number, the faster the refresh
        World.setDelay(50);

        // make it possible to see the world window
        World.setVisible(true);

        // our program/assignment shall be tested/run !
        runExercise();
    }

    public static void runExercise(){
        Robot kaspar = new Robot(0,0,RIGHT,20);
        Robot alfred = new Robot(World.getWidth()-1,World.getHeight()-1,LEFT,0);
        
        // Arnold's first act of craziness
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                kaspar.putCoin();
                kaspar.move();
            }
            kaspar.turnLeft();
        }
        kaspar.putCoin();
        kaspar.move();

        // Bernd's try of heroism
        alfred.turnLeft();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                alfred.pickCoin();
                alfred.move();
            }
            for (int j = 0; j < 3; j++) {
                alfred.turnLeft();
            }
        }
        alfred.pickCoin();
        alfred.move();

        // Arnold's craziness continues
        while(kaspar.hasAnyCoins()){
            kaspar.putCoin();
            alfred.turnLeft();
        }


    }
}
