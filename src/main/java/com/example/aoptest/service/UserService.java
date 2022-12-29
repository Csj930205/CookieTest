package com.example.aoptest.service;

import com.example.aoptest.entity.User;
import com.example.aoptest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> topThreeList() {
        return userRepository.findTop3ByOrderByCountUserDescSeqAsc();
    }
    public List<User> randomList() {
        return userRepository.findAllRandom();
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User countUp(Long seq) {
        User userDetail = userRepository.findById(seq).orElseThrow(() -> new RuntimeException());

        userDetail.countUp(userDetail.getCountUser());
        userDetail.delete();
        return userDetail;
    }

    @Transactional
    public User countDown(Long seq) {
        User userDetail = userRepository.findById(seq).orElseThrow(() -> new RuntimeException());

        userDetail.countDown(userDetail.getCountUser());
        userDetail.notDelete();
        return userDetail;
    }
}
