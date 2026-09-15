package be.gim.geostressed.main.jmeter;

import java.util.List;

public record TemplateConfiguration
        (List<ThreadGroup> threadGroups,
         Integer users,
         Integer loops,
         Url url) {

    public record ThreadGroup(
            String crs,
            String protocol,
            Protocol wfs,
            Protocol wms,
            Protocol wmts,
            Protocol features,
            Protocol maps
    ) {}

    public record Protocol(
            String protocol,
            String crs,
            List<Layer> layers
    ) {}

    public record Url(String protocol, String host, String path) {
    }

    public record Layer(String name, String defaultStyle) {
    }
}
