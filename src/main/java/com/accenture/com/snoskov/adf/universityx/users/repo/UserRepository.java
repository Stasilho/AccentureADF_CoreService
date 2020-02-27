package com.accenture.com.snoskov.adf.universityx.users.repo;

import java.util.List;

import com.accenture.com.snoskov.adf.universityx.users.model.ApplicationUser;

public interface UserRepository {

    void addNewUser(ApplicationUser user);

    ApplicationUser findByUsername(String username);

    List<ApplicationUser> findByIds(List<Integer> userIds);
}