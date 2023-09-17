package apptive.london.bridge.domain.user.controller;

import apptive.london.bridge.domain.user.dto.UserInfo;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
