package com.springboot.blog.service;

import com.springboot.blog.domain.Member;
import com.springboot.blog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository; //bean di

    public void test() {
        //
        // 1. 생성
        memberRepository.save(new Member(1L, "A"));

        // 2. read
        Optional<Member> member = memberRepository.findById(1L);
        List<Member> members = memberRepository.findAll();

        // 3. delete
        memberRepository.deleteById(1L);
        System.out.println("member :" + member);
    }
}
