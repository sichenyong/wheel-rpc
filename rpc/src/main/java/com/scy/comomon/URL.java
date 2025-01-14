package com.scy.comomon;

public class URL {
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
}
