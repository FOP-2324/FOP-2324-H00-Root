package h00;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H00_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H00")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("Kaspar, der Zimmerchaot")
                .addChildCriteria(
                    criterion(
                        "Kaspar erreicht die Position (3, 4) über die korrekten Bewegungen",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasperMovement")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasperEndPosition")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasperFor")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    ),
                    criterion(
                        "Kaspar platziert die korrekte Anzahl an Münzen",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasparCoins")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasperFor")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    )
                )
                .build(),
            Criterion.builder()
                .shortDescription("Alfred, der Ordnungsfanatiker")
                .addChildCriteria(
                    criterion(
                        "Alfred erreicht die korrekte Position und nimmt den korrekten Weg",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredMovement")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredEndPosition")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredWhile")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    ),
                    criterion(
                        "Die Münzen wurden korrekt platziert",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredCoins")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredWhile")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    )
                )
                .build()
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}