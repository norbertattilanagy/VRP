package com.example.vrp;

import java.io.Serializable;

public class VRPLocation implements Serializable {

    int id;
    int problem_id;
    String name;
    double x;
    double y;
    int request;
    Boolean warehouse;

    public VRPLocation(int id, int problem_id, String name, double x, double y, int request, Boolean warehouse) {
        this.id = id;
        this.problem_id = problem_id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.request = request;
        this.warehouse = warehouse;
    }

    public VRPLocation(int problem_id, String name, double x, double y, int request, Boolean warehouse) {
        this.problem_id = problem_id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.request = request;
        this.warehouse = warehouse;
    }

    public VRPLocation(String name, double x, double y, int request, Boolean warehouse) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.request = request;
        this.warehouse = warehouse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(int problem_id) {
        this.problem_id = problem_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public Boolean getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Boolean warehouse) {
        this.warehouse = warehouse;
    }
}
