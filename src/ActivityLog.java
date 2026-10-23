
/**
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Advanced Feature — Admin Activity Log & Session Statistics.
 *
 * Records every change the admin makes during this run, with a timestamp, and
 * keeps running counts for the session summary. Like DataIntegrityChecker this
 * class holds no Swing code, so it can be tested and explained on its own.
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ActivityLog {

    // Action names. Constants instead of loose strings so a typo becomes a
    // compile error rather than a wrong count.
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_DELETE = "DELETE";
    public static final String ACTION_RATING = "RATING";
    public static final String ACTION_UNDO = "UNDO";
    public static final String ACTION_SAVE = "SAVE";

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    /** One line in the log. */
    public static class Entry {

        private final LocalDateTime time;
        private final String action;
        private final String details;

        public Entry(String action, String details) {
            this.time = LocalDateTime.now();
            this.action = action;
            this.details = details;
        }

        public LocalDateTime getTime() {
            return time;
        }

        public String getAction() {
            return action;
        }

        public String getDetails() {
            return details;
        }

        /** Fixed-width layout so the log lines up in a monospaced list. */
        @Override
        public String toString() {
            return String.format("%s  %-7s  %s", time.format(TIME_FORMAT), action, details);
        }
    }

    private final ArrayList<Entry> entries = new ArrayList<>();

    // Session statistics
    private int studentsCreated = 0;
    private int studentsDeleted = 0;
    private int ratingsAdded = 0;
    private int ratingsDeleted = 0;
    private int undos = 0;
    private int saves = 0;

    /**
     * Adds one line to the log and updates the matching session counter.
     */
    public void record(String action, String details) {
        entries.add(new Entry(action, details));

        switch (action) {
            case ACTION_CREATE:
                studentsCreated++;
                break;
            case ACTION_DELETE:
                studentsDeleted++;
                break;
            case ACTION_RATING:
                ratingsAdded++;
                break;
            case ACTION_UNDO:
                undos++;
                break;
            case ACTION_SAVE:
                saves++;
                break;
            default:
                // Anything else is logged but not counted.
                break;
        }
    }

    /** Recorded separately because a deleted rating is not a deleted student. */
    public void recordRatingDeleted(String details) {
        entries.add(new Entry(ACTION_DELETE, details));
        ratingsDeleted++;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int getStudentsCreated() {
        return studentsCreated;
    }

    public int getStudentsDeleted() {
        return studentsDeleted;
    }

    public int getRatingsAdded() {
        return ratingsAdded;
    }

    public int getRatingsDeleted() {
        return ratingsDeleted;
    }

    public int getUndos() {
        return undos;
    }

    public int getSaves() {
        return saves;
    }

    /** One-line summary used in the Activity tab and the status area. */
    public String getSessionSummary() {
        return "Created " + studentsCreated
                + "   |   Deleted " + studentsDeleted
                + "   |   Ratings added " + ratingsAdded
                + "   |   Ratings removed " + ratingsDeleted
                + "   |   Undos " + undos
                + "   |   Saves " + saves;
    }
}
