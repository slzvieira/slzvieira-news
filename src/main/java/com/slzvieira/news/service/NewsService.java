package com.slzvieira.news.service;

import com.slzvieira.news.model.News;
import com.slzvieira.news.repository.NewsRepository;
import org.springframework.stereotype.Service;

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
}
