package es.slzvieira.study.news.service;

import es.slzvieira.study.news.model.News;
import es.slzvieira.study.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository repository;

    public News getRandomNews() {

        final int totalNews = repository.findTotal();
        final int randomIndex = (int) (Math.random() * totalNews);

        return repository.findById(randomIndex);
    }
}
