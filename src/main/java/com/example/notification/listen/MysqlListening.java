package com.example.notification.listen;

import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



/**
 * App启动的时候，自动调用这个方法
 */
@Service
public class MysqlListening implements ApplicationContextAware {



    //websocket发送消息的类
    @Autowired
    private SimpMessagingTemplate template;

    private ThreadLocal<String> localString = new ThreadLocal<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


        try {


            //指定文件可读可写
            ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
            exec.scheduleWithFixedDelay(new Runnable() {
                public void run() {
                    try {

                        Random random = new Random();
                        int i = random.nextInt(5); //0-5
                        List<String> list = new ArrayList<String>();
                        list.add("a");
                        list.add("b");
                        list.add("c");
                        list.add("d");
                        list.add("e");
                        list.add("f");
                        List<String> rs = new ArrayList<>();
                        for (int j = 0; j < i; j++) {
                            rs.add(list.get(j));
                        }

                        String t = JSON.toJSONString(rs);



                        if(localString.get() == null){
                            template.convertAndSend("/topic/pushNotification", t);
                            localString.set(t);
                        }else {
                            if(!localString.get().equals(t)){
                                template.convertAndSend("/topic/pushNotification", t);
                                localString.set(t);
                            }
                        }


//                        lastTimeFileSize = randomFile.length();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
        }catch (Exception e){
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