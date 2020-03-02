package com.stanislavnoskov.adf.universityx.users.model;

import java.util.HashMap;
import java.util.Map;

public enum Role {
    USER("USER"),  // for students
    ADMIN("ADMIN");

    private static Map<String, Role> ROLES_BY_NAMES;

    static {
        ROLES_BY_NAMES = new HashMap<>();
        for (Role role : Role.values()) {
            ROLES_BY_NAMES.put(role.name, role);
        }
    }

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Role fromName(String name) {
        return ROLES_BY_NAMES.get(name);
    }
}
