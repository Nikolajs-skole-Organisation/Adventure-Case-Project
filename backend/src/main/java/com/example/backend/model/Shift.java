package com.example.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Activity> activities = new HashSet<>();

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int Capacity;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Employee> employees = new HashSet<>();

    public Shift(long id, LocalDateTime startTime, LocalDateTime endTime, int capacity) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        Capacity = capacity;
    }

    public Shift() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    // Helper methods

    public void addActivity(Activity activity) {
        if (activity == null) return;
        this.activities.add(activity);
        activity.getShifts().add(this);
    }

    public void removeActivity(Activity activity) {
        if (activity == null) return;
        this.activities.remove(activity);
        activity.getShifts().remove(this);
    }

    public void addEmployee(Employee employee) {
        if (employee == null) return;
        this.employees.add(employee);
        employee.getShifts().add(this);
    }

    public void removeEmployee(Employee employee) {
        if (employee == null) return;
        this.employees.remove(employee);
        employee.getShifts().remove(this);
    }
}
