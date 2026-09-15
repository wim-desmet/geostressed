package be.gim.geostressed.main.geoserver;

import be.gim.geostressed.main.geoserver.wfs.WFSCapabilities;
import be.gim.geostressed.main.geoserver.wms.Layer;
import be.gim.geostressed.main.geoserver.wms.WMSCapabilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@RestController
@RequestMapping("api/geoserver")
public class GeoserverConfigController {

    private final RestClient restClient;

    public GeoserverConfigController(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping("wfs_capabilities")
    public ServerInfo getMapping(@RequestBody String geoserverBaseUrl) throws JsonProcessingException, MalformedURLException {
        URI wfsCapabilitiesURI = UriComponentsBuilder
                .fromUriString(geoserverBaseUrl + "/ows")
                .queryParam("service", "WFS")
                .queryParam("acceptversions", "2.0.0")
                .queryParam("request", "GetCapabilities")
                .encode()
                .build()
                .toUri();

        URI wmsCapabilitiesURI = UriComponentsBuilder
                .fromUriString(geoserverBaseUrl + "/ows")
                .queryParam("service", "WMS")
                .queryParam("acceptversions", "1.3.0")
                .queryParam("request", "GetCapabilities")
                .encode()
                .build()
                .toUri();

        System.out.println(geoserverBaseUrl);
        System.out.println(wfsCapabilitiesURI.toString());

        WFSCapabilities wfsCapabilities = restClient.get()
                .uri(wfsCapabilitiesURI)
                .retrieve()
                .body(WFSCapabilities.class);

        WMSCapabilities wmsCapabilities = restClient.get()
                .uri(wmsCapabilitiesURI)
                .retrieve()
                .body(WMSCapabilities.class);

        List<Layer> flattenedWmsLayers = new ArrayList<>();

        flattenedWmsLayers.addAll(wmsCapabilities.getCapability().getTopLayer().getLayers());
        flattenedWmsLayers.addAll(wmsCapabilities.getCapability().getTopLayer().getLayers().stream().flatMap(layer -> {
            if (layer.getLayers() != null) {
                return layer.getLayers().stream();
            } else {
                return Stream.empty();
            }
        }).toList());

        assert wfsCapabilities != null;

        wfsCapabilities.getFeatureTypeList().forEach(featureType -> {
            Optional<Layer> matchedWMSLayer = flattenedWmsLayers.stream().filter(layer -> layer.getName().equals(featureType.getName())).findFirst();
            matchedWMSLayer.ifPresent(layer -> featureType.setDefaultStyle(layer.getStyle().getName()));
        });

        return new ServerInfo(
                wfsCapabilities.getFeatureTypeList(),
                wmsCapabilities.getCapability().getTopLayer().getCRS()
        );
    }
}
