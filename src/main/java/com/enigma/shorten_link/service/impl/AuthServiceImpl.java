package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.enums.UserRole;
import com.enigma.shorten_link.entity.Credential;
import com.enigma.shorten_link.entity.Role;
import com.enigma.shorten_link.entity.User;
import com.enigma.shorten_link.model.request.AuthRequest;
import com.enigma.shorten_link.model.response.LoginResponse;
import com.enigma.shorten_link.model.response.RegisterResponse;
import com.enigma.shorten_link.service.AuthService;
import com.enigma.shorten_link.service.CredentialService;
import com.enigma.shorten_link.service.RoleService;
import com.enigma.shorten_link.service.UserService;
import com.enigma.shorten_link.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final CredentialService credentialService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${sholin.username.superadmin}")
    private String superAdminUsername;
    @Value("${sholin.password.superadmin}")
    private String superAdminPassword;


    @Transactional(rollbackFor = Exception.class)
    @PostConstruct // berguna untuk mengeksekusi method yg akan dijalankan pada saat aplikasi pertama kali dijalankan
    public void initSuperAdmin() {
        Optional<Credential> currentUser = credentialService.findByUsername(superAdminUsername);
        if (currentUser.isPresent()) return;

        Role superAdmin = roleService.getOrCreate(UserRole.ROLE_SUPER_ADMIN);
        Role admin = roleService.getOrCreate(UserRole.ROLE_ADMIN);
        Role user = roleService.getOrCreate(UserRole.ROLE_USER);
        Role guest = roleService.getOrCreate(UserRole.ROLE_GUEST);

        Credential account = Credential.builder()
                .username(superAdminUsername)
                .password(passwordEncoder.encode(superAdminPassword))
                .roles(List.of(superAdmin, admin, user))
                .created_at(new Date())
                .build();

        credentialService.save(account);
    }

    private RegisterResponse register(AuthRequest request, Role role) {
        Credential credential = Credential.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(role))
                .created_at(new Date())
                .build();
        credentialService.save(credential);

        User user = User.builder()
                .name(credential.getUsername())
                .credential(credential)
                .created_at(new Date())
                .build();

        userService.create(user);

        List<String> roles = credential.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .username(credential.getUsername())
                .roles(roles)
                .build();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse registerUser(AuthRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrCreate(UserRole.ROLE_USER);

        return register(request, role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse registerAdmin(AuthRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrCreate(UserRole.ROLE_ADMIN);

        return register(request, role);
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        Credential credential = (Credential) authenticate.getPrincipal();
        String token = jwtUtil.generateToken(credential);
        return LoginResponse.builder()
                .username(credential.getUsername())
                .roles(credential.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }

}
