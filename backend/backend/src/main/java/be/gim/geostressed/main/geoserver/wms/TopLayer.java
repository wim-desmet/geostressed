package be.gim.geostressed.main.geoserver.wms;

import be.gim.geostressed.main.geoserver.wfs.TYPES;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class TopLayer {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "CRS", namespace = TYPES.NAMESPACE_WMS)
    private List<String> CRS;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Layer", namespace = TYPES.NAMESPACE_WMS)
    private List<Layer> layers;


    public List<String> getCRS() {
        return CRS;
    }

    public void setCRS(List<String> CRS) {
        this.CRS = CRS;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }
}
