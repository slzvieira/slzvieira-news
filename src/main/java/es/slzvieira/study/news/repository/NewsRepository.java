package es.slzvieira.study.news.repository;

import es.slzvieira.study.news.model.News;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class NewsRepository {

    private static final String NEWS_RESOURCE = "/news.txt";
    private static final DateTimeFormatter NEWS_DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    private List<News> news;

    public News findById(int id) {
        return news.get(id);
    }

    public List<News> findAll() {
        return news;
    }

    public int findTotal() {
        return news.size();
    }

    @PostConstruct
    private void loadNews() throws IOException {
        news = getNewsContent()
                .stream()
                .map(this::mapNews)
                .toList();
    }

    private List<Map<String, String>> getNewsContent() throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(NEWS_RESOURCE)))) {

            final List<Map<String, String>> list = new ArrayList<>();
            Map<String, String> itemMap = new java.util.HashMap<>();
            boolean itemIncluded = false;

            while (reader.ready()) {

                final String line = reader.readLine();

                if (line.isBlank() && !itemMap.isEmpty()) {
                    itemMap = new java.util.HashMap<>();
                    itemIncluded = false;
                }

                int separatorIndex = line.indexOf(":");

                if (separatorIndex < 0) {
                    // Skip lines without separator
                    continue;
                }

                String key = cleanString(line.substring(0, separatorIndex));
                String value = cleanString(line.substring(separatorIndex + 1));
                itemMap.put(key, value);

                if (!itemIncluded) {
                    list.add(itemMap);
                    itemIncluded = true;
                }
            }

            return list;
        }
    }

    private News mapNews(Map<String, String> newsMap) {
        return News.builder()
                .title(newsMap.get("Title"))
                .description(newsMap.get("Description"))
                .url(newsMap.get("Source URL"))
                .urlToImage(newsMap.get("Image URL"))
                .publishedAt(LocalDate.parse(newsMap.get("Date"), NEWS_DATE_FORMAT))
                .build();
    }

    private String cleanString(String str) {

        String cleaned = str.trim();

        if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }

        return cleaned;
    }
}
