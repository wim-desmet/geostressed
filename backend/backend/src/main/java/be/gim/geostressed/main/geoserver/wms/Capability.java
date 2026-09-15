package be.gim.geostressed.main.geoserver.wms;

import be.gim.geostressed.main.geoserver.wfs.TYPES;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "/", namespace = TYPES.NAMESPACE_WMS)
public class Capability {
    @JacksonXmlProperty(localName = "Layer", namespace = TYPES.NAMESPACE_WMS)
    private TopLayer topLayer;

    public TopLayer getTopLayer() {
        return topLayer;
    }

    public void setTopLayer(TopLayer topLayer) {
        this.topLayer = topLayer;
    }
}
