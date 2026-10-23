
/**
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Advanced Feature — Undo support.
 *
 * Remembers one thing that was deleted, together with the position it used to
 * occupy, so it can be put back exactly where it was. Only deletions are
 * undoable: that keeps the feature small enough to hold in one class instead
 * of needing a full undo framework.
 */
public class DeletedRecord {

    /** What kind of thing was deleted. */
    public enum Kind {
        STUDENT, RATING
    }

    private final Kind kind;
    private final Student student;                   // deleted student, or the owner of the deleted rating
    private final Student.PerformanceRating rating;  // only used when kind == RATING
    private final int index;                         // position it was removed from

    private DeletedRecord(Kind kind, Student student, Student.PerformanceRating rating, int index) {
        this.kind = kind;
        this.student = student;
        this.rating = rating;
        this.index = index;
    }

    /** Records that a whole student record was deleted. */
    public static DeletedRecord forStudent(Student student, int index) {
        return new DeletedRecord(Kind.STUDENT, student, null, index);
    }

    /** Records that one rating was deleted from a student. */
    public static DeletedRecord forRating(Student owner, Student.PerformanceRating rating, int index) {
        return new DeletedRecord(Kind.RATING, owner, rating, index);
    }

    public Kind getKind() {
        return kind;
    }

    public Student getStudent() {
        return student;
    }

    public Student.PerformanceRating getRating() {
        return rating;
    }

    public int getIndex() {
        return index;
    }

    /** Wording used on the Undo button's tooltip and in the status area. */
    public String describe() {
        if (kind == Kind.STUDENT) {
            return "deletion of " + student.getName() + " (" + student.getStudentId() + ")";
        }
        return "removal of rating period " + (index + 1) + " from " + student.getName();
    }
}
