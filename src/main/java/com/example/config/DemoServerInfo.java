package com.example.config;

import lombok.Data;

@Data
public class DemoServerInfo {

    /**
     * 服务器端口
     */
   private  int port = 56789;
    /**
     * 服务器地址
     */
   private String host = "localhost";
}
