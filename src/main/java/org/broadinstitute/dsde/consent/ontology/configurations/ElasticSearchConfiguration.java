package org.broadinstitute.dsde.consent.ontology.configurations;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ElasticSearchConfiguration {

    @NotNull
    public List<String> servers;

    @NotNull
    public String index;

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
