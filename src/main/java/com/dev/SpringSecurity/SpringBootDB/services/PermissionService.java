package com.dev.SpringSecurity.SpringBootDB.services;

import com.dev.SpringSecurity.SpringBootDB.Repository.PermissionUrlRepository;
import com.dev.SpringSecurity.SpringBootDB.models.PermissionUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionUrlRepository permissionUrlRepository;

    public List<PermissionUrl> getPermissions() {
        return permissionUrlRepository.findAll();
    }

   public void savePermission(PermissionUrl permission) {
       String fullUrl = permission.getUrl();
       String extractedUrl = fullUrl.substring(fullUrl.indexOf("[") + 1, fullUrl.indexOf("]"));
       permission.setUrl(extractedUrl);
       permissionUrlRepository.save(permission);
   }
    public void deletePermission(Long id) {
        permissionUrlRepository.deleteById(id);
    }
}
