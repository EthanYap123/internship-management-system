
/**
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Advanced Feature — Rating History table (table model).
 *
 * Shows every performance rating belonging to the currently selected student.
 * It reads the live ArrayList returned by Student.getRatings(), so deleting a
 * row here really removes the rating from that Student object.
 */
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class RatingTableModel extends AbstractTableModel {

    public static final int COL_PERIOD = 0;
    public static final int COL_WEEKS = 1;
    public static final int COL_MARKS = 2;
    public static final int COL_GRADE_POINT = 3;

    private static final String[] COLUMN_NAMES = {"Period", "Weeks", "Marks", "Grade Point"};

    /** The student whose ratings are shown. Null means "nothing selected". */
    private Student student;

    @Override
    public int getRowCount() {
        return (student == null) ? 0 : student.getRatings().size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case COL_MARKS:
                return Double.class;
            default:
                return Integer.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (student == null || row < 0 || row >= student.getRatings().size()) {
            return "";
        }

        Student.PerformanceRating rating = student.getRatings().get(row);

        switch (column) {
            case COL_PERIOD:
                return row + 1;
            case COL_WEEKS:
                return rating.getWeeks();
            case COL_MARKS:
                return rating.getMarks();
            case COL_GRADE_POINT:
                return rating.getGradePoint();
            default:
                return "";
        }
    }

    /** Switches the table to a different student (or to nothing). */
    public void setStudent(Student student) {
        this.student = student;
        fireTableDataChanged();
    }

    public Student getStudent() {
        return student;
    }

    public Student.PerformanceRating getRatingAt(int row) {
        if (student == null || row < 0 || row >= student.getRatings().size()) {
            return null;
        }
        return student.getRatings().get(row);
    }

    /** Redraws after a rating has been added, deleted or restored. */
    public void refresh() {
        fireTableDataChanged();
    }

    /** Convenience used by the impact preview. */
    public ArrayList<Student.PerformanceRating> getRatings() {
        return (student == null) ? new ArrayList<>() : student.getRatings();
    }
}
