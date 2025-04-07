package com.springboot.blog.service;

import com.springboot.blog.domain.Article;
import com.springboot.blog.dto.AddArticleRequest;
import com.springboot.blog.dto.UpdateArticleRequest;
import com.springboot.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// RequiredArgsConstructor 빈을 생성자로 생성하는 롬복에서 지원하는 애너테이션입니다.
@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 서블릿 컨테이너에 등록
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
@Transactional
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
@Transactional
    public Article update(Long id , UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found :" + id));
        article.update(request.getTitle(), request.getContent());
        return article;
    }
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void  delete(Long id) {
        blogRepository.deleteById(id);
    }
}
