/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettygearmand;

import org.gearman.common.GearmanNIOJobServerConnection;
import org.gearman.worker.GearmanWorker;
import org.gearman.worker.GearmanWorkerImpl;

/**
 *
 * @author tunguyen1323
 */
public class Worker {
    
    public static int testCC = 100;
    
    public static void main(String[] args) {
        System.out.println("Worker");
        
        GearmanWorker worker = new GearmanWorkerImpl();
        worker.addServer(new GearmanNIOJobServerConnection("localhost",4730));
        
        worker.registerFunction(ProcessImage.class);
        
        worker.work();
        
        
    }
    
}
