package apptive.london.bridge.global.s3;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final AwsS3Uploader awsS3Uploader;

    public FileController(AwsS3Uploader awsS3Uploader) {
        this.awsS3Uploader = awsS3Uploader;
    }

    @PostMapping("/upload")
    public FileInfo upload(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        return awsS3Uploader.upload(multipartFile, "file");
    }
}
