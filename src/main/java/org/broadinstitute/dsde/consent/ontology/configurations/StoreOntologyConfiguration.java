package org.broadinstitute.dsde.consent.ontology.configurations;

import javax.validation.constraints.NotNull;

/**
 * Created by SantiagoSaucedo on 4/20/2016.
 */

public class StoreOntologyConfiguration {

    @NotNull
    public String bucketSubdirectory;

    @NotNull
    public String configurationFileName;

    public String getBucketSubdirectory() {
        return bucketSubdirectory;
    }

    public void setBucketSubdirectory(String bucketSubdirectory) {
        this.bucketSubdirectory = bucketSubdirectory;
    }

    public String getConfigurationFileName() {
        return configurationFileName;
    }

    public void setConfigurationFileName(String configurationFileName) {
        this.configurationFileName = configurationFileName;
    }
}