package marcoantn020.crud.controller;

import marcoantn020.crud.controller.dto.LoginRequestDto;
import marcoantn020.crud.controller.dto.RegisterRequestDto;
import marcoantn020.crud.controller.dto.TokenResponseDto;
import marcoantn020.crud.domain.user.User;
import marcoantn020.crud.domain.user.UserRepository;
import marcoantn020.crud.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Email e/ou senha incorretos."));

        if (!passwordEncoder.matches(dto.password(),user.getPassword())) {
            throw new RuntimeException("Email e/ou senha incorretos.");
        }

        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new TokenResponseDto(user.getName(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody RegisterRequestDto dto) {
        Optional<User> userExists = userRepository.findByEmail(dto.email());
        if (userExists.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User(dto.name(), dto.email(), passwordEncoder.encode(dto.password()));
        userRepository.save(user);

        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new TokenResponseDto(user.getName(), token));
    }

}
