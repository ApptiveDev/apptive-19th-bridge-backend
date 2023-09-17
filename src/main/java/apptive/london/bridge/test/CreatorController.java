package apptive.london.bridge.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/creator")
@Tag(name = "Creator")
public class CreatorController {


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
    @GetMapping
    public String get() {
        return "GET:: creator controller";
    }
    @PostMapping
    public String post() {
        return "POST:: creator controller";
    }
    @PutMapping
    public String put() {
        return "PUT:: creator controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE:: creator controller";
    }
}
