package jettygearmand;


import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kyotocabinet.DB;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.util.ByteArrayBuffer;
import org.gearman.util.ByteUtils;
import org.gearman.worker.AbstractGearmanFunction;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tunguyen1323
 */
public class ProcessImage extends AbstractGearmanFunction{

    @Override
    public GearmanJobResult executeFunction() {
        
        System.load("/usr/local/lib/libkyotocabinet.so");
        
        System.out.printf("gearmand process image execute\n");
        //String directory = "/home/tunguyen1323/NetBeansProjects/JettyGearmand/src/thumbnail/";
        String directory = "/home/cpu10663-local/Desktop/JettyGearmand/src/thumbnail/";
        
        ByteArrayBuffer bab = new ByteArrayBuffer((byte[]) this.data);
        List<byte[]> params = bab.split(new byte[]{'\0'});
        
        int bufImageLength = 0;
        for(int i = 1;  i < params.size() ; i++){
            bufImageLength+=params.get(i).length;
            bufImageLength++;
        }
        bufImageLength--;
        //bufImageLength*=2;
        
       // System.out.printf("params length = %d\n",params.size());
        //System.out.printf("buf image length = %d\n",bufImageLength);
        
        byte[] bufImageName = params.get(0);
        String nameFile = ByteUtils.fromAsciiBytes(bufImageName);
        System.out.printf("file name = %s\n", nameFile);
        //byte[] bufImage = params.get(1);
        
        byte[] bufImage = new byte[bufImageLength];
       // System.out.printf("buf image = %d\n", bufImage.length);
        int curLength = 0;
        for(int i =1 ; i< params.size() ; i++){
            System.arraycopy(params.get(i), 0, bufImage, curLength, params.get(i).length);
            curLength+=params.get(i).length;
            //++curLength;
            if(i < params.size() - 1){
                bufImage[curLength] = '\0';
                ++curLength;
            }
            //System.out.printf("i = %d  curLength = %d\n",i,curLength);
            //System.out.printf("aaa + " + Integer.toString(i) + "\n");
                
        }
        
        
        // serve anh goc bang kyoto
        
        
        DB db = new DB();
        
        if (!db.open("imagesDB.kch", DB.OWRITER)){
            db.open("imagesDB.kch", DB.OCREATE | DB.OWRITER);
        }
        
        // insert anh vao database 
        db.set(nameFile.getBytes(),bufImage);
        
        
        
        
        // end serve anh goc bang kyoto
        
        
       // System.out.printf("buf imagename length = %d buf image length = %d\n",bufImageName.length,bufImage.length);
        
        try {
            FileUtils.writeByteArrayToFile(new File(directory + nameFile), bufImage);
            
            Thumbnails.of(new File(directory+ nameFile))
                    .size(100,100)
                    .toFile(new File(directory + "thumbnail100-" + nameFile));
                    
            Thumbnails.of(new File(directory+ nameFile))
                    .size(200,200)
                    .toFile(new File(directory + "thumbnail200-" + nameFile));
            
            Thumbnails.of(new File(directory+ nameFile))
                    .size(400,400)
                    .toFile(new File(directory + "thumbnail400-" + nameFile));
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ProcessImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // index anh vao elasticsearch o day
        
        // bufImage la byte array 
        // nameFile la name file cua image
        
         Settings settings = Settings.builder().put("cluster.name","ahihiCluster").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("localhost",9300)));
       
        
        Instant instant = Instant.now();
        String time = instant.toString();
        System.out.printf("date = %s\n",time); 
        
    
        try {
            
            String imageDataAsString = new String(bufImage,"ISO-8859-1");
            
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("content",imageDataAsString)
                    .field("date",time)
                    .endObject();
            
            IndexResponse indexresponse = client.prepareIndex("images","user1",nameFile)
                .setSource(builder).get();
            
            System.out.printf("buf image length : %d\n", imageDataAsString.length());
            
            
            String _index = indexresponse.getIndex();
            String _type = indexresponse.getType();
            String _id = indexresponse.getId();
            long _version = indexresponse.getVersion();
            System.out.printf("index name = %s    type = %s id = %s version = %d\n",_index,_type,_id,_version);
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ProcessImage.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        
        
        
        
        
        // het index anh vao elasticsearch
        
        
        GearmanJobResult gjr = new GearmanJobResultImpl(this.jobHandle,true,new byte[0],new byte[0],new byte[0],0,0);
        
        return gjr;
    }
    
}
