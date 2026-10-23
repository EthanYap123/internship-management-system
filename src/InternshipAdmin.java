
/**
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 */
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InternshipAdmin {

    private InternshipManagement manager;
    private static final String TITLE = "Internship Admin System";

    public InternshipAdmin(InternshipManagement manager) {

        this.manager = manager;
    }

    // ---------- Helper methods ----------
    private Company findCompanyByName(String name) {

        java.util.ArrayList<Company> companies = manager.getCompanies();

        for (int i = 0; i < companies.size(); i++) {
            Company c = companies.get(i);

            if (c.getCompanyName().equalsIgnoreCase(name.trim())) {
                return c;
            }
        }
        return null;
    }

    private Student findStudentById(String id) {

        java.util.ArrayList<Student> students = manager.getStudents();

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);

            if (s.getStudentId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    private Integer promptPositiveInt(String message) {

        while (true) {

            String input = JOptionPane.showInputDialog(null, message, TITLE, JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                return null; // user cancelled
            }

            try {
                int value = Integer.parseInt(input.trim());

                if (value <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a whole number greater than 0.",
                            TITLE, JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a valid whole number.",
                        TITLE, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Double promptMarks(String message) {

        while (true) {

            String input = JOptionPane.showInputDialog(null, message, TITLE, JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                return null; // user cancelled
            }

            try {
                double value = Double.parseDouble(input.trim());

                if (value < 0 || value > 100) {
                    JOptionPane.showMessageDialog(null, "Marks must be between 0 and 100.",
                            TITLE, JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a valid number for marks.",
                        TITLE, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------- Advanced Feature: Sound Effects ----------
    // generates a simple tone instead of relying on Toolkit.beep() since that
    // sometimes doesn't play depending on windows sound settings
    private void playTone(double frequencyHz, int durationMs) {

        try {

            float sampleRate = 44100f;
            int numSamples = (int) (durationMs / 1000.0 * sampleRate);
            byte[] buffer = new byte[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double angle = 2.0 * Math.PI * i * frequencyHz / sampleRate;
                buffer[i] = (byte) (Math.sin(angle) * 100);
            }

            javax.sound.sampled.AudioFormat format
                    = new javax.sound.sampled.AudioFormat(sampleRate, 8, 1, true, false);
            javax.sound.sampled.SourceDataLine line
                    = javax.sound.sampled.AudioSystem.getSourceDataLine(format);

            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();
        } catch (Exception e) {
            // ignore if no audio device
        }
    }

    private void playSuccessSound() {
        playTone(880, 150);
    }

    private void playErrorSound() {
        playTone(220, 200);
    }

    // can type "back" at any prompt to go to the previous question
    public void createStudentRecord() {

        String name = "";
        String studentId = "";
        String course = "";
        Company company = null;
        String role = "";
        Double allowance = null;
        Integer numPeriods = null;

        java.util.ArrayList<Integer> weeksList = new java.util.ArrayList<>();
        java.util.ArrayList<Double> marksList = new java.util.ArrayList<>();

        int step = 0;

        final int RATINGS_START_STEP = 7; // steps 0-6 are the basic fields + num periods

        while (true) {

            int totalSteps;

            if (numPeriods == null) {
                totalSteps = RATINGS_START_STEP + 1;
            } else {
                totalSteps = RATINGS_START_STEP + (numPeriods * 2);
            }

            if (step >= totalSteps) {
                break;
            }

            if (step >= RATINGS_START_STEP) {
                int offset = step - RATINGS_START_STEP;
                int periodIndex = offset / 2;
                int periodNumber = periodIndex + 1;
                boolean isWeeksStep = (offset % 2 == 0);

                if (isWeeksStep) {

                    String input = JOptionPane.showInputDialog(null,
                            "Enter Weeks for rating period " + periodNumber + ":\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        return; // cancelled
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    try {

                        int value = Integer.parseInt(input.trim());

                        if (value <= 0) {
                            JOptionPane.showMessageDialog(null,
                                    "Please enter a whole number greater than 0.",
                                    TITLE, JOptionPane.WARNING_MESSAGE);
                            continue;
                        }
                        if (periodIndex < weeksList.size()) {
                            weeksList.set(periodIndex, value); // overwrite if re-visiting this step
                        } else {
                            weeksList.add(value);
                        }
                        step++;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Please enter a valid whole number.",
                                TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                } else {

                    String input = JOptionPane.showInputDialog(null,
                            "Enter Marks for rating period " + periodNumber + ":\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        return;
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    try {
                        double value = Double.parseDouble(input.trim());

                        if (value < 0 || value > 100) {
                            JOptionPane.showMessageDialog(null, "Marks must be between 0 and 100.",
                                    TITLE, JOptionPane.WARNING_MESSAGE);
                            continue;
                        }
                        if (periodIndex < marksList.size()) {
                            marksList.set(periodIndex, value);
                        } else {
                            marksList.add(value);
                        }
                        step++;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Please enter a valid number for marks.",
                                TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                }
                continue;
            }

            switch (step) {
                case 0: {
                    String input = JOptionPane.showInputDialog(null, "Enter name:", TITLE,
                            JOptionPane.QUESTION_MESSAGE);
                    if (input == null) {
                        return; // cancelled
                    }
                    if (input.trim().isEmpty() || input.trim().equalsIgnoreCase("back")) {
                        continue; // first step: nothing to go back to, just re-ask
                    }
                    name = input.trim();
                    step++;
                    break;
                }
                case 1: {
                    String input = JOptionPane.showInputDialog(null,
                            "Enter Student ID:\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        return;
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    if (input.trim().isEmpty()) {
                        continue;
                    }
                    // Prevent duplicate student IDs
                    if (findStudentById(input.trim()) != null) {
                        playErrorSound();

                        JOptionPane.showMessageDialog(null,
                                "A student with ID \"" + input.trim() + "\" already exists!",
                                TITLE, JOptionPane.ERROR_MESSAGE);
                        continue; // re-ask the same step
                    }

                    studentId = input.trim();
                    step++;
                    break;
                }
                case 2: {
                    String input = JOptionPane.showInputDialog(null,
                            "Enter Course:\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        return;
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    if (input.trim().isEmpty()) {
                        continue;
                    }

                    course = input.trim();
                    step++;
                    break;
                }
                case 3: {
                    String input = JOptionPane.showInputDialog(null,
                            "Enter Company Name:\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        return;
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    if (input.trim().isEmpty()) {
                        continue;
                    }
                    Company found = findCompanyByName(input.trim());
                    if (found == null) {

                        playErrorSound();

                        JOptionPane.showMessageDialog(null,
                                "Company \"" + input.trim() + "\" does not exist!",
                                TITLE, JOptionPane.ERROR_MESSAGE);
                        continue; // re-ask the same step
                    }

                    company = found;
                    step++;
                    break;
                }
                case 4: {
                    String input = JOptionPane.showInputDialog(null,
                            "Enter Internship Role:\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        return;
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    if (input.trim().isEmpty()) {
                        continue;
                    }

                    role = input.trim();
                    step++;
                    break;
                }
                case 5: {
                    String input = JOptionPane.showInputDialog(null,
                            "Enter Monthly Allowance ($):\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);

                    if (input == null) {
                        return;
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    try {
                        double value = Double.parseDouble(input.trim());
                        if (value < 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a value of 0 or more.",
                                    TITLE, JOptionPane.WARNING_MESSAGE);
                            continue;
                        }
                        allowance = value;
                        step++;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Please enter a valid number.",
                                TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
                case 6: {
                    String input = JOptionPane.showInputDialog(null,
                            "Enter number of Rating Periods:\n(type 'back' to go to the previous question)",
                            TITLE, JOptionPane.QUESTION_MESSAGE);
                    if (input == null) {
                        return;
                    }
                    if (input.trim().equalsIgnoreCase("back")) {
                        step--;
                        continue;
                    }
                    try {
                        int value = Integer.parseInt(input.trim());

                        if (value <= 0) {
                            JOptionPane.showMessageDialog(null,
                                    "Please enter a whole number greater than 0.",
                                    TITLE, JOptionPane.WARNING_MESSAGE);
                            continue;
                        }
                        numPeriods = value;
                        step++;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Please enter a valid whole number.",
                                TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        }

        // Create the student and attach every rating period collected above
        Student newStudent = new Student(studentId, name, course, company, role, allowance);

        for (int i = 0; i < numPeriods; i++) {
            newStudent.addRating(weeksList.get(i), marksList.get(i));
        }

        // Add to the shared student list
        manager.getStudents().add(newStudent);

        playSuccessSound();
        JOptionPane.showMessageDialog(null, "Student record added!", TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- Option 2: Delete Student Record ----------
    public void deleteStudentRecord() {

        String studentId = JOptionPane.showInputDialog(null, "Enter Student ID to delete:", TITLE,
                JOptionPane.QUESTION_MESSAGE);

        if (studentId == null || studentId.trim().isEmpty()) {
            return;
        }

        Student student = findStudentById(studentId);

        if (student == null) {
            playErrorSound();
            JOptionPane.showMessageDialog(null, "Student not found!", TITLE, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        manager.getStudents().remove(student);

        playSuccessSound();
        JOptionPane.showMessageDialog(null, "Student record deleted!", TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- Option 3: Add Rating for Student ----------
    public void addPerformanceRating() {
        String studentId = JOptionPane.showInputDialog(null, "Enter Student ID:", TITLE,
                JOptionPane.QUESTION_MESSAGE);

        if (studentId == null || studentId.trim().isEmpty()) {
            return;
        }

        Student student = findStudentById(studentId);

        if (student == null) {
            playErrorSound();
            JOptionPane.showMessageDialog(null, "Student not found!", TITLE, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean addMore = true;

        while (addMore) {
            Integer weeks = promptPositiveInt("Enter Weeks for rating period:");
            if (weeks == null) {
                return;
            }
            Double marks = promptMarks("Enter Marks for rating period:");
            if (marks == null) {
                return;
            }
            student.addRating(weeks, marks);
            playSuccessSound();

            String moreInput = JOptionPane.showInputDialog(null,
                    "Rating added successfully.\nAre there more ratings to add?\n\n"
                    + "1. Yes\n"
                    + "2. No",
                    TITLE, JOptionPane.QUESTION_MESSAGE);

            addMore = (moreInput != null && moreInput.trim().equals("1"));
        }
    }

    // ---------- Advanced Feature: Tabulated Display ----------
    public void displayStudentsTabulated() {

        if (manager.getStudents().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No student records to display.", TITLE,
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-12s %-15s %-20s %-10s%n", "Student ID", "Name", "Company", "Score"));
        sb.append("------------------------------------------------------------\n");

        java.util.ArrayList<Student> allStudents = manager.getStudents();

        for (int i = 0; i < allStudents.size(); i++) {

            Student s = allStudents.get(i);

            sb.append(String.format("%-12s %-15s %-20s %-10s%n",
                    s.getStudentId(),
                    s.getName(),
                    s.getAssignedCompany().getCompanyName(),
                    String.format("%.2f", s.calculateOverallScore())));
        }

        sb.append("------------------------------------------------------------\n");

        // monospaced so columns line up
        JTextArea textArea = new JTextArea(sb.toString());

        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));

        JOptionPane.showMessageDialog(null, scrollPane, TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- Admin System menu loop ----------
    public void runAdminSystem() {

        boolean inAdmin = true;

        while (inAdmin) {

            String choiceInput = JOptionPane.showInputDialog(null,
                    "Enter your option:\n\n"
                    + "1. Add new student record\n"
                    + "2. Delete student record\n"
                    + "3. Add rating for student\n"
                    + "4. View all student records (table)\n"
                    + "5. Quit",
                    TITLE, JOptionPane.QUESTION_MESSAGE);

            if (choiceInput == null || choiceInput.trim().equals("5")) {
                inAdmin = false;
                break;
            }

            try {
                int option = Integer.parseInt(choiceInput.trim());

                switch (option) {
                    case 1:
                        createStudentRecord();
                        break;
                    case 2:
                        deleteStudentRecord();
                        break;
                    case 3:
                        addPerformanceRating();
                        break;
                    case 4:
                        displayStudentsTabulated();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Please choose a number between 1 and 5.",
                                TITLE, JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please input a valid numeric digit.",
                        TITLE, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
