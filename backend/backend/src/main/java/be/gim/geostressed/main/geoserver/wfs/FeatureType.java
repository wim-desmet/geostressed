package be.gim.geostressed.main.geoserver.wfs;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class FeatureType {
    @JacksonXmlProperty(localName = "Name", namespace = TYPES.NAMESPACE_WFS)
    private String name;

    @JacksonXmlProperty(localName = "Title", namespace = TYPES.NAMESPACE_WFS)
    private String title;

    @JacksonXmlProperty(localName = "Abstract", namespace = TYPES.NAMESPACE_WFS)
    private String type_abstract;

    @JacksonXmlProperty(localName = "DefaultCRS", namespace = TYPES.NAMESPACE_WFS)
    private String defaultCRS;

    private String defaultStyle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_abstract() {
        return type_abstract;
    }

    public void setType_abstract(String type_abstract) {
        this.type_abstract = type_abstract;
    }

    public String getDefaultCRS() {
        return defaultCRS;
    }

    public void setDefaultCRS(String defaultCRS) {
        this.defaultCRS = defaultCRS;
    }


    public String getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(String defaultStyle) {
        this.defaultStyle = defaultStyle;
    }
}

