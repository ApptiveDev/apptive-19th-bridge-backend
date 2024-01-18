package apptive.london.bridge.global.third_party.s3;

import lombok.Data;

@Data
public class FileInfo {
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;
}
