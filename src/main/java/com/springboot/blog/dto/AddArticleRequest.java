package com.springboot.blog.dto;

import com.springboot.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파마미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {

    private String title;
    private String content;
    //toEntity는 빌더 패턴을 사용해 DTO로 만들어 주는 메서드
    public Article toEntity() { //생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
