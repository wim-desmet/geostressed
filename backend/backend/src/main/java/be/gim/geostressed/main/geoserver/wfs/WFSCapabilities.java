package be.gim.geostressed.main.geoserver.wfs;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "WFS_Capabilities", namespace = TYPES.NAMESPACE_WFS)
public class WFSCapabilities {


    @JacksonXmlProperty (localName = "updateSequence", isAttribute = true)
    private int updateSequence;

    @JacksonXmlElementWrapper(localName = "FeatureTypeList", namespace = TYPES.NAMESPACE_WFS)
    @JacksonXmlProperty(localName = "FeatureType", namespace = TYPES.NAMESPACE_WFS)
    private List<FeatureType> featureTypeList;

    public int getUpdateSequence() {
        return updateSequence;
    }

    public void setUpdateSequence(int updateSequence) {
        this.updateSequence = updateSequence;
    }

    public List<FeatureType> getFeatureTypeList() {
        return featureTypeList;
    }

    public void setFeatureTypeList(List<FeatureType> featureTypeList) {
        this.featureTypeList = featureTypeList;
    }
}