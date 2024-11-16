package com.dev.SpringSecurity.SpringBootDB.controllers;


import com.dev.SpringSecurity.SpringBootDB.services.PermissionService;
import com.dev.SpringSecurity.SpringBootDB.models.PermissionUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/list")
    public String getPermissions(Model model) {
        // Fetch permissions from database
        List<PermissionUrl> allPermissions = permissionService.getPermissions();

        List<PermissionUrl> adminPermissions = allPermissions.stream()
                .filter(p -> p.getPermissionRole().equals("ADMIN"))
                .collect(Collectors.toList());

        List<PermissionUrl> userPermissions = allPermissions.stream()
                .filter(p -> p.getPermissionRole().equals("USER"))
                .collect(Collectors.toList());

        model.addAttribute("adminPermissions", adminPermissions);
        model.addAttribute("userPermissions", userPermissions);

        return "api/permissions";
    }

    @PostMapping("/delete/{id}")
    public String deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return "redirect:/permissions/list";
    }
}
