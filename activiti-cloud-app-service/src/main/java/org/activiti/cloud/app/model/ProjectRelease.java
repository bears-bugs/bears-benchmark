package org.activiti.cloud.app.model;

import java.util.Date;
import java.util.List;

public class ProjectRelease {

    private String id;
    private String name;
    private String version;
    private Date releaseDate;
    private List<ActivitiBusinessAsset> activitiBusinessAssets;

    public ProjectRelease() {
    }

    public ProjectRelease(String id,
                          String name,
                          String version,
                          Date releaseDate,
                          List<ActivitiBusinessAsset> activitiBusinessAssets) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.releaseDate = releaseDate;
        this.activitiBusinessAssets = activitiBusinessAssets;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public List<ActivitiBusinessAsset> getActivitiBusinessAssets() {
        return activitiBusinessAssets;
    }
}
