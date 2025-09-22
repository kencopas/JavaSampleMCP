package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PresentationTools {

    private List<Presentation> presentations = new ArrayList<>();

    public PresentationTools() {
        var keynoteOne = new Presentation("Keynote One", "An introduction to our topic.", 2022);
        var keynoteTwo = new Presentation("Keynote Two", "A deeper dive into our topic.", 2023);
        var concerto = new Presentation("Concerto", "A musical interlude.", 2021);
        var gatherers = new Presentation("Gatherers", "A discussion on community building.", 2020);
        var ai202 = new Presentation("AI 202", "Advanced topics in AI.", 2024);
        var sequenced = new Presentation("Sequenced", "The art of sequencing in presentations.", 2023);

        this.presentations.addAll(List.of(keynoteOne, keynoteTwo, concerto, gatherers, ai202, sequenced));
    }

    public List<Presentation> getPresentations() {
        return presentations;
    }

    public List<Presentation> getPresentationsByYear(int year) {
        return presentations.stream().filter(p -> p.year() == year).toList();
    }

    public List<Map<String, Object>> getPresentationsAsMapList() {
        return presentations.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", p.title());
                    map.put("url", p.url());
                    map.put("year", p.year());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
