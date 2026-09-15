package be.gim.geostressed.main.geoserver.wms;

import be.gim.geostressed.main.geoserver.wfs.TYPES;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "WMS_Capabilities", namespace = TYPES.NAMESPACE_WMS)
public class WMSCapabilities {

    @JacksonXmlProperty(localName = "Capability", namespace = TYPES.NAMESPACE_WMS)
    private Capability capability;

    public Capability getCapability() {
        return capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }
}