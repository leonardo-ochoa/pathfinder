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

package pathfinder.scriptTestRunner;

import graph.GraphADT;
import pathfinder.datastructures.Dijkstra;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {
    private final Map<String, GraphADT<String, Double>> graphs = new HashMap<String, GraphADT<String, Double>>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        // TODO: Implement this, reading commands from `r` and writing output to `w`.
        // See GraphTestDriver as an example.
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        // TODO: Implement this.
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        // TODO Insert your code here.

        graphs.put(graphName, new GraphADT<String, Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        // TODO Insert your code here.
        GraphADT<String, Double> temp = graphs.get(graphName);
        temp.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        // TODO Insert your code here.
        GraphADT<String, Double> temp = graphs.get(graphName);
        Double edge = Double.parseDouble(edgeLabel);
        temp.addEdges(parentName, childName, edge);
        output.println(String.format("added edge %.3f", edge) + " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        // TODO Insert your code here.
        GraphADT<String, Double> temp = graphs.get(graphName);
        Set<String> list = orderString(temp.getNodes());
        String print = "";
        for(String s: list){
            print += " " + s;
        }
        output.println(graphName + " contains:" + print);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        // TODO Insert your code here.
        GraphADT<String, Double> temp = graphs.get(graphName);
        String print = "";
        Set<String> children = orderString(temp.childrenOfParent(parentName));
        for (String child: children) {
            Set<Double> edges = orderEdge(temp.getEdges(parentName, child));
            for(Double e: edges){
                print += " " + child + "(" + String.format(" %.3f", e) + ")";
            }
        }
        output.println("the children of " + parentName + " in " + graphName + " are:" + print);
    }

    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String start = arguments.get(1);
        String end = arguments.get(2);
        findPath(graphName, start, end);
    }

    private void findPath(String graphName, String start, String end) {
        // TODO Insert your code here.
        GraphADT<String, Double> temp = graphs.get(graphName);
        Dijkstra<String> dijkstra = new Dijkstra<String>();
        if(temp.findNode(start) && temp.findNode(end)){
            Path<String> path = dijkstra.findPath(temp, start, end);
            if(path == null){
                output.println("path from " + start + " to " + end + ":");
                output.println("no path found");
            } else{
                output.println("path from " + start + " to " + end +":");
                Iterator<Path<String>.Segment> iterator = path.iterator();
                while(iterator.hasNext()){
                    Path<String>.Segment curr = iterator.next();
                    Double currCost = curr.getCost();
                    output.println(curr.getStart() + " to " + curr.getEnd() + String.format(" with weight %.3f", currCost));
                }
                output.println(String.format("total cost: %.3f", path.getCost()));
            }
        }
        if(!temp.findNode(start)){
            output.println("unknown: " + start);
        }
        if(!temp.findNode(end)){
            output.println("unknown: " + end);
        }
    }

    private Set<String> orderString(Set<String> set){
        Set<String> orderSet = new TreeSet<>();
        orderSet.addAll(set);
        return orderSet;
    }

    private Set<Double> orderEdge(Set<Double> set){
        Set<Double> orderSet = new TreeSet<>();
        orderSet.addAll(set);
        return orderSet;
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
