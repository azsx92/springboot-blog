package com.springboot.blog.service;

import com.springboot.blog.domain.Member;
import com.springboot.blog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    MemberRepository memberRepository; //bean di

    public List<Member> getAllMember() {
        return memberRepository.findAll(); //member list
    }
}
