package com.slzvieira.news.controller;

import com.slzvieira.news.model.News;
import com.slzvieira.news.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/news/v1")
public class NewsController {

	private final NewsService service;

	public NewsController(NewsService service) {
		this.service = service;
	}

	@GetMapping("/random")
	public ResponseEntity<News> getRandom() {
		return ResponseEntity.ok(service.getRandom());
	}
}
