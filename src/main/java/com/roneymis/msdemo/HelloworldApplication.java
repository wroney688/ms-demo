
package com.roneymis.msdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloworldApplication {
  @RequestMapping("/")
  public String home() {
    return "Hello World!";
  }

  @RequestMapping("/A")
  public String methodA() {
    methodA demo = new methodA();                                                                                                                                     
    return demo.foo();
  }

  @RequestMapping("/B")
  public String methodB() {
    methodB demo = new methodB();                                                                                                                                     
    return demo.foo();
  }

  @RequestMapping("/C")
  public String methodC() {
    methodC demo = new methodC();                                                                                                                                     
    return demo.foo();
  }

  @RequestMapping("/D")
  public String methodD() {
    methodD demo = new methodD();                                                                                                                                     
    return demo.foo();
  }

  @RequestMapping("/E")
  public String methodE() {
    methodE demo = new methodE();                                                                                                                                     
    return demo.foo();
  }

  @RequestMapping("/F")
  public String methodF() {
    methodF demo = new methodF();                                                                                                                                     
    return demo.foo();
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloworldApplication.class, args);
  }
}
