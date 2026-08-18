package com.slzvieira.news.controller;

import com.slzvieira.news.model.News;
import com.slzvieira.news.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping({"", "/"})
	public ResponseEntity<Page<News>> getAll(Pageable pageable) {
		return ResponseEntity.ok(service.getAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<News> getById(@PathVariable int id) {
		return service.getById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
