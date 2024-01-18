package apptive.london.bridge.global.third_party.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AwsS3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    /**
     * MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
     */
    public FileInfo upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)        // 파일 생성
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile에서 File 전환 실패"));

        return upload(uploadFile, dirName);
    }

    // 1. 로컬에 파일생성
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    /**
     * S3에 dirName(폴더이름)/고유번호+파일이름으로 업로드
     */
    private FileInfo upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName(); // 폴더/고유파일이름
        String uploadUrl = putS3(uploadFile, fileName);    // s3로 업로드
        removeNewFile(uploadFile);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(uploadFile.getName());
        fileInfo.setUploadFileName(fileName);
        fileInfo.setUploadFilePath(dirName);
        fileInfo.setUploadFileUrl(uploadUrl);

        return fileInfo;
    }

    /**
     * S3에 파일업로드
     */
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        System.out.println("File Upload : " + fileName);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 로컬에 생성된 파일삭제
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("File delete success");
            return;
        }
        System.out.println("File delete fail");
    }

    /**
     * S3 파일 삭제
     */
    public void delete(String fileName) {
        System.out.println("File Delete : " + fileName);
        amazonS3Client.deleteObject(bucket, fileName);
    }
}
