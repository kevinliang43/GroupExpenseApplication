import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by KevinLiang on 12/3/16.
 */


public class ApplicationModel {

  private Account login;
  private Connection connection;

  public ApplicationModel() {
    this.login = null;
    this.connection = null;
  }

  
  //establish a connection to the database.
  public void getConnection(String username, String password, String database) {
    String connectionURL = "jdbc:mysql://localhost:3306/" + database +
            "?autoReconnect=true&useSSL=false";

    Connection connect = null;

    try {
      connect = DriverManager.getConnection(connectionURL, username, password);
    } catch (SQLException e) {
      System.out.println("Unable to connect to the database. Please Try again: \n");
//      System.out.println(e.getMessage());
//      System.out.println(e.getErrorCode());
//      System.out.println(e.getSQLState());
    }
    this.connection = connect;

  }

  // close connection to the database.
  public void close() {

    if (this.connection == null) {
      throw new IllegalStateException(
              "Cannot disconnect from database when no connection has been established. ");
    }

    try {
      connection.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Login

  public void accountLogin(String username, String password) throws Exception {
    if (this.connection == null) {
      throw new IllegalStateException(
              "Cannot login to the Database when no connection has been established. ");
    }

    if (this.login != null) {
      throw new IllegalArgumentException(
              "Cannot attempt to login to different account, while already logged into one. ");
    }


    String passwordDB = "";
    int accountID = 0;

    // gets the account password and the accountID with the given username.
    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT accounts.password, accountID FROM accounts WHERE username = '" + username + "';");

      resultSet.first();

      passwordDB = resultSet.getString("password");
      accountID = resultSet.getInt("accountID");


    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    // checks if account exists in database.
    if (passwordDB == "" || accountID == 0) {
      throw new Exception("No such account exists in database. ");
    }

    // checks if the password given is the same as the password in the database.
    if (!passwordDB.equals(password)) {
      throw new Exception("Password given does not match the password to the given Username. ");
    }

    // sets session with this account logged in
    this.login = new Account(accountID, this.connection);

  }
  
  // Sign up
  public boolean userNameExist(String uname) {
      Statement statement;
    try {
        statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT username " +
                        "FROM accounts;");
        while(resultSet.next()) {
            if (uname.equals(resultSet.getString("username"))) {
                return true;
            }
        }
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
    }
      return false;
  }
  
  public boolean accountIDExist(int id) {
      Statement statement;
    try {
        statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT accountID " +
                        "FROM accounts;");
        while(resultSet.next()) {
            if (Integer.toString(id).equals(resultSet.getString("accountID"))) {
                return true;
            }
        }
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
    }
      return false;
  }
  
  // log out
  public void logout() {
      login = null;
  }
  
  /**
   * returns a list of all table names in this database.
   *
   * @return an ArrayList of Strings of the table names.
   */
  private ArrayList<String> getTableNames() {

    ArrayList<String> tableNames = new ArrayList<String>(5);

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT TABLE_NAME " +
                      "FROM information_schema.tables " +
                      "WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA = 'GroupExpensesApplication';");

      while (resultSet.next()) {
        tableNames.add(resultSet.getString("TABLE_NAME"));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return tableNames;

  }

  /**
   * returns the ResultSet of selecting * from a given table within the database.
   * 
   * @param tableName represents the name of the table to be selected from.
   * @return a ResultSet containing all elemnents within the table matching the given name.
   */
  public ResultSet getTable(String tableName) {

    ResultSet resultSet = null;

    try {
      Statement statement = this.connection.createStatement();
      resultSet = statement.executeQuery(
              "SELECT * FROM " + tableName + ";");


    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return resultSet;

  }

  // gets field names of a table

  public ArrayList<String> getFieldNames(String tableName) {
    ArrayList<String> fieldNames = new ArrayList<String>();

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT * FROM " + tableName + " LIMIT 0;");
      ResultSetMetaData metaData = resultSet.getMetaData();

      for (int i = 1; i < metaData.getColumnCount(); i++) {
        fieldNames.add(metaData.getColumnName(i));
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return fieldNames;
  }


  // add account (cannot add if account already exists, autoincrement shouldnt be problem)

  /**
   * Adds an account to the database.
   * Cannot add an account if the username already exists in the database.
   *
   * @param firstName represents first name of account.
   * @param lastName represents the last name of the account.
   * @param username represents the username of the account.
   * @param password represents the password of the account.
   */
  public void addAccount(String firstName, String lastName, String username, String password) {

    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "INSERT INTO accounts (firstName, lastName, username, password) " +
                      "VALUES ('" +
                      firstName + "', '" +
                      lastName + "', '" +
                      username + "', '" +
                      password + "');");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.print("Please enter a differnt Username. ");
    }
  }

