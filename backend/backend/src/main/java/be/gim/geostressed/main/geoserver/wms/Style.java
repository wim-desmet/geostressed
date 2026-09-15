package be.gim.geostressed.main.geoserver.wms;

import be.gim.geostressed.main.geoserver.wfs.TYPES;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Style {

    @JacksonXmlProperty(localName = "Name", namespace = TYPES.NAMESPACE_WMS)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
