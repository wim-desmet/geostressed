package be.gim.geostressed.main.jmeter;

import java.util.List;

public class TemplateConfiguration {
    private List<Layer> wfs;
    private List<Layer> wms;
    private List<Layer> wmts;
    private Integer users;
    private Integer loops;
    private Url url;

    public List<Layer> getWfs() {
        return wfs;
    }

    public void setWfs(List<Layer> wfs) {
        this.wfs = wfs;
    }

    public List<Layer> getWms() {
        return wms;
    }

    public void setWms(List<Layer> wms) {
        this.wms = wms;
    }

    public List<Layer> getWmts() {
        return wmts;
    }

    public void setWmts(List<Layer> wmts) {
        this.wmts = wmts;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
