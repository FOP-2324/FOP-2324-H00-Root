package h00;

import h01.ExampleJUnitTest;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H00_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H00")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("Kaspar, der Zimmerchaot")
                .minPoints(0)
                .addChildCriteria(
                    criterion(
                        "Kaspar erreicht die Position (3, 4) über die korrekten Bewegungen",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasperMovement")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasperEndPosition")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    ),
                    criterion(
                        "Kaspar platziert die korrekte Anzahl an Münzen",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasparCoins")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    ),
                    criterion(
                        "Verbindliche Anforderungen wurden eingehalten.",
                        JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testKasperFor")),
                        -1
                    )
                )
                .build(),
            Criterion.builder()
                .shortDescription("Alfred, der Ordnungsfanatiker")
                .minPoints(0)
                .addChildCriteria(
                    criterion(
                        "Alfred erreicht die korrekte Position und nimmt den korrekten Weg",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredMovement")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredEndPosition")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    ),
                    criterion(
                        "Am Ende liegt auf jedem Feld die gewünschte Anzahl an Münzen.",
                        JUnitTestRef.and(
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredCoins")),
                            JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testCorrectRobotCount"))
                        )
                    ),
                    criterion(
                        "Verbindliche Anforderungen wurden eingehalten.",
                        JUnitTestRef.ofMethod(() -> TutorTests.class.getDeclaredMethod("testAlfredWhile")),
                        -1
                    )
                )
                .build()
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(final RubricConfiguration configuration) {
        configuration.addFileNameSolutionOverride(ExampleJUnitTest.class);
    }
}
