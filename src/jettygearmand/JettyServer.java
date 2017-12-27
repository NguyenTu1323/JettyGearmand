import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javafx.scene.NodeBuilder;
import jettygearmand.Home;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.gearman.client.GearmanClient;
import org.gearman.client.GearmanClientImpl;
import org.gearman.common.GearmanNIOJobServerConnection;

/**
 * Created by nguye on 12/08/2017.
 */


public class JettyServer {
    
    static GearmanClient client = new GearmanClientImpl();
   

    public static void main(String[] args) throws Exception {
        //System.out.println("clgt");
        
                  
        client.addJobServer(new GearmanNIOJobServerConnection("localhost",4730));

        Server server = new Server(9090);

        ServletHandler handler = new ServletHandler();

        handler.addServletWithMapping(Welcome.class,"/welcome");
        handler.addServletWithMapping(Add.class,"/add");
        // localhost;8080/add?a=10&b=20
        handler.addServletWithMapping(getImage.class,"/images/*");

        handler.addServletWithMapping(FileUpload.class,"/fileUpload");
        
        handler.addServletWithMapping(Home.class, "/");

        server.setHandler(handler);

        server.start();

        server.join();

    }

}
