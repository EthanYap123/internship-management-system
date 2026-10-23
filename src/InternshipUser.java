
/**
 * Member A:
 * Class: DIT/FT/2B/21
 * Student ID: P2523752
 * Name: Yap Kai Jun Ethan
 *
 * Created the logic system required by member a tasks
 *
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Added the code to connect my Admin System to the main menu.
 * For CA2 option 2 now opens my Swing GUI (InternshipAdminGUI) instead of the
 * CA1 JOptionPane admin menu. The old InternshipAdmin class is kept in the
 * project as my CA1 version.
 */
import javax.swing.JOptionPane;

public class InternshipUser {

    public static void main(String[] args) {
        // One shared manager. Both subsystems are given this same object, so
        // records added or deleted in one are visible in the other.
        InternshipManagement manager = new InternshipManagement();
        boolean runningApp = true;

        while (runningApp) {
            String masterMenuInput = JOptionPane.showInputDialog(null,
                    "Select Subsystem Menu:\n\n"
                    + "1. Internship Enquiry System (Member A)\n"
                    + "2. Internship Admin System (Member B)\n"
                    + "3. Exit Entire Application",
                    "Master Gateway", JOptionPane.QUESTION_MESSAGE);

            if (masterMenuInput == null || masterMenuInput.equals("3")) {
                JOptionPane.showMessageDialog(null, "Program terminated.\nThank You!", "Message",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            if (masterMenuInput.equals("1")) {
                // Run Member A's Subsystem (Swing GUI)
                final Object lock = new Object();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    InternshipEnquiryGUI enquiryGUI = new InternshipEnquiryGUI(manager);
                    enquiryGUI.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            synchronized (lock) {
                                lock.notifyAll();
                            }
                        }
                    });
                    enquiryGUI.setVisible(true);
                });
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            } else if (masterMenuInput.equals("2")) {
                // Run Member B's Subsystem (Swing GUI).
                // Same pattern as Member A: wait until the admin window has
                // been closed, then show the master menu again.
                final Object adminLock = new Object();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    InternshipAdminGUI adminGUI = new InternshipAdminGUI(manager);
                    adminGUI.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            synchronized (adminLock) {
                                adminLock.notifyAll();
                            }
                        }
                    });
                    adminGUI.setVisible(true);
                });
                synchronized (adminLock) {
                    try {
                        adminLock.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice! Please select 1, 2, or 3.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Member A Loop Logic System
    private static void runEnquirySystem(InternshipManagement manager) {
        boolean inEnquiry = true;

        while (inEnquiry) {
            String choiceInput = JOptionPane.showInputDialog(null,
                    "Enter your option:\n\n"
                    + "1. Display all internships\n"
                    + "2. Search by student name\n"
                    + "3. Search by company\n"
                    + "4. Search by Student ID (Bonus)\n"
                    + // NEW MENU OPTION
                    "5. Quit", // CHANGED TO 5
                    "Internship Enquiry System", JOptionPane.QUESTION_MESSAGE);

            if (choiceInput == null || choiceInput.equals("5")) { // CHANGED TO 5
                JOptionPane.showMessageDialog(null, "Exiting Enquiry System...", "Message",
                        JOptionPane.INFORMATION_MESSAGE);
                inEnquiry = false;
                break;
            }

            try {
                int option = Integer.parseInt(choiceInput.trim());

                switch (option) {
                    case 1:
                        manager.displayAllInternships();
                        break;
                    case 2:
                        manager.searchByStudentName();
                        break;
                    case 3:
                        manager.searchByCompany();
                        break;
                    case 4: // NEW CASE HANDLER
                        manager.searchByStudentId();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Please choose a number between 1 and 5.", "Invalid Option",
                                JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please input a valid numeric digit.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
