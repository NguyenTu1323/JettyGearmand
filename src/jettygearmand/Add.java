import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nguye on 13/08/2017.
 */
public class Add extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));
        int ans = a + b;
        resp.setHeader("ahihi","dongok");
        resp.setHeader("leuleu","dongu");
        resp.setHeader("Content-Type","text/html");
        resp.getWriter().printf("<html><body><p>Result = %s</p></body></html>",Integer.toString(ans));
        String contentLength = req.getHeader("Content-Length");
        resp.getWriter().printf("<p>length = %s</p>",contentLength);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
