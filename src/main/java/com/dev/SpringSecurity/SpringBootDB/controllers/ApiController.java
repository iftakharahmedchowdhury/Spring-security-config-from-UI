package com.dev.SpringSecurity.SpringBootDB.controllers;
import com.dev.SpringSecurity.SpringBootDB.models.PermissionUrl;
import com.dev.SpringSecurity.SpringBootDB.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ApiController {

    private final RequestMappingHandlerMapping handlerMapping;
    private final PermissionService permissionService;

    @Autowired
    public ApiController(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
                         PermissionService permissionService) {
        this.handlerMapping = handlerMapping;
        this.permissionService = permissionService;
    }

    @GetMapping("/endpoints")
    public String getEndpoints(Model model) {

        List<String> roles = List.of("ADMIN", "USER");

        // map all url from controller
        Map<String, String> apiEndpoints = handlerMapping.getHandlerMethods().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().getMethod().getDeclaringClass().getSimpleName() + "#" +
                                entry.getValue().getMethod().getName()));

        model.addAttribute("apiEndpoints", apiEndpoints);
        model.addAttribute("roles", roles);
        return "api/endpoints";
    }

    @PostMapping("/addPermission")
    public String addPermission(String url, String role) {
        PermissionUrl permission = new PermissionUrl();
        permission.setUrl(url);
        permission.setPermissionRole(role);
        permissionService.savePermission(permission);
        return "redirect:/api/endpoints";
    }
}
