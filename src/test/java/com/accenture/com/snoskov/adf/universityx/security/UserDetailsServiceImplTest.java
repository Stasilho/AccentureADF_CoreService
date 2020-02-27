package com.accenture.com.snoskov.adf.universityx.security;

import com.accenture.com.snoskov.adf.universityx.users.model.ApplicationUser;
import com.accenture.com.snoskov.adf.universityx.users.model.Role;
import com.accenture.com.snoskov.adf.universityx.users.repo.UserRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    private static UserDetailsServiceImpl userDetailsService;
    private static UserRepository userRepository;

    @BeforeClass
    public static void initialize() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void testLoadUserByUsername() {
        ApplicationUser user = new ApplicationUser("admin", "password");
        user.setRole(Role.ADMIN);
        when(userRepository.findByUsername(eq("admin"))).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(Role.ADMIN.getName(), userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse(""));
    }
}