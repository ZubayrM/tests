package com.zubayr.tests.controller;

import com.zubayr.tests.dto.request.UserInfoDto;
import com.zubayr.tests.model.User;
import com.zubayr.tests.model.enums.Role;
import com.zubayr.tests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/adminPage")
    public String adminPage(Model model){
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        List<User> users = userRepository.findAll();

        users.forEach(u-> {
            UserInfoDto dto = new UserInfoDto();
            dto.setId(u.getId());
            dto.setFullName(u.getFullName());
            dto.setStatus(u.getIsBlocked() ? "ЗАБЛОКИРОВАН" : "АКТИВЕН");
            dto.setRole(u.getRole().name());
            userInfoDtoList.add(dto);
        });
        model.addAttribute("users", userInfoDtoList);
        return "adminPage";
    }

    @PostMapping("/blocking")
    public String blockingUser(@RequestParam Long id, Model model){
        userRepository.findById(id).ifPresent(u -> {
            u.setIsBlocked(!u.getIsBlocked());
            userRepository.save(u);
        });
        return adminPage(model);
    }
}
