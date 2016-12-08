import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by KevinLiang on 12/7/16.
 */
public class GroupInvite {

  private int groupID;
  private Account accountInvited;
  private Connection connection;

  public GroupInvite(int groupID, Account accountInvited, Connection connection) {
    this.groupID = groupID;
    this.accountInvited = accountInvited;
    this.connection = connection;
  }

  /**
   * Gets the row value for the specified field of this group Invite.
   *
   * @param fieldName represents the field required.
   * @return a String of the row value.
   */
  public String getField(String fieldName) {


    String field = "";

    try {
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT * FROM groupInvite " +
                      "WHERE groupID = " + this.groupID + " AND " +
                      "accountID = " + this.accountInvited.getField("accountID") + ";");

      resultSet.next();
      field = resultSet.getString(fieldName);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return field;
  }


  /**
   * produces the String representation of this group Invite.
   *
   * @return the String representation of this group Invite.
   */
  @Override
  public String toString() {
    Group group = new Group(this.groupID, this.connection);

    String output = group.getField("groupName") + " (GroupID: " + this.groupID + ")";

    return output;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GroupInvite that = (GroupInvite) o;

    if (groupID != that.groupID) return false;
    return accountInvited != null ? accountInvited.equals(that.accountInvited) : that.accountInvited == null;

  }

  @Override
  public int hashCode() {
    int result = groupID;
    result = 31 * result + (accountInvited != null ? accountInvited.hashCode() : 0);
    return result;
  }
}
