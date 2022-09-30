package com.mohitvijayv.map.service;

import com.mohitvijayv.map.model.Point;
import org.springframework.stereotype.Service;

import static com.mohitvijayv.map.utils.Constants.AVERAGE_RADIUS_OF_EARTH_KM;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

@Service
public class GeometryService {

    public Point interpolate(float fraction, Point from, Point to) {
        double fromLat = toRadians(from.getLat());
        double fromLng = toRadians(from.getLng());
        double toLat = toRadians(to.getLat());
        double toLng = toRadians(to.getLng());
        double cosFromLat = cos(fromLat);
        double cosToLat = cos(toLat);
        double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
        double sinAngle = sin(angle);
        if (sinAngle < 1E-6) {
            return from;
        }
        double a = sin((1 - fraction) * angle) / sinAngle;
        double b = sin(fraction * angle) / sinAngle;
        double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
        double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
        double z = a * sin(fromLat) + b * sin(toLat);
        double lat = atan2(z, sqrt(x * x + y * y));
        double lng = atan2(y, x);
        return new Point(toDegrees(lat), toDegrees(lng));
    }


    private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {
        double dLat = fromLat - toLat;
        double dLng = fromLng - toLng;
        return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
            cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
    }


    public int calculateDistanceInMeter(double userLat, double userLng,
        double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
            * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) ((AVERAGE_RADIUS_OF_EARTH_KM * c)*1000);
    }

}


