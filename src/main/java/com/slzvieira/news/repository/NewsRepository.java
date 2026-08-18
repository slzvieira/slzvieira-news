package com.slzvieira.news.repository;

import com.slzvieira.news.model.News;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        String category = block.get(2);
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
