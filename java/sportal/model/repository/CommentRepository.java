package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
