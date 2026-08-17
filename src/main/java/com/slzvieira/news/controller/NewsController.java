package com.slzvieira.news.controller;

import com.slzvieira.news.model.News;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/news/v1")
public class NewsController {

	@GetMapping("/random")
	public ResponseEntity<News> getRandom() {
		News news = News.builder()
				.id(382)
				.title("A Vaca Vitória")
				.content("Era uma vez a vaca Vitória... Deu um peido e acabou a história")
				.date(LocalDate.of(1992, 5, 27))
				.build();
		return ResponseEntity.ok(news);
	}
}
