package com.point.web.listener;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;

import com.point.web.util.MMSTool;

public class ResourceListener {  
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);  
    private WatchService ws;  
    private String listenerPath;  
    private ResourceListener(String path,ApplicationContext ac) {  
        try {  
            ws = FileSystems.getDefault().newWatchService();  
            this.listenerPath = path;  
            start(ac);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private void start(ApplicationContext ac) {  
        fixedThreadPool.execute(new FolderListner(ws,this.listenerPath,ac));  
    }  
  
    public static void addListener(String path,ApplicationContext ac) throws IOException {  
        ResourceListener resourceListener = new ResourceListener(path,ac);  
        Path p = Paths.get(path);  
        p.register(resourceListener.ws,StandardWatchEventKinds.ENTRY_CREATE);  
    }  
      
  
    public static void main(ApplicationContext ac) throws IOException {
    	
        ResourceListener.addListener(MMSTool.getMMSMap().get("uppath"),ac);  
    }  
}  
  
