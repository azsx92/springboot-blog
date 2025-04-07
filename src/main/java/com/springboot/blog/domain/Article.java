package com.springboot.blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//entity란 JPA에서 데이터베이스 테이블과 매핑되는 클래스를 나타냅니다.
// 이 클래스는 객체 지향적으로 데이터를 다루기 위해 데이터베이스의 한 행(row)을 객체로 표현하는 역할
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article { // 기사라는 뜻

    @Id
    // strategy 전략
    //GeneratedValue 생성된 가치
    //id 값을 데이터베이스에서 자동으로 증가시키는 방식
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    //제목
    @Column(name = "title", nullable = false) // 'title' 이라는 not null 컬럼과 매핑
    private String title;
    // 내용
    @Column(name = "content", nullable = false)
    private String content;

    @Builder // 빌더 패턴으로 객체 생성
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
