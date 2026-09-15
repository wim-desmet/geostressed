package be.gim.geostressed.main.geoserver;

import be.gim.geostressed.main.geoserver.wfs.FeatureType;

import java.util.List;

public record ServerInfo(List<FeatureType> featureTypeList, List<String> CRS) {
}
