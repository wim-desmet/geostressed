package be.gim.geostressed.main.geoserver.wms;

import be.gim.geostressed.main.geoserver.wfs.TYPES;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Layer {

    @JacksonXmlProperty(localName = "Name", namespace = TYPES.NAMESPACE_WMS)
    private String name;

    @JacksonXmlProperty(localName = "Style", namespace = TYPES.NAMESPACE_WMS)
    private Style style;


    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Layer", namespace = TYPES.NAMESPACE_WMS)
    private List<Layer> layers;

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Style getStyle() {
        return style;
    }


    public void setStyle(Style style) {
        this.style = style;
    }
}
