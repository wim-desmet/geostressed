package be.gim.geostressed.main.geoserver;

import be.gim.geostressed.main.geoserver.wfs.WFSCapabilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;


@RestController
@RequestMapping("api/geoserver")
public class GeoserverConfigController {

    private final RestClient restClient;

    public GeoserverConfigController(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping("wfs_capabilities")
    public WFSCapabilities getMapping(@RequestBody String geoserverBaseUrl) throws JsonProcessingException, MalformedURLException {
        URI wfsCapabilitiesURI = UriComponentsBuilder
                .fromUriString(geoserverBaseUrl)
                .queryParam("service", "WFS")
                .queryParam("acceptversions", "2.0.0")
                .queryParam("request", "GetCapabilities")
                .encode()
                .build()
                .toUri();

        System.out.println(geoserverBaseUrl);
        System.out.println(wfsCapabilitiesURI.toString());

        WFSCapabilities capabilities = restClient.get()
                .uri(wfsCapabilitiesURI)
                .retrieve()
                .body(WFSCapabilities.class);

        return capabilities;
    }
}
