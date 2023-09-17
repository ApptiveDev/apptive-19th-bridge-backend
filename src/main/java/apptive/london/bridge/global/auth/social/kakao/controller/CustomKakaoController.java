package apptive.london.bridge.global.auth.social.kakao.controller;

import apptive.london.bridge.global.auth.local.data.AuthenticationResponse;
import apptive.london.bridge.global.auth.social.kakao.service.CustomKakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class CustomKakaoController {

    private final CustomKakaoService customKakaoService;


    @PostMapping("/authenticate/kakao/v1")
    public ResponseEntity<AuthenticationResponse> kakaoAuthenticate(@RequestParam("code") String code) {
        AuthenticationResponse authenticationResponse = customKakaoService.authenticate(code);

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}
