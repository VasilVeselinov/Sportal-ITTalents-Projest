package sportal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.user.UserLoginModel;
import sportal.controller.models.user.UserResponseModel;
import sportal.exception.*;
import sportal.model.service.IUserService;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Positive;
import java.sql.SQLException;
import java.util.List;

import static sportal.util.GlobalConstants.HAS_AUTHORITY_EDITOR;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    @Autowired
    private IUserService userService;
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @DeleteMapping(value = "/remove/{" + USER_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public UserResponseModel removeUser(
            @PathVariable(name = USER_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long userId,
            HttpSession session) {
        LOGGER.info("DELETE /users/remove/{" + USER_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new UserResponseModel(this.userService.deleteById(userId, logUser.getId()));
    }

    @GetMapping(value = "/all")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public List<UserResponseModel> allUsers(
            @RequestParam("page") @Positive(message = MASSAGE_FOR_INVALID_NUMBER_OF_PAGE) int page) {
        LOGGER.info("GET /users/all");
        return UserResponseModel.fromDTOToModel(this.userService.findAll(page));
    }

    @GetMapping(value = "/{" + USER_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public UserResponseModel getUserById(
            @PathVariable(name = USER_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long userId) {
        LOGGER.info("GET /users/{" + USER_ID + "}");
        return new UserResponseModel(this.userService.findById(userId));
    }

    @PostMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> likeOfArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws SQLException, BadRequestException {
        LOGGER.info("POST /users/like_articles/{" + ARTICLE_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.likeArticle(articleId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> removeLikeOfArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws BadRequestException {
        LOGGER.info("DELETE /users/like_articles/{" + ARTICLE_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.deleteVoteForArticle(articleId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> likeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        LOGGER.info("POST /users/like_comments/{" + COMMENT_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.likeComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> dislikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        LOGGER.info("POST /users/dislike_comments/{" + COMMENT_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.dislikeComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeLikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws BadRequestException {
        LOGGER.info("DELETE /users/like_comments/{" + COMMENT_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.deleteLikeOfComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeDislikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws BadRequestException {
        LOGGER.info("DELETE /users/dislike_comments/{" + COMMENT_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.deleteDislikeOfComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
