package org.activiti.cloud.app.model;

public class ActivitiBusinessAsset {
    private String id;
    private String name;
    private String version;
    private String path;

    public ActivitiBusinessAsset() {
    }

    public ActivitiBusinessAsset(String id,
                                 String name,
                                 String version,
                                 String path) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getPath() {
        return path;
    }
}
