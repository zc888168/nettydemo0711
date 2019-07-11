package com.example.service;

import com.example.client.TimerClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class DemoService {
    @Resource
    private TimerClient timerClient;

    public void setMsg(String msg){
        if (StringUtils.isEmpty(msg)){
            msg ="nihao";
        }
        timerClient.sendMessage(msg);
    }
}
