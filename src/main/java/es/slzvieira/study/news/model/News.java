package es.slzvieira.study.news.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private LocalDate publishedAt;
}
