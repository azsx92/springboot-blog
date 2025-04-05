package com.springboot.blog;

import com.springboot.blog.domain.Member;
import com.springboot.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트 생성
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class EntityMangerTest {

    @Autowired
    EntityManager em;


    @Test
    public void getAllMembers() throws Exception {
        // given
        // 1. 엔티티 매니저가 엔티티를 관리하지 않는 상태(비영속 상태)
        Member member = new Member(1L,"홍길동");

        // when
        // 2. 엔티티가 관리되는 상태
        em.persist(member);
        // 3. 엔티티 객체가 분리된 상태
        em.detach(member);
        // 4. 엔티티 객체가 삭제된 상태
        em.remove(member);



    }
}