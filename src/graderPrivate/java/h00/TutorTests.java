package h00;

import fopbot.Coin;
import fopbot.Direction;
import fopbot.Field;
import fopbot.Robot;
import fopbot.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLoop;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static fopbot.Direction.DOWN;
import static fopbot.Direction.LEFT;
import static fopbot.Direction.RIGHT;
import static fopbot.Direction.UP;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertFalse;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.emptyContext;

/**
 * The Tutor Tests for Submission H00.
 *
 * @author Ruben Deisenroth, Patrick Blumenstein
 */
@TestForSubmission
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TutorTests {
    //------------//
    //--Messages--//
    //------------//

    public static final String NO_FOR_LOOP = "Für die Bewegung von Kaspar wurde keine for-Schleife verwendet.";
    public static final String WRONG_FOR_COUNT = "Für die Bewegung von Kaspar wurde die falsche Anzahl an for-Schleifen"
        + "oder ein falscher Schleifentyp verwendet.";
    public static final String NO_WHILE_LOOP = "Für die Bewegung von Alfred wurde keine while-Schleife verwendet.";
    public static final String NOT_ALL_WHILE_LOOP = "Für die Bewegung von Alfred wurden nicht nur while-Schleifen "
        + "verwendet.";
    public static final String NO_STATES_MESSAGE = "Der Roboter hat sich nicht bewegt.";
    public static final String WRONG_X_COORDINATE = "Die X-Koordinate des Roboters ist inkorrekt.";
    public static final String WRONG_Y_COORDINATE = "Die Y-Koordinate des Roboters ist inkorrekt.";
    public static final String WRONG_ROBOT_AMOUNT = "Die Anzahl der Roboter auf dem Feld ist inkorrekt.";
    public static final String WRONG_COIN_AMOUNT_KASPER = "Die Anzahl der Münzen auf dem Feld ist inkorrekt nachdem "
        + "Kasper seine Aktionen ausgeführt hat.";
    public static final String WRONG_COIN_AMOUNT_ALFRED = "Die Anzahl der Münzen auf dem Feld ist inkorrekt nachdem "
        + "Alfred seine Aktionen ausgeführt hat.";
    public static final String WRONG_VIEWING_DIRECTION = "Die Blickrichtung des Roboters ist inkorrekt.";
    public static final String WRONG_MOVEMENT_AMOUNT = "Die Anzahl der Bewegungen ist inkorrekt.";
    private static final String END_POSITION_KASPER_NOT_CORRECT = "Die Endposition von Kasper ist nicht korrekt.";
    private static final String END_POSITION_ALFRED_NOT_CORRECT = "Die Endposition des Alfred ist nicht korrekt.";
    private static final String EXCEPTION_MESSAGE = "Es ist ein Fehler aufgetreten: %s";

    private List<CtLoop> loops;

    /**
     * Returns a custom error Message for wrong movement at a given index.
     *
     * @param movementId the Index of the movement
     * @return the message
     */
    public static String getWrongMovementAtMoveMessage(final int movementId) {
        return String.format("Die %s. Bewegung ist inkorrekt: ", movementId + 1);
    }

    //-------------//
    //--Utilities--//
    //-------------//

    /**
     * Prepare the World for testing.
     */
    public static void setupWorld() {
        World.reset();
        World.setSize(5, 5);
        World.setDelay(0);
    }

    /**
     * A Movement state of a Robot.
     *
     * @param x the X-Coordinate of the Robot
     * @param y the Y-Coordinate of the Robot
     * @param d the {@link Direction} the robot is facing
     */
    private record MovementState(int x, int y, Direction d) {
    }

    /**
     * Asserts that the actual movement matches the expected Movement with custom error messages.
     *
     * @param expected the expected movement
     * @param actual   the actual movement
     * @param endsWith if it is expected, that the end of the actual movements should match or the beginning
     */
    private static void assertMovementEquals(
        final ArrayList<MovementState> expected,
        final ArrayList<MovementState> actual,
        final boolean endsWith
    ) {
        // length
        assertTrue(
            expected.size() <= actual.size(),
            emptyContext(),
            c -> WRONG_MOVEMENT_AMOUNT
        );


        if (endsWith) {
            Collections.reverse(expected);
            Collections.reverse(actual);
        }

        // elements
        IntStream.range(0, expected.size()).forEach(i -> {
            final var expectedState = expected.get(i);
            final var actualState = actual.get(i);
            assertEquals(
                expectedState.x,
                actualState.x,
                emptyContext(),
                c -> getWrongMovementAtMoveMessage(i) + WRONG_X_COORDINATE
            );
            assertEquals(
                expectedState.y,
                actualState.y,
                emptyContext(),
                c -> getWrongMovementAtMoveMessage(i) + WRONG_Y_COORDINATE
            );
            assertEquals(
                expectedState.d,
                actualState.d,
                emptyContext(),
                c -> getWrongMovementAtMoveMessage(i) + WRONG_VIEWING_DIRECTION
            );
        });
    }

    /**
     * Converts a given list of States to a List of {@link MovementState}.
     *
     * @param states the list to convert
     * @return the converted List
     */
    private static List<MovementState> toMovementStates(final List<Field> states) {
        return states.stream()
            .filter(x -> !x.getEntities().isEmpty())
            .map(x -> {
                final Robot robot = x.getEntities().stream()
                    .filter(Robot.class::isInstance)
                    .map(Robot.class::cast)
                    .findFirst().orElse(null);
                assertNotNull(robot, emptyContext(), c -> NO_STATES_MESSAGE);
                return new MovementState(Objects.requireNonNull(robot).getX(), robot.getY(), robot.getDirection());
            })
            .reduce(new ArrayList<>(), (acc, x) -> {
                if (acc.isEmpty() || !acc.get(acc.size() - 1).equals(x)) {
                    acc.add(x);
                }
                return acc;
            }, (a, b) -> {
                a.addAll(b);
                return a;
            });
    }

    /**
     * Returns the Java-Code to generate a given Movement-State list.
     *
     * @param states the List to generate
     * @return the Java-Code
     */
    @SuppressWarnings("unused")
    public static String getMovementStringListGenerationCode(final List<MovementState> states) {
        return "List.of(\n" + states.stream()
            .map(s -> String.format("\tnew MovementState(%s, %s, %s)", s.x, s.y, s.d))
            .collect(Collectors.joining(",\n")) + "\n);";
    }

    /**
     * Returns the Coin Counts of a given State.
     *
     * @param state the state
     * @return the coin Counts
     */
    public static int[][] getCoinCounts(final Field state) {
        final var result = new int[World.getHeight()][World.getWidth()];
        state.getEntities().stream().filter(Coin.class::isInstance)
            .forEach(c -> result[c.getX()][c.getY()] += ((Coin) c).getCount());
        return result;
    }

    /**
     * Asserts that the Coin Count arrays match.
     *
     * @param expected the expected coin counts
     * @param actual   the actual Coin Counts
     */
    public static boolean checkCoinCountsEqual(final int[][] expected, final int[][] actual) {
        for (int x = 0; x < World.getWidth(); x++) {
            for (int y = 0; y < World.getHeight(); y++) {
                if (expected[y][x] != actual[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the states list of the World.
     *
     * @return the state list
     */
    private List<Field> getStates() {
        final var states = World.getGlobalWorld().getEntityStates();
        assertNotNull(states, emptyContext(), c -> NO_STATES_MESSAGE);
        assertFalse(states.isEmpty(), emptyContext(), c -> NO_STATES_MESSAGE);
        return states;
    }

    /**
     * Collects all loops from the given method. Recursively checks all called methods from the given Class for further loops.
     *
     * @param clazz  only methods of this class will be checked for loops.
     * @param method the method to check.
     * @return a list of all loops that are directly or indirectly contained in the method.
     */
    private static Stream<CtLoop> getLoops(final Class<?> clazz, final CtMethod<?> method) {
        return method.getElements(new TypeFilter<>(CtElement.class) {
                @Override
                public boolean matches(final CtElement element) {
                    return (element instanceof CtLoop || (element instanceof CtInvocation<?>))
                        && element.getParent(CtMethod.class).getDeclaringType().getActualClass().equals(clazz);
                }
            })
            .stream()
            .flatMap(element -> {
                if (element instanceof final CtInvocation<?> call) {
                    final var calledMethod = call.getExecutable();
                    if (!calledMethod.getDeclaringType().getQualifiedName().startsWith("h00.")) {
                        return Stream.of();
                    }
                    final var actualCalledMethod = calledMethod.getActualMethod();
                    final CtMethod<?> calledMethodCt = BasicMethodLink.of(actualCalledMethod).getCtElement();
                    return getLoops(clazz, calledMethodCt);
                }
                return Stream.of((CtLoop) element);
            });
    }

    /**
     * Returns the final State of the World.
     *
     * @return the final State of the World
     */
    private Field getFinalState() {
        final var states = getStates();
        return states.get(states.size() - 1);
    }

    //---------//
    //--Tests--//
    //---------//

    @BeforeAll
    public void setupLoops() {
        final BasicTypeLink classLink = BasicTypeLink.of(Main.class);
        final CtMethod<Void> exerciseMethod = ((CtClass<?>) classLink.getCtElement()).getMethod("runExercise");
        loops = getLoops(Main.class, exerciseMethod).toList();
    }

    /**
     * Test setup.
     */
    @BeforeEach
    public void setup() {
        setupWorld();

        Assertions2.call(
            Main::runExercise,
            emptyContext(),
            r -> String.format(EXCEPTION_MESSAGE, r.cause().toString())
        );
    }

    @Test
    public void testKasperMovement() {
        final var movementStates = toMovementStates(getStates());
        final var expectedMovement = List.of(
            //Kasper Drehung
            new MovementState(0, 0, LEFT),
            new MovementState(0, 0, DOWN),
            new MovementState(0, 0, RIGHT),
            //Kasper läuft nach rechts
            new MovementState(1, 0, RIGHT),
            new MovementState(2, 0, RIGHT),
            new MovementState(3, 0, RIGHT),
            new MovementState(4, 0, RIGHT),
            //Kasper Drehung
            new MovementState(4, 0, UP),
            //Kasper läuft nach oben
            new MovementState(4, 1, UP),
            new MovementState(4, 2, UP),
            new MovementState(4, 3, UP),
            //Kasper dreht sich nach links und geht nach links
            new MovementState(4, 4, RIGHT),
            new MovementState(3, 4, LEFT)
        );
        assertMovementEquals(new ArrayList<>(expectedMovement), new ArrayList<>(movementStates), false);
    }

    @Test
    public void testKasperEndPosition() {
        final Context.Builder<?> context = contextBuilder();
        final boolean correctPosition = World.getGlobalWorld().getAllFieldEntities().stream()
            .filter(fieldEntity -> fieldEntity instanceof Robot)
            .peek(r -> context.add("Robot", contextBuilder().add("x", r.getX()).add("y", r.getY()).add(
                "Direction",
                ((Robot) r).getDirection()
            ).build()))
            .anyMatch(robot ->
                          robot.getX() == 3 && robot.getY() == 4 && ((Robot) robot).getDirection() == LEFT
            );
        context.add("Expected", contextBuilder().add("x", 3).add("y", 4).add("Direction", LEFT).build());
        assertTrue(correctPosition, context.build(), r -> END_POSITION_KASPER_NOT_CORRECT);
    }

    @Test
    public void testKasperFor() {
        assertTrue(loops.stream().anyMatch(l -> l instanceof CtFor), emptyContext(), r -> NO_FOR_LOOP);
        assertTrue(
            loops.get(0) instanceof CtFor,
            contextBuilder().add("Die Schleife an folgender Position ist keine for-Schleife", "1").build(),
            r -> WRONG_FOR_COUNT
        );
        assertTrue(
            loops.get(1) instanceof CtFor,
            contextBuilder().add("Die Schleife an folgender Position ist keine for-Schleife", "2").build(),
            r -> WRONG_FOR_COUNT
        );
        assertEquals(
            2L,
            loops.stream().filter(l -> l instanceof CtFor).count(),
            emptyContext(),
            r -> WRONG_FOR_COUNT
        );
    }

    @Test
    public void testKasparCoins() {
        final var expectedCoinCounts = new int[][]{
            {1, 0, 0, 0, 0},
            {1, 0, 0, 0, 0},
            {1, 0, 0, 0, 0},
            {1, 0, 0, 0, 0},
            {1, 1, 1, 1, 1}
        };
        assertTrue(
            getStates().stream().anyMatch(field -> checkCoinCountsEqual(expectedCoinCounts, getCoinCounts(field))),
            emptyContext(),
            r -> WRONG_COIN_AMOUNT_KASPER
        );
    }

    @Test
    public void testAlfredMovement() {
        final var movementStates = toMovementStates(getStates());
        final var expectedMovement = List.of(
            //Alfred läuft nach unten
            new MovementState(4, 3, DOWN),
            new MovementState(4, 2, DOWN),
            new MovementState(4, 1, DOWN),
            new MovementState(4, 0, DOWN),
            //Alfred dreht sich nach links
            new MovementState(4, 0, RIGHT),
            new MovementState(4, 0, UP),
            new MovementState(4, 0, LEFT),
            //Alfred läuft nach links
            new MovementState(3, 0, LEFT),
            new MovementState(2, 0, LEFT),
            new MovementState(1, 0, LEFT),
            new MovementState(0, 0, LEFT),
            //Alfred dreht sich und geht nach oben
            new MovementState(0, 0, DOWN),
            new MovementState(0, 0, RIGHT),
            new MovementState(0, 0, UP),
            new MovementState(0, 1, UP),
            //Alfred dreht sich im kreis
            new MovementState(0, 1, LEFT),
            new MovementState(0, 1, DOWN),
            new MovementState(0, 1, RIGHT),
            new MovementState(0, 1, UP),
            new MovementState(0, 1, LEFT),
            new MovementState(0, 1, DOWN),
            new MovementState(0, 1, RIGHT),
            new MovementState(0, 1, UP),
            new MovementState(0, 1, LEFT),
            new MovementState(0, 1, DOWN),
            new MovementState(0, 1, RIGHT)
        );
        assertMovementEquals(new ArrayList<>(expectedMovement), new ArrayList<>(movementStates), true);
    }

    @Test
    public void testAlfredEndPosition() {
        final Context.Builder<?> context = contextBuilder();
        final int[] i = {0};
        final boolean correctPosition = World.getGlobalWorld().getAllFieldEntities().stream()
            .filter(fieldEntity -> fieldEntity instanceof Robot)
            .peek(r -> {
                context.add("Robot" + i[0], contextBuilder().add("x", r.getX()).add("y", r.getY()).add(
                    "Direction",
                    ((Robot) r).getDirection()
                ).build());
                i[0]++;
            })
            .anyMatch(robot ->
                          robot.getX() == 0 && robot.getY() == 1 && ((Robot) robot).getDirection() == RIGHT
            );
        context.add("Expected", contextBuilder().add("x", 0).add("y", 1).add("Direction", RIGHT).build());
        assertTrue(correctPosition, context.build(), r -> END_POSITION_ALFRED_NOT_CORRECT);
    }

    @Test
    public void testAlfredWhile() {
        assertTrue(loops.stream().anyMatch(l -> l instanceof CtWhile), emptyContext(), r -> NO_WHILE_LOOP);

        boolean allWhile = false;

        final int forLoopIndex = loops.indexOf(loops.stream().filter(l -> l instanceof CtFor).findFirst().orElse(null));

        for (int i = Math.max(forLoopIndex, 0); i < loops.size(); i++) {
            final List<CtLoop> currentLoops = loops.subList(i, loops.size());
            final boolean allWhileSublist = currentLoops.stream().allMatch(l -> l instanceof CtWhile);

            if (currentLoops.get(0) instanceof CtWhile) {
                assertTrue(allWhileSublist, emptyContext(), r -> NOT_ALL_WHILE_LOOP);
            }

            if (allWhileSublist) {
                allWhile = true;
                break;
            }
        }

        assertTrue(allWhile, emptyContext(), r -> NOT_ALL_WHILE_LOOP);
    }

    @Test
    public void testAlfredCoins() {
        final var expectedCoinCounts = new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 11},
            {0, 0, 0, 0, 0}
        };
        assertTrue(getStates().stream().anyMatch(field -> checkCoinCountsEqual(
            expectedCoinCounts,
            getCoinCounts(field)
        )), emptyContext(), r -> WRONG_COIN_AMOUNT_ALFRED);
    }

    @Test
    public void testCorrectRobotCount() {
        assertEquals(
            2L,
            World.getGlobalWorld().getAllFieldEntities().stream().filter(Robot.class::isInstance).count(),
            emptyContext(),
            r -> WRONG_ROBOT_AMOUNT
        );
    }

    @Test
    public void print() {
        System.out.println((getMovementStringListGenerationCode(toMovementStates(getStates()))));
    }
}
