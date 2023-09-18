package h00;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.GradeResult;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;

import java.util.List;

/**
 * Utility class for creating {@link Criterion}s.
 */
public class RubricUtils {
    /**
     * Creates a new {@link Criterion.Builder} with the given description and test.
     *
     * @param description The description of the criterion.
     * @param testRef     The test reference of the criterion.
     * @return The newly created criterion builder.
     */
    public static Criterion.Builder defaultCriterionBuilder(final String description, final JUnitTestRef testRef) {
        final var grader = Grader.testAwareBuilder();
        if (testRef != null) {
            grader.requirePass(testRef);
        }
        grader.pointsFailedMin();
        grader.pointsPassedMax();
        return Criterion.builder()
            .shortDescription(description)
            .grader(grader.build());
    }

    /**
     * Creates a new {@link Criterion.Builder} with the given description and test.
     *
     * @param description The description of the criterion.
     * @param testRefs     The test references of the criterion.
     * @return The newly created criterion builder.
     */
    public static Criterion.Builder defaultCriterionBuilder(final String description, final JUnitTestRef... testRefs) {
        final var grader = Grader.testAwareBuilder();
        if (testRefs != null) {
            for (final JUnitTestRef testRef : testRefs) {
                grader.requirePass(testRef);
            }
        }
        grader.pointsFailedMin();
        grader.pointsPassedMax();
        return Criterion.builder()
            .shortDescription(description)
            .grader(grader.build());
    }

    public static Grader testAwareGrader(final JUnitTestRef testRef) {
        final var grader = Grader.testAwareBuilder();
        if (testRef != null) {
            grader.requirePass(testRef);
        }
        grader.pointsFailedMin();
        grader.pointsPassedMax();
        return grader.build();
    }

    /**
     * Creates a new {@link Criterion} with the given description and test.
     *
     * @param description The description of the criterion.
     * @param testRef     The test reference of the criterion.
     * @return The newly created criterion.
     */
    public static Criterion criterion(final String description, final JUnitTestRef testRef) {
        return defaultCriterionBuilder(description, testRef).build();
    }

    public static Criterion criterion(final String description, final JUnitTestRef... testRefs) {
        return defaultCriterionBuilder(description, testRefs).build();
    }

    /**
     * Creates a new {@link Criterion} with the given description and test.
     *
     * @param description The description of the criterion.
     * @param testRef     The test reference of the criterion.
     * @param points      The points of the criterion.
     * @return The newly created criterion.
     */
    public static Criterion criterion(final String description, final JUnitTestRef testRef, final int points) {
        final var cb = defaultCriterionBuilder(description, testRef);
        if (points >= 0) {
            cb.minPoints(0);
            cb.maxPoints(points);
        } else {
            cb.maxPoints(0);
            cb.minPoints(points);
        }
        return cb.build();
    }

    /**
     * Creates a new {@link Criterion} with the given description and test.
     *
     * @param description The description of the criterion.
     * @param testRefs     The test references of the criterion that need to pass.
     * @param points      The points of the criterion.
     * @return The newly created criterion.
     */
    public static Criterion criterion(final String description, final int points, final JUnitTestRef... testRefs) {
        final var cb = defaultCriterionBuilder(description, testRefs);
        if (points >= 0) {
            cb.minPoints(0);
            cb.maxPoints(points);
        } else {
            cb.maxPoints(0);
            cb.minPoints(points);
        }
        return cb.build();
    }

    /**
     * Creates a {@link Grader} for manual tutor grading.
     *
     * @param points The maximum points to be awarded.
     * @return The newly created grader.
     */
    public static Grader manualGrader(final int points) {
        return (testCycle, criterion) -> GradeResult.of(Math.min(points, 0), Math.max(points, 0), "This criterion will be graded manually.");
    }

    /**
     * Creates a {@link Grader} for manual tutor grading with a maximum of 1 point.
     *
     * @return The newly created grader.
     */
    public static Grader manualGrader() {
        return manualGrader(1);
    }

    /**
     * Creates a {@link Grader} for manual tutor grading.
     *
     * @param points The maximum points to be awarded.
     * @return The newly created grader.
     */
    public static Grader graderPrivateOnly(final int points) {
        return (testCycle, criterion) -> GradeResult.of(Math.min(points, 0), Math.max(points, 0), "This criterion is not graded by the public Tests.");
    }

    /**
     * Creates a {@link Grader} for manual tutor grading with a maximum of 1 point.
     *
     * @return The newly created grader.
     */
    public static Grader graderPrivateOnly() {
        return graderPrivateOnly(1);
    }
}
