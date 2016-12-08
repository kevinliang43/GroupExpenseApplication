import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by KevinLiang on 12/6/16.
 */
public class Expense {

  private int expenseID;
  private Connection connection;

  public Expense(int expenseID, Connection connection) {
    this.expenseID = expenseID;
    this.connection = connection;
  }
  
  public int getExpenseID() {
      return expenseID;
  }

  /**
   * Gets the row value for the specified field of this expense.
   *
   * @param fieldName represents the field required.
   * @return a String of the row value.
   */
  public ArrayList<String> getField(String fieldName) {


    ArrayList<String> fields = new ArrayList<String>();

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT * FROM expense " +
                      "WHERE expenseID = " + this.expenseID + ";");

      while (resultSet.next()) {
        fields.add(resultSet.getString(fieldName));
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return fields;
  }

  /**
   * Returns all subexpenses that make up this expense.
   *
   * @return an ArrayList of all Subexpenses.
   */
  public ArrayList<SubExpense> getSubExpenses() {

    ArrayList subExpenses = new ArrayList<SubExpense>();

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT * FROM expense " +
                      "WHERE expenseID = " + this.expenseID + ";");

      while (resultSet.next()) {
        subExpenses.add(new SubExpense(
                resultSet.getInt("expenseID"),
                new Account(resultSet.getInt("accountOwes"), this.connection),
                this.connection));
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return subExpenses;
  }

  /**
   * retrieves a list of all accounts that owe money for this expense.
   *
   * @return an ArrayList of all accounts that owes money.
   */
  public ArrayList<Account> getAccountsOwes() {
    ArrayList<String> accountOwes = this.getField("accountOwes");
    ArrayList<Account> newAccountOwes = new ArrayList<Account>();

    for (String accountID : accountOwes) {
      newAccountOwes.add(new Account(Integer.valueOf(accountID), this.connection));
    }
    return  newAccountOwes;
  }

  /**
   * returns the the String version of this Expense.
   *
   * @return a String representing this subexpense.
   */
  public String toString() {

    Account accountOwed = new Account(Integer.valueOf(this.getField("accountOwed").get(0)), this.connection);
    ArrayList<Account> accountsOwes = this.getAccountsOwes();

    String output = "Expense #" + this.getField("expenseID").get(0) +": " + this.getField("name").get(0) + "\n";
    output += "Date: " + this.getField("date").get(0) + "\n";
    output += "Person Owed: " + accountOwed.getField("firstName") + " " + accountOwed.getField("lastName") + "\n";
    output += "People that Owe: ";

    for (int i = 0; i < accountsOwes.size(); i++) {
      Account account = accountsOwes.get(i);
      if (new SubExpense(this.expenseID, account, this.connection).getField("paid").equals("0")) {
        output += account.getField("firstName") + " " + account.getField("lastName");
      }
      if (i != accountsOwes.size() - 1) {
        output += ", ";
      }
    }
    output += "\n";
    int total = 0;
//    for (String amount : this.getField("amountOwed")) {
//      total += Integer.valueOf(amount);
//    }
    
    for (SubExpense sub : this.getSubExpenses()) {
        if (sub.getField("paid").equals("0")) {
            total += Integer.valueOf(sub.getField("amountOwed"));
        }
    }
    
    
    
    output += "Total Amount : $" + total + "\n";

    return output;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Expense expense = (Expense) o;

    return expenseID == expense.expenseID;

  }

  @Override
  public int hashCode() {
    return expenseID;
  }
}
