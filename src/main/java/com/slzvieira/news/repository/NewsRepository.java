package com.slzvieira.news.repository;

import com.slzvieira.news.model.News;
import com.slzvieira.news.model.NewsCategory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class NewsRepository {

    private static final String NEWS_FILE = "news.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final List<News> newsList;

    public NewsRepository() {
        this.newsList = loadNews();
    }

    public int getNewsCount() {
        return newsList.size();
    }

    public News findByIndex(int index) {
        return newsList.get(index);
    }

    public Page<News> findAll(Pageable pageable) {
        int fromIndex = (int) pageable.getOffset();
        if (fromIndex >= newsList.size()) {
            return new PageImpl<>(List.of(), pageable, newsList.size());
        }
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), newsList.size());
        return new PageImpl<>(newsList.subList(fromIndex, toIndex), pageable, newsList.size());
    }

    public Optional<News> findById(int id) {
        return newsList.stream()
                .filter(news -> news.getId() == id)
                .findFirst();
    }

    private List<News> loadNews() {
        List<List<String>> blocks = splitIntoBlocks(readLines());

        List<News> news = new ArrayList<>();
        int id = 1;
        for (List<String> block : blocks) {
            news.add(parseNews(id++, block));
        }
        return news;
    }

    private List<String> readLines() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(NEWS_FILE).getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + NEWS_FILE, e);
        }
    }

    private List<List<String>> splitIntoBlocks(List<String> lines) {
        List<List<String>> blocks = new ArrayList<>();
        List<String> currentBlock = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank()) {
                if (!currentBlock.isEmpty()) {
                    blocks.add(currentBlock);
                    currentBlock = new ArrayList<>();
                }
                continue;
            }
            currentBlock.add(line);
        }
        if (!currentBlock.isEmpty()) {
            blocks.add(currentBlock);
        }
        return blocks;
    }

    private News parseNews(int id, List<String> block) {
        String title = block.get(0);
        String content = block.get(1);
        NewsCategory category = NewsCategory.fromString(block.get(2));
        LocalDate date = LocalDate.parse(block.get(3), DATE_FORMATTER);

        return News.builder()
                .id(id)
                .title(title)
                .content(content)
                .category(category)
                .date(date)
                .build();
    }
}
