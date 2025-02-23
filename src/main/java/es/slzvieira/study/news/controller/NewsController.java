package es.slzvieira.study.news.controller;

import es.slzvieira.study.news.model.News;
import es.slzvieira.study.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService service;

    @GetMapping
    public ResponseEntity<News> findOne() {
        return ResponseEntity.ok(service.getRandomNews());
    }
}
