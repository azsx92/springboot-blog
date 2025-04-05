package com.springboot.blog.service;

import com.springboot.blog.domain.Member;
import com.springboot.blog.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    void test1() {
        //
        // 1. 생성
        memberRepository.save(new Member(1L, "A"));

        // 2. read
        Optional<Member> member = memberRepository.findById(1L);
        List<Member> members = memberRepository.findAll();
        System.out.println("member :" + member.toString());
        System.out.println("members :" + members.toString());
        // 3. delete
        memberRepository.deleteById(1L);
        System.out.println("member :" + member);
    }
}