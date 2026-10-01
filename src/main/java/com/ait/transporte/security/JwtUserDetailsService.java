package com.ait.transporte.security;


import com.ait.transporte.exception.DriverNotFoundException;
import com.ait.transporte.model.Driver;
import com.ait.transporte.model.Order;
import com.ait.transporte.repository.IDriverRepository;
import com.ait.transporte.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.build.Plugin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final IDriverRepository repo;

    @Override
    public UserDetails loadUserByUsername(String driver) throws UsernameNotFoundException {
        Driver conductor = repo.findOneByName(driver).orElseThrow(() -> new DriverNotFoundException("User not found: " + driver));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(conductor.getName()));

        return new org.springframework.security.core.userdetails.User(conductor.getName(), conductor.getPasswordHash(), roles);
    }


}
