package com.roneymis.msdemo.it;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("checkstyle:abbreviationaswordinname")
public class HelloWorldApplicationIT {

  @Autowired
  private TestRestTemplate template;

  @Test
  public void contextLoads() throws Exception {
  }

  @Test
  public void testRequest() throws Exception {
    ResponseEntity<String> responseEntity = template.getForEntity("/", String.class);
    assertTrue(responseEntity.toString().contains("Hello World!"));
    ResponseEntity<String> responseEntityA = template.getForEntity("/A", String.class);
    assertTrue(responseEntityA.toString().contains("*****ALPHA*****"));
    ResponseEntity<String> responseEntityB = template.getForEntity("/B", String.class);
    assertTrue(responseEntityB.toString().contains("*****BETA*****"));
    ResponseEntity<String> responseEntityC = template.getForEntity("/C", String.class);
    assertTrue(responseEntityC.toString().contains("*****CHARLIE*****"));
    ResponseEntity<String> responseEntityD = template.getForEntity("/D", String.class);
    assertTrue(responseEntityD.toString().contains("*****DELTA*****"));
    ResponseEntity<String> responseEntityE = template.getForEntity("/E", String.class);
    assertTrue(responseEntityE.toString().contains("*****ECHO*****"));
    ResponseEntity<String> responseEntityF = template.getForEntity("/F", String.class);
    assertTrue(responseEntityF.toString().contains("*****FOXTROT*****"));
  }
}
