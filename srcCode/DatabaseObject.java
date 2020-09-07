package srcCode;

import java.sql.*;


public class DatabaseObject {
    
	String url = "jdbc:sqlite:DatabaseFile.db";
	Connection conn = null;
    static String loggedIn = "";

	public DatabaseObject() {
		
        try{
        	conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();

            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initialCall() {


        createTable("CREATE TABLE IF NOT EXISTS ACCOUNTS (\n"
            + " USERNAME TEXT PRIMARY KEY NOT NULL, " + 
                " PASSWORD TEXT NOT NULL, " +
                "ACCOUNT_KIND TEXT NOT NULL, "+ 
                "LESSONS TEXT);");


        insertAccount("teacher1", "banana", "Teacher", "Calculus,Electronics");
        insertAccount("teacher2", "apple", "Teacher", "Programming,Algorithms");
        insertAccount("student1", "potato", "Student", "Calculus,Programming,Algorithms");
        insertAccount("student2", "rice", "Student", "Electronics");


        createTable("CREATE TABLE IF NOT EXISTS POSTLIST (\n"
            + "LESSON TEXT,"
            + " USERNAME TEXT  NOT NULL, " + 
                " POST TEXT);");

        createTable("CREATE TABLE IF NOT EXISTS ANNOUNCEMENTS (\n"
            + "LESSON TEXT,"
            + " USERNAME TEXT  NOT NULL, " + 
                " POST TEXT);");

    }

    public void createTable(String sqlForWhatTableToCreate) {

    	String sql = sqlForWhatTableToCreate;
        try (
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

     public void insertAccount(String username, String password, String account_kind, String lessons) {
        String sql = "INSERT INTO ACCOUNTS(USERNAME,PASSWORD,ACCOUNT_KIND, LESSONS) VALUES(?,?,?,?);";
 
         try (

            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, account_kind);
            pstmt.setString(4, lessons);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


     public void insertPost(String lesson, String username ,String post, String postKind) {
        
        String sql = ""; 

        if(postKind.equals("Announcement")) {
            sql = "INSERT INTO ANNOUNCEMENTS(LESSON,USERNAME,POST) VALUES(?,?,?);";
        }
        else {
            sql = "INSERT INTO POSTLIST(LESSON,USERNAME,POST) VALUES(?,?,?);";
        }
        try (

            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lesson);
            pstmt.setString(2, username);
            pstmt.setString(3, post);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

   public void printAllAccounts(){
        String sql = "SELECT USERNAME, PASSWORD FROM ACCOUNTS";
        
        try (
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println( "Username: " + rs.getString("USERNAME") + "\t" + "Password: " + rs.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } 

    public boolean checkLoginDetais(String username, String password) {
        String sql = "SELECT (count(*) > 0) as found FROM ACCOUNTS WHERE USERNAME LIKE ? AND PASSWORD LIKE ?;";

    	try (

            PreparedStatement pp = conn.prepareStatement(sql)) {
            pp.setString(1,username);
            pp.setString(2,password);

            ResultSet rs = pp.executeQuery();

            if (rs.next()) {
                boolean found = rs.getBoolean(1); // "found" column
                if (found) {
                    return true;
                }
            }

		}
		catch ( SQLException e ) {
		    e.printStackTrace();
		    System.exit(0);
		}

        return false;

    }   

      public String printAllPosts(String lesson){
        String sql = "SELECT USERNAME, POST FROM POSTLIST WHERE LESSON LIKE ?;";
        
        String allPosts = "";

        try (

            PreparedStatement pp = conn.prepareStatement(sql)) {
            pp.setString(1,lesson);
  
            ResultSet rs = pp.executeQuery();
            
            // loop through the result set
            while (rs.next()) {
                allPosts = rs.getString("USERNAME") + rs.getString("POST") + allPosts;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return allPosts;
    }

    public String printAllAnnouncements(String lesson) {
        String sql = "SELECT USERNAME, POST FROM ANNOUNCEMENTS WHERE LESSON LIKE ?;";
       
        String allPosts = "";

         try (

            PreparedStatement pp = conn.prepareStatement(sql)) {
            pp.setString(1,lesson);
  
            ResultSet rs = pp.executeQuery();
            
            // loop through the result set
            while (rs.next()) {
                allPosts = rs.getString("USERNAME") + "\t" + rs.getString("POST") + allPosts;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return allPosts;

    }

    public String getAccountKind(String username) {
        String account_kind = "";

        String sql = "SELECT ACCOUNT_KIND FROM ACCOUNTS WHERE USERNAME LIKE ?;";

        try (

            PreparedStatement pp = conn.prepareStatement(sql)) {
            pp.setString(1,username);

            ResultSet rs = pp.executeQuery();
            account_kind = rs.getString("ACCOUNT_KIND");
        }
        catch ( SQLException e ) {
           // e.printStackTrace();
          //  System.exit(0);
        }
        return account_kind;
    }

    public void setUsername(String username) {
       // System.out.println("GOT " + username);
        loggedIn = username;
    }

    //The lessons are all in one column, separated by ',' 
    public String[] getUserLessons(String username) {

        String Lessons = "";
        String sql = "SELECT LESSONS FROM ACCOUNTS WHERE USERNAME LIKE ?;";

        try (

            PreparedStatement pp = conn.prepareStatement(sql)) {
            pp.setString(1,username);

            ResultSet rs = pp.executeQuery();
            Lessons = rs.getString("LESSONS");
        }
        catch ( SQLException e ) {
           // e.printStackTrace();
           // System.exit(0);
        }
        String[] LessonArray = Lessons.split(",");
        return LessonArray;
    }

}