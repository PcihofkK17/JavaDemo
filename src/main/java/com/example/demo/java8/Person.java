package com.example.demo.java8;

import java.util.List;
import java.util.Objects;

/**
 * @author f_bao
 * @create 2018/6/4
 */
public class Person {

    private int id;

    private String name;

    private List<String> filed;

    public Person(int id) {
        this.id = id;
    }

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getFiled() {
        return filed;
    }

    public void setFiled(List<String> filed) {
        this.filed = filed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