  /**
   * Adds a group to the group table within the database.
   * cannot add a group with an admin (account) that does not exist in the account table.
   *
   * @param groupName represents the name of the group.
   * @param adminID represents the account with admin status of this group.
   */
  public void addGroup(String groupName, int adminID ) {

    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "INSERT INTO GroupExpensesApplication.group (groupName, adminID) " +
                      "VALUES ('" +
                      groupName + "', '" +
                      adminID + ");");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // add expense (check if group exists first, accounts exist within group)

  /**
   * Adds an expense to the database.
   *
   * @param expenseID represents the ID of the expense.
   * @param groupID represents the group in which the expense happens.
   * @param accountOwes represents the account that owes money.
   * @param accountOwed represents the account that is owed.
   * @param amountOwed represents teh amount owed.
   * @param paid boolean representing whether the expense has been paid.
   * @param date date of the expense.
   * @param expenseName name of the expense.
   */
  public void addExpense(int expenseID, int groupID, int accountOwes, int accountOwed, int amountOwed,
                         boolean paid, String date, String expenseName) {

    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "INSERT INTO expense " +
                      "(expenseID, groupID, accountOwes, accountOwed, amountOwed, paid, date, name) " +
                      "VALUES (" +
                      expenseID + ", " +
                      groupID + ", '" +
                      accountOwes + "', '" +
                      accountOwed + "', '" +
                      amountOwed + "', " +
                      paid + ", '" +
                      date + "', '" +
                      expenseName + "');");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // add groupInvite

  /**
   * Adds a group invite to the group invite table in the database.
   * Cannot add a group invite, if the group invite already exists.
   * (Pair of groupID, acccountID exists).
   *
   * @param groupID represents the group inviting the person.
   * @param accountID represents the person being invited.
   */
  public void addGroupInvite(int groupID, int accountID) {

    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "INSERT INTO groupInvite (groupID, accountID) " +
                      "VALUES ('" +
                      groupID + "', " +
                      accountID + ");");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // add groupList (add group, remove group invite)


  /**
   * Removes an account from the database.
   * Cannot remove an account that does not exist. (Query will run, nothing will happen).
   *
   * @param accountID represents the ID of the account to be deleted.
   */
  public void removeAccount(int accountID) {
    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "DELETE FROM accounts " +
                      "WHERE accountID = " + accountID +"; ");
  } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // remove group

  /**
   * Removes a group from the database.
   * Cannot remove a group that does not exist. (Query will run, nothing will happen).
   *
   * @param groupID represents the ID of the group to be deleted.
   */
  public void removeGroup(int groupID) {
    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "DELETE FROM GroupExpensesApplication.group " +
                      "WHERE groupID = " + groupID +"; ");
    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Removes an expense from the database.
   * Cannot remove an account that does not exist. (Query will run, nothing will happen).
   *
   * @param expenseID represents the ID of the expense to be deleted.
   */
  public void removeExpense(int expenseID) {
    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "DELETE FROM expense " +
                      "WHERE expenseID = " + expenseID +"; ");
    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // remove group Invite

  /**
   * Removes a group Invite from the database.
   * Cannot remove a group invite that does not exist. (Query will run, nothing will happen).
   *
   * @param groupID represents the group ID of the group Invite to be deleted.
   * @param accountID represents the account ID of the invite to be deleted
   */
  public void removeGroupInvite(int groupID, int accountID) {
    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "DELETE FROM groupInvite " +
                      "WHERE groupID = " + groupID +
                      " AND accountID = " + accountID +"; ");
    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // remove groupList

  /**
   * Removes a group from the Account's groupList.
   *
   * Cannot remove a group from an account that does not exist.
   * Cannot remove a group from an account who doesnt have that group on their list.
   * Account cannot remove a group that doesnt exist.
   * (Query will run, nothing will happen).
   *
   * @param groupID represents the group ID of the group being removed.
   * @param accountID represents the account ID of the person removing the group.
   */
  public void removeGroupList(int accountID, int groupID) {
    try {
      Statement statement = this.connection.createStatement();
      statement.executeUpdate(
              "DELETE FROM groupList " +
                      "WHERE groupID = " + groupID +
                      " AND accountID = " + accountID +"; ");
    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  //generate a new ExpenseID

  public int generateExpenseID() {
    int id = 0;
    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT DISTINCT maxExpenseID() AS ExpenseID");

      resultSet.next();
      id = resultSet.getInt("ExpenseID");


    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
    return id;
  }


  public Account getLogin() {
    return login;
  }

  public Connection getConnection() {
    return connection;
  }


}