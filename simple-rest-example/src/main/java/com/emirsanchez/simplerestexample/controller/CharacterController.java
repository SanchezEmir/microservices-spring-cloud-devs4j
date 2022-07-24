package com.emirsanchez.simplerestexample.controller;

import com.emirsanchez.simplerestexample.error.CharacterNotFound;
import com.github.javafaker.Faker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/characters")
@RestController
public class CharacterController {

    private Faker faker = new Faker();
    private List<String> characters = new ArrayList<>();

    @PostConstruct
    public void init() {
//        for (int i = 0; i < 10; i++) {
//            characters.add(faker.gameOfThrones().character());
//        }
        IntStream.range(0, 10)
                .forEach(i -> characters.add(faker.dragonBall().character()));

    }

    @GetMapping(value = "/dragonBall")
    public List<String> getCharacters() {
        return characters;
    }

    @GetMapping(value = "/dragonBall/{name}")
    public String getCharactersByName(@PathVariable("name") String name) {
        return characters.stream()
                .filter(c -> c.equals(name))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Character %s not found", name)));
    }

    // search?param1=value1&param2=value2&param3=value3
    @GetMapping(value = "/dragonBall/search")
    public List<String> getCharactersByPrefix(@RequestParam("prefix") String prefix) {
        List<String> result = characters.stream()
                .filter(c -> c.startsWith(prefix))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new CharacterNotFound();
        }

        return result;
    }

}
