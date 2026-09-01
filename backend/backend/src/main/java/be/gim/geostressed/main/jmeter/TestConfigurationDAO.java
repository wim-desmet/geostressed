package be.gim.geostressed.main.jmeter;

import java.util.List;

public class TestConfigurationDAO {
    private List<Layer> layers;
    private Integer users;
    private Integer loops;
    private Url url;

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getLoops() {
        return loops;
    }

    public void setLoops(Integer loops) {
        this.loops = loops;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public static class Url {

        private String protocol;
        private String host;
        private String path;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class Layer {
        private String name;
        private Boolean wfs;
        private Boolean wms;
        private Boolean wmts;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getWfs() {
            return wfs;
        }

        public void setWfs(Boolean wfs) {
            this.wfs = wfs;
        }

        public Boolean getWms() {
            return wms;
        }

        public void setWms(Boolean wms) {
            this.wms = wms;
        }

        public Boolean getWmts() {
            return wmts;
        }

        public void setWmts(Boolean wmts) {
            this.wmts = wmts;
        }
    }
}
