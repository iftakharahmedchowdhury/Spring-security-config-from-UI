package com.dev.SpringSecurity.SpringBootDB.Repository;

import com.dev.SpringSecurity.SpringBootDB.models.PermissionUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionUrlRepository extends JpaRepository<PermissionUrl, Long> {

}

