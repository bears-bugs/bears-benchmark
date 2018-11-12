package org.broadinstitute.dsde.consent.ontology.resources.model;

import java.util.List;

/**
 * Syntactic sugar for creating a DataUse object.
 */
public class DataUseBuilder {
    private DataUse du;
    
    public DataUseBuilder() {
        du = new DataUse();
    }
    
    public DataUse build() {
        return du;
    }
    
    
    public DataUseBuilder setGeneralUse(Boolean generalUse) {
        du.setGeneralUse(generalUse);
        return this;
    }
    
    public DataUseBuilder setHmbResearch(Boolean hmbResearch) {
        du.setHmbResearch(hmbResearch);
        return this;
    }
    
    public DataUseBuilder setDiseaseRestrictions(List<String> diseaseRestrictions) {
        du.setDiseaseRestrictions(diseaseRestrictions);
        return this;
    }
    
    public DataUseBuilder setPopulationOriginsAncestry(Boolean populationOriginsAncestry) {
        du.setPopulationOriginsAncestry(populationOriginsAncestry);
        return this;
    }
    
    public DataUseBuilder setPopulationStructure(Boolean populationStructure) {
        du.setPopulationStructure(populationStructure);
        return this;
    }
    
    public DataUseBuilder setCommercialUse(Boolean commercialUse) {
        du.setCommercialUse(commercialUse);
        return this;
    }
    
    public DataUseBuilder setMethodsResearch(Boolean methodsResearch) {
        du.setMethodsResearch(methodsResearch);
        return this;
    }
    
    public DataUseBuilder setAggregateResearch(String aggregateResearch) {
        du.setAggregateResearch(aggregateResearch);
        return this;
    }
    
    public DataUseBuilder setControlSetOption(String controlSetOption) {
        du.setControlSetOption(controlSetOption);
        return this;
    }
    
    public DataUseBuilder setGender(String gender) {
        du.setGender(gender);
        return this;
    }
    
    public DataUseBuilder setPediatric(Boolean pediatric) {
        du.setPediatric(pediatric);
        return this;
    }
    
    public DataUseBuilder setPopulationRestrictions(List<String> populationRestrictions) {
        du.setPopulationRestrictions(populationRestrictions);
        return this;
    }
    
    public DataUseBuilder setDateRestriction(String dateRestriction) {
        du.setDateRestriction(dateRestriction);
        return this;
    }
    
    public DataUseBuilder setRecontactingDataSubjects(Boolean recontactingDataSubjects) {
        du.setRecontactingDataSubjects(recontactingDataSubjects);
        return this;
    }
    
    public DataUseBuilder setRecontactMay(String recontactMay) {
        du.setRecontactMay(recontactMay);
        return this;
    }
    
    public DataUseBuilder setRecontactMust(String recontactMust) {
        du.setRecontactMust(recontactMust);
        return this;
    }
    
    public DataUseBuilder setGenomicPhenotypicData(String genomicPhenotypicData) {
        du.setGenomicPhenotypicData(genomicPhenotypicData);
        return this;
    }
    
    public DataUseBuilder setOtherRestrictions(Boolean otherRestrictions) {
        du.setOtherRestrictions(otherRestrictions);
        return this;
    }
    
    public DataUseBuilder setCloudStorage(String cloudStorage) {
        du.setCloudStorage(cloudStorage);
        return this;
    }
    
    public DataUseBuilder setEthicsApprovalRequired(Boolean ethicsApprovalRequired) {
        du.setEthicsApprovalRequired(ethicsApprovalRequired);
        return this;
    }
    
    public DataUseBuilder setGeographicalRestrictions(String geographicalRestrictions) {
        du.setGeographicalRestrictions(geographicalRestrictions);
        return this;
    }
    
    public DataUseBuilder setOther(String other) {
        du.setOther(other);
        return this;
    }
    
    public DataUseBuilder setIllegalBehavior(Boolean illegalBehavior) {
        du.setIllegalBehavior(illegalBehavior);
        return this;
    }
    
    public DataUseBuilder setAddiction(Boolean addiction) {
        du.setAddiction(addiction);
        return this;
    }
    
    public DataUseBuilder setSexualDiseases(Boolean sexualDiseases) {
        du.setSexualDiseases(sexualDiseases);
        return this;
    }
    
    public DataUseBuilder setStigmatizeDiseases(Boolean stigmatizeDiseases) {
        du.setStigmatizeDiseases(stigmatizeDiseases);
        return this;
    }
    
    public DataUseBuilder setVulnerablePopulations(Boolean vulnerablePopulations) {
        du.setVulnerablePopulations(vulnerablePopulations);
        return this;
    }
    
    public DataUseBuilder setPsychologicalTraits(Boolean psychologicalTraits) {
        du.setPsychologicalTraits(psychologicalTraits);
        return this;
    }
    
    public DataUseBuilder setNonBiomedical(Boolean nonBiomedical) {
        du.setNonBiomedical(nonBiomedical);
        return this;
    }
}
