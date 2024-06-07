package com.budait.spring_security.comment;


import com.budait.spring_security.article.Article;
import com.budait.spring_security.article.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    // Entity -> DTO로 변환하여 전달
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(Comment::toDTO).toList();
    }

    public CommentDTO getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null).toDTO();
    }

    @Transactional
    public CommentDTO createComment(CommentDTO dto) {
        // Article ID가 존재하지 않는 경우
        Article article = articleRepository.findById(dto.getArticleId()).orElse(null);
        if (article == null) {
            throw new RuntimeException("존재하지 않는 Article");
        }

        // DTO -> 엔티티 변환 (양자 택일)
        // 1. 생성 메서드 사용 DTO -> 엔티티 변환
        // Comment comment = Comment.createComment(dto);
        // 2. toEntity 사용 변환
        Comment comment = dto.toEntity(article);

        Comment saved = commentRepository.save(comment);
        return saved.toDTO();
    }

    @Transactional
    public CommentDTO updateComment(Long id, Comment updateComment) {
        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment != null) {
            if (updateComment.getContent() != null) {
                comment.setContent(updateComment.getContent());
            }
            if (updateComment.getAuthor() != null) {
                comment.setAuthor(updateComment.getAuthor());
            }
            Comment saved = commentRepository.save(comment);
            return saved.toDTO();
        } else {
            return null;
        }
    }

    @Transactional
    public Boolean deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            commentRepository.delete(comment);
            return true;
        } else {
            return false;
        }
    }
}
