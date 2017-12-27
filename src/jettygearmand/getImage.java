import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by nguye on 13/08/2017.
 */
public class getImage  extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String imageName = req.getPathInfo().substring(1);
       // System.out.printf("%s\n",imageName);
        String directory = "/home/cpu10663-local/Desktop/JettyGearmand/src/images/";
        RandomAccessFile file = new RandomAccessFile(directory+imageName,"r");
        FileChannel fileChannel = file.getChannel();

        resp.setHeader("Content-Type","image/png");
        resp.setHeader("ahihi","anh png ne");
        resp.setHeader("Content-Length",Long.toString(fileChannel.size()));

        ByteBuffer buf = ByteBuffer.allocate(1024);
        OutputStream os = resp.getOutputStream();
        int dataCount = 0;
        while((dataCount = fileChannel.read(buf)) != -1){
           //System.out.printf("read %d\n",dataCount);
            buf.flip();
            os.write(buf.array());
            buf.clear();


        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
