package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.CommentDAO;
import sportal.model.service.dto.CommentServiceDTO;
import sportal.model.service.dto.UserServiceDTO;
import sportal.model.db.pojo.Comment;
import sportal.model.db.repository.CommentRepository;
import sportal.model.service.ICommentService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleServiceImpl articleService;
    @Autowired
    private CommentDAO commentDAO;

    @Override
    public long addComment(CommentServiceDTO serviceDTO, UserServiceDTO user) {
        this.articleService.existsById(serviceDTO.getArticleId());
        Comment comment = new Comment(serviceDTO, user.getId());
        comment = this.commentRepository.save(comment);
        comment.setUserName(user.getUsername());
        return comment.getArticleId();
    }

    @Override
    public long edit(CommentServiceDTO serviceDTO, UserServiceDTO user) {
        Comment existsComment = this.commentRepository.findById(serviceDTO.getId())
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));
        if (user.getId() != existsComment.getUserId()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        existsComment.setFullCommentText(serviceDTO.getText());
        existsComment.setDatePublished(Timestamp.valueOf(LocalDateTime.now()));
        Comment editComment = this.commentRepository.save(existsComment);
        return editComment.getId();
    }

    @Override
    public long delete(long commentId, UserServiceDTO user) {
        Comment existsComment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));
        if (user.getId() != existsComment.getUserId()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        this.commentRepository.deleteById(commentId);
        return existsComment.getArticleId();
    }

    @Override
    public long deleteFromEditor(long commentId) {
        Comment existsComment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));
        this.commentRepository.deleteById(existsComment.getId());
        return existsComment.getArticleId();
    }

    @Override
    public List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws SQLException {
        List<Comment> comments = this.commentDAO.allCommentsByArticleId(articleId);
        return CommentServiceDTO.fromPOJOToDTO(comments);
    }

    @Override
    public CommentServiceDTO getCommentsById(long commentId) throws SQLException {
        Comment existsComment = this.commentDAO.findById(commentId);
        if (existsComment == null) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        return new CommentServiceDTO(existsComment);
    }

    @Override
    public boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException {
        return this.commentDAO.existsVoteForThatCommentFromThisUser(commentId, userId);
    }

    @Override
    public void existsById(long commentId) {
        if (!this.commentRepository.existsById(commentId)) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
    }
}
