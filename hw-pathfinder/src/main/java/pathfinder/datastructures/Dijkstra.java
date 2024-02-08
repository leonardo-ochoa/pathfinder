package pathfinder.datastructures;

import graph.GraphADT;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra<T> {

    class doubleComparator implements Comparator<Path<T>> {
        @Override
        public int compare(Path<T> x, Path<T> y){
            if(x.getCost() < y.getCost()){
                return -1;
            }
            if(x.getCost() > y.getCost()){
                return 1;
            }
            return 0;
        }
    }

    private Comparator<Path<T>> comparator;
    private PriorityQueue<Path<T>> active;
    public Dijkstra(){
        comparator = new doubleComparator();
        active = new PriorityQueue<Path<T>>(comparator);
    }

    public Path<T> findPath(GraphADT<T, Double> graph, T nodeA, T nodeB){
        T start = nodeA;
        T dest = nodeB;
        Set<T> finished = new HashSet<T>();
        Path<T> path = new Path<T>(start);
        active.add(path);
        while (!active.isEmpty()){
            Path<T> minPath = active.remove();
            T minDest = minPath.getEnd();
            if (minDest.equals(dest)){
                return minPath;
            }
            if (finished.contains(minDest)){
                continue;
            }
            for(Double edge: graph.outGoingEdges(minDest)){
                for(T child: graph.findEdgeFrom(minDest, edge)){
                    if(!finished.contains(child)){
                        Path<T> newPath = minPath.extend(child, edge);
                        active.add(newPath);
                    }
                }
                finished.add(minDest);
            }
        }
        return null;
    }
}
