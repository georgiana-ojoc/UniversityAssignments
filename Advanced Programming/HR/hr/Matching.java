package com.georgiana.ojoc.hr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Matching {
    private List<Element> matchList;

    public Matching() {
        matchList = new ArrayList<>();
    }

    public Matching(List<Element> matchList) {
        this.matchList = matchList;
    }

    public List<Element> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<Element> matchList) {
        this.matchList = matchList;
    }

    public void addMatch(Element element) {
        matchList.add(element);
        element.getHospital().addResident();
    }

    public void removeMatch(Element element) {
        matchList.remove(element);
        element.getHospital().removeResident();
    }

    @Override
    public String toString() {
        return "Matching:\n" + matchList.stream()
                                    .map(Element::toString)
                                    .sorted()
                                    .collect(Collectors.joining(", ", "[", "]"));
    }
}
