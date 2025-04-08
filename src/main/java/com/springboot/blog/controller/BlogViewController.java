package com.springboot.blog.controller;

import com.springboot.blog.domain.Article;
import com.springboot.blog.dto.AddArticleRequest;
import com.springboot.blog.dto.ArticleListViewResponse;
import com.springboot.blog.dto.ArticleResponse;
import com.springboot.blog.dto.UpdateArticleRequest;
import com.springboot.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles); // 블로그 글 리스트 저장

        return "articleList"; // articleList.html 조회
    }
}
