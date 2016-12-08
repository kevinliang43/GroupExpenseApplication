import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by KevinLiang on 12/3/16.
 */
public class ApplicationController {

  public static void main(String[] args) {

    ApplicationModel model = new ApplicationModel();
    Scanner scanner = new Scanner(System.in);
    boolean loggedin = false;
    String userinput ="";
    boolean exit = false;
    // Step 1: Attempts to get a connection

    while (model.getConnection() == null) {
      String username;
      String password;

      System.out.println("Please enter your database username: ");
      username = scanner.nextLine();
      System.out.println("Please enter your database password: ");
      password = scanner.nextLine();

      System.out.println("Attempting to get Connection..WAIT");
      model.getConnection(username, password, "GroupExpensesApplication");

    }

    if (model.getConnection() != null) {

      System.out.println("Connection successful");
      while (model.getConnection() != null) {
    while (!loggedin) {
    System.out.println("Do you want to 'login' or 'signup'?");
    userinput = scanner.nextLine();
    if (userinput.equalsIgnoreCase("login")) {
        String uname = "";
        String pwd = "";
        System.out.println("Enter Username:");
        uname = scanner.nextLine();
        System.out.println("Enter Password:");
        pwd = scanner.nextLine();
        try {
            model.accountLogin(uname, pwd);
            loggedin = true;
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
    }
    else if (userinput.equalsIgnoreCase("signup")) {
        boolean matched = false;
        String fname = "";
        String lname = "";
        String uname = "";
        String pwd = "";
        String pwd2 = "";
        System.out.println("Enter First Name:");
        fname = scanner.nextLine();
        System.out.println("Enter Last Name:");
        lname = scanner.nextLine();
        
        while (!matched) {
        System.out.println("Enter User Name:");
        uname = scanner.nextLine();
        System.out.println("Enter Password:");
        pwd = scanner.nextLine();
        System.out.println("Re-enter Password:");
        pwd2 = scanner.nextLine(); 
        
        if (model.userNameExist(uname)) {
            System.out.println("Username already exists");
        }
        if (!pwd.equals(pwd2)) {
            System.out.println("Passwords did not match");
        }
        if (!model.userNameExist(uname) && pwd.equals(pwd2)) {
            System.out.println("Sign Up Successful");
            matched = true;
        }
        }
        
        if (matched)
            model.addAccount(fname, lname, uname, pwd);
    }

  }
    boolean goBack = false;
    while(loggedin) {
        Account account = new Account(model.getLogin().getAccountID(), model.getConnection());

        goBack = false;
        while(!goBack) {
        System.out.println("Do you want to enter 'Home', 'Groups, 'Invites', "
                + "'Account Info', or 'Logout'?");
        userinput = scanner.nextLine();
        
        if (userinput.equalsIgnoreCase("Home")) {
            System.out.println("Name: " + account.getField("firstName"));
            System.out.println("You owe: $"+ account.totalOwes());
            System.out.println("Friends owe you: $" + account.totalOwed());
            System.out.println("5 Most Recent Expenses: ");
            for (int i = 0; i < account.recent5Expense().size(); i++) {
                System.out.println(account.recent5Expense().get(i));
            }
            System.out.println("Do you want to 'Go Back' or 'Logout'?");
            userinput = scanner.nextLine();
            if (userinput.equalsIgnoreCase("Go Back"))
                goBack = true;
            else if (userinput.equalsIgnoreCase("Logout")) {
                goBack = true;
                loggedin = false;
                model.logout();
            }   
        }
        
        
//        // Groups -> Expenses -> individual expenses -> paid/not paid -> show individual expenses
        else if (userinput.equalsIgnoreCase("Groups")) {
            boolean exitGroup = false;
            while(!exitGroup) {
                userinput = "";
                System.out.println(account.getGroupList().toString());
                System.out.println("Do you want to 'Enter' group, 'Go Back' or 'Logout'?");
                userinput = scanner.nextLine();
                if (userinput.equalsIgnoreCase("Go Back")) {
                    exitGroup = true;
                    goBack = true;
                }
                else if (userinput.equalsIgnoreCase("Logout")) {
                    exitGroup = true;
                    goBack = true;
                    loggedin = false;
                    model.logout();
                }   
            else if (userinput.equalsIgnoreCase("Enter")) {
                int input = 0;
                System.out.println("Enter Group ID");
                try {
                input = scanner.nextInt();
                scanner.nextLine();
                }
                catch(InputMismatchException exception)
                {
                  //Print "This is not an integer"
                  //when user put other than integer
                  System.out.println("This is not an integer");
                  scanner.nextLine();
                }
                Group g = new Group (input, model.getConnection()); 
                if(account.getGroupList().contains(g)) {
                    boolean exitIGroup = false;
                    while(!exitIGroup) {
                        System.out.println(g.getExpenses().toString());
                        System.out.println("Do you want to 'Enter expense', 'Add' expense, 'Delete' expense, 'Go Back' or 'Logout'?");
                        userinput = scanner.nextLine();
                        if (userinput.equalsIgnoreCase("Go Back")) {
                            exitIGroup = true; 
                        }
                        else if (userinput.equalsIgnoreCase("Logout")) {
                            goBack = true;
                            exitIGroup = true;
                            exitGroup = true;
                            loggedin = false;
                            model.logout();
                        }
                        else if (userinput.equalsIgnoreCase("Add"))  {
                            int loop = 0;
                            while(loop == 0) {
                            System.out.println("How many people to add to your expense sheet?");
                            try {
                                 loop = scanner.nextInt();
                                scanner.nextLine();
                                }
                                catch(InputMismatchException exception)
                                {
                                  //Print "This is not an integer"
                                  //when user put other than integer
                                  System.out.println("This is not an integer");
                                  scanner.nextLine();
                                }
                            if (loop == 0) {
                                System.out.println("Cannot add 0 people");
                            }
                            }
                            List<Account> myList = new ArrayList<Account>();
                            for (int i = 0; i < loop; i++) {
                                boolean found = false;
                                while(!found) {
                                    try {
                                System.out.println("Enter AccountID " + (i+1));
                                input = scanner.nextInt();
                                scanner.nextLine();
                                if (model.accountIDExist(input) && input == account.getAccountID()) {
                                    System.out.println("Cannot add yourself");
                                }
                                else if (model.accountIDExist(input)) {
                                    myList.add(new Account(input, model.getConnection()));
                                    found = true;
                                }
                              
                                else if (model.accountIDExist(input)) {
                                    myList.add(new Account(input, model.getConnection()));
                                    found = true;
                                }
                                else if (!model.accountIDExist(input)) {
                                    System.out.println("The AccountID does not exist or is incorrect");
                                }
                                }
                                    catch(InputMismatchException exception)
                                    {
                                      //Print "This is not an integer"
                                      //when user put other than integer
                                      System.out.println("This is not an integer");
                                      scanner.nextLine();
                                    }
                                }
                                
                            }
                            int owed = 0;
                            while (owed == 0) {
                            try {
                            System.out.println("How much owed?");
                            owed = scanner.nextInt();
                            scanner.nextLine();
                            }
                            catch(InputMismatchException exception)
                            {
                              //Print "This is not an integer"
                              //when user put other than integer
                              System.out.println("This is not an integer");
                              scanner.nextLine();
                            }
                            }
                            
                            System.out.println("Date of expense (YYYY-MM-DD): ");
                            String date = scanner.nextLine();
                            System.out.println("Name of expense: ");
                            String name = scanner.nextLine();
                            account.addExpense(g, model.generateExpenseID(), myList,
                                    owed, false, date, name);
                        }
                        else if (userinput.equalsIgnoreCase("Delete")) {
                            int id = 0;
                            while (id == 0) {
                            try {
                            System.out.println("Enter ExpenseID");
                             id = scanner.nextInt();
                            scanner.nextLine();
                            }
                            catch(InputMismatchException exception)
                            {
                              //Print "This is not an integer"
                              //when user put other than integer
                              System.out.println("This is not an integer");
                              scanner.nextLine();
                            }
                            }
                            account.deleteExpense(g, new Expense(id, model.getConnection()));
                        }
                        else if (userinput.equalsIgnoreCase("Enter expense")) {
                            
                            int id = 0;
                            while (id == 0) {
                            try {
                            System.out.println("Enter ExpenseID to View");
                                id = scanner.nextInt();
                                scanner.nextLine();
                            }
                            catch(InputMismatchException exception)
                            {
                              //Print "This is not an integer"
                              //when user put other than integer
                              System.out.println("This is not an integer");
                              scanner.nextLine();
                            }
                            }
                                Expense e = new Expense(id, model.getConnection()); 
                                if (g.getExpenses().contains(e)) {
                                   boolean exitExpense = false;
                                   while(!exitExpense) {
                                   System.out.println(e.getSubExpenses().toString());
                                   System.out.println("'Update' to not paid or paid, 'Go Back' or 'Logout'?");
                                   userinput = scanner.nextLine();
                                   if (userinput.equalsIgnoreCase("Go Back")) {
                                       exitExpense = true;
                                   }
                                   else if (userinput.equalsIgnoreCase("Logout")) {
                                       exitExpense = true;
                                       goBack = true;
                                       exitIGroup = true;
                                       exitGroup = true;
                                       loggedin = false;
                                       model.logout();
                                   }
                                   else if (userinput.equalsIgnoreCase("Update")) {
                                       int paid = -1;
                                       while (paid == -1) {
                                       try {
                                       System.out.println("If paid, enter 1. If not paid, enter 0");
                                        paid = scanner.nextInt();
                                       scanner.nextLine();
                                       }
                                       catch(InputMismatchException exception)
                                       {
                                         //Print "This is not an integer"
                                         //when user put other than integer
                                         System.out.println("This is not an integer");
                                         scanner.nextLine();
                                       }
                                       }
                                       if (paid == 0 || paid == 1) {
                                           SubExpense sub = e.getSubExpenses().get(0);
                                           account.updateExpense(sub, paid);
                                       }
                                       else if (paid != 0 || paid != 1) {
                                           System.out.println("ExpenseID or AccountID is incorrect or does not exist, or paid is not 1 or 0");
                                       }
                                   }
                                   }
                                }
                        
                                else if (!g.getExpenses().contains(e)) {
                                    System.out.println("ExpenseID is incorrect or does not exist");
                                }
                        }
                  
                    }
               
            }
            
                else if(!account.getGroupList().contains(g)) { 
                    System.out.println("GroupID is incorrect or does not exist.");
                }
            }
        }
        }
        
        else if (userinput.equalsIgnoreCase("Invites")) {
            boolean exitInvite = false;
            while(!exitInvite) {
            System.out.println(account.getGroupInvites().toString());
            System.out.println("Do you want to 'Go Back', 'Logout', 'Invite' a user, 'Accept' Invite, or 'Decline' Invite");
            userinput = scanner.nextLine();
            if (userinput.equalsIgnoreCase("Go Back")) {
                exitInvite = true;
                goBack = true;
            }
            else if (userinput.equalsIgnoreCase("logout")) {
                goBack = true;
                exitInvite = true;
                loggedin = false;
                model.logout();
            }
            else if (userinput.equalsIgnoreCase("invite")) {
                    int input = -1;
                    while (input == -1) {
                    try {
                    System.out.println("Enter AccountID of the account which you are sending the invite to: ");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    if (!model.accountIDExist(input) || account.getAccountID() == input) {
                        System.out.println("AccountID does not exist or you cannot add yourself");
                        input = -1;
                    }
                    }
                    catch(InputMismatchException exception)
                    {
                      //Print "This is not an integer"
                      //when user put other than integer
                      System.out.println("This is not an integer");
                      scanner.nextLine();
                    }
                    }
                    Account a = new Account(input, model.getConnection());
                    input = -1;
                    while (input == -1) {
                    try {
                    System.out.println("Enter GroupID from which group you are sending the invite to: ");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    Group g = new Group(input,  model.getConnection());
                        if (!account.getGroupList().contains(g)) {
                            input = -1;
                            System.out.println("GroupID does not exist");

                        }
                    }
                    catch(InputMismatchException exception)
                    {
                      //Print "This is not an integer"
                      //when user put other than integer
                      System.out.println("This is not an integer");
                      scanner.nextLine();
                    }
                    }
                    Group group = new Group(input,  model.getConnection());
                    account.sendInvite(group, a);
                    System.out.println("Invite Sent");
            }
            
            else if (userinput.equalsIgnoreCase("accept")) {
                int input = -1;
                while (input == -1) {
                try {
                System.out.println("Enter Group ID that you want to accept:");
                input = scanner.nextInt();
                scanner.nextLine();
                }
                catch(InputMismatchException exception)
                {
                  //Print "This is not an integer"
                  //when user put other than integer
                  System.out.println("This is not an integer");
                  scanner.nextLine();
                }
                }
                GroupInvite gi = new GroupInvite(input, account, model.getConnection());
                if (account.getGroupInvites().contains(gi)) {
                    account.acceptInvite(gi);
                }
                else if (!account.getGroupInvites().contains(gi)) {
                    System.out.println("Group Invite does not exist");

                }
            }
            else if (userinput.equalsIgnoreCase("decline")) {
                int input = -1;
                while (input == -1) {
                try {
                System.out.println("Enter Group ID that you want to decline:");
                input = scanner.nextInt();
                scanner.nextLine();
                }
                catch(InputMismatchException exception)
                {
                  //Print "This is not an integer"
                  //when user put other than integer
                  System.out.println("This is not an integer");
                  scanner.nextLine();
                }
                }
                GroupInvite gi = new GroupInvite(input, account, model.getConnection());
                if (account.getGroupInvites().contains(gi)) {
                    account.declineInvite(gi); 
                }
                else if (!account.getGroupInvites().contains(gi)) {
                    System.out.println("Group Invite does not exist");

                }
            }
            
        }
        }
       
        else if (userinput.equalsIgnoreCase("Account Info")) {
            boolean exitAccount = false;
              while(!exitAccount) {
            System.out.println("Username: "+account.getField("username"));
            System.out.println("Password: "+account.getField("password"));
            System.out.println("Do you want to'Go Back', 'Logout', or 'Edit' Personal Info?");
            userinput = scanner.nextLine();
            if (userinput.equalsIgnoreCase("Go Back")) {
                exitAccount = true;
                goBack = true;
            }
            else if (userinput.equalsIgnoreCase("logout")) {
                goBack = true;
                exitAccount = true;
                loggedin = false;
                model.logout();
            }
            else if (userinput.equalsIgnoreCase("edit")) {
                String old_password = "";
                String new_password = "";
                String new_password2 = "";
                System.out.println("Enter Old Password:");
                old_password = scanner.nextLine();
                System.out.println("Enter New Password:");
                new_password = scanner.nextLine();
                System.out.println("Re-enter New Password:");
                new_password2 = scanner.nextLine();
                if (account.getField("password").equals(old_password)) {
                    if (new_password.equals(new_password2)) {
                        account.updatePassword(old_password, new_password, new_password2);
                        System.out.println("Password Changed");
                    }
                }
                else if (!account.getField("password").equals(old_password) || !new_password.equals(new_password2)) {
                    System.out.println("Did not update password. Either old password was incorrect or the passwords were not the same.");
                }
                }
            }
            
        }
        
        else if (userinput.equalsIgnoreCase("Logout")) {
            goBack = true;
            loggedin=false;
            model.logout();
        }
        
    }
    }
  }
    }
  }


}
