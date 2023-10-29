package apptive.london.bridge.domain.user.controller;

import apptive.london.bridge.domain.user.dto.CreatorInfo;
import apptive.london.bridge.domain.user.dto.ModifyCreatorRequest;
import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/creator")
@Tag(name = "Creator")
@RequiredArgsConstructor
public class CreatorController {

    private final UserService userService;

    @Operation(
            description = "Get endpoint for manager",
            summary = "This is a summary for creator get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )

    @GetMapping("/myInfo/v1")
    public ResponseEntity<CreatorInfo> getMyInfo(@AuthenticationPrincipal User user) {
        CreatorInfo creatorInfo = userService.creatorInfo(user);

        return ResponseEntity.ok(creatorInfo);
    }

    @PostMapping("/modify/v1")
    public ResponseEntity<?> modifyCreator(@AuthenticationPrincipal User user, @RequestBody @Valid ModifyCreatorRequest modifyCreatorRequest) {
        userService.modifyCreator(user, modifyCreatorRequest);

        return ResponseEntity.ok(null);
    }
}
