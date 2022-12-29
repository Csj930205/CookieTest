package com.example.aoptest.util;


import com.example.aoptest.entity.User;
import com.example.aoptest.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class CommonUtil {
    private final UserService userService;
    private static final String SEP = "|";

    /**
     * 쿠키 받아오기(공통)
     * @param request
     * @return
     */
    public Cookie commonCookie (HttpServletRequest request) {
        Cookie[] myCookie = request.getCookies();
        Cookie commonCookie = null;

        if (myCookie != null) {
            for (Cookie cookie : myCookie) {
                commonCookie = cookie;
            }
        }
        return commonCookie;
    }

    /**
     * Cookie 생성 및 삭제 (같은 요청이 2번 올 경우 쿠키 삭제 등)
     * @param seq
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> createCookie(Long seq, HttpServletRequest request, HttpServletResponse response) {

        Cookie commonCookie = commonCookie(request);

        Map<String, Object> result = new HashMap<>();

        if (commonCookie != null) {
            String cookieSeq = SEP + seq + SEP;
            String getCookie = commonCookie.getValue();

            String cookieDecoding = new String(Base64.getDecoder().decode(getCookie));

            if (!cookieDecoding.contains(cookieSeq)) {
                User detailUser = userService.countUp(seq);

                String cookieSeqEncoding = Base64Utils.encodeToString(cookieSeq.getBytes());

                commonCookie.setValue(getCookie + cookieSeqEncoding);
                commonCookie.setPath("/");
                commonCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(commonCookie);
                response.setHeader("Access-control-Allow-Credentials", "true");

                result.put("result", "success");
                result.put("code", HttpStatus.OK);
                result.put("count", detailUser.getCountUser());
                result.put("delYn", detailUser.isDelYn());

                return result;
            } else {
                User detailUser = userService.countDown(seq);

                 String replaceCookie = cookieDecoding.replace(cookieSeq, "");
                 String replaceCookieEncoding = Base64Utils.encodeToString(replaceCookie.getBytes());

                 Cookie kc = new Cookie("seq", replaceCookieEncoding);
                 kc.setMaxAge(60 * 60 * 24);
                 kc.setPath("/");
                 response.addCookie(kc);
                 response.setHeader("Access-control-Allow-Credentials", "true");

                 if (kc.getValue().equals("")) {
                     kc.setMaxAge(0);
                     response.addCookie(kc);
                 }

                result.put("result", "success");
                result.put("code", HttpStatus.OK);
                result.put("count", detailUser.getCountUser());
                result.put("delYn", detailUser.isDelYn());

                return result;
            }
        } else {
            User detailUser = userService.countUp(seq);
            String testEncoding = Base64Utils.encodeToString((SEP + seq + SEP).getBytes());

            Cookie createCookie = new Cookie("seq", testEncoding);
            createCookie.setMaxAge(60 * 60 * 24);
            createCookie.setPath("/");
            response.addCookie(createCookie);
            response.setHeader("Access-control-Allow-Credentials", "true");

            result.put("result", "success");
            result.put("code", HttpStatus.OK);
            result.put("count", detailUser.getCountUser());
            result.put("delYn", detailUser.isDelYn());

            return result;
        }
    }

    /**
     * 이벤트 리스트 작성
     * @param request
     * @return
     */
    public Map<String, Object> hosbList(HttpServletRequest request) {
        Cookie commonCookie = commonCookie(request);
        Map<String, Object> result = new HashMap<>();

        List<User> topThreeList = userService.topThreeList();
        List<User> randomList = userService.randomList();

        if (commonCookie != null) {
            byte[] cookieDecoding = Base64Utils.decode(commonCookie.getValue().getBytes());
            String cookie = new String(cookieDecoding);

            String[] split = StringUtils.split(cookie, "|");

            for (int i = 0; i<split.length; i++) {
                Long seq = Long.parseLong(split[i]);
                for (User topThreeCheck : topThreeList) {
                    if (topThreeCheck.getSeq().equals(seq) && topThreeCheck.isDelYn() == false) {
                        topThreeCheck.delete();
                    }
                }
                for (User randomCheck : randomList) {
                    if (randomCheck.getSeq().equals(seq) && randomCheck.isDelYn() == false) {
                        randomCheck.delete();
                    }
                }
            }
        }

        result.put("result", "success");
        result.put("code", HttpStatus.OK);
        result.put("topThreeList", topThreeList);
        result.put("randomList", randomList);

        return result;
    }
}
