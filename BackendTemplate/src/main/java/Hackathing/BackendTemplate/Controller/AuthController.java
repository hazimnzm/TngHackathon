package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.DTO.AuthLoginRequest;
import Hackathing.BackendTemplate.DTO.AuthResponse;
import Hackathing.BackendTemplate.DTO.AuthSignupRequest;
import Hackathing.BackendTemplate.Security.JwtService;
import Hackathing.BackendTemplate.Service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MerchantService merchantService;
    private final JwtService jwtService;

    public AuthController(MerchantService merchantService, JwtService jwtService) {
        this.merchantService = merchantService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody AuthSignupRequest req) {
        var merchant = merchantService.register(req.getEmail(), req.getDisplayName(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse(jwtService.generateToken(merchant)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest req) {
        var merchant = merchantService.authenticate(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse(jwtService.generateToken(merchant)));
    }
}

