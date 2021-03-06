package sportal.model.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import sportal.model.db.pojo.Picture;
import sportal.model.db.pojo.Video;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static sportal.util.GlobalConstants.SUCCESSFUL_TRANSFER_OF_FILES;

public class FileManagerDAO extends Thread {

    private List<MultipartFile> multipartFiles;
    private String packageName;
    private List<Picture> pictures;
    private MultipartFile multipartFile;
    private Video video;
    private static final Logger LOGGER = LogManager.getLogger(FileManagerDAO.class);

    public FileManagerDAO(List<MultipartFile> multipartFiles, String packageName, List<Picture> pictures) {
        this.multipartFiles = multipartFiles;
        this.packageName = packageName;
        this.pictures = pictures;
    }

    public FileManagerDAO(MultipartFile multipartFile, String packageName, Video video) {
        this.multipartFile = multipartFile;
        this.packageName = packageName;
        this.video = video;
    }

    @Override
    public void run() {
        if (this.pictures != null) {
            for (int i = 0; i < multipartFiles.size(); i++) {
                try {
                    this.save(this.multipartFiles.get(i),
                            this.packageName + this.pictures.get(i).getUrlOfPicture());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            try {
                this.save(this.multipartFile,
                        this.packageName + this.video.getUrlOfVideo());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        LOGGER.info(SUCCESSFUL_TRANSFER_OF_FILES);
    }

    private void save(MultipartFile multipartFile, String fullPathUrlOfPicture) throws IOException {
        multipartFile.transferTo(Paths.get(fullPathUrlOfPicture));
    }
}
