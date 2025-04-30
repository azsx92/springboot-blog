package com.springboot.blog.config.exception;

import com.springboot.blog.config.ErrorCode;

public class ArticleNotFoundException extends NotFoundException{
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
