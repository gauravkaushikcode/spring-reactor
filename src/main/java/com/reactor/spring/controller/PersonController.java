package com.reactor.spring.controller;

import com.reactor.spring.model.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {
    private static final List<Person> personList = new ArrayList<>();

    static {
        personList.add(new Person(1, "Zaq"));
        personList.add(new Person(2, "Das"));
        personList.add(new Person(3, "Lan"));
        personList.add(new Person(4, "Mal"));
        personList.add(new Person(5, "Lon"));
    }

    @GetMapping("/person/{id}")
    public Person getPerson(@PathVariable int id, @RequestParam(defaultValue = "2") int delay) throws InterruptedException {
        // to simulate network lag of 2 seconds
        Thread.sleep(delay * 1000);
        return personList.stream().filter((person) -> person.getId() == id).findFirst().get();
    }
}
