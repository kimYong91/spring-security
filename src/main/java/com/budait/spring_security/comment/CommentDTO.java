package com.budait.spring_security.comment;


import com.budait.spring_security.article.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO : 데이터 전속 객체 (실제 데이터를 보여주는것이 아니라서 데이터는 받지만 내가 원하는것만 골라서 데이터를 사용자에게 보여줄 수 있음)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private String author;
    private Long articleId;

    // DTO -> 엔티티(엔티티에 @Builder 적용, 빌터 패턴 사용)
    public Comment toEntity(Article article){
        return Comment.builder()
                .id(id)
                .content(content)
                .author(author)
                .article(article)
                .build();
    }
}
