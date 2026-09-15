package be.gim.geostressed.main.jmeter;

import java.util.List;

public record TestConfigurationDAO(
        List<String> crs,
        List<Layer> layers,
        Integer users,
        Integer loops,
        Url url) {

    public record Layer(
            String name,
            String defaultStyle,
            Boolean wfs,
            Boolean wms,
            Boolean wmts,
            Boolean features,
            Boolean maps
    ) {
    }

    public record Url(String protocol, String host, String path) {
    }
}
