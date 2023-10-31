package apptive.london.bridge.domain.user.controller;

import apptive.london.bridge.domain.user.dto.*;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/myInfo/v1")
    public ResponseEntity<UserInfo> getMyInfo(@AuthenticationPrincipal User user) {
        UserInfo userInfo = userService.userInfo(user);

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/modify/v1")
    public ResponseEntity<?> modifyUser(@AuthenticationPrincipal User user, @RequestBody @Valid ModifyUserRequest modifyUserRequest) {
        userService.modifyUser(user, modifyUserRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/convert-to-creator/{email}/v1")
    public ResponseEntity<?> convertToCreator(@PathVariable String email, @RequestBody @Valid ModifyCreatorRequest modifyCreatorRequest) {
        userService.convertToCreator(email, modifyCreatorRequest);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/profileImgUpload/v1")
    public ResponseEntity<?> profileImgUpload(@AuthenticationPrincipal User user, @RequestParam("img") MultipartFile multipartFile) throws IOException {
        userService.accoutProfileImgUpload(user, multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profileImg/v1")
    public ResponseEntity<UserProfileImg> getProfileImg(@AuthenticationPrincipal User user) {
        UserProfileImg userProfileImg = userService.getUserProfileImg(user.getId());
        return ResponseEntity.ok(userProfileImg);
    }

    @PostMapping("/follow/{creatorId}/v1")
    public ResponseEntity<?> followCreator(@AuthenticationPrincipal User user, @PathVariable Long creatorId) {
        userService.followCreator(user, creatorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unFollow/{creatorId}/v1")
    public ResponseEntity<?> unFollowCreator(@AuthenticationPrincipal User user, @PathVariable Long creatorId) {
        userService.unFollowCreator(user, creatorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/follow/list/v1")
    public ResponseEntity<UserFollowListResponse> getFollowList(@AuthenticationPrincipal User user) {
        UserFollowListResponse userFollowList = userService.getFollowList(user);
        return ResponseEntity.ok(userFollowList);
    }
}
