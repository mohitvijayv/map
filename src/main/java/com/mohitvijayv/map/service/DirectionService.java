package com.mohitvijayv.map.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohitvijayv.map.model.Direction;
import com.mohitvijayv.map.model.Point;
import com.mohitvijayv.map.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DirectionService {

    private final PolylineService polylineService;
    private final GeometryService geometryService;
    private final HTTPClientService httpClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DirectionService(final PolylineService polylineService, final GeometryService geometryService, final HTTPClientService httpClient) {
        this.polylineService = polylineService;
        this.geometryService = geometryService;
        this.httpClient = httpClient;
    }

    public String constructUrl(String origin, String destination){
        String delimiter = "&";
        StringBuilder url = new StringBuilder(Constants.MAPS_URL);
        url.append("origin=");
        url.append(origin);
        url.append(delimiter);
        url.append("destination=");
        url.append(destination);
        url.append(delimiter);
        url.append("key=");
        url.append(Constants.API_KEY);

        return url.toString();
    }

    public String getPoints(String origin, String destination) throws IOException {
        String url = constructUrl(origin, destination);

        String response = httpClient.get(url);

        String polyline = getPolyline(response);
        List<Point> points = new ArrayList<>(polylineService.decode(polyline));
        return PolylineService.toString(equidistantPointsOnRoute(points));
    }

    public String getPolyline(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper.readValue(response, Direction.class).getRoutes().get(0).getOverview_polyline().getPoints();
    }

    public List<Point> equidistantPointsOnRoute(List<Point> points){
        List<Point> equidistantPoints = new ArrayList<>();
        equidistantPoints.add(points.get(0));

        float sum=0;
        float sumPrev;
        int curr=Constants.DISTANCE;

        for(int i=1; i<points.size(); i++){
            sumPrev=sum;
            sum+=geometryService.calculateDistanceInMeter(points.get(i-1).getLat(), points.get(i-1).getLng(), points.get(i).getLat(), points.get(i).getLng());
            while(sum>curr){
                float fraction = (curr-sumPrev)/(sum-sumPrev);
                Point a = geometryService.interpolate(fraction, points.get(i-1), points.get(i));
                logger.info("{},{},#FFA500,marker", a.getLat(),a.getLng());
                equidistantPoints.add(a);
                curr+=Constants.DISTANCE;
            }
            if(sum==curr){
                logger.info("{},{},#FFA500,marker", points.get(i).getLat(),points.get(i).getLng());
                equidistantPoints.add(points.get(i));
                curr+=Constants.DISTANCE;
            }
            else if (i==points.size()-1) {
                logger.info("{},{},#FFA500,marker", points.get(i).getLat(),points.get(i).getLng());
                equidistantPoints.add(points.get(i));
            }
        }
        return equidistantPoints;
    }
}
