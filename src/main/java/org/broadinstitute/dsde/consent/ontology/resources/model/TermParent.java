package org.broadinstitute.dsde.consent.ontology.resources.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TermParent {
    public String id;
    public Integer order;
    public String label;
    public String definition;
    public List<String> synonyms;

    public TermParent() {
    }

    public String getId() {
        return this.id;
    }

    public Integer getOrder() {
        return order;
    }

    public String getLabel() {
        return label;
    }

    public String getDefinition() {
        return definition;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

}
