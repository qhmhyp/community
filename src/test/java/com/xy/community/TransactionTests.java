package com.xy.community;

import com.xy.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTests {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSava1() {
        Object obj = alphaService.save1();
        System.out.println(obj);
    }

    @Test
    public void testSava2() {
        Object obj = alphaService.save2();
        System.out.println(obj);
    }

}
