package com.accenture.com.snoskov.adf.universityx.programs.model;

public class AcademicDegree {

    private final String name;
    private final String type;

    public AcademicDegree(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
