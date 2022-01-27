package com.zubayr.tests.controller;

import com.zubayr.tests.config.JwtTokenAdapter;
import com.zubayr.tests.dto.request.RegisUserDto;
import com.zubayr.tests.dto.response.AuthUserDto;
import com.zubayr.tests.model.User;
import com.zubayr.tests.model.enums.Role;
import com.zubayr.tests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenAdapter tokenAdapter;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenAdapter tokenAdapter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenAdapter = tokenAdapter;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AuthUserDto dto, ServletResponse response){
        String username = dto.getUsername();
        String password = dto.getPassword();
        Optional<User> optionalUser = userRepository.findByUserName(username);
        try {
            optionalUser.ifPresent(user -> authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    password,
                                    new HashSet<>() {{
                                        new SimpleGrantedAuthority(user.getRole().name());
                                    }}
                            )));
            String token = tokenAdapter.createToken(username);
            Cookie cookie = new Cookie("Authorization", token);
            ((HttpServletResponse)response).addCookie(cookie);

            return "redirect:/";
        } catch (AuthenticationException e){
            e.printStackTrace();
            return "redirect:/auth";
        }
    }


    @GetMapping("/regis")
    public String regisPage(Model model){
        Set<String> roles = Set.of(Role.STUDENT.name(), Role.TEACHER.name(), Role.ADMIN.name());
        model.addAttribute("roles", roles);
        return "regis";
    }

    @PostMapping("/regis")
    public String regis(@ModelAttribute RegisUserDto dto, ServletResponse response){
        if (userRepository.findByUserName(dto.getLogin()).isEmpty()){
            User user = new User();
            user.setUserName(dto.getLogin());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setFullName(String.join(" ", dto.getSurname() , dto.getName(), dto.getPatronymic()));
            user.setRole(Role.valueOf(dto.getRole()));
            user.setIsBlocked(false);
            userRepository.save(user);
        }
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setUsername(dto.getLogin());
        authUserDto.setPassword(dto.getPassword());
        return login(authUserDto, response);
    }

}
