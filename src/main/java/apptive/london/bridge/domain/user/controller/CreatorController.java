package apptive.london.bridge.domain.user.controller;

import apptive.london.bridge.domain.user.dto.CreatorFollowerListResponse;
import apptive.london.bridge.domain.user.dto.CreatorInfo;
import apptive.london.bridge.domain.user.dto.ModifyCreatorRequest;
import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.service.CreatorService;
import apptive.london.bridge.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creator")
@Tag(name = "Creator")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;

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
    public ResponseEntity<CreatorInfo> getMyInfo(@AuthenticationPrincipal Creator creator) {
        CreatorInfo creatorInfo = creatorService.creatorInfo(creator);

        return ResponseEntity.ok(creatorInfo);
    }

    @PostMapping("/modify/v1")
    public ResponseEntity<?> modifyCreator(@AuthenticationPrincipal User user, @RequestBody @Valid ModifyCreatorRequest modifyCreatorRequest) {
        creatorService.modifyCreator(user, modifyCreatorRequest);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/follower/list/v1")
    public ResponseEntity<CreatorFollowerListResponse> getFollowerList(@AuthenticationPrincipal User user) {
        CreatorFollowerListResponse creatorFollowerList = creatorService.getFollowerList(user);

        return ResponseEntity.ok(creatorFollowerList);
    }

    @GetMapping("/follower/list/nonBlock/v1")
    public ResponseEntity<CreatorFollowerListResponse> getNonBlockFollowerList(@AuthenticationPrincipal User user) {
        CreatorFollowerListResponse creatornonBlockFollowerList = creatorService.getNonBlockFollowerList(user);

        return ResponseEntity.ok(creatornonBlockFollowerList);
    }

    @GetMapping("/follower/list/block/v1")
    public ResponseEntity<CreatorFollowerListResponse> getBlockFollowerList(@AuthenticationPrincipal User user) {
        CreatorFollowerListResponse creatorBlockFollowerList = creatorService.getBlockFollowerList(user);

        return ResponseEntity.ok(creatorBlockFollowerList);
    }

    @PostMapping("/block/{userId}/v1")
    public ResponseEntity<?> blockUser(@AuthenticationPrincipal Creator creator, @PathVariable Long userId) {
        creatorService.blockUser(creator, userId);

        return ResponseEntity.ok().build();
    }

}
