package com.example.vrp;

import java.util.ArrayList;
import java.util.List;

public class VRP {

    VRPLocation deposits;
    List<VRPLocation> locations;
    int vehicleCapacity;

    public VRP(VRPLocation deposits, List<VRPLocation> locations, int vehicleCapacity) {
        this.deposits = deposits;
        this.locations = locations;
        this.vehicleCapacity = vehicleCapacity;
    }

    public List<VRPRoute> solveVRP() {
        List<VRPLocation> unassignedLocation = new ArrayList<>(locations);
        List<VRPRoute> routes = new ArrayList<>();

        while (!unassignedLocation.isEmpty()) {
            VRPRoute route = new VRPRoute();
            int currentDemand = 0;
            VRPLocation currentLocation = deposits;

            while (currentLocation != null) {
                if (route.locations.isEmpty() || isValidAddition(route, currentLocation, vehicleCapacity, currentDemand)) {
                    route.locations.add(currentLocation);
                    currentDemand += currentLocation.request;
                    unassignedLocation.remove(currentLocation);
                    currentLocation = findNearestNeighbor(currentLocation, unassignedLocation);
                } else {
                    currentLocation = null;
                }
            }
            // Add the depot as the ending point of the route
            route.locations.add(deposits);
            routes.add(route);
        }
        return routes;
    }

    public boolean isValidAddition(VRPRoute route, VRPLocation location, int vehicleCapacity, int currentDemand) {
        // Check if adding the customer to the route exceeds the vehicle capacity
        return (currentDemand + location.request) <= vehicleCapacity;
    }

    public VRPLocation findNearestNeighbor(VRPLocation customer, List<VRPLocation> candidates) {
        double minDistance = Double.MAX_VALUE;
        VRPLocation nearestNeighbor = null;

        for (VRPLocation candidate : candidates) {
            double distance = calculateDistance(customer, candidate);
            if (distance < minDistance) {
                minDistance = distance;
                nearestNeighbor = candidate;
            }
        }
        return nearestNeighbor;
    }

    public double calculateDistance(VRPLocation c1, VRPLocation c2) {
        return Math.sqrt(Math.pow(c2.x - c1.x, 2) + Math.pow(c2.y - c1.y, 2));
    }

}
