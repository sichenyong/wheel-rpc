package com.scy.comomon;

import java.io.Serializable;
import java.util.Objects;

public class URL implements Serializable {
    private String Host;
    private Integer port;

    public URL(String host, Integer port) {
        Host = host;
        this.port = port;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return Objects.equals(Host, url.Host) && Objects.equals(port, url.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Host, port);
    }
}
