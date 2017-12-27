import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nguye on 12/08/2017.
 */


public class Welcome extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Welcome");
        //System.out.println("clgt");
        
        PrintWriter writer = resp.getWriter();
        writer.write("<html><body><h1>Welcome !!! </h1>");
        writer.printf("<img src = %s>","images/ahihi1.png");
        writer.write("</body></html>");
    }
}
