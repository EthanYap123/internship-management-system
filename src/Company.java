
/**
 * Member A:
 * Class: DIT/FT/2B/21
 * Student ID: P2523752
 * Name: Yap Kai Jun Ethan
 *
 * Member B:
 * Class: DIT/FT/2B/21
 * Student ID: P2523075
 * Name: Tay Yu Xiang Janston
 *
 * Only change made by Member B: made this class Serializable so that Company
 * objects can be written to the object file by FileHandler (CA2 Function 4).
 */
import java.io.Serializable;

public class Company implements Serializable {

    // Version stamp used by Java serialization to check that a saved file
    // still matches this version of the class.
    private static final long serialVersionUID = 1L;

    private String companyName;
    private double monthlyAllowance;
    private String contactPerson;

    // constuct new company
    public Company(String companyName, double monthlyAllowance, String contactPerson) {
        this.companyName = companyName;
        this.monthlyAllowance = monthlyAllowance;
        this.contactPerson = contactPerson;
    }

    // set variables
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getMonthlyAllowance() {
        return monthlyAllowance;
    }

    public void setMonthlyAllowance(double monthlyAllowance) {
        this.monthlyAllowance = monthlyAllowance;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
}
