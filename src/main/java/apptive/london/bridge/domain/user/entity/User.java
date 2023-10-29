package apptive.london.bridge.domain.user.entity;

import apptive.london.bridge.global.auth.local.data.Token;
import apptive.london.bridge.global.common.BaseEntity;
import apptive.london.bridge.global.s3.AwsS3Uploader;
import apptive.london.bridge.global.s3.FileInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String birthday;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_img_id")
    private ProfileImg profileImg;

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

    public ProfileImg uploadProfileImage(MultipartFile multipartFile, AwsS3Uploader awsS3UpLoader) throws IOException {
        FileInfo fileInfo = awsS3UpLoader.upload(multipartFile, this.getEmail());
        this.profileImg = ProfileImg.of(fileInfo);
        return this.profileImg;
    }

    public void deleteProfileImage(AwsS3Uploader awsS3Uploader) {
        if (this.profileImg != null) {
            awsS3Uploader.delete(this.profileImg.getUploadFileName());
        }
        this.profileImg = null;
    }
}
