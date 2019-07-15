package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoForLogControler {
   private static final Logger logger =LoggerFactory.getLogger(DemoForLogControler.class);
   @GetMapping("/log")
   public void index(){
       logger.debug("DemoForLogControler");
   }
}
