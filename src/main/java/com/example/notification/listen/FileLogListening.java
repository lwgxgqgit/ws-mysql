package com.example.notification.listen;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * App启动的时候，自动调用这个方法
 */
@Service
public class FileLogListening implements ApplicationContextAware {

    private long lastTimeFileSize = 0;  //上次文件大小


    //websocket发送消息的类
    @Autowired
    private SimpMessagingTemplate template;

    private ThreadLocal<String> localString = new ThreadLocal<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String logPath = "D:\\work-heikn\\其他代码\\java\\技术栈\\springboot整合websocket\\notification-service-master\\log\\a.log";

        try {
            File logFile = ResourceUtils.getFile(logPath);
            final RandomAccessFile randomFile = new RandomAccessFile(logFile, "rw");
            //指定文件可读可写
            ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
            exec.scheduleWithFixedDelay(new Runnable() {
                public void run() {
                    try {
                        randomFile.seek(lastTimeFileSize); //定位文件的位置
                        String all = "";
                        String tmp = "";
                        while ((tmp = randomFile.readLine()) != null) {
                            String log=new String(tmp.getBytes("ISO8859-1"));
                            all = all + "\r\n" + log;
                           // template.convertAndSend("/topic/pushNotification", log);
                        }



                       if(localString.get() == null){
                            template.convertAndSend("/topic/pushNotification", all);
                            localString.set(all);
                        }else {
                            if(!localString.get().equals(all)){
                                template.convertAndSend("/topic/pushNotification", all);
                                localString.set(all);
                            }
                        }


//                        lastTimeFileSize = randomFile.length();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
        }catch (IOException e){
            e.printStackTrace();
        }

        /**
         * 1.监听一个文件，每分钟轮训一次。如果有数据，就直接掉一共send
         */
    }

    /**
     * 监听日志文件
     *
     * @throws IOException
     */
    @PostConstruct
    public void start() throws IOException {

    }
}