package es.slzvieira.study.news.service;

import es.slzvieira.study.news.model.News;
import es.slzvieira.study.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository repository;

    public News getRandomNews() {

        final int totalNews = repository.findTotal();
        final int randomIndex = (int) (Math.random() * totalNews);

        log.info("Recovering new no. {}...", randomIndex);
        News news = repository.findById(randomIndex);
        log.info("Title: {}", news.getTitle());

        return news;
    }
}
