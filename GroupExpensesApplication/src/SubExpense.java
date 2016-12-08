import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by KevinLiang on 12/6/16.
 */
public class SubExpense {

  private int expenseID;
  private Account accountOwes;
  private Connection connection;

  public SubExpense(int expenseID, Account accountOwes, Connection connection) {
    this.expenseID = expenseID;
    this.accountOwes = accountOwes;
    this.connection = connection;
  }

  /**
   * Gets the row value for the specified field of this group.
   *
   * @param fieldName represents the field required.
   * @return a String of the row value.
   */
  public String getField(String fieldName) {


    String field = "";

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT * FROM expense " +
                      "WHERE expenseID = " + this.expenseID + " AND " +
                      "accountOwes = " + this.accountOwes.getField("accountID") +";");

      resultSet.next();
      field = resultSet.getString(fieldName);


    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return field;
  }


  /**
   * returns the the String version of this subexpense.
   *
   * @return a String representing this subexpense.
   */
  public String toString() {

    Account accountOwed = new Account(Integer.valueOf(this.getField("accountOwed")), this.connection);
    Account accountOwes = new Account(Integer.valueOf(this.getField("accountOwes")), this.connection);

    String output = "Expense #" + this.getField("expenseID") +": " + this.getField("name") + "\n";
    output += "Date: " + this.getField("date") + "\n";
    output += "Person Owed: " + accountOwed.getField("firstName") + " " + accountOwed.getField("lastName") + "\n";
    output += "Person Owes: " + accountOwes.getField("firstName") + " " + accountOwes.getField("lastName") + "\n";
    output += "Amount : $" + this.getField("amountOwed") + "\n";
    output += "Status: ";
    if (Integer.valueOf(this.getField("paid")) == 0) {
      output += "Not Paid";
    }
    else {
      output += "Paid";
    }

    return output;

  }

}
