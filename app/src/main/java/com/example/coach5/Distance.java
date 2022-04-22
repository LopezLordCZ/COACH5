package com.example.coach5;

public class Distance {

    //Distance calculation function
    public static double calcDistance(double lat1, double lat2, double lon1, double lon2) {

        //Earth radius
        final int R = 6371;

        //Latitude
        double latDistance = Math.toRadians(lat2 - lat1);
        //Longitude
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        //Result
        double distance = R * c;

        return Math.round(distance);

    }
}
