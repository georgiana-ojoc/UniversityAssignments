package com.georgiana.ojoc.model.graph;

import java.util.List;

/**
 * implementation: https://arxiv.org/pdf/1201.3011.pdf
 */
public class FruchtermanReingold {
    private Graph graph;
    private List<Node> nodes;
    private List<Edge> edges;
    int width;
    int height;
    double frameArea;
    double temperature;
    int iterations = 1;
    double optimalDistance;
    double[] displacementX;
    double[] displacementY;
    double[] positionX;
    double[] positionY;

    public FruchtermanReingold(Graph graph) {
        this.graph = graph;
        this.width = graph.width;
        this.height = graph.height;
        frameArea = width * height;
        temperature = width / 10.0;
        nodes = graph.getNodes();
        edges = graph.getEdges();
        optimalDistance = 0.75 * Math.sqrt(frameArea / nodes.size());
        displacementX = new double[nodes.size()];
        displacementY = new double[nodes.size()];
        positionX = new double[nodes.size()];
        positionY = new double[nodes.size()];
    }

    public Graph getGraph() { return graph; }

    public void run() {
        initialize();
        for (int iteration = 0; iteration < iterations; ++iteration) {
            computeRepulsions();
            computeAttractions();
            computePositions();
            cool(iteration);
        }
        scalePositions();
        finish();
    }

    private void initialize() {
        for (int index = 0; index < nodes.size(); ++index) {
            positionX[index] = nodes.get(index).getX();
            positionY[index] = nodes.get(index).getY();
        }
    }

    private void computeRepulsions() {
        for (int source = 0; source < nodes.size(); ++source) {
            displacementX[source] = 0.0;
            displacementY[source] = 0.0;
            for (int target = 0; target < nodes.size(); ++target) {
                if (!nodes.get(source).equals(nodes.get(target))) {
                    double sourceX = positionX[source];
                    double sourceY = positionY[source];
                    double targetX = positionX[target];
                    double targetY = positionY[target];
                    double distanceX = sourceX - targetX;
                    double distanceY = sourceY - targetY;
                    double distance = Math.max(0.000001, Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));
                    double repulsiveForce = Math.pow(optimalDistance, 2) / distance;
                    displacementX[source] += (distanceX / distance) * repulsiveForce;
                    displacementY[source] += (distanceY / distance) * repulsiveForce;
                }
            }
        }
    }

    private void computeAttractions() {
        for (Edge edge : edges) {
            double sourceX = positionX[graph.getNodeIndex(edge.getSource())];
            double sourceY = positionY[graph.getNodeIndex(edge.getSource())];
            double targetX = positionX[graph.getNodeIndex(edge.getTarget())];
            double targetY = positionY[graph.getNodeIndex(edge.getTarget())];
            double distanceX = sourceX - targetX;
            double distanceY = sourceY - targetY;
            double distance = Math.max(0.000001, Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));
            double attractiveForce = Math.pow(distance, 2) / optimalDistance;
            double attractiveDisplacementX = (distanceX / distance) * attractiveForce;
            double attractiveDisplacementY = (distanceY / distance) * attractiveForce;
            displacementX[graph.getNodeIndex(edge.getSource())] -= attractiveDisplacementX;
            displacementY[graph.getNodeIndex(edge.getSource())] -= attractiveDisplacementY;
            displacementX[graph.getNodeIndex(edge.getTarget())] += attractiveDisplacementX;
            displacementY[graph.getNodeIndex(edge.getTarget())] += attractiveDisplacementY;
        }
    }

    private void computePositions() {
        for (int index = 0; index < nodes.size(); ++index) {
            double distance = Math.max(0.000001, Math.sqrt(Math.pow(displacementX[index], 2) +
                    Math.pow(displacementY[index], 2)));
            positionX[index] += displacementX[index] / distance * Math.min(displacementX[index], temperature);
            positionY[index] += displacementY[index] / distance * Math.min(displacementY[index], temperature);
        }
    }

    private void scalePositions() {
        for (int index = 0; index < positionX.length; ++index) {
            positionX[index] %= width;
        }
        for (int index = 0; index < positionY.length; ++index) {
            positionY[index] %= height;
        }
    }

    private void finish() {
        for (int index = 0; index < nodes.size(); ++index) {
            nodes.get(index).setX(positionX[index]);
            nodes.get(index).setY(positionY[index]);
        }
    }

    public void cool(int iteration) {
        temperature *= (1.0 - (double) iteration / iterations);
    }
}
