package org.broadinstitute.dsde.consent.ontology.datause.models;

public class OntologyTerm {
    private String id;
    private String comment;
    private String label;
    private String definition;
    private String[] synonyms;

    public OntologyTerm() {
    }

    public OntologyTerm(String id, String comment, String label, String definition, String[] synonyms) {
        this.id = id;
        this.comment = comment;
        this.label = label;
        this.definition = definition;
        this.synonyms = synonyms;
    }

    public String getId() { return id; }
    public String getComment() { return comment; }
    public String getLabel() { return label; }
    public String getDefinition() { return definition; }
    public String[] getSynonyms() { return synonyms; }

    public void setId(String id) { this.id = id; }
    public void setComment(String comment) { this.comment = comment; }
    public void setLabel(String label) { this.label = label; }
    public void setDefinition(String definition) { this.definition = definition; }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String toString() {
        String synonym = null;
        for (String s: synonyms) {
            if (synonym == null) {
                synonym = s;
            } else {
                synonym += "," + s;
            }
        }
        return "OntologyTerm{" +
                "id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                ", label='" + label + '\'' +
                ", definition='" + definition + '\'' +
                ", synonyms=" + synonym +
                '}';
    }
}
