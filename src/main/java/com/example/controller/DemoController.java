package com.example.controller;

import com.example.client.TimerClient;
import com.example.server.TimerServer;
import com.example.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Resource
    private DemoService demoService;
    @Resource
    private TimerServer timerServer;
    @Resource
   private TimerClient timerClient;
    @RequestMapping("/send")
    public String sendMsg(@RequestParam(value = "msg") String msg){

        demoService.setMsg(msg);
        return "success";
    }
    @RequestMapping("/startServer")
    public String startServer(){
        timerServer.start();
        return "success";
    }
    @RequestMapping("/startClient")
    public String startClient(){
        try {
            timerClient.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
