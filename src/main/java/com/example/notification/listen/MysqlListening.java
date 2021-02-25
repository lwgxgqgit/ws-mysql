package com.example.notification.listen;

import com.alibaba.fastjson.JSON;
import com.example.notification.util.MysqlUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



/**
 * @author lwg
 */
//ApplicationContextAware ： 程序启动就执行setApplicationContext里面的方法
@Service
public class MysqlListening implements ApplicationContextAware {


    /**
     * 服务端给客户端发送消息的类
     */
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 线程变量
     */
    private ThreadLocal<String> localString = new ThreadLocal<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        try {

            ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
            /**
             * 每1s查询下数据库，比对上次查询的数据。如果不同就输出到页面中
             */
            exec.scheduleWithFixedDelay(new Runnable() {
                public void run() {
                    try {
                        //查询数据库信息
                        List<String> rs = MysqlUtil.getNameList();
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void start() throws IOException {

    }
}