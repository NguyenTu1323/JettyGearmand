import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.List;
import jettygearmand.ProcessImage;
import org.apache.commons.io.FileUtils;
import org.gearman.client.GearmanJob;
import org.gearman.client.GearmanJobImpl;
import org.gearman.util.ByteUtils;

/**
 * Created by nguye on 13/08/2017.
 */
public class FileUpload extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println("get file upload");
        resp.setHeader("Content-Type","text/html");
        PrintWriter writer = resp.getWriter();
        writer.printf("<html>\n" +
                "<body>\n" +
                "<form action = \"fileUpload\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                "<input type=\"file\" name=\"file\" multiple/>\n" +
                "<input type=\"submit\">\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //ystem.out.println("post file upload");
        ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
        
        //String directory = "/home/tunguyen1323/NetBeansProjects/JettyGearmand/src/images/";
        String directory = "/home/cpu10663-local/Desktop/JettyGearmand/src/images/";
        
        
        try {
            List<FileItem> files =  servletFileUpload.parseRequest(req);

            for(FileItem file : files){
               // System.out.println("ahihi1");
                file.write(new File(directory + file.getName()));
               // System.out.println("ahihi2");
                System.out.printf("Uploaded file %s\n",file.getName());
                System.out.printf("file size %d\n",file.getSize());
                
                
                byte[] bufImage = FileUtils.readFileToByteArray(new File(directory + file.getName()));
                
                byte[] bufImageName = ByteUtils.toAsciiBytes(file.getName());
                
                System.out.printf("buf image length = %d buf imagename length = %d\n", bufImage.length,bufImageName.length);
                
                byte[] buf = new byte[bufImage.length + 1 + bufImageName.length];
           
                
                System.arraycopy(bufImageName, 0, buf, 0, bufImageName.length);
                buf[bufImageName.length] = '\0';
                System.arraycopy(bufImage, 0, buf, bufImageName.length+1, bufImage.length);
                
                System.out.printf("buf length = %d\n",buf.length);
                
                GearmanJob job = GearmanJobImpl.createBackgroundJob(ProcessImage.class.getName(),buf,null);
                JettyServer.client.submit(job);
                System.out.println("ok done submit job");
                
                
                
            }

            resp.sendRedirect("/fileUpload");
            

        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
