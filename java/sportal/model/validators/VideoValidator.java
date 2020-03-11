package sportal.model.validators;

import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.model.db.pojo.Video;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static sportal.GlobalConstants.WRONG_REQUEST;

public class VideoValidator {

    private static final List<String> CONTENT_TYPES_LIST = Arrays.asList(
            "video/x-flv", "video/mp4", "image/gif", "application/x-mpegURL", "video/MP2T", "video/3gpp",
            "video/quicktime", "video/x-msvideo", "video/x-ms-wmv");
    private static final String DATE_AND_TIME_OF_UPLOAD = "date_and_time_of_upload_";
    private static final String FILE_EXPANSION = ".mp4";

    public static Video checkForValidContentType(MultipartFile multipartFile) throws BadRequestException {
        String fileContentType = multipartFile.getContentType();
        Video video = new Video();
        if (CONTENT_TYPES_LIST.contains(fileContentType)) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss.SSS"));
            String urlOfPicture = DATE_AND_TIME_OF_UPLOAD + now + FILE_EXPANSION;
            video.setUrlOfVideo(urlOfPicture);
        } else {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return video;
    }
}
