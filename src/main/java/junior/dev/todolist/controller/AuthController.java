package junior.dev.todolist.controller;

import junior.dev.todolist.model.ERole;
import junior.dev.todolist.model.Person;
import junior.dev.todolist.model.Role;
import junior.dev.todolist.repository.PersonRepository;
import junior.dev.todolist.repository.RoleRepository;
import junior.dev.todolist.request.RequestLogin;
import junior.dev.todolist.request.RequestSignup;
import junior.dev.todolist.response.ResponseJwt;
import junior.dev.todolist.response.ResponseMessage;
import junior.dev.todolist.security.jwt.JwtUtils;
import junior.dev.todolist.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PersonRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<ResponseJwt> authenticateUser(@Valid @RequestBody RequestLogin loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);//!!!
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        //System.out.println(userDetails.getId() + " in login2 "+ jwt);
        return ResponseEntity.ok(new ResponseJwt(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> registerUser(@Valid @RequestBody RequestSignup signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage("Error: Username is already taken!"));
        }


        // Create new user's account
        Person user = new Person(signUpRequest.getUsername(),

                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
    }

}
