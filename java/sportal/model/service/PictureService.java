package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.FileManagerDAO;
import sportal.model.data_validators.PictureValidator;
import sportal.model.data_validators.SessionValidator;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.pojo.Picture;
import sportal.model.pojo.User;
import sportal.model.repository.ArticleRepository;
import sportal.model.repository.PictureRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.WRONG_REQUEST;
import static sportal.model.data_validators.AbstractValidator.THE_PICTURES_DO_NOT_EXIST;
import static sportal.model.data_validators.AbstractValidator.THIS_ARTICLE_IS_NOT_EXISTS;

@Service
public class PictureService {
    // Vasko : please fix me, if you change directory
    private static final String PACKAGE_NAME = "C:\\Users\\ACER\\Desktop\\uploadPictures\\";
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Transactional
    public List<Picture> upload(List<MultipartFile> multipartFiles, HttpSession session) throws BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        File fileCreateDirectory = new File(PACKAGE_NAME);
        if (!fileCreateDirectory.exists()) {
            fileCreateDirectory.mkdir();
        }
        List<Picture> pictures = PictureValidator.checkForValidContentType(multipartFiles);
        FileManagerDAO fileManagerDAO = new FileManagerDAO(multipartFiles, PACKAGE_NAME, pictures);
        fileManagerDAO.start();
        return this.pictureRepository.saveAll(pictures);
    }

    public PictureDTO delete(long pictureId, HttpSession session) throws BadRequestException {
        if (pictureId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Optional<Picture> picture = this.pictureRepository.findById(pictureId);
        if (!picture.isPresent()) {
            throw new ExistsObjectException(THE_PICTURES_DO_NOT_EXIST);
        }
        this.pictureRepository.deleteById(pictureId);
        File fileForDelete = new File(PACKAGE_NAME + picture.get().getUrlOFPicture());
        fileForDelete.delete();
        return new PictureDTO(picture.get());
    }

    public PictureDTO addPictureToTheArticleById(long pictureId, long articleId,
                                                 HttpSession session) throws BadRequestException {
        if (pictureId < 0 || articleId < 0) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Optional<Picture> optionalPicture = this.pictureRepository.findById(pictureId);
        Picture validPicture = PictureValidator.checkForValidPicture(optionalPicture);
        if (!this.articleRepository.existsById(articleId)) {
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        validPicture.setArticleId(articleId);
        Picture updatedPicture = this.pictureRepository.save(validPicture);
        return new PictureDTO(updatedPicture);
    }
}
