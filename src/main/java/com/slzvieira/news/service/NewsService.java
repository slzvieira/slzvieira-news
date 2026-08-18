package com.slzvieira.news.service;

import com.slzvieira.news.model.News;
import com.slzvieira.news.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public News getRandom() {
        int index = (int) (Math.random() * newsRepository.getNewsCount());
        return newsRepository.findByIndex(index);
    }

    public Page<News> getAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public Optional<News> getById(int id) {
        return newsRepository.findById(id);
    }
}
