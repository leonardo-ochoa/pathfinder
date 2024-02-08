package graph.junitTests;
import graph.GraphADT;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class testGraphADT {

    private static GraphADT<String, String> A = new GraphADT<String, String>(); // always empty
    private static GraphADT<String, String> B = new GraphADT<String, String>();
    private static GraphADT<String, String> C = new GraphADT<String, String>();
    private static GraphADT<String, String> D = new GraphADT<String, String>();
    private static GraphADT<String, String> F = new GraphADT<String, String>();
    private static GraphADT<String, String> H = new GraphADT<String, String>();

    @BeforeClass
    public static void setUp(){
        B.addNode("1");
        B.addNode("2");
        B.addNode("3");
        // Graph C with 1 node
        C.addNode("1");
        // Graph D with 2 nodes
        D.addNode("1");
        D.addNode("2");
        D.addEdges("1", "1", "a");
        D.addEdges("1", "2", "a");
        D.addEdges("1", "1", "b");
        D.addEdges("2", "1", "b");
        D.addEdges("1", "2", "c");
        D.addEdges("1", "2", "d");
        D.addEdges("2", "1", "e");
        D.addEdges("2", "1", "f");
        // Graph F starting with 3 nodes
        F.addNode("1");
        F.addNode("2");
        F.addNode("3");
        //
        H.addNode("1");
        H.addNode("2");
        H.addNode("3");
        H.addNode("4");
    }

    @Test
    public void testSize() {
        assertTrue(A.size() == 0);
        assertTrue(B.size() == 3);
        B.addNode("4");
        assertTrue(B.size() == 4);
        assertTrue(C.size() == 1);
    }

    @Test
    public void testGraphD(){
        assertTrue(D.size() == 2);
        assertEquals(Arrays.asList("1", "2"), toArray(D.getNodes()));
        // check if nodes exist in graph
        assertTrue(D.findNode("1"));
        assertTrue(D.findNode("2"));
        // check for the parents of the node in graph
        assertEquals(Arrays.asList("1", "2"), toArray(D.parenOfChildren("1")));
        assertEquals(Arrays.asList("1"), toArray(D.parenOfChildren("2")));
        // check for the children of the node in graph
        assertEquals(Arrays.asList("1", "2"), toArray(D.childrenOfParent("1")));
        assertEquals(Arrays.asList("1"), toArray(D.childrenOfParent("2")));
        // find child from given edge and parent
        assertEquals(Arrays.asList("1"), toArray(D.findEdgeFrom("1", "b")));
        assertEquals(Arrays.asList("1", "2"), toArray(D.findEdgeFrom("1", "a")));
        assertEquals(Arrays.asList(), toArray(D.findEdgeFrom("2", "a"))); // if a is not a valid edge
        assertEquals(Arrays.asList("1"), toArray(D.findEdgeFrom("2", "f")));
        // find parent from given edge and child
        assertEquals(Arrays.asList("1", "2"), toArray(D.findEdgeTo("1", "b")));
        assertEquals(Arrays.asList("1"), toArray(D.findEdgeTo("1", "a")));
        assertEquals(Arrays.asList("1"), toArray(D.findEdgeTo("2", "a")));
        assertEquals(Arrays.asList("2"), toArray(D.findEdgeTo("1", "f")));
        assertEquals(Arrays.asList(), toArray(D.findEdgeTo("1", "c"))); // if c is not a valid edge
        // return the edges of parent and children
        assertEquals(Arrays.asList("a", "c", "d"), toArray(D.getEdges("1", "2")));
        assertEquals(Arrays.asList("b", "e", "f"), toArray(D.getEdges("2", "1")));
        assertEquals(Arrays.asList("a", "b"), toArray(D.getEdges("1", "1")));
        // outgoing edges
        assertEquals(Arrays.asList("a", "b", "c", "d"), toArray(D.outGoingEdges("1")));
        assertEquals(Arrays.asList("b", "e", "f"), toArray(D.outGoingEdges("2")));
        // incoming edges
        assertEquals(Arrays.asList("a", "b", "e", "f"), toArray(D.incomingEdges("1")));
        assertEquals(Arrays.asList("a", "c", "d"), toArray(D.incomingEdges("2")));
        // find edges
        assertEquals(Arrays.asList("1", "1", "1", "2"), extractMap(D.findEdge("a")));
        assertEquals(Arrays.asList("1", "1", "2", "1"), extractMap(D.findEdge("b")));
        assertEquals(Arrays.asList("2", "1"), extractMap(D.findEdge("e")));
        assertEquals(Arrays.asList(), extractMap(D.findEdge("x"))); // x is not a valid edge
    }

    @Test
    public void testGraphF(){
        // test when no added edges
        assertEquals(Arrays.asList(), toArray(F.getEdges("1", "2")));
        // adding edges
        F.addEdges("1", "2", "d");
        assertEquals(Arrays.asList("d"), toArray(F.getEdges("1", "2")));
        F.addEdges("1", "2", "e");
        assertEquals(Arrays.asList("d", "e"), toArray( F.getEdges("1", "2")));
        F.addEdges("1", "2", "a");
        assertEquals(Arrays.asList("a", "d", "e"), toArray(F.getEdges("1", "2")));
        // find edge
        assertEquals(Arrays.asList("1", "2"), extractMap(F.findEdge("a"))); //////// map
        // add edge
        F.addEdges("1", "3", "a");
        // find parent and children of edge
        assertEquals(Arrays.asList("1", "2", "1", "3"), extractMap(F.findEdge("a")));
        //add edge
        F.addEdges("3", "2", "a");
        // find parent and children of edge
        assertEquals(Arrays.asList("1", "2", "1", "3", "3", "2"), extractMap(F.findEdge("a")));
        // find children of parent with specified edge
        assertEquals(Arrays.asList("2", "3"), toArray(F.findEdgeFrom("1", "a")));
        assertEquals(Arrays.asList("2"), toArray(F.findEdgeFrom("1", "e")));
        assertEquals(Arrays.asList(), toArray(F.findEdgeFrom("1", "x"))); //parent exist but not edge
        // find parents of child with specified edge
        assertEquals(Arrays.asList("1", "3"), toArray(F.findEdgeTo("2", "a")));
        assertEquals(Arrays.asList("1"), toArray(F.findEdgeTo("2", "d")));
        assertEquals(Arrays.asList(), toArray(F.findEdgeTo("2", "x")));
    }

    @Test
    public void testNodes(){
        assertTrue(H.findNode("1"));
        assertFalse(H.findNode("5"));
        assertEquals(Arrays.asList("1", "2", "3", "4"), toArray(H.getNodes()));
        assertEquals(Arrays.asList(),toArray(A.getNodes()));
        H.addEdges("1", "2", "a");
        H.addEdges("1", "3", "a");
        H.addEdges("1", "1", "a");
        H.addEdges("3", "2", "a");
        H.addEdges("4", "3", "a");
        assertEquals(Arrays.asList("1", "2", "3"), toArray(H.childrenOfParent("1")));
        assertEquals(Arrays.asList(), toArray(H.childrenOfParent("2")));
        assertEquals(Arrays.asList("2"), toArray(H.childrenOfParent("3")));
        assertEquals(Arrays.asList("3"), toArray(H.childrenOfParent("4")));
        assertEquals(Arrays.asList("1"), toArray(H.parenOfChildren("1")));
        assertEquals(Arrays.asList("1", "3"), toArray(H.parenOfChildren("2")));
        assertEquals(Arrays.asList("1", "4"), toArray(H.parenOfChildren("3")));
        assertEquals(Arrays.asList(), toArray(H.parenOfChildren("4")));
    }

    @AfterClass
    public static void TestAfter(){
        assertTrue(B.findNode("4"));
        assertFalse(B.findNode("5"));
    }

    private List<String> toArray(Set<String> s){
        List<String> list = new ArrayList<>();
        s = order(s);
        for(String x: s){
            list.add(x);
        }
        return list;
    }

    private Set<String> order(Set<String> set){
        Set<String> orderSet = new TreeSet<>();
        orderSet.addAll(set);
        return orderSet;
    }

    private List<String> extractMap(Map<String, Set<String>> map){
        List<String> list = new ArrayList<>();
        for(String parent: map.keySet()){
            Set<String> children = map.get(parent);
            for(String child: children){
                list.add(parent);
                list.add(child);
            }
        }
        return list;
    }

}
