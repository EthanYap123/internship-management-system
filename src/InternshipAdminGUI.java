
/**
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Member B — Internship Admin System GUI Application (CA2).
 *
 * Built with the NetBeans GUI Builder: the screen layout lives in
 * InternshipAdminGUI.form and the Form Editor generates initComponents()
 * below. Everything under "MY CODE" is written by hand.
 *
 * It works on the SAME InternshipManagement object that Member A's Enquiry
 * System uses, so any record created, deleted or rated here is immediately
 * visible to Member A.
 *
 * REQUIRED FUNCTIONS (Admin tab)
 *   Function 1 - Create student internship record
 *   Function 2 - Search a student (by ID or name) and add a performance rating
 *   Function 3 - Delete the currently selected student internship record
 *   Function 4 - Exit and save all Student + Company data to an object
 *                serialized file through FileHandler
 *
 * ADVANCED FEATURES
 *   1. Tabbed admin interface (JTabbedPane)
 *   2. Student management JTable backed by a custom AbstractTableModel
 *   3. Column sorting, live multi-filtering and a dynamic result count
 *   4. Rating history table, delete-a-rating, and a live score impact preview
 *   5. Activity log, unsaved-changes indicator, Save Now and session statistics
 *   6. Data integrity and attention alerts with colour-coded severity
 *   7. Editable company combo box that fills in the company's details
 *   8. Undo for the last deletion (student or rating)
 *   9. Save verification, timestamped backups and keyboard shortcuts
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class InternshipAdminGUI extends javax.swing.JFrame {

    // ================= MY CODE — colours and constants =================
    /** Shared width for every form caption label. */
    private static final int FORM_LABEL_WIDTH = 140;

    private static final Color BORDER_GREY = new Color(148, 163, 184);
    private static final Color ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);
    private static final Color TEXT_OK = new Color(21, 128, 61);
    private static final Color TEXT_BAD = new Color(185, 28, 28);

    private static final Color ROW_CRITICAL = new Color(254, 226, 226);
    private static final Color ROW_WARNING = new Color(255, 237, 213);
    private static final Color ROW_INFO = new Color(254, 249, 195);

    private static final int TAB_ADMIN = 0;
    private static final int TAB_STUDENTS = 1;
    private static final int TAB_ALERTS = 2;

    private static final String PRESET_ALL = "All students";
    private static final String PRESET_NO_RATINGS = "No ratings yet";
    private static final String PRESET_LOW = "Low performance (below 2.00)";
    private static final String PRESET_TOP = "Top performers (3.50 and above)";
    private static final String PRESET_ATTENTION = "Needs attention";

    private static final String ANY_COURSE = "All courses";
    private static final String ANY_COMPANY = "All companies";

    // ================= MY CODE — state =================
    /**
     * The shared data manager. Member A's GUI is given the same object by
     * InternshipUser, so both subsystems read and write one set of records.
     */
    private final InternshipManagement manager;

    /** The student the admin is currently working on. */
    private Student selectedStudent;

    private final ActivityLog activityLog = new ActivityLog();
    private final ArrayDeque<DeletedRecord> undoStack = new ArrayDeque<>();
    private ArrayList<DataIntegrityChecker.Alert> currentAlerts = new ArrayList<>();
    private final HashMap<Student, DataIntegrityChecker.Severity> worstSeverityByStudent = new HashMap<>();

    private boolean hasUnsavedChanges = false;
    private String lastSavedAt = null;

    /** Guards stop listeners from re-triggering each other while refreshing. */
    private boolean isRebuildingFilters = false;
    private boolean isSyncingTableSelection = false;

    // Models and helpers that are not screen components, so the Form Editor
    // does not manage them.
    private StudentTableModel studentTableModel;
    private RatingTableModel ratingTableModel;
    private TableRowSorter<StudentTableModel> studentSorter;
    private DefaultListModel<DataIntegrityChecker.Alert> alertListModel;
    private DefaultListModel<String> activityListModel;

    public InternshipAdminGUI(InternshipManagement manager) {
        this.manager = manager;
        this.selectedStudent = null;

        initComponents();   // generated by the Form Editor
        setupAfterInit();   // my own wiring

        refreshAllViews();
        showWelcomeMessage();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        searchTypeGroup = new javax.swing.ButtonGroup();
        headerPanel = new javax.swing.JPanel();
        headerTextPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        tabbedPane = new javax.swing.JTabbedPane();
        adminTab = new javax.swing.JPanel();
        leftColumn = new javax.swing.JPanel();
        studentDetailsPanel = new javax.swing.JPanel();
        lblStudentName = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        lblStudentIdCaption = new javax.swing.JLabel();
        tfStudentId = new javax.swing.JTextField();
        lblCourseCaption = new javax.swing.JLabel();
        tfCourse = new javax.swing.JTextField();
        lblCompanyCaption = new javax.swing.JLabel();
        cbCompany = new javax.swing.JComboBox();
        lblCompanyDetails = new javax.swing.JLabel();
        studentButtonRow = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();
        btnClearForm = new javax.swing.JButton();
        internshipPanel = new javax.swing.JPanel();
        lblRoleCaption = new javax.swing.JLabel();
        tfRole = new javax.swing.JTextField();
        lblAllowanceCaption = new javax.swing.JLabel();
        tfAllowance = new javax.swing.JTextField();
        lblNewRatingHeading = new javax.swing.JLabel();
        lblWeeksCaption = new javax.swing.JLabel();
        tfWeeks = new javax.swing.JTextField();
        lblMarksCaption = new javax.swing.JLabel();
        tfMarks = new javax.swing.JTextField();
        lblRatingImpact = new javax.swing.JLabel();
        addRatingRow = new javax.swing.JPanel();
        btnAddRating = new javax.swing.JButton();
        lblRatingHistoryHeading = new javax.swing.JLabel();
        ratingScroll = new javax.swing.JScrollPane();
        ratingTable = new javax.swing.JTable();
        deleteRatingRow = new javax.swing.JPanel();
        btnDeleteRating = new javax.swing.JButton();
        rightColumn = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        searchRadioRow = new javax.swing.JPanel();
        rbSearchById = new javax.swing.JRadioButton();
        rbSearchByName = new javax.swing.JRadioButton();
        lblSearchValue = new javax.swing.JLabel();
        tfSearch = new javax.swing.JTextField();
        searchButtonRow = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        resultsPanel = new javax.swing.JPanel();
        resultsScroll = new javax.swing.JScrollPane();
        taResults = new javax.swing.JTextArea();
        studentsTab = new javax.swing.JPanel();
        filterPanel = new javax.swing.JPanel();
        lblContains = new javax.swing.JLabel();
        tfTableFilter = new javax.swing.JTextField();
        lblCourseFilter = new javax.swing.JLabel();
        cbFilterCourse = new javax.swing.JComboBox();
        lblCompanyFilter = new javax.swing.JLabel();
        cbFilterCompany = new javax.swing.JComboBox();
        lblShow = new javax.swing.JLabel();
        cbFilterPreset = new javax.swing.JComboBox();
        btnResetFilters = new javax.swing.JButton();
        tablePanel = new javax.swing.JPanel();
        studentTableScroll = new javax.swing.JScrollPane();
        studentTable = new javax.swing.JTable();
        studentsBottomBar = new javax.swing.JPanel();
        lblResultCount = new javax.swing.JLabel();
        studentsActionRow = new javax.swing.JPanel();
        btnLoadSelected = new javax.swing.JButton();
        alertsTab = new javax.swing.JPanel();
        alertsTopBar = new javax.swing.JPanel();
        lblAlertSummary = new javax.swing.JLabel();
        btnRescan = new javax.swing.JButton();
        alertListPanel = new javax.swing.JPanel();
        alertScroll = new javax.swing.JScrollPane();
        alertList = new javax.swing.JList();
        alertDetailPanel = new javax.swing.JPanel();
        alertDetailScroll = new javax.swing.JScrollPane();
        taAlertDetail = new javax.swing.JTextArea();
        activityTab = new javax.swing.JPanel();
        sessionStatsPanel = new javax.swing.JPanel();
        lblSessionStats = new javax.swing.JLabel();
        activityLogPanel = new javax.swing.JPanel();
        activityScroll = new javax.swing.JScrollPane();
        activityList = new javax.swing.JList();
        bottomBar = new javax.swing.JPanel();
        statusHolder = new javax.swing.JPanel();
        lblSelectedStudent = new javax.swing.JLabel();
        lblSaveState = new javax.swing.JLabel();
        bottomButtonHolder = new javax.swing.JPanel();
        btnUndo = new javax.swing.JButton();
        btnSaveNow = new javax.swing.JButton();
        btnExitSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Internship Admin System - Member B (Tay Yu Xiang Janston)");

        headerPanel.setBackground(new java.awt.Color(30, 41, 59));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerTextPanel.setLayout(new java.awt.GridLayout(2, 1));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Mini Student Internship Management System");
        headerTextPanel.add(lblTitle);

        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSubtitle.setForeground(new java.awt.Color(203, 213, 225));
        lblSubtitle.setText("Admin Subsystem (Member B: Tay Yu Xiang Janston | P2523075)");
        headerTextPanel.add(lblSubtitle);

        headerPanel.add(headerTextPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        tabbedPane.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        adminTab.setLayout(new java.awt.GridLayout(1, 2));

        leftColumn.setLayout(new java.awt.GridBagLayout());

        studentDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Student Details"));
        studentDetailsPanel.setLayout(new java.awt.GridBagLayout());

        lblStudentName.setText("Student Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(lblStudentName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(tfName, gridBagConstraints);

        lblStudentIdCaption.setText("Student ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(lblStudentIdCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(tfStudentId, gridBagConstraints);

        lblCourseCaption.setText("Course:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(lblCourseCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(tfCourse, gridBagConstraints);

        lblCompanyCaption.setText("Company Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(lblCompanyCaption, gridBagConstraints);

        cbCompany.setEditable(true);
        cbCompany.setToolTipText("Pick an existing company, or type a name to test the validation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(cbCompany, gridBagConstraints);

        lblCompanyDetails.setFont(new java.awt.Font("Segoe UI", 2, 11)); // NOI18N
        lblCompanyDetails.setForeground(new java.awt.Color(100, 116, 139));
        lblCompanyDetails.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(lblCompanyDetails, gridBagConstraints);

        btnDelete.setText("Delete Record");
        btnDelete.setToolTipText("Delete the currently selected student");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        studentButtonRow.add(btnDelete);

        btnCreate.setText("Create Record");
        btnCreate.setToolTipText("Register a new student internship record (Ctrl+N clears the form)");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });
        studentButtonRow.add(btnCreate);

        btnClearForm.setText("Clear Form");
        btnClearForm.setToolTipText("Empty the form and clear the selection (Esc)");
        btnClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFormActionPerformed(evt);
            }
        });
        studentButtonRow.add(btnClearForm);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        studentDetailsPanel.add(studentButtonRow, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        leftColumn.add(studentDetailsPanel, gridBagConstraints);

        internshipPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Internship & Rating Details"));
        internshipPanel.setLayout(new java.awt.GridBagLayout());

        lblRoleCaption.setText("Internship Role:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(lblRoleCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(tfRole, gridBagConstraints);

        lblAllowanceCaption.setText("Monthly Allowance ($):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(lblAllowanceCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(tfAllowance, gridBagConstraints);

        lblNewRatingHeading.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblNewRatingHeading.setForeground(new java.awt.Color(71, 85, 105));
        lblNewRatingHeading.setText("New Performance Rating");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 6);
        internshipPanel.add(lblNewRatingHeading, gridBagConstraints);

        lblWeeksCaption.setText("Weeks Assessed:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(lblWeeksCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(tfWeeks, gridBagConstraints);

        lblMarksCaption.setText("Marks (0 - 100):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(lblMarksCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(tfMarks, gridBagConstraints);

        lblRatingImpact.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblRatingImpact.setForeground(new java.awt.Color(100, 116, 139));
        lblRatingImpact.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(lblRatingImpact, gridBagConstraints);

        btnAddRating.setText("Add Rating");
        btnAddRating.setToolTipText("Add this rating period to the selected student");
        btnAddRating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRatingActionPerformed(evt);
            }
        });
        addRatingRow.add(btnAddRating);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(addRatingRow, gridBagConstraints);

        lblRatingHistoryHeading.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblRatingHistoryHeading.setForeground(new java.awt.Color(71, 85, 105));
        lblRatingHistoryHeading.setText("Rating History");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 6);
        internshipPanel.add(lblRatingHistoryHeading, gridBagConstraints);

        ratingScroll.setMinimumSize(new java.awt.Dimension(320, 96));
        ratingScroll.setPreferredSize(new java.awt.Dimension(320, 96));

        ratingTable.setToolTipText("Select a rating period, then click Delete Selected Rating");
        ratingScroll.setViewportView(ratingTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(ratingScroll, gridBagConstraints);

        btnDeleteRating.setText("Delete Selected Rating");
        btnDeleteRating.setToolTipText("Remove the highlighted rating period from this student");
        btnDeleteRating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteRatingActionPerformed(evt);
            }
        });
        deleteRatingRow.add(btnDeleteRating);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        internshipPanel.add(deleteRatingRow, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        leftColumn.add(internshipPanel, gridBagConstraints);

        adminTab.add(leftColumn);

        rightColumn.setLayout(new java.awt.BorderLayout());

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Search Student"));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchRadioRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        searchTypeGroup.add(rbSearchById);
        rbSearchById.setSelected(true);
        rbSearchById.setText("By Student ID");
        searchRadioRow.add(rbSearchById);

        searchTypeGroup.add(rbSearchByName);
        rbSearchByName.setText("By Name");
        searchRadioRow.add(rbSearchByName);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        searchPanel.add(searchRadioRow, gridBagConstraints);

        lblSearchValue.setText("Search Value:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        searchPanel.add(lblSearchValue, gridBagConstraints);

        tfSearch.setToolTipText("Press Enter to search (Ctrl+F jumps here)");
        tfSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        searchPanel.add(tfSearch, gridBagConstraints);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        searchButtonRow.add(btnSearch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        searchPanel.add(searchButtonRow, gridBagConstraints);

        rightColumn.add(searchPanel, java.awt.BorderLayout.NORTH);

        resultsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Results / Status"));
        resultsPanel.setLayout(new java.awt.BorderLayout());

        taResults.setColumns(20);
        taResults.setRows(5);
        taResults.setEditable(false);
        taResults.setBackground(new java.awt.Color(248, 250, 252));
        taResults.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        taResults.setLineWrap(true);
        taResults.setWrapStyleWord(true);
        resultsScroll.setViewportView(taResults);

        resultsPanel.add(resultsScroll, java.awt.BorderLayout.CENTER);

        rightColumn.add(resultsPanel, java.awt.BorderLayout.CENTER);

        adminTab.add(rightColumn);

        tabbedPane.addTab("Admin", adminTab);

        studentsTab.setLayout(new java.awt.BorderLayout());

        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Search & Filter"));
        filterPanel.setLayout(new java.awt.GridBagLayout());

        lblContains.setText("Contains:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(lblContains, gridBagConstraints);

        tfTableFilter.setToolTipText("Matches any column as you type");
        tfTableFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTableFilterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.34;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(tfTableFilter, gridBagConstraints);

        lblCourseFilter.setText("Course:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(lblCourseFilter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(cbFilterCourse, gridBagConstraints);

        lblCompanyFilter.setText("Company:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(lblCompanyFilter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(cbFilterCompany, gridBagConstraints);

        lblShow.setText("Show:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(lblShow, gridBagConstraints);

        cbFilterPreset.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All students", "No ratings yet", "Low performance (below 2.00)", "Top performers (3.50 and above)", "Needs attention" }));
        cbFilterPreset.setToolTipText("Ready-made filters for common admin questions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.67;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(cbFilterPreset, gridBagConstraints);

        btnResetFilters.setText("Reset Filters");
        btnResetFilters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetFiltersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        filterPanel.add(btnResetFilters, gridBagConstraints);

        studentsTab.add(filterPanel, java.awt.BorderLayout.NORTH);

        tablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "All Student Internship Records"));
        tablePanel.setLayout(new java.awt.BorderLayout());

        studentTable.setToolTipText("Click a row to load that student. Double-click to jump to the Admin tab.");
        studentTableScroll.setViewportView(studentTable);

        tablePanel.add(studentTableScroll, java.awt.BorderLayout.CENTER);

        studentsTab.add(tablePanel, java.awt.BorderLayout.CENTER);

        studentsBottomBar.setLayout(new java.awt.BorderLayout());

        lblResultCount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblResultCount.setForeground(new java.awt.Color(100, 116, 139));
        lblResultCount.setText("Showing 0 of 0 students");
        studentsBottomBar.add(lblResultCount, java.awt.BorderLayout.WEST);

        studentsActionRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnLoadSelected.setText("Load Selected Into Admin Tab");
        btnLoadSelected.setToolTipText("Open the highlighted student on the Admin tab");
        btnLoadSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadSelectedActionPerformed(evt);
            }
        });
        studentsActionRow.add(btnLoadSelected);

        studentsBottomBar.add(studentsActionRow, java.awt.BorderLayout.EAST);

        studentsTab.add(studentsBottomBar, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Students", studentsTab);

        alertsTab.setLayout(new java.awt.BorderLayout());

        alertsTopBar.setLayout(new java.awt.BorderLayout());

        lblAlertSummary.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAlertSummary.setText("Scanning...");
        alertsTopBar.add(lblAlertSummary, java.awt.BorderLayout.WEST);

        btnRescan.setText("Re-scan Records");
        btnRescan.setToolTipText("Run the data integrity checks again");
        btnRescan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRescanActionPerformed(evt);
            }
        });
        alertsTopBar.add(btnRescan, java.awt.BorderLayout.EAST);

        alertsTab.add(alertsTopBar, java.awt.BorderLayout.NORTH);

        alertListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Data Integrity & Attention Alerts"));
        alertListPanel.setLayout(new java.awt.BorderLayout());

        alertList.setToolTipText("Double-click an alert to open that student on the Admin tab");
        alertScroll.setViewportView(alertList);

        alertListPanel.add(alertScroll, java.awt.BorderLayout.CENTER);

        alertsTab.add(alertListPanel, java.awt.BorderLayout.CENTER);

        alertDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Alert Detail"));
        alertDetailPanel.setPreferredSize(new java.awt.Dimension(100, 140));
        alertDetailPanel.setLayout(new java.awt.BorderLayout());

        taAlertDetail.setEditable(false);
        taAlertDetail.setBackground(new java.awt.Color(248, 250, 252));
        taAlertDetail.setColumns(20);
        taAlertDetail.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        taAlertDetail.setLineWrap(true);
        taAlertDetail.setRows(4);
        taAlertDetail.setWrapStyleWord(true);
        alertDetailScroll.setViewportView(taAlertDetail);

        alertDetailPanel.add(alertDetailScroll, java.awt.BorderLayout.CENTER);

        alertsTab.add(alertDetailPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Alerts", alertsTab);

        activityTab.setLayout(new java.awt.BorderLayout());

        sessionStatsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Session Statistics"));
        sessionStatsPanel.setPreferredSize(new java.awt.Dimension(100, 60));
        sessionStatsPanel.setLayout(new java.awt.BorderLayout());

        lblSessionStats.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblSessionStats.setText(" ");
        sessionStatsPanel.add(lblSessionStats, java.awt.BorderLayout.CENTER);

        activityTab.add(sessionStatsPanel, java.awt.BorderLayout.NORTH);

        activityLogPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Admin Activity Log (newest first)"));
        activityLogPanel.setLayout(new java.awt.BorderLayout());

        activityList.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        activityScroll.setViewportView(activityList);

        activityLogPanel.add(activityScroll, java.awt.BorderLayout.CENTER);

        activityTab.add(activityLogPanel, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Activity", activityTab);

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

        bottomBar.setBackground(new java.awt.Color(241, 245, 249));
        bottomBar.setLayout(new java.awt.BorderLayout());

        statusHolder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblSelectedStudent.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSelectedStudent.setText("Selected student: none");
        statusHolder.add(lblSelectedStudent);

        lblSaveState.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSaveState.setText("No changes yet");
        statusHolder.add(lblSaveState);

        bottomBar.add(statusHolder, java.awt.BorderLayout.WEST);

        bottomButtonHolder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnUndo.setText("Undo Delete");
        btnUndo.setToolTipText("Restore the last deleted student or rating (Ctrl+Z)");
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });
        bottomButtonHolder.add(btnUndo);

        btnSaveNow.setText("Save Now");
        btnSaveNow.setToolTipText("Save without closing the Admin System (Ctrl+S)");
        btnSaveNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNowActionPerformed(evt);
            }
        });
        bottomButtonHolder.add(btnSaveNow);

        btnExitSave.setBackground(new java.awt.Color(239, 68, 68));
        btnExitSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExitSave.setForeground(new java.awt.Color(255, 255, 255));
        btnExitSave.setText("Exit & Save");
        btnExitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitSaveActionPerformed(evt);
            }
        });
        bottomButtonHolder.add(btnExitSave);

        bottomBar.add(bottomButtonHolder, java.awt.BorderLayout.EAST);

        getContentPane().add(bottomBar, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ================= Event handlers generated by the Form Editor =================
    // Each one simply calls the matching method in MY CODE below, so the real
    // logic is never buried inside a generated stub.
    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        handleCreateStudent();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        handleDeleteStudent();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFormActionPerformed
        handleClearForm();
    }//GEN-LAST:event_btnClearFormActionPerformed

    private void btnAddRatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRatingActionPerformed
        handleAddRating();
    }//GEN-LAST:event_btnAddRatingActionPerformed

    private void btnDeleteRatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteRatingActionPerformed
        handleDeleteRating();
    }//GEN-LAST:event_btnDeleteRatingActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        handleSearchStudent();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tfSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfSearchActionPerformed
        handleSearchStudent();
    }//GEN-LAST:event_tfSearchActionPerformed

    private void btnResetFiltersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetFiltersActionPerformed
        handleResetFilters();
    }//GEN-LAST:event_btnResetFiltersActionPerformed

    private void btnLoadSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadSelectedActionPerformed
        handleLoadSelectedFromTable();
    }//GEN-LAST:event_btnLoadSelectedActionPerformed

    private void btnRescanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRescanActionPerformed
        handleRescanAlerts();
    }//GEN-LAST:event_btnRescanActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        handleUndo();
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnSaveNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNowActionPerformed
        handleSaveNow();
    }//GEN-LAST:event_btnSaveNowActionPerformed

    private void btnExitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitSaveActionPerformed
        handleExitAndSave();
    }//GEN-LAST:event_btnExitSaveActionPerformed

    private void tfNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfNameActionPerformed

    private void tfTableFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTableFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTableFilterActionPerformed

    // =====================================================================
    // MY CODE — everything below is hand written
    // =====================================================================
    /**
     * Finishes off what the Form Editor cannot do: attaching the table models,
     * the sorter, the renderers and the listeners that are not simple button
     * clicks, plus the window sizing and padding.
     */
    @SuppressWarnings("unchecked")
    private void setupAfterInit() {

        // ----- window -----
        setSize(1140, 800);
        setMinimumSize(new Dimension(1060, 760));
        setLocationRelativeTo(null);

        // Clicking the X runs the same "Exit & Save" path so that CA2
        // Function 4 always happens before the Admin System terminates.
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                handleExitAndSave();
            }
        });

        // ----- styling the Form Editor does not set -----
        // One fixed width for every caption label so the input boxes line up
        // across all the panels.
        for (javax.swing.JLabel caption : new javax.swing.JLabel[]{lblStudentName,
            lblStudentIdCaption, lblCourseCaption, lblCompanyCaption, lblRoleCaption,
            lblAllowanceCaption, lblWeeksCaption, lblMarksCaption, lblSearchValue}) {
            caption.setPreferredSize(new Dimension(FORM_LABEL_WIDTH, caption.getPreferredSize().height));
        }

        // Layout gaps, opacity and row heights are applied here rather than in
        // the .form so the Form Editor only holds plain component properties.
        headerTextPanel.setOpaque(false);
        statusHolder.setOpaque(false);
        bottomButtonHolder.setOpaque(false);

        ratingTable.setRowHeight(20);
        studentTable.setRowHeight(22);

        ((java.awt.BorderLayout) rightColumn.getLayout()).setHgap(10);
        ((java.awt.BorderLayout) rightColumn.getLayout()).setVgap(10);
        for (javax.swing.JPanel tab : new javax.swing.JPanel[]{studentsTab, alertsTab, activityTab}) {
            ((java.awt.BorderLayout) tab.getLayout()).setHgap(10);
            ((java.awt.BorderLayout) tab.getLayout()).setVgap(10);
        }
        for (javax.swing.JPanel row : new javax.swing.JPanel[]{studentButtonRow, addRatingRow,
            deleteRatingRow, searchRadioRow, studentsActionRow}) {
            ((java.awt.FlowLayout) row.getLayout()).setHgap(8);
            ((java.awt.FlowLayout) row.getLayout()).setVgap(2);
        }
        ((java.awt.FlowLayout) statusHolder.getLayout()).setHgap(0);
        ((java.awt.FlowLayout) statusHolder.getLayout()).setVgap(4);
        ((java.awt.FlowLayout) bottomButtonHolder.getLayout()).setHgap(10);
        ((java.awt.FlowLayout) bottomButtonHolder.getLayout()).setVgap(4);

        // ----- padding the Form Editor does not set -----
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        adminTab.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        studentsTab.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        alertsTab.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        activityTab.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        alertsTopBar.setBorder(BorderFactory.createEmptyBorder(0, 4, 6, 4));
        bottomBar.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 12));
        lblSaveState.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));
        taResults.setMargin(new java.awt.Insets(6, 8, 6, 8));
        taAlertDetail.setMargin(new java.awt.Insets(6, 8, 6, 8));

        tabbedPane.setToolTipTextAt(TAB_ADMIN, "Create, search, rate and delete student internship records");
        tabbedPane.setToolTipTextAt(TAB_STUDENTS, "Browse, sort and filter every student record");
        tabbedPane.setToolTipTextAt(TAB_ALERTS, "Data problems and students who need attention");
        tabbedPane.setToolTipTextAt(3, "Everything changed during this session");

        // ----- student table: model, sorter, renderer -----
        studentTableModel = new StudentTableModel(manager);
        studentTable.setModel(studentTableModel);

        studentSorter = new TableRowSorter<>(studentTableModel);
        studentTable.setRowSorter(studentSorter);
        studentTable.setAutoCreateRowSorter(false);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        StudentHealthRenderer healthRenderer = new StudentHealthRenderer();
        studentTable.setDefaultRenderer(Object.class, healthRenderer);
        studentTable.setDefaultRenderer(Double.class, healthRenderer);
        studentTable.setDefaultRenderer(Integer.class, healthRenderer);

        // ----- rating table -----
        ratingTableModel = new RatingTableModel();
        ratingTable.setModel(ratingTableModel);
        ratingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ratingTable.getTableHeader().setReorderingAllowed(false);

        // ----- lists -----
        alertListModel = new DefaultListModel<>();
        alertList.setModel(alertListModel);
        alertList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alertList.setCellRenderer(new AlertCellRenderer());
        alertList.setFixedCellHeight(26);

        activityListModel = new DefaultListModel<>();
        activityList.setModel(activityListModel);

        // ----- listeners that are not plain button clicks -----
        tfTableFilter.getDocument().addDocumentListener(new SimpleDocumentListener(this::applyRowFilter));
        cbFilterCourse.addActionListener(e -> applyRowFilter());
        cbFilterCompany.addActionListener(e -> applyRowFilter());
        cbFilterPreset.addActionListener(e -> applyRowFilter());

        tfWeeks.getDocument().addDocumentListener(new SimpleDocumentListener(this::updateRatingImpactPreview));
        tfMarks.getDocument().addDocumentListener(new SimpleDocumentListener(this::updateRatingImpactPreview));

        cbCompany.addActionListener(e -> updateCompanyDetails());
        Component editor = cbCompany.getEditor().getEditorComponent();
        if (editor instanceof javax.swing.JTextField) {
            ((javax.swing.JTextField) editor).getDocument()
                    .addDocumentListener(new SimpleDocumentListener(this::updateCompanyDetails));
        }

        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || isSyncingTableSelection) {
                return;
            }
            handleTableRowSelected();
        });
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleLoadSelectedFromTable();
                }
            }
        });

        alertList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedAlertDetail();
            }
        });
        alertList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openStudentFromSelectedAlert();
                }
            }
        });

        installKeyboardShortcuts();
    }

    /**
     * ADVANCED FEATURE: keyboard shortcuts. WHEN_IN_FOCUSED_WINDOW means they
     * work no matter which control currently has focus.
     */
    private void installKeyboardShortcuts() {
        JComponent root = getRootPane();
        javax.swing.InputMap inputMap = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        javax.swing.ActionMap actionMap = root.getActionMap();

        bindShortcut(inputMap, actionMap,
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "saveNow", this::handleSaveNow);
        bindShortcut(inputMap, actionMap,
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo", this::handleUndo);
        bindShortcut(inputMap, actionMap,
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "focusSearch", this::focusSearchField);
        bindShortcut(inputMap, actionMap,
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "newRecord", this::startNewRecord);
        bindShortcut(inputMap, actionMap,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clearForm", this::handleClearForm);
    }

    /** Helper so each shortcut is one readable line above. */
    private void bindShortcut(javax.swing.InputMap inputMap, javax.swing.ActionMap actionMap,
            KeyStroke keyStroke, String name, Runnable action) {

        inputMap.put(keyStroke, name);
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    /**
     * DocumentListener normally forces you to write the same code three times
     * (insert / remove / change). This adapter runs one action for all three.
     */
    private static class SimpleDocumentListener implements DocumentListener {

        private final Runnable action;

        SimpleDocumentListener(Runnable action) {
            this.action = action;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            action.run();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            action.run();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            action.run();
        }
    }

    // =====================================================================
    // FUNCTION 1 — Create Student Internship Record
    // =====================================================================
    /**
     * Validates every field, checks the company exists, then adds the new
     * Student to the shared collection and selects it so ratings can be
     * added straight away.
     */
    private void handleCreateStudent() {
        String name = tfName.getText().trim();
        String studentId = tfStudentId.getText().trim();
        String course = tfCourse.getText().trim();
        String companyName = getCompanyInput();
        String role = tfRole.getText().trim();
        String allowanceText = tfAllowance.getText().trim();

        // 1. All fields are compulsory.
        if (name.isEmpty() || studentId.isEmpty() || course.isEmpty()
                || companyName.isEmpty() || role.isEmpty() || allowanceText.isEmpty()) {
            showStatus("[ERROR] Cannot create record.\n\n"
                    + "All fields are required: Student Name, Student ID, Course,\n"
                    + "Company Name, Internship Role and Monthly Allowance.");
            return;
        }

        // 2. Student IDs must be unique.
        if (manager.findStudentById(studentId) != null) {
            showStatus("[ERROR] Cannot create record.\n\n"
                    + "A student with ID \"" + studentId + "\" already exists.\n"
                    + "Please use a different Student ID.");
            return;
        }

        // 3. Allowance must be a number that is not negative.
        double allowance;
        try {
            allowance = Double.parseDouble(allowanceText);
        } catch (NumberFormatException e) {
            showStatus("[ERROR] Cannot create record.\n\n"
                    + "Monthly Allowance must be a number, for example 1200 or 1200.50.\n"
                    + "You entered: \"" + allowanceText + "\"");
            return;
        }
        if (allowance < 0) {
            showStatus("[ERROR] Cannot create record.\n\n"
                    + "Monthly Allowance cannot be negative.");
            return;
        }

        // 4. The company must already exist in the Company collection.
        Company company = manager.findCompanyByName(companyName);
        if (company == null) {
            showStatus("[ERROR] Cannot create record.\n\n"
                    + "Company \"" + companyName + "\" does not exist in the system.\n"
                    + "The student was NOT created.\n\n"
                    + buildCompanyList());
            return;
        }

        // 5. Everything is valid — add to the SAME list Member A reads from.
        Student newStudent = new Student(studentId, name, course, company, role, allowance);
        manager.addStudent(newStudent);

        activityLog.record(ActivityLog.ACTION_CREATE, studentId + " - " + name + " at " + company.getCompanyName());
        markUnsaved();

        // Refresh BEFORE selecting: the table has just gained a row, and the
        // row sorter has to know about it before that row can be highlighted.
        refreshAllViews();
        setSelectedStudent(newStudent);

        showStatus("[SUCCESS] Student internship record created.\n\n"
                + buildStudentSummary(newStudent)
                + "\nThis student is now selected.\n"
                + "Add ratings using Weeks Assessed and Marks.");
    }

    // =====================================================================
    // FUNCTION 2 — Search, then Add Performance Rating
    // =====================================================================
    /**
     * Searches by Student ID or by Name depending on the selected radio
     * button. A successful search selects the student and loads their
     * details; a failed search clears the panels and the selection.
     */
    private void handleSearchStudent() {
        String query = tfSearch.getText().trim();
        boolean searchById = rbSearchById.isSelected();
        String searchType = searchById ? "Student ID" : "Student Name";

        if (query.isEmpty()) {
            clearSelectedStudent();
            showStatus("[ERROR] Search failed.\n\n"
                    + "Please enter a " + searchType + " to search for.");
            return;
        }

        // The lookup itself lives in InternshipManagement and is
        // case-insensitive, so it is not repeated here.
        Student found = searchById
                ? manager.findStudentById(query)
                : manager.findStudentByName(query);

        if (found == null) {
            // The assignment requires the panels to be cleared on a failed
            // search, and clearing the selection also stops Delete / Add
            // Rating from acting on the previous student by accident.
            clearSelectedStudent();
            showStatus("[NOT FOUND] No such student found.\n\n"
                    + "No record matches the " + searchType + " \"" + query + "\".\n"
                    + "The student and internship panels have been cleared.");
            return;
        }

        setSelectedStudent(found);
        showStatus("[FOUND] Student record loaded.\n\n"
                + buildStudentSummary(found)
                + "\nThis student is now selected.\n"
                + "You can add a rating or delete this record.");
    }

    /**
     * Adds one performance rating to the currently selected student.
     */
    private void handleAddRating() {
        // A rating can never be added when nothing is selected.
        if (selectedStudent == null) {
            showStatus("[ERROR] Cannot add rating.\n\n"
                    + "No student is currently selected.\n"
                    + "Create a student, or search for one first.");
            return;
        }

        String weeksText = tfWeeks.getText().trim();
        String marksText = tfMarks.getText().trim();

        if (weeksText.isEmpty() || marksText.isEmpty()) {
            showStatus("[ERROR] Cannot add rating.\n\n"
                    + "Both Weeks Assessed and Marks are required.");
            return;
        }

        // Weeks must be a whole number greater than 0.
        int weeks;
        try {
            weeks = Integer.parseInt(weeksText);
        } catch (NumberFormatException e) {
            showStatus("[ERROR] Cannot add rating.\n\n"
                    + "Weeks Assessed must be a whole number, for example 4.\n"
                    + "You entered: \"" + weeksText + "\"");
            return;
        }
        if (weeks <= 0) {
            showStatus("[ERROR] Cannot add rating.\n\n"
                    + "Weeks Assessed must be greater than 0.");
            return;
        }

        // Marks must be a number from 0 to 100.
        double marks;
        try {
            marks = Double.parseDouble(marksText);
        } catch (NumberFormatException e) {
            showStatus("[ERROR] Cannot add rating.\n\n"
                    + "Marks must be a number, for example 75 or 75.5.\n"
                    + "You entered: \"" + marksText + "\"");
            return;
        }
        if (marks < 0 || marks > 100) {
            showStatus("[ERROR] Cannot add rating.\n\n"
                    + "Marks must be between 0 and 100.");
            return;
        }

        double scoreBefore = selectedStudent.calculateOverallScore();
        selectedStudent.addRating(weeks, marks);
        double scoreAfter = selectedStudent.calculateOverallScore();

        activityLog.record(ActivityLog.ACTION_RATING,
                selectedStudent.getStudentId() + " - " + weeks + " week(s) at "
                + String.format("%.1f", marks) + " marks");
        markUnsaved();

        // Keep the same student selected and empty the two rating boxes so
        // the next rating period can be typed in immediately.
        clearRatingInputFields();
        refreshAllViews();

        showStatus("[SUCCESS] Performance rating added.\n\n"
                + "Added " + weeks + " week(s) at " + String.format("%.1f", marks) + " marks.\n"
                + String.format("Overall score moved from %.2f to %.2f.%n%n", scoreBefore, scoreAfter)
                + buildStudentSummary(selectedStudent)
                + "\n" + selectedStudent.getName() + " is still selected.");
    }

    // =====================================================================
    // FUNCTION 3 — Delete Student Internship Record
    // =====================================================================
    /**
     * Deletes the currently selected student after a confirmation, then
     * clears every panel. The record is pushed onto the undo stack first.
     */
    private void handleDeleteStudent() {
        if (selectedStudent == null) {
            showStatus("[ERROR] Cannot delete.\n\n"
                    + "No student is currently selected.\n"
                    + "Search for the student you want to delete first.");
            return;
        }

        Student toDelete = selectedStudent;
        String deletedName = toDelete.getName();
        String deletedId = toDelete.getStudentId();

        // Showing the full record in the confirmation makes an accidental
        // delete much less likely.
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this internship record?\n\n"
                + "Name    : " + deletedName + "\n"
                + "ID      : " + deletedId + "\n"
                + "Course  : " + toDelete.getCourseOfStudy() + "\n"
                + "Company : " + (toDelete.getAssignedCompany() == null
                        ? "-" : toDelete.getAssignedCompany().getCompanyName()) + "\n"
                + "Ratings : " + toDelete.getRatings().size() + "\n\n"
                + "This can be reversed with the Undo Delete button.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            showStatus("[CANCELLED] Delete cancelled.\n\n"
                    + deletedName + " (" + deletedId + ") is still in the system\n"
                    + "and is still selected.");
            return;
        }

        // Remember where it was so Undo can put it back in the same place.
        int originalIndex = manager.getStudents().indexOf(toDelete);
        undoStack.push(DeletedRecord.forStudent(toDelete, originalIndex));

        manager.removeStudent(toDelete);

        activityLog.record(ActivityLog.ACTION_DELETE, deletedId + " - " + deletedName);
        markUnsaved();

        clearSelectedStudent();
        refreshAllViews();

        showStatus("[SUCCESS] Student internship record deleted.\n\n"
                + deletedName + " (" + deletedId + ") has been removed from the system.\n"
                + "There are now " + manager.getStudents().size() + " student record(s).\n\n"
                + "Click Undo Delete if this was a mistake.");
    }

    // =====================================================================
    // FUNCTION 4 — Exit and Save to an object serialized file
    // =====================================================================
    /**
     * Saves all current Student and Company data to the object serialized
     * file through FileHandler, and only then closes the Admin System. If
     * the save fails the window stays open and the error is reported.
     */
    private void handleExitAndSave() {
        SaveResult result = performSave();

        if (!result.successful) {
            // Saving failed, so the Admin System deliberately stays open.
            showStatus("[ERROR] Could not save the data file.\n\n"
                    + "Reason: " + result.message + "\n\n"
                    + "The Admin System has NOT been closed,\n"
                    + "so that no data is lost.");
            JOptionPane.showMessageDialog(this,
                    "Failed to save " + FileHandler.SERIALIZED_FILE + ".\n\n"
                    + result.message + "\n\nThe Admin System will stay open.",
                    "Save Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "All data has been saved to the object serialized file.\n\n"
                + result.message + "\n"
                + "Returning to the Master Gateway menu.",
                "Data Saved", JOptionPane.INFORMATION_MESSAGE);

        // dispose() closes only this window, so InternshipUser can show the
        // master menu again instead of the whole application terminating.
        dispose();
    }

    /**
     * ADVANCED FEATURE: save without leaving the Admin System, so a long
     * editing session is never at risk. Exit & Save still works exactly as
     * the assignment requires.
     */
    private void handleSaveNow() {
        SaveResult result = performSave();

        if (result.successful) {
            showStatus("[SAVED] Data written to the object serialized file.\n\n" + result.message);
        } else {
            showStatus("[ERROR] Could not save the data file.\n\n"
                    + "Reason: " + result.message + "\n\n"
                    + "Nothing has been lost - the records are still in memory.\n"
                    + "Fix the problem and try Save Now again.");
        }
    }

    /** Small carrier for the outcome of a save attempt. */
    private static class SaveResult {

        final boolean successful;
        final String message;

        SaveResult(boolean successful, String message) {
            this.successful = successful;
            this.message = message;
        }
    }

    /**
     * The one place that writes the object file. Takes a timestamped backup
     * first, writes the data, then reads it straight back to prove the file
     * really contains what it should.
     */
    private SaveResult performSave() {
        String backupNote = "";

        // ADVANCED FEATURE: back up the previous save before overwriting it.
        // A failed backup must not stop the save itself.
        try {
            String backupPath = FileHandler.createBackup(FileHandler.SERIALIZED_FILE);
            if (backupPath != null) {
                backupNote = "Backup   : " + new File(backupPath).getName() + "\n";
            }
        } catch (IOException e) {
            backupNote = "Backup   : could not be created (" + e.getMessage() + ")\n";
        }

        try {
            FileHandler.saveSerializedData(FileHandler.SERIALIZED_FILE,
                    manager.getCompanies(), manager.getStudents());
        } catch (IOException e) {
            return new SaveResult(false, e.getMessage());
        }

        // ADVANCED FEATURE: verification. Read the file back and confirm the
        // counts match what is in memory.
        int expectedStudents = manager.getStudents().size();
        int expectedCompanies = manager.getCompanies().size();
        try {
            FileHandler.SystemData reloaded = FileHandler.readSerializedData(FileHandler.SERIALIZED_FILE);

            if (reloaded.students.size() != expectedStudents
                    || reloaded.companies.size() != expectedCompanies) {
                return new SaveResult(false,
                        "the saved file did not match memory when it was read back");
            }
        } catch (IOException | ClassNotFoundException e) {
            return new SaveResult(false, "the file was written but could not be verified: " + e.getMessage());
        }

        lastSavedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        hasUnsavedChanges = false;

        activityLog.record(ActivityLog.ACTION_SAVE,
                expectedStudents + " student(s), " + expectedCompanies + " company(s) saved and verified");

        refreshActivityView();
        updateSaveState();

        String savedPath = new File(FileHandler.SERIALIZED_FILE).getAbsolutePath();
        return new SaveResult(true,
                "File     : " + savedPath + "\n"
                + backupNote
                + "Companies: " + expectedCompanies + "\n"
                + "Students : " + expectedStudents + "\n"
                + "Verified : read back from disk, counts match\n");
    }

    // =====================================================================
    // ADVANCED FEATURE — delete an individual rating (with undo)
    // =====================================================================
    private void handleDeleteRating() {
        if (selectedStudent == null) {
            showStatus("[ERROR] Cannot delete a rating.\n\n"
                    + "No student is currently selected.");
            return;
        }

        int viewRow = ratingTable.getSelectedRow();
        if (viewRow < 0) {
            showStatus("[ERROR] Cannot delete a rating.\n\n"
                    + "Select a row in the Rating History table first.");
            return;
        }

        int modelRow = ratingTable.convertRowIndexToModel(viewRow);
        Student.PerformanceRating rating = ratingTableModel.getRatingAt(modelRow);
        if (rating == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove rating period " + (modelRow + 1) + " from "
                + selectedStudent.getName() + "?\n\n"
                + rating.getWeeks() + " week(s) at " + String.format("%.1f", rating.getMarks()) + " marks",
                "Confirm Delete Rating", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            showStatus("[CANCELLED] The rating was not removed.");
            return;
        }

        double scoreBefore = selectedStudent.calculateOverallScore();

        // getRatings() hands back the live list, so removing from it really
        // removes the rating from the Student object.
        undoStack.push(DeletedRecord.forRating(selectedStudent, rating, modelRow));
        selectedStudent.getRatings().remove(modelRow);

        activityLog.recordRatingDeleted(selectedStudent.getStudentId()
                + " - removed rating period " + (modelRow + 1));
        markUnsaved();

        refreshAllViews();

        showStatus("[SUCCESS] Rating period " + (modelRow + 1) + " removed.\n\n"
                + String.format("Overall score moved from %.2f to %.2f.%n%n",
                        scoreBefore, selectedStudent.calculateOverallScore())
                + buildStudentSummary(selectedStudent));
    }

    // =====================================================================
    // ADVANCED FEATURE — undo the last deletion
    // =====================================================================
    /**
     * Pops the most recent deletion off the stack and puts it back where it
     * came from. Only deletions are undoable, which keeps this to one short
     * method instead of a whole undo framework.
     */
    private void handleUndo() {
        if (undoStack.isEmpty()) {
            showStatus("[NOTHING TO UNDO]\n\n"
                    + "No student or rating has been deleted in this session yet.");
            return;
        }

        DeletedRecord record = undoStack.pop();

        if (record.getKind() == DeletedRecord.Kind.STUDENT) {
            manager.addStudentAt(record.getIndex(), record.getStudent());
        } else {
            ArrayList<Student.PerformanceRating> ratings = record.getStudent().getRatings();

            int index = record.getIndex();
            if (index > ratings.size()) {
                index = ratings.size();
            }
            ratings.add(index, record.getRating());
        }

        activityLog.record(ActivityLog.ACTION_UNDO, "restored " + record.describe());
        markUnsaved();

        // Same ordering as Create: let the table learn about the restored row
        // first, then highlight it.
        refreshAllViews();
        setSelectedStudent(record.getStudent());

        showStatus("[UNDONE] Restored the " + record.describe() + ".\n\n"
                + buildStudentSummary(record.getStudent())
                + (undoStack.isEmpty()
                        ? "\nThere is nothing further to undo."
                        : "\n" + undoStack.size() + " earlier deletion(s) can still be undone."));
    }

    // =====================================================================
    // ADVANCED FEATURE — table selection, filtering and sorting
    // =====================================================================
    /** Loads whichever student the admin clicked in the table. */
    private void handleTableRowSelected() {
        int viewRow = studentTable.getSelectedRow();
        if (viewRow < 0) {
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(viewRow);
        Student student = studentTableModel.getStudentAt(modelRow);

        if (student != null && student != selectedStudent) {
            setSelectedStudent(student);
            showStatus("[SELECTED] Loaded from the student table.\n\n"
                    + buildStudentSummary(student)
                    + "\nSwitch to the Admin tab to edit, rate or delete this record.");
        }
    }

    /** Explicit button / double-click: load the row and show the Admin tab. */
    private void handleLoadSelectedFromTable() {
        int viewRow = studentTable.getSelectedRow();
        if (viewRow < 0) {
            showStatus("[ERROR] No table row selected.\n\n"
                    + "Click a student in the table first.");
            return;
        }
        handleTableRowSelected();
        tabbedPane.setSelectedIndex(TAB_ADMIN);
    }

    /**
     * Combines the text box and the three drop-downs into one RowFilter. The
     * sorter then hides any row that fails ANY of the active filters.
     */
    private void applyRowFilter() {
        if (isRebuildingFilters || studentSorter == null) {
            return;
        }

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        // Free text: matches any column, case-insensitive. Pattern.quote stops
        // characters such as "/" in a course code being read as regex.
        String text = tfTableFilter.getText().trim();
        if (!text.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        }

        String course = (String) cbFilterCourse.getSelectedItem();
        if (course != null && !ANY_COURSE.equals(course)) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(course) + "$",
                    StudentTableModel.COL_COURSE));
        }

        String company = (String) cbFilterCompany.getSelectedItem();
        if (company != null && !ANY_COMPANY.equals(company)) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(company) + "$",
                    StudentTableModel.COL_COMPANY));
        }

        String preset = (String) cbFilterPreset.getSelectedItem();
        if (preset != null && !PRESET_ALL.equals(preset)) {
            filters.add(buildPresetFilter(preset));
        }

        studentSorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
        updateResultCount();
    }

    /**
     * The ready-made filters. Instead of reading the text out of table cells,
     * this asks the model for the actual Student and uses the real business
     * logic, which keeps the rules in one language.
     */
    private RowFilter<Object, Object> buildPresetFilter(final String preset) {
        return new RowFilter<Object, Object>() {
            @Override
            public boolean include(Entry<?, ?> entry) {
                int modelRow = (Integer) entry.getIdentifier();
                Student student = studentTableModel.getStudentAt(modelRow);
                if (student == null) {
                    return false;
                }

                switch (preset) {
                    case PRESET_NO_RATINGS:
                        return student.getRatings().isEmpty();
                    case PRESET_LOW:
                        return !student.getRatings().isEmpty()
                                && student.calculateOverallScore() < 2.00;
                    case PRESET_TOP:
                        return student.calculateOverallScore() >= 3.50;
                    case PRESET_ATTENTION:
                        DataIntegrityChecker.Severity worst = worstSeverityByStudent.get(student);
                        return worst == DataIntegrityChecker.Severity.CRITICAL
                                || worst == DataIntegrityChecker.Severity.WARNING;
                    default:
                        return true;
                }
            }
        };
    }

    private void handleResetFilters() {
        isRebuildingFilters = true;
        tfTableFilter.setText("");
        cbFilterCourse.setSelectedItem(ANY_COURSE);
        cbFilterCompany.setSelectedItem(ANY_COMPANY);
        cbFilterPreset.setSelectedItem(PRESET_ALL);
        isRebuildingFilters = false;

        applyRowFilter();
        showStatus("Filters reset. Showing all " + manager.getStudents().size() + " student record(s).");
    }

    /** ADVANCED FEATURE: "Showing 8 of 47 students". */
    private void updateResultCount() {
        int shown = studentTable.getRowCount();
        int total = studentTableModel.getRowCount();

        lblResultCount.setText("Showing " + shown + " of " + total + " students");
        lblResultCount.setForeground(shown == total ? TEXT_MUTED : ACCENT_BLUE);
    }

    /**
     * Rebuilds the course and company drop-downs from the current data,
     * keeping the admin's choice if it is still available.
     */
    private void rebuildFilterCombos() {
        isRebuildingFilters = true;

        Object previousCourse = cbFilterCourse.getSelectedItem();
        Object previousCompany = cbFilterCompany.getSelectedItem();

        cbFilterCourse.removeAllItems();
        cbFilterCourse.addItem(ANY_COURSE);
        ArrayList<String> courses = new ArrayList<>();
        for (Student s : manager.getStudents()) {
            if (!courses.contains(s.getCourseOfStudy())) {
                courses.add(s.getCourseOfStudy());
            }
        }
        java.util.Collections.sort(courses);
        for (String course : courses) {
            cbFilterCourse.addItem(course);
        }

        cbFilterCompany.removeAllItems();
        cbFilterCompany.addItem(ANY_COMPANY);
        for (Company c : manager.getCompanies()) {
            cbFilterCompany.addItem(c.getCompanyName());
        }

        // Restore the previous choice; if it has gone, the combo stays on the
        // "All ..." entry that was added first.
        if (previousCourse != null) {
            cbFilterCourse.setSelectedItem(previousCourse);
        }
        if (previousCompany != null) {
            cbFilterCompany.setSelectedItem(previousCompany);
        }

        isRebuildingFilters = false;
    }

    // =====================================================================
    // ADVANCED FEATURE — company combo box details
    // =====================================================================
    private void populateCompanyCombo() {
        String current = getCompanyInput();

        isRebuildingFilters = true;
        cbCompany.removeAllItems();
        cbCompany.addItem("");
        for (Company c : manager.getCompanies()) {
            cbCompany.addItem(c.getCompanyName());
        }
        isRebuildingFilters = false;

        setCompanyInput(current);
    }

    /** Reads the editor, so both picked and typed values are handled. */
    private String getCompanyInput() {
        if (cbCompany == null) {
            return "";
        }
        Object item = cbCompany.getEditor().getItem();
        return (item == null) ? "" : item.toString().trim();
    }

    private void setCompanyInput(String value) {
        cbCompany.getEditor().setItem(value == null ? "" : value);
    }

    /**
     * Shows the contact person and standard allowance for the chosen company,
     * and offers the company's rate when the allowance box is still empty.
     */
    private void updateCompanyDetails() {
        String typed = getCompanyInput();

        if (typed.isEmpty()) {
            lblCompanyDetails.setText(" ");
            lblCompanyDetails.setForeground(TEXT_MUTED);
            return;
        }

        Company company = manager.findCompanyByName(typed);

        if (company == null) {
            lblCompanyDetails.setText("Not an existing company - Create will reject this");
            lblCompanyDetails.setForeground(TEXT_BAD);
            return;
        }

        lblCompanyDetails.setText(String.format("Contact: %s   |   Standard allowance: $%.2f",
                company.getContactPerson(), company.getMonthlyAllowance()));
        lblCompanyDetails.setForeground(TEXT_OK);

        // Only fill in a blank allowance; never overwrite something typed.
        if (tfAllowance != null && tfAllowance.getText().trim().isEmpty()) {
            tfAllowance.setText(String.format("%.2f", company.getMonthlyAllowance()));
        }
    }

    // =====================================================================
    // ADVANCED FEATURE — live rating impact preview
    // =====================================================================
    /**
     * Works out what the overall score WOULD become if the rating currently
     * typed in were added, without changing the Student object at all.
     */
    private void updateRatingImpactPreview() {
        if (lblRatingImpact == null) {
            return;
        }

        if (selectedStudent == null) {
            lblRatingImpact.setText(" ");
            lblRatingImpact.setForeground(TEXT_MUTED);
            return;
        }

        String weeksText = tfWeeks.getText().trim();
        String marksText = tfMarks.getText().trim();

        if (weeksText.isEmpty() || marksText.isEmpty()) {
            lblRatingImpact.setText(String.format("Current overall score: %.2f / 4.00",
                    selectedStudent.calculateOverallScore()));
            lblRatingImpact.setForeground(TEXT_MUTED);
            return;
        }

        int weeks;
        double marks;
        try {
            weeks = Integer.parseInt(weeksText);
            marks = Double.parseDouble(marksText);
        } catch (NumberFormatException e) {
            lblRatingImpact.setText("Enter a whole number of weeks and marks from 0 to 100");
            lblRatingImpact.setForeground(TEXT_MUTED);
            return;
        }

        if (weeks <= 0 || marks < 0 || marks > 100) {
            lblRatingImpact.setText("Weeks must be above 0 and marks must be 0 to 100");
            lblRatingImpact.setForeground(TEXT_MUTED);
            return;
        }

        double current = selectedStudent.calculateOverallScore();
        double projected = projectedOverallScore(selectedStudent, weeks, marks);

        String direction;
        if (projected > current + 0.005) {
            direction = "improving";
            lblRatingImpact.setForeground(TEXT_OK);
        } else if (projected < current - 0.005) {
            direction = "declining";
            lblRatingImpact.setForeground(TEXT_BAD);
        } else {
            direction = "no change";
            lblRatingImpact.setForeground(TEXT_MUTED);
        }

        lblRatingImpact.setText(String.format("Overall score %.2f -> %.2f  (%s)", current, projected, direction));
    }

    /**
     * The CA1 weighted formula applied to the existing ratings plus one
     * hypothetical extra period. A throwaway PerformanceRating is created just
     * to reuse its getGradePoint(), so the mark bands are not duplicated here.
     */
    private double projectedOverallScore(Student student, int weeks, double marks) {
        Student.PerformanceRating candidate = new Student.PerformanceRating(weeks, marks);

        double weightedPoints = candidate.getGradePoint() * (double) weeks;
        int totalWeeks = weeks;

        for (Student.PerformanceRating r : student.getRatings()) {
            weightedPoints += r.getGradePoint() * (double) r.getWeeks();
            totalWeeks += r.getWeeks();
        }

        return (totalWeeks == 0) ? 0.0 : weightedPoints / totalWeeks;
    }

    // =====================================================================
    // ADVANCED FEATURE — alerts
    // =====================================================================
    private void handleRescanAlerts() {
        refreshAlerts();
        tabbedPane.setSelectedIndex(TAB_ALERTS);
        showStatus("[SCAN COMPLETE] " + describeAlertCounts());
    }

    /** Re-runs the integrity rules and refills the alert list. */
    private void refreshAlerts() {
        currentAlerts = DataIntegrityChecker.checkAll(manager);

        // Cache the worst severity per student so the table renderer does not
        // have to search the whole alert list for every cell it draws.
        worstSeverityByStudent.clear();
        for (DataIntegrityChecker.Alert alert : currentAlerts) {
            Student s = alert.getStudent();
            if (s == null) {
                continue;
            }
            DataIntegrityChecker.Severity existing = worstSeverityByStudent.get(s);
            if (existing == null || alert.getSeverity().ordinal() < existing.ordinal()) {
                worstSeverityByStudent.put(s, alert.getSeverity());
            }
        }

        alertListModel.clear();
        for (DataIntegrityChecker.Alert alert : currentAlerts) {
            alertListModel.addElement(alert);
        }

        lblAlertSummary.setText(describeAlertCounts());
        lblAlertSummary.setForeground(currentAlerts.isEmpty() ? TEXT_OK : TEXT_BAD);

        if (currentAlerts.isEmpty()) {
            taAlertDetail.setText("No problems found. Every student record has a valid company,\n"
                    + "a unique ID, recorded ratings and a reasonable score.");
        } else {
            taAlertDetail.setText("Select an alert above to see the details.\n"
                    + "Double-click an alert to open that student on the Admin tab.");
        }
    }

    private String describeAlertCounts() {
        if (currentAlerts.isEmpty()) {
            return "No data problems found";
        }

        int critical = 0;
        int warning = 0;
        int info = 0;
        for (DataIntegrityChecker.Alert a : currentAlerts) {
            switch (a.getSeverity()) {
                case CRITICAL:
                    critical++;
                    break;
                case WARNING:
                    warning++;
                    break;
                default:
                    info++;
                    break;
            }
        }

        return currentAlerts.size() + " issue(s) found:  "
                + critical + " critical,  " + warning + " warning,  " + info + " info";
    }

    /** The list is raw, so the selected value is cast back to an Alert. */
    private DataIntegrityChecker.Alert getSelectedAlert() {
        Object value = alertList.getSelectedValue();
        return (value instanceof DataIntegrityChecker.Alert)
                ? (DataIntegrityChecker.Alert) value : null;
    }

    private void showSelectedAlertDetail() {
        DataIntegrityChecker.Alert alert = getSelectedAlert();
        if (alert == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Severity : ").append(alert.getSeverity().getLabel()).append("\n");
        if (alert.getStudent() != null) {
            sb.append("Student  : ").append(alert.getStudent().getName())
                    .append(" (").append(alert.getStudent().getStudentId()).append(")\n");
        }
        sb.append("Issue    : ").append(alert.getIssue()).append("\n\n");
        sb.append(alert.getDetail());

        taAlertDetail.setText(sb.toString());
        taAlertDetail.setCaretPosition(0);
    }

    private void openStudentFromSelectedAlert() {
        DataIntegrityChecker.Alert alert = getSelectedAlert();
        if (alert == null || alert.getStudent() == null) {
            return;
        }

        setSelectedStudent(alert.getStudent());
        tabbedPane.setSelectedIndex(TAB_ADMIN);
        showStatus("[FROM ALERT] " + alert.getIssue() + "\n\n"
                + alert.getDetail() + "\n\n"
                + buildStudentSummary(alert.getStudent()));
    }

    // =====================================================================
    // Selection handling and display
    // =====================================================================
    /**
     * Makes a student the current selection and loads their details into the
     * Student Details and Internship panels.
     */
    private void setSelectedStudent(Student student) {
        this.selectedStudent = student;

        tfName.setText(student.getName());
        tfStudentId.setText(student.getStudentId());
        tfCourse.setText(student.getCourseOfStudy());
        setCompanyInput(student.getAssignedCompany() == null
                ? "" : student.getAssignedCompany().getCompanyName());
        tfRole.setText(student.getInternshipRole());
        tfAllowance.setText(String.format("%.2f", student.getMonthlyAllowance()));

        clearRatingInputFields();
        ratingTableModel.setStudent(student);

        updateSelectionControls();
        updateCompanyDetails();
        updateRatingImpactPreview();
        syncTableSelection();
    }

    /**
     * Forgets the current selection and clears every display field, as the
     * assignment requires after an unsuccessful search or after a delete.
     */
    private void clearSelectedStudent() {
        this.selectedStudent = null;
        clearStudentAndInternshipFields();
        clearRatingInputFields();
        ratingTableModel.setStudent(null);

        updateSelectionControls();
        updateRatingImpactPreview();
        syncTableSelection();
    }

    private void clearStudentAndInternshipFields() {
        tfName.setText("");
        tfStudentId.setText("");
        tfCourse.setText("");
        setCompanyInput("");
        tfRole.setText("");
        tfAllowance.setText("");
        lblCompanyDetails.setText(" ");
    }

    private void clearRatingInputFields() {
        tfWeeks.setText("");
        tfMarks.setText("");
    }

    /**
     * Updates the "Selected student" label in the bottom bar so the admin can
     * always see which record Delete and Add Rating would act on.
     *
     * The buttons are deliberately left enabled: handleDeleteStudent() and
     * handleAddRating() each check for a selection themselves and explain the
     * problem in the results panel, which is more helpful than a button that
     * silently does nothing.
     */
    private void updateSelectionControls() {
        if (selectedStudent != null) {
            lblSelectedStudent.setText("Selected student: "
                    + selectedStudent.getName() + " (" + selectedStudent.getStudentId() + ")");
            lblSelectedStudent.setForeground(TEXT_OK);
        } else {
            lblSelectedStudent.setText("Selected student: none");
            lblSelectedStudent.setForeground(TEXT_MUTED);
        }

        btnUndo.setToolTipText(undoStack.isEmpty()
                ? "Nothing has been deleted yet (Ctrl+Z)"
                : "Undo the " + undoStack.peek().describe() + " (Ctrl+Z)");
    }

    /** Highlights the selected student's row in the table, if it is visible. */
    private void syncTableSelection() {
        if (studentTable == null || studentTableModel == null) {
            return;
        }

        isSyncingTableSelection = true;
        try {
            if (selectedStudent == null) {
                studentTable.clearSelection();
                return;
            }

            int modelRow = studentTableModel.rowOf(selectedStudent);
            if (modelRow < 0) {
                studentTable.clearSelection();
                return;
            }

            int viewRow = studentTable.convertRowIndexToView(modelRow);
            if (viewRow >= 0) {
                studentTable.setRowSelectionInterval(viewRow, viewRow);
                studentTable.scrollRectToVisible(studentTable.getCellRect(viewRow, 0, true));
            } else {
                studentTable.clearSelection(); // hidden by the current filter
            }
        } finally {
            isSyncingTableSelection = false;
        }
    }

    /**
     * Clears the form so a brand new record can be typed in.
     */
    private void handleClearForm() {
        clearSelectedStudent();
        tfSearch.setText("");
        showStatus("Form cleared.\n\n"
                + "Enter the details of a new student and click Create\n"
                + "Record, or search for an existing student.\n\n"
                + buildCompanyList());
    }

    private void startNewRecord() {
        tabbedPane.setSelectedIndex(TAB_ADMIN);
        handleClearForm();
        tfName.requestFocusInWindow();
    }

    private void focusSearchField() {
        if (tabbedPane.getSelectedIndex() == TAB_STUDENTS) {
            tfTableFilter.requestFocusInWindow();
        } else {
            tabbedPane.setSelectedIndex(TAB_ADMIN);
            tfSearch.requestFocusInWindow();
        }
    }

    // =====================================================================
    // Refreshing the views after a data change
    // =====================================================================
    /**
     * Called after anything changes the data. Every view is rebuilt from the
     * shared collections, so nothing can drift out of step.
     */
    private void refreshAllViews() {
        populateCompanyCombo();
        rebuildFilterCombos();

        refreshAlerts();                 // must run before the table redraws,
        studentTableModel.refresh();     // because the renderer uses its result
        ratingTableModel.refresh();

        applyRowFilter();
        syncTableSelection();

        refreshActivityView();
        updateSelectionControls();
        updateSaveState();
        updateRatingImpactPreview();
    }

    private void refreshActivityView() {
        if (activityListModel == null) {
            return;
        }

        activityListModel.clear();
        ArrayList<ActivityLog.Entry> entries = activityLog.getEntries();

        // Newest first so the most recent action is always at the top.
        for (int i = entries.size() - 1; i >= 0; i--) {
            activityListModel.addElement(entries.get(i).toString());
        }

        if (entries.isEmpty()) {
            activityListModel.addElement("   (no changes made yet this session)");
        }

        lblSessionStats.setText("  " + activityLog.getSessionSummary());
    }

    /** ADVANCED FEATURE: the unsaved-changes indicator. */
    private void updateSaveState() {
        if (hasUnsavedChanges) {
            lblSaveState.setText("● Unsaved changes");
            lblSaveState.setForeground(TEXT_BAD);
        } else if (lastSavedAt != null) {
            lblSaveState.setText("✓ All changes saved at " + lastSavedAt);
            lblSaveState.setForeground(TEXT_OK);
        } else {
            lblSaveState.setText("✓ No changes yet");
            lblSaveState.setForeground(TEXT_MUTED);
        }
    }

    /** Records that the in-memory data no longer matches the saved file. */
    private void markUnsaved() {
        hasUnsavedChanges = true;
        updateSaveState();
    }

    // =====================================================================
    // Message building
    // =====================================================================
    /**
     * Replaces the contents of the results / status area.
     */
    private void showStatus(String message) {
        taResults.setText(message);
        taResults.setCaretPosition(0);
    }

    /**
     * Startup message confirming the data loaded from internships.txt.
     */
    private void showWelcomeMessage() {
        showStatus("Internship Admin System ready.\n\n"
                + "Loaded from internships.txt:\n"
                + "  Companies : " + manager.getCompanies().size() + "\n"
                + "  Students  : " + manager.getStudents().size() + "\n\n"
                + buildCompanyList()
                + "\nData check: " + describeAlertCounts() + "\n\n"
                + "Create a new record on the left, or search for\n"
                + "an existing student on the right.\n"
                + "Use the Students tab to browse, sort and filter.\n"
                + "Click Exit & Save when you are done.\n\n"
                + "Shortcuts: Ctrl+S save, Ctrl+F search,\n"
                + "Ctrl+N new, Ctrl+Z undo, Esc clear.");
    }

    /**
     * Lists the valid company names, since a new student can only be
     * assigned to a company that already exists.
     */
    private String buildCompanyList() {
        StringBuilder sb = new StringBuilder("Existing companies:\n");
        for (Company c : manager.getCompanies()) {
            sb.append("  - ").append(c.getCompanyName()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Builds the readable block of student, internship and rating details
     * that is shown in the results area.
     */
    private String buildStudentSummary(Student student) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-14s: %s%n", "Student Name", student.getName()));
        sb.append(String.format("%-14s: %s%n", "Student ID", student.getStudentId()));
        sb.append(String.format("%-14s: %s%n", "Course", student.getCourseOfStudy()));
        sb.append(String.format("%-14s: %s%n", "Company",
                student.getAssignedCompany() == null ? "-" : student.getAssignedCompany().getCompanyName()));
        sb.append(String.format("%-14s: %s%n", "Role", student.getInternshipRole()));
        sb.append(String.format("%-14s: $%.2f / month%n", "Allowance", student.getMonthlyAllowance()));

        sb.append("\nPerformance Ratings (").append(student.getRatings().size()).append("):\n");

        if (student.getRatings().isEmpty()) {
            sb.append("  No ratings recorded yet.\n");
        } else {
            int period = 1;
            for (Student.PerformanceRating r : student.getRatings()) {
                sb.append(String.format("  Period %d: %2d week(s), %5.1f marks, grade point %d%n",
                        period, r.getWeeks(), r.getMarks(), r.getGradePoint()));
                period++;
            }
            sb.append(String.format("%nOverall Internship Score: %.2f / 4.00%n",
                    student.calculateOverallScore()));
        }

        return sb.toString();
    }

    // =====================================================================
    // Custom renderers
    // =====================================================================
    /**
     * ADVANCED FEATURE: colour-codes each student row by DATA HEALTH (from the
     * integrity checker), formats the money and score columns, and right
     * aligns the numbers. Keeping the formatting in the renderer lets the
     * model keep real Double values, which is what makes sorting correct.
     */
    private class StudentHealthRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int modelColumn = table.convertColumnIndexToModel(column);

            // Number formatting and alignment.
            if (value instanceof Double) {
                double d = (Double) value;
                if (modelColumn == StudentTableModel.COL_ALLOWANCE) {
                    setText(String.format("$%.2f", d));
                } else {
                    setText(String.format("%.2f", d));
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (value instanceof Integer) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            // Health tint. The selected row keeps the normal highlight so the
            // admin can always see what they clicked.
            if (!isSelected) {
                Student student = studentTableModel.getStudentAt(table.convertRowIndexToModel(row));
                DataIntegrityChecker.Severity severity = worstSeverityByStudent.get(student);

                if (severity == DataIntegrityChecker.Severity.CRITICAL) {
                    c.setBackground(ROW_CRITICAL);
                } else if (severity == DataIntegrityChecker.Severity.WARNING) {
                    c.setBackground(ROW_WARNING);
                } else if (severity == DataIntegrityChecker.Severity.INFO) {
                    c.setBackground(ROW_INFO);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    /**
     * ADVANCED FEATURE: draws each alert with a severity colour. A
     * ListCellRenderer is the JList equivalent of a table cell renderer.
     */
    private static class AlertCellRenderer extends javax.swing.JLabel
            implements ListCellRenderer<Object> {

        AlertCellRenderer() {
            setOpaque(true);
            setFont(new Font("Consolas", Font.PLAIN, 12));
            setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            if (!(value instanceof DataIntegrityChecker.Alert)) {
                setText(String.valueOf(value));
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
                return this;
            }

            DataIntegrityChecker.Alert alert = (DataIntegrityChecker.Alert) value;

            String who = (alert.getStudent() == null)
                    ? "System"
                    : alert.getStudent().getName() + " (" + alert.getStudent().getStudentId() + ")";

            setText(String.format("%-9s  %-34s  %s",
                    alert.getSeverity().getLabel().toUpperCase(), who, alert.getIssue()));

            if (isSelected) {
                setBackground(new Color(203, 213, 225));
                setForeground(Color.BLACK);
                return this;
            }

            switch (alert.getSeverity()) {
                case CRITICAL:
                    setBackground(ROW_CRITICAL);
                    setForeground(new Color(153, 27, 27));
                    break;
                case WARNING:
                    setBackground(ROW_WARNING);
                    setForeground(new Color(154, 52, 18));
                    break;
                default:
                    setBackground(ROW_INFO);
                    setForeground(new Color(133, 77, 14));
                    break;
            }
            return this;
        }
    }

    /**
     * Lets the Admin System be run on its own during testing. Normally it is
     * opened from the InternshipUser master menu instead.
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InternshipManagement manager = new InternshipManagement();
                new InternshipAdminGUI(manager).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList activityList;
    private javax.swing.JPanel activityLogPanel;
    private javax.swing.JScrollPane activityScroll;
    private javax.swing.JPanel activityTab;
    private javax.swing.JPanel addRatingRow;
    private javax.swing.JPanel adminTab;
    private javax.swing.JPanel alertDetailPanel;
    private javax.swing.JScrollPane alertDetailScroll;
    private javax.swing.JList alertList;
    private javax.swing.JPanel alertListPanel;
    private javax.swing.JScrollPane alertScroll;
    private javax.swing.JPanel alertsTab;
    private javax.swing.JPanel alertsTopBar;
    private javax.swing.JPanel bottomBar;
    private javax.swing.JPanel bottomButtonHolder;
    private javax.swing.JButton btnAddRating;
    private javax.swing.JButton btnClearForm;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteRating;
    private javax.swing.JButton btnExitSave;
    private javax.swing.JButton btnLoadSelected;
    private javax.swing.JButton btnRescan;
    private javax.swing.JButton btnResetFilters;
    private javax.swing.JButton btnSaveNow;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUndo;
    private javax.swing.JComboBox cbCompany;
    private javax.swing.JComboBox cbFilterCompany;
    private javax.swing.JComboBox cbFilterCourse;
    private javax.swing.JComboBox cbFilterPreset;
    private javax.swing.JPanel deleteRatingRow;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerTextPanel;
    private javax.swing.JPanel internshipPanel;
    private javax.swing.JLabel lblAlertSummary;
    private javax.swing.JLabel lblAllowanceCaption;
    private javax.swing.JLabel lblCompanyCaption;
    private javax.swing.JLabel lblCompanyDetails;
    private javax.swing.JLabel lblCompanyFilter;
    private javax.swing.JLabel lblContains;
    private javax.swing.JLabel lblCourseCaption;
    private javax.swing.JLabel lblCourseFilter;
    private javax.swing.JLabel lblMarksCaption;
    private javax.swing.JLabel lblNewRatingHeading;
    private javax.swing.JLabel lblRatingHistoryHeading;
    private javax.swing.JLabel lblRatingImpact;
    private javax.swing.JLabel lblResultCount;
    private javax.swing.JLabel lblRoleCaption;
    private javax.swing.JLabel lblSaveState;
    private javax.swing.JLabel lblSearchValue;
    private javax.swing.JLabel lblSelectedStudent;
    private javax.swing.JLabel lblSessionStats;
    private javax.swing.JLabel lblShow;
    private javax.swing.JLabel lblStudentIdCaption;
    private javax.swing.JLabel lblStudentName;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWeeksCaption;
    private javax.swing.JPanel leftColumn;
    private javax.swing.JScrollPane ratingScroll;
    private javax.swing.JTable ratingTable;
    private javax.swing.JRadioButton rbSearchById;
    private javax.swing.JRadioButton rbSearchByName;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JScrollPane resultsScroll;
    private javax.swing.JPanel rightColumn;
    private javax.swing.JPanel searchButtonRow;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel searchRadioRow;
    private javax.swing.ButtonGroup searchTypeGroup;
    private javax.swing.JPanel sessionStatsPanel;
    private javax.swing.JPanel statusHolder;
    private javax.swing.JPanel studentButtonRow;
    private javax.swing.JPanel studentDetailsPanel;
    private javax.swing.JTable studentTable;
    private javax.swing.JScrollPane studentTableScroll;
    private javax.swing.JPanel studentsActionRow;
    private javax.swing.JPanel studentsBottomBar;
    private javax.swing.JPanel studentsTab;
    private javax.swing.JTextArea taAlertDetail;
    private javax.swing.JTextArea taResults;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTextField tfAllowance;
    private javax.swing.JTextField tfCourse;
    private javax.swing.JTextField tfMarks;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfRole;
    private javax.swing.JTextField tfSearch;
    private javax.swing.JTextField tfStudentId;
    private javax.swing.JTextField tfTableFilter;
    private javax.swing.JTextField tfWeeks;
    // End of variables declaration//GEN-END:variables
}
