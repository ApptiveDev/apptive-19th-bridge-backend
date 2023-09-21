package apptive.london.bridge.domain.user.controller;

import apptive.london.bridge.domain.user.dto.ModifyCreatorRequest;
import apptive.london.bridge.domain.user.dto.ModifyUserRequest;
import apptive.london.bridge.domain.user.dto.UserInfo;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.ok().build();    }

    @PostMapping("/modify/creator/v1")
    public ResponseEntity<?> modifyCreator(@AuthenticationPrincipal User user, @RequestBody @Valid ModifyCreatorRequest modifyCreatorRequest) {
        userService.modifyCreator(user, modifyCreatorRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/convert-to-creator/{email}/v1")
    public ResponseEntity<?> converToCreator(@PathVariable String email, @RequestBody @Valid ModifyCreatorRequest modifyCreatorRequest) {
        userService.convertToCreator(email, modifyCreatorRequest);
        return ResponseEntity.ok().build();
    }
}
