package cn.yiwen.utiltest;

import cn.yiwen.util.SendEmail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendEmailTest {

    @Test
    void send() {
        try {
            String code=SendEmail.send("2543422509@qq.com");
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}