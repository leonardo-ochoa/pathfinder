package graph;

import java.util.*;

/**
 * A GraphADT is a mutable representation of a directed labeled graph
 * @param <N> represents a node in the graph
 * @param <E> represents an edge from one node to another in this graph
 */
public class GraphADT<N, E> {
    public static final boolean DEBUG = false;
    /**
     *  A node is mutable class that has information about the nodes name, edges, children and parents
     */
    public class Node{
        //RI: data != null,
        // edgeParent != null, no value must equal null, and no string in set must equal null
        // edgeChild != null, no value must equal null, and no string in set must equal null
        // outGoingEdges != null, no string in set equals null
        // incomingEdges != null, no string in set equals null
        //AF(this): a node whose name is stored in data.
        // the node stores its children and all their shared edges in edgeParent
        // the node stores its parents and all their shared edges in edgeChild
        // The node assumes it's a parent when it stores its outgoing edges in outGoingEdges
        // The node assumes it's a child when it stores its incoming edges in incomingEdges
        private final N data;
        private Map<N, Set<E>> edgeParent;
        private Map<N, Set<E>> edgeChild;
        private Set<E> outGoingEdges;
        private Set<E> incomingEdges;
        /**
         * creates a Node that has the given string and initializes its properties
         * @param data sets the Nodes name
         * @spec.requires data != null
         * @spec.effects this = data
         */
        public Node(N data){
            this.data = data;
            this.edgeParent = new HashMap<>();
            this.edgeChild = new HashMap<>();
            this.outGoingEdges = new HashSet<>();
            this.incomingEdges = new HashSet<>();
        }
    }

    // RI: list[0] != null, list[i] != null, list[n-1] != null, and list != null and no duplicates
    // AF(this): list[i].string stores the nodes name,
    // list[i].edgeParent contains this node's child as the value and all edges connecting them as a set.
    // list[i].edgeChild contains this node's parent as the value and all edges connecting them as a set.
    // list[i].outGoingEdges assumes that this node is the parent and stores all the edges pointing away from this node
    // list[i].incoming edges assumes that this node is the child and stores all the edges pointing to this node
    private Set<Node> list;

    /**
     * creates a GraphADT that is empty
     * @spec.effects this != null
     */
    public GraphADT(){
        list = new HashSet<>();
        checkRep();
    }

    /**
     * adds a new node to the graph
     * @param nodeLabel the New Node to add
     * @spec.requires nodeLabel != null and cannot have a node with the same label
     * @spec.effects  this has a new node
     */
    public void addNode(N nodeLabel){
        checkRep();
        list.add(new Node(nodeLabel));
        checkRep();
    }

    /**
     * creates a labeled edge of type E from parent to child
     * @param parent a Node where the edge will come from
     * @param child a Node where the edge will go into
     * @param edgeLabel is what we will label the edge as
     * @spec.requires child, parent, edgeLabel to not equal null and parent and child must
     * not have an existing edgeLabel
     */
    public void addEdges(N parent, N child, E edgeLabel){
        checkRep();
        Node parentNode = getNode(parent);
        Node childNode = getNode(child);
        parentNode.outGoingEdges.add(edgeLabel);
        childNode.incomingEdges.add(edgeLabel);
        if(!parentNode.edgeParent.containsKey(child)){
            parentNode.edgeParent.put(child, new HashSet<>());
        }
        parentNode.edgeParent.get(child).add(edgeLabel);
        if(!childNode.edgeChild.containsKey(parent)){
            childNode.edgeChild.put(parent, new HashSet<>());
        }
        childNode.edgeChild.get(parent).add(edgeLabel);
        checkRep();
    }

    /**
     * returns number of nodes in this graph
     * @return number of nodes
     */
    public int size(){
        checkRep();
        return list.size();
    }

    /**
     * returns a list containing all the nodes in this
     * @return if this is not empty then returns a list of objects
     * otherwise returns empty set
     */
    public Set<N> getNodes(){
        checkRep();
        Set<N> nodes = new HashSet<>();
        for(Node x: list){
            nodes.add(x.data);
        }
        checkRep();
        return copyToNode(nodes);
    }


    /**
     * check if the given node exists in the graph
     * @param node the node we will be looking for
     * @spec.requires node to not be null
     * @return if node is in the graph then returns true
     * otherwise returns false
     */
    public boolean findNode(N node){
        checkRep();
        for (Node x: list){
            if (x.data.equals(node)){
                return true;
            }
        }
        checkRep();
        return false;
    }

    /**
     * checks if the given edge exists in the graph
     * @param edge the edge we are looking for
     * @spec.requires parent, child, and edge to not be null
     * @return if edge exists in the graph then return a map that has the
     * parent as the key and a list of children as the value otherwise returns null
     */
    public Map<N, Set<N>> findEdge(E edge){
        checkRep();
        Map<N, Set<N>> map = new HashMap<>();
        for(Node i: list){
            Set<N> children = findEdgeFrom(i.data, edge);
            map.put(i.data, children);
        }
        checkRep();
        return map;
    }

