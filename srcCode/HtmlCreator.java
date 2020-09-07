package srcCode;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.UnsupportedEncodingException;
import java.lang.IndexOutOfBoundsException;


public class HtmlCreator {

	static String postList = "";
    static String announcementList = "";
    String postKind = "";
    NewServer mitsos = new NewServer();
	byte[] _content = null;
	int callingaddpost = 0;
    String accountName = "";
    String whichLessonFile = "";
    static String actualLesson = "";
	DatabaseObject db = new DatabaseObject();


	public byte[] serveMainPage() {
	    
        String radioButtons = "";
        String ifTeacher = "";

        //Teachers get to choose what kind of post to make
        if(db.getAccountKind(mitsos.getUser()).equals("Teacher")) {
            radioButtons =  "<label class=\"containerRadio\">Post<input type=\"radio\" name=\"Post_Kind\" checked=\"checked\" value=\"Post\"><span class=\"checkmark\"></span></label><label class=\"containerRadio\">Announcement<input type=\"radio\" name=\"Post_Kind\" value=\"Announcement\"><span class=\"checkmark\"></span></label>";
        }

        whichLessonFile = mitsos.getResource();

        if(whichLessonFile.equals("/Algorithms.html")) {
            actualLesson = "Algorithms";
        }
        else if(whichLessonFile.equals("/Calculus.html")) {
            actualLesson = "Calculus";
        }
        else if(whichLessonFile.equals("/Programming.html")) {
            actualLesson = "Programming";
        }
        else if(whichLessonFile.equals("/Electronics.html")) {
            actualLesson = "Electronics";
        }

        String firstString = "<htmL><head><link rel=\"icon\" type=\"image/png\" href=\"images/favicon.ico\" /><title>" + actualLesson + "</title><meta charset=\"UTF-8\"><link href=\"default.css\" rel=\"stylesheet\" type=\"text/css\"></head><body><div id=\"header-wrapper\"><div id=\"header\" class=\"container\"><div id=\"menu\"><ul><li><a href=\"Lessons.html\" accesskey=\"1\" title=\"\">Lessons</a></li><li><a href=\"about.html\" accesskey=\"2\" title=\"\">About</a></li><li class=\"dropdown\"><button class=\"dropbtn\">" + mitsos.getUser() + "</button> <div class=\"dropdown-content\"><a href=\"esxoli.html\" accesskey=\"3\">Log out</a></div></li></ul></div><div id=\"logo\"><h1>e-σχολή</h1></div></div></div><div id=\"wrapper3\"><div id=\"portfolio\" class=\"container\"><div class=\"title\"><h2 id=\"hilighted\">";
        String secondString = "</h2></div><div class=\"pbox1\"><div class=\"column1\"><div class=\"postsColumn\">";
        String thirdString = " </div></div><div class=\"column2\"><div class=\"announcementsColumn\"><div class=\"box\"> <span class=\"icon icon-cloud\"></span><h4> ανακοινωσεις</h4></div>";
        String lastString = "</div></div><div class=\"columnp\"><div class=\"formColumn\"><form name=\"txtArea\" action = \"" + actualLesson + ".html" + "\" method = \"post\" ><textarea name=\"txtArea\" rows=\"8\" cols=\"60\"></textarea><br/><!--Post <input type=\"radio\" name=\"Post_Kind\" value=\"Post\" /> Announcement <input type=\"radio\" name=\"Post_Kind\" value=\"Announcement\"> --><br/>" + radioButtons + "<input type=\"submit\" class=\"button\" value=\"Submit\"/></form></div></div></div></div><div id=\"wrapper1\"><div id=\"featured\" class=\"container\"><div class=\"box1\"><h2>Πληροφορίες Χρήστη</h2><div id=\"accountDetails\"><p>username: " + mitsos.getUser() + " <br />"+ db.getAccountKind(mitsos.getUser()) + " Account</p></div></div><div class=\"box2\"><h2>Project Demo</h2><p>Για το μάθημα της Σχεδίασης και Ανάπτυξης Λογισμικού</p></div></div></div></body></htmL>";

        getTables();

        String content = firstString + actualLesson + secondString + postList + thirdString + announcementList + lastString;

		File fdd = new File("html" + whichLessonFile);

        try{
            if (!fdd.exists()) {
                fdd.createNewFile();
            }
       
        
            PrintWriter writer = new PrintWriter(fdd);
            writer.println(content);
            writer.close();
            _content = Files.readAllBytes(Paths.get("html" + whichLessonFile));
          
        }catch( FileNotFoundException a) {
            a.printStackTrace();
        }catch(IOException fs) {
           fs.printStackTrace();

        }

        return _content;
    }


