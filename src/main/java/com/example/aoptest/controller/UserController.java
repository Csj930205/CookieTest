package com.example.aoptest.controller;

import com.example.aoptest.entity.User;
import com.example.aoptest.service.UserService;
import com.example.aoptest.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final CommonUtil commonUtil;

    @GetMapping("list")
    public ResponseEntity<Map<String, Object>> userList(HttpServletRequest request) {
        Map<String, Object> result = commonUtil.hosbList(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User saveUser = userService.saveUser(user);
        return new ResponseEntity<>(saveUser, HttpStatus.OK);
    }

    @PatchMapping("/countUser/{seq}")
    public ResponseEntity<Map<String, Object>> countUp(@PathVariable("seq") Long seq, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = commonUtil.createCookie(seq, request, response);


        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
