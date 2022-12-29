package com.example.aoptest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

@SpringBootTest
class AopTestApplicationTests {

    @Test
    void contextLoads(HttpServletResponse response) {
        final String SEP = "|";

        String test = SEP + "1" + SEP;
        String cookieEncoding = Base64Utils.encodeToString(test.getBytes());

        Cookie cookie = new Cookie("seq", cookieEncoding);

        response.addCookie(cookie);

        byte[] setCookie = Base64Utils.decode(cookieEncoding.getBytes());
        String cookieDecoding = new String(setCookie);


        System.out.println(cookieEncoding);
        System.out.println(cookieDecoding);
    }

}