    //Every user has different lessons
    public byte[] serveLessons() {

        String[] lessons = null;

        lessons = db.getUserLessons(mitsos.getUser());

        String firstLine = "<html><head><link rel=\"icon\" type=\"image/png\" href=\"images/favicon.ico\" /><title> Lessons </title><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><link href=\"default.css\" rel=\"stylesheet\" type=\"text/css\"></head><body><div id=\"header-wrapper\"><div id=\"header\" class=\"container\"><div id=\"menu\"><ul><li class=\"current_page_item\"><a href=\"#\" accesskey=\"1\" title=\"\">Lessons</a></li><li><a href=\"about.html\" accesskey=\"2\" title=\"\">About</a></li><li class=\"dropdown\"><button class=\"dropbtn\">" + mitsos.getUser() + "</button> <div class=\"dropdown-content\"><a href=\"esxoli.html\" accesskey=\"3\">Log out</a></div></li></ul></div><div id=\"logo\"><h1>e-σχολή</h1></div></div></div><div id=\"wrapper3\"><div id=\"portfolio\" class=\"container\"><div class=\"title\"><h2 id=\"hilighted\">Κατάλογος Μαθημάτων</h2><div class=\"pbox1\">";
        String lastLine = "</div></div></div><div id=\"wrapper1\"><div id=\"featured\" class=\"container\"><div class=\"box1\"><h2>Πληροφορίες Χρήστη</h2><div id=\"accountDetails\"><p>username: " + mitsos.getUser() + " <br />"+ db.getAccountKind(mitsos.getUser()) + " Account</p></div></div><div class=\"box2\"><h2>Project Demo</h2><p>Για το μάθημα της Σχεδίασης και Ανάπτυξης Λογισμικού</p></div></div></div></body></htmL>";
        int numberOfLessons = lessons.length;
        
        String lessonsList = "";
        String teachedBy = "";
        
        for(int i=1; i < numberOfLessons + 1; i++ ) {

            if(lessons[i-1].equals("Calculus") || lessons[i-1].equals("Electronics")) {
                teachedBy = "teacher1";
            }
            else{
                teachedBy = "teacher2";
            }


            lessonsList = lessonsList + "<div class=\"column" + i + "\"><div class=\"box\"> <span class=\"icon icon-comments\"></span><h3> <a href=\"" +  lessons[i-1] + ".html\">" +  lessons[i-1] + "</a></h3><p>Καθηγητής: "+ teachedBy +"</p></div></div>";
        }

        String content = firstLine + lessonsList + lastLine;

        File fdd = new File("html/Lessons.html");

        try{
            if (!fdd.exists()) {
                fdd.createNewFile();
            }
       
        
            PrintWriter writer = new PrintWriter(fdd);
            writer.println(content);
            writer.close();
            _content = Files.readAllBytes(Paths.get("html/Lessons.html"));
          
        }catch( FileNotFoundException a) {
            a.printStackTrace();
        }catch(IOException fs) {
           fs.printStackTrace();

        }

        return _content;
    }

    public void addPost(String text, String date) {

        //The (first) post request sends a duplicate post. This eliminates the problem
    	callingaddpost++;
    	if(callingaddpost>1) {
    		return;
    	}

        //Getting the part of the body after the =
        String[] parts = text.split("=");
        String inputText = "";

        try{
        	inputText = URLDecoder.decode(parts[1], "UTF8");
            //Getting the part of the body before the &, after that is the post kind parameter
            String[] parts2 = inputText.split("&");
            inputText = parts2[0];

            try {
                postKind = parts[2];
            }
            catch(IndexOutOfBoundsException p) {
                //Students dont select this parameter so it creates an exception
                postKind = "Post";
            }

    	}catch(UnsupportedEncodingException a) {
    		a.printStackTrace();
    	}

        //Announcements dont print the username
        String user = "";
        if(postKind.equals("Post")) {
            user = "<h3>" + mitsos.getUser() + "  |  " + date + "</h3>";
            
            //A teacher post has the username coloured orange
            if(db.getAccountKind(mitsos.getUser()).equals("Teacher")) {
                user = "<h3><font color=\"#ff6816\">" + mitsos.getUser() + "</font>  |  " +  date + "</h3>";
            }
        }
        else {
            //Adding the date of the post to the post
            inputText = "<b>" + date + "<br />" + inputText + "</b>";
        }

        String postBeginning = "<div class=\"box\"><span class=\"icon icon-comments\"></span>" + user;       
        String post = "<p>" + inputText + "</p></div>";

        db.insertPost(actualLesson, postBeginning, post, postKind);
    }


    public void getTables() {
        postList = db.printAllPosts(actualLesson);
        announcementList = db.printAllAnnouncements(actualLesson);
    }


    public void getPostKind(String kindofPost) {
        postKind = kindofPost;
    }

}
