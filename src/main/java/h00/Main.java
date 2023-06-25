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
        World.setDelay(300);

        // make it possible to see the world window
        World.setVisible(true);

        // our program/assignment shall be tested/run !
        runExercise();
    }

    public static void runExercise() {
        Robot kaspar = new Robot(0, 0, LEFT, 20);
        Robot alfred = new Robot(World.getWidth() - 1, World.getHeight() - 1, RIGHT, 0);

        // -- Kaspars's first act of craziness -- 
        // TODO H4
        // turning from west to east
        kaspar.turnLeft();
        kaspar.turnLeft();
        // moving towards bottom right corner, putting coin and moving
        for (int j = 0; j < 4; j++) {
            kaspar.putCoin();
            kaspar.move();
        }
        kaspar.turnLeft();
        // moving towards top right corner, putting coin and moving
        // /!\ the coin for the bottom right corner gets placed here in one iteration
        for (int j = 0; j < 4; j++) {
            kaspar.putCoin();
            kaspar.move();
        }
        kaspar.turnLeft();
        // coin placement for top right corner
        kaspar.putCoin();
        kaspar.move();

        // -- Alfred's try of heroism -- 
        // TODO H5
        // turning from east to south, 3x turn left = 1 x turn right
        for (int i = 0; i < 3; i++) {
            alfred.turnLeft();
        }
        // moving towards bottom right corner, removing coin and moving
        for (int i = 0; i < 4; i++) {
            alfred.pickCoin();
            alfred.move();
        }
        // turning from south to west
        for (int i = 0; i < 3; i++) {
            alfred.turnLeft();
        }
        // moving towards bottom left corner, removing coin and moving
        // /!\ the coin for the bottom right corner gets removed here in one iteration
        for (int i = 0; i < 4; i++) {
            alfred.pickCoin();
            alfred.move();
        }
        // turning from west to north
        for (int i = 0; i < 3; i++) {
            alfred.turnLeft();
        }
        // // coin removal for bottom left corner
        alfred.pickCoin();
        alfred.move();

        // -- Kaspar's chaos continues -- 
        // TODO H6
        while (kaspar.hasAnyCoins()) {
            kaspar.putCoin();
            alfred.turnLeft();
        }

    }
}