    /**
     * returns the children of this parent that has the specified edge
     * @param parent the node from which the edge comes from
     * @param edge the edge we are looking for
     * @spec.requires parent, and edge to not be null and parent node must exist in this
     * @return if parent and edge exist in this then it returns a set of children
     * otherwise if only parent exist in this then it returns an empty set
     */
    public Set<N> findEdgeFrom(N parent, E edge){
        checkRep();
        Set<N> children = childrenOfParent(parent); ///
        Set<N> childrenFound = new HashSet<>();
        Node p = getNode(parent);
        for(N x: children){
            if(p.edgeParent.get(x).contains(edge)){
                childrenFound.add(x);
            }
        }
        checkRep();
        return copyToNode(childrenFound);
    }

    /**
     * returns the parents of this child that has the specified edge
     * @param child the node from which the edge goes into
     * @param edge the edge we are looking for
     * @spec.requires child, and edge to not be null and child must exist in this
     * @return if child and edge exist in this then it returns a set of parents
     *  otherwise if only child exist in this then it returns an empty set
     */
    public Set<N> findEdgeTo(N child, E edge){
        checkRep();
        Set<N> parent = parenOfChildren(child);
        Set<N> parentFound = new HashSet<>();
        Node c = getNode(child);
        for(N x: parent){
            if(c.edgeChild.get(x).contains(edge)){
                parentFound.add(x);
            }
        }
        checkRep();
        return copyToNode(parentFound);
    }

    /**
     * returns a list of all the nodes who share the same parent
     * @param parent a Node whose children we will look for
     * @spec.requires parent to be a valid node in the graph
     * @return if parent has children then it will return a list of objects
     *  otherwise it will return an empty list
     */
    public Set<N> childrenOfParent(N parent){
        checkRep();
        Node p = getNode(parent);
        checkRep();
        return copyToNode(p.edgeParent.keySet());
    }

    /**
     * returns a list of all the nodes who share the same child
     * @param child a Node whose Parents we will look for
     * @spec.requires ch to be a valid node in the graph
     * @return if child has parents then it will return a list of objects,
     *  otherwise it will return an empty list
     */
    public Set<N> parenOfChildren(N child){
        checkRep();
        Node c = getNode(child);
        checkRep();
        return copyToNode(c.edgeChild.keySet());
    }

    /**
     *
     * gets the edges that connect a parent to a child
     * @param parent the Node from which the edge comes from
     * @param child the Node to where the edge goes to
     * @return if parent connect with a children then return label
     *  otherwise return empty set
     */
    public Set<E> getEdges(N parent, N child){
        checkRep();
        Node p = getNode(parent);
        checkRep();
        Set<E> edge = p.edgeParent.get(child); //////////////////////////////////////////////////
        return copyToEdge(edge);
    }

    /**
     * Since we are returning the outgoing edges this node is a parent node
     * @param node the nodes which we are trying to find its outgoing edges
     * @spec.requires node to not be null
     * @return the out going edges of the node
     */
    public Set<E> outGoingEdges(N node){
        checkRep();
        Node n = getNode(node);
        checkRep();
        return copyToEdge(n.outGoingEdges);
    }

    /**
     * Since we are returning the incoming edges this node is a child node
     * @param node the nodes which we are trying to find its outgoing edges
     * @spec.requires node to not be null
     * @return the incoming edges of the node
     */
    public Set<E> incomingEdges(N node){
        checkRep();
        Node n = getNode(node);
        checkRep();
        return copyToEdge(n.incomingEdges);
    }

    /**
     * returns the specified node
     * @param node the node we are looking for
     * @spec.requires node to be a node in this
     * @returns the node in this we found
     */
    private Node getNode(N node){
        checkRep();
        Node temp = null;
        for(Node x: list){
            if(x.data.equals(node)){
                temp = x;
            }
        }
        checkRep();
        return temp;
    }

    private Set<N> copyToNode(Set<N> current){
        Set<N> copy = new HashSet<N>();
        for(N node: current){
            copy.add(node);
        }
        return copy;
    }

    private Set<E> copyToEdge(Set<E> current){
        Set<E> copy = new HashSet<E>();
        if(current != null){
            for(E edge: current){
                copy.add(edge);
            }
        }
        return copy;
    }

    public void checkRep() {
        if(list == null){
            throw new IllegalArgumentException();
        }
        if (DEBUG)  {
            for(Node nodes: list){
                if(nodes == null){
                    throw new IllegalArgumentException();
                }
            }

        }
    }
}
