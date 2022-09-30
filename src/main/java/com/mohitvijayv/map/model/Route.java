package com.mohitvijayv.map.model;

public class Route {
    private OverviewPolyline overview_polyline;
    private String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public OverviewPolyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(final OverviewPolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }
}
