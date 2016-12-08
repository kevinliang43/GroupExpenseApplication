import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by KevinLiang on 12/6/16.
 */
public class Group {

  private int groupID;
  private Connection connection;

  public Group(int groupID, Connection connection) {
    this.groupID = groupID;
    this.connection = connection;
  }

  public int groupID() {
      return groupID;
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
              "SELECT * FROM GroupExpensesApplication.group " +
                      "WHERE groupID = " + this.groupID + ";");

      resultSet.next();
      field = resultSet.getString(fieldName);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return field;
  }

  /**
   * Gets all the field Names of a group and stores it in an ArrayList.
   *
   * @return an arrayList with all fieldNames.
   */
  public ArrayList<String> getFieldNames() {

    ArrayList<String> fieldNames = new ArrayList<String>(5);

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT * FROM GroupExpensesApplication.group WHERE groupID = 0;");
      ResultSetMetaData metaData = resultSet.getMetaData();

      for (int i = 1; i < metaData.getColumnCount(); i++) {
        fieldNames.add(metaData.getColumnName(i));
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return fieldNames;
  }

  // get the admin of this group.

  /**
   * Returns an the admin Account of this group.
   *
   * @return an Account that is the admin of this group.
   */
  public Account getAdmin() {

    Account admin = null;

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT adminID FROM GroupExpensesApplication.group " +
                      "WHERE groupID = " + this.groupID + ";");

      resultSet.next();
      admin = new Account(resultSet.getInt("adminID"), this.connection);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return admin;
  }


  /**
   * Gets a list of all members of this group.
   *
   * @return an ArrayList of all Accounts that are apart of this group.
   */
  public ArrayList<Account> getMemberList() {

    ArrayList<Account> memberList = new ArrayList<Account>();

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT accountID FROM groupList WHERE groupID = " + this.groupID +";");

      while (resultSet.next()) {
        memberList.add(new Account(resultSet.getInt("accountID"), this.connection));
      }


    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return memberList;

  }

  // get all expenses of this group.

  public ArrayList<Expense> getExpenses() {
    ArrayList<Expense> expenses = new ArrayList<Expense>();

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT DISTINCT expenseID FROM expense " +
                      "WHERE groupID = " + this.groupID +";");

      while (resultSet.next()) {
        expenses.add(new Expense(resultSet.getInt("expenseID"),
                this.connection));
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return expenses;
  }

  // to String

  public String toString() {
    String output = this.getField("groupName") + " (GroupID: " + this.groupID + ")";

    return output;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Group group = (Group) o;

    return groupID == group.groupID;

  }

  @Override
  public int hashCode() {
    return groupID;
  }
}
