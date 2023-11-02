package apptive.london.bridge.domain.user.entity;

import apptive.london.bridge.global.auth.local.data.Token;
import apptive.london.bridge.global.common.BaseEntity;
import apptive.london.bridge.global.s3.AwsS3Uploader;
import apptive.london.bridge.global.s3.FileInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected String email;
    protected String password;
    protected String nickname;
    protected LocalDate birthday;

    @Enumerated(EnumType.STRING)
    protected Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    protected List<Token> tokens;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_img_id")
    protected ProfileImg profileImg;

    //============ UserDetail Method ============//

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //============ Business Method ============//

    //===== Profile Img =====//
    /**
     * 프로필 이미지 업로드
     * @param multipartFile
     * @param awsS3UpLoader
     * @return
     * @throws IOException
     */
    public ProfileImg uploadProfileImage(MultipartFile multipartFile, AwsS3Uploader awsS3UpLoader) throws IOException {
        FileInfo fileInfo = awsS3UpLoader.upload(multipartFile, this.getEmail());
        this.profileImg = ProfileImg.of(fileInfo);
        return this.profileImg;
    }

    /**
     * 프로필 이미지 삭제
     * TODO 유저가 삭제될 때, S3 서버의 이미지도 삭제하도록 구현해야함
     * @param awsS3Uploader
     */
    public void deleteProfileImage(AwsS3Uploader awsS3Uploader) {
        if (this.profileImg != null) {
            awsS3Uploader.delete(this.profileImg.getUploadFileName());
        }
        this.profileImg = null;
    }

    /**
     * 프로필 이미지가 있다면 url을 반환한다.
     * @return 프로필 이미지 url
     */
    public String getProfileImgUrl() {
        return this.profileImg != null ? this.profileImg.getUploadFileUrl() : "";
    }
}
