package h00;

import fopbot.Robot;
import fopbot.RobotFamily;
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
        Robot kaspar = new Robot(0, 0, LEFT, 20, RobotFamily.SQUARE_ORANGE);
        Robot alfred = new Robot(4, 4, RIGHT, 0, RobotFamily.SQUARE_BLUE);

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
        // turning from east to south
        while (!alfred.isFacingDown())
            alfred.turnLeft();
        // moving towards bottom right corner, removing coin and moving, until wall
        // infront
        while (alfred.isFrontClear()) {
            alfred.pickCoin();
            alfred.move();
        }
        // turning from south to west
        while (!alfred.isFacingLeft())
            alfred.turnLeft();
        // moving towards bottom left corner, removing coin and moving, until wall
        // infront
        while (alfred.isFrontClear()) {
            alfred.pickCoin();
            alfred.move();
        }
        // turning from west to north
        while (!alfred.isFacingUp())
            alfred.turnLeft();
        // // coin removal for bottom left corner
        alfred.pickCoin();
        alfred.move();

        // the last dance
        while (kaspar.hasAnyCoins()) {
            kaspar.putCoin();
            alfred.turnLeft();
        }

    }
}
