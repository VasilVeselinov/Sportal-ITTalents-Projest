package sportal.model.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentEditDTO {

    private long oldCommentId;
    private String fullCommentText;
    private String newTextOfComment;
}
