/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder;

import graph.GraphADT;
import pathfinder.datastructures.Dijkstra;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {

    @Override
    public boolean shortNameExists(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        CampusPathsParser buildings = new CampusPathsParser();
        List<CampusBuilding> listB = buildings.parseCampusBuildings("campus_buildings.csv");
        for(CampusBuilding b: listB){
            if(b.getShortName().equals(shortName)){
                return true;
            }
        }
        return false;
        //throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public String longNameForShort(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        if(!shortNameExists(shortName)){
            throw new IllegalArgumentException("shortName does not exist");
        }
        CampusPathsParser buildings = new CampusPathsParser();
        List<CampusBuilding> listB = buildings.parseCampusBuildings("campus_buildings.csv");
        String longName = "";
        for(CampusBuilding b: listB){
            if(b.getShortName().equals(shortName)){
                longName =  b.getLongName();
                break;
            }
        }
        return longName;
        //throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public Map<String, String> buildingNames() {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        Map<String, String> map = new HashMap<>();
        CampusPathsParser buildings = new CampusPathsParser();
        List<CampusBuilding> listB = buildings.parseCampusBuildings("campus_buildings.csv");
        for(CampusBuilding b: listB){
            map.put(b.getShortName(), b.getLongName());
        }
        return map;
        //throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        if(!shortNameExists(startShortName) || !shortNameExists(endShortName)
                  || startShortName == null || endShortName == null){
            throw new IllegalArgumentException("args do not exists or are null");
        }
        CampusPathsParser campus = new CampusPathsParser();
        List<CampusBuilding> listBuildings = campus.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> listPaths = campus.parseCampusPaths("campus_paths.csv");
        GraphADT<Point, Double> graph = new GraphADT<Point, Double>();
        Point shortName = new Point(getX(listBuildings, startShortName), getY(listBuildings, startShortName));
        Point longName = new Point(getX(listBuildings, endShortName), getY(listBuildings, endShortName));
        for(CampusPath c: listPaths){
            Point point1 = new Point(c.getX1(), c.getY1());
            Point point2 = new Point(c.getX2(), c.getY2());
            Double distance = c.getDistance();
            if(!graph.findNode(point1)){
                graph.addNode(point1);
            }
            if(!graph.findNode(point2)){
                graph.addNode(point2);
            }
            graph.addEdges(point1, point2, distance);
        }
        Dijkstra<Point> dijkstra = new Dijkstra<Point>();
        return dijkstra.findPath(graph, shortName, longName);
        //throw new RuntimeException("Not Implemented Yet");
    }

    private double getX(List<CampusBuilding> buildings, String name){
        for(CampusBuilding b: buildings){
            if(b.getShortName().equals(name)){
                return b.getX();
            }
        }
        return 0;
    }

    private double getY(List<CampusBuilding> buildings, String name){
        for(CampusBuilding b: buildings){
            if(b.getShortName().equals(name)){
                return b.getY();
            }
        }
        return 0;
    }

}
