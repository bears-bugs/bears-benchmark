package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class DARRestrictionBuilderTest {

    private UseRestrictionBuilder restrictionBuilder = new DARRestrictionBuilder();

    @Test
    public void testGeneralUse() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(new Everything()));
    }

    @Test
    public void testFemale() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(false);
        dataUse.setMethodsResearch(false);
        dataUse.setControlSetOption("No");
        dataUse.setPopulationStructure(false);
        dataUse.setCommercialUse(true);
        dataUse.setGender("Female");
        dataUse.setPediatric(false);
        dataUse.setIllegalBehavior(false);
        dataUse.setAddiction(false);
        dataUse.setSexualDiseases(false);
        dataUse.setStigmatizeDiseases(false);
        dataUse.setVulnerablePopulations(false);
        dataUse.setPsychologicalTraits(false);
        dataUse.setNonBiomedical(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(
            new And(
                new And(
                    new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
                    new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
                    new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
                ),
                new And(
                    new Named(UseRestrictionBuilderSupport.FEMALE),
                    new Not(new Named(UseRestrictionBuilderSupport.NON_PROFIT))
                )
            )
        ));
    }

    @Test
    public void testMale() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(false);
        dataUse.setHmbResearch(true);
        dataUse.setMethodsResearch(false);
        dataUse.setControlSetOption("No");
        dataUse.setPopulationStructure(false);
        dataUse.setCommercialUse(true);
        dataUse.setGender("Male");
        dataUse.setPediatric(false);
        dataUse.setIllegalBehavior(false);
        dataUse.setAddiction(false);
        dataUse.setSexualDiseases(false);
        dataUse.setStigmatizeDiseases(false);
        dataUse.setVulnerablePopulations(false);
        dataUse.setPsychologicalTraits(false);
        dataUse.setNonBiomedical(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(
            new And(
                new And(
                    new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
                    new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
                    new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
                ),
                new And(
                    new Named(UseRestrictionBuilderSupport.MALE),
                    new Not(new Named(UseRestrictionBuilderSupport.NON_PROFIT))
                )
            )
        ));
    }

    @Test
    public void testBoys() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(false);
        dataUse.setHmbResearch(true);
        dataUse.setMethodsResearch(false);
        dataUse.setControlSetOption("No");
        dataUse.setPopulationStructure(false);
        dataUse.setCommercialUse(true);
        dataUse.setGender("Male");
        dataUse.setPediatric(true);
        dataUse.setIllegalBehavior(false);
        dataUse.setAddiction(false);
        dataUse.setSexualDiseases(false);
        dataUse.setStigmatizeDiseases(false);
        dataUse.setVulnerablePopulations(false);
        dataUse.setPsychologicalTraits(false);
        dataUse.setNonBiomedical(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(
            new And(
                new And(
                    new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
                    new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
                    new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
                ),
                new And(
                    new Named(UseRestrictionBuilderSupport.MALE),
                    new Named(UseRestrictionBuilderSupport.PEDIATRIC),
                    new Not(new Named(UseRestrictionBuilderSupport.NON_PROFIT))
                )
            )
        ));
    }

    @Test
    public void testGirls() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(false);
        dataUse.setMethodsResearch(false);
        dataUse.setControlSetOption("No");
        dataUse.setPopulationStructure(false);
        dataUse.setCommercialUse(true);
        dataUse.setGender("Female");
        dataUse.setPediatric(true);
        dataUse.setIllegalBehavior(false);
        dataUse.setAddiction(false);
        dataUse.setSexualDiseases(false);
        dataUse.setStigmatizeDiseases(false);
        dataUse.setVulnerablePopulations(false);
        dataUse.setPsychologicalTraits(false);
        dataUse.setNonBiomedical(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(
            new And(
                new And(
                    new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
                    new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
                    new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
                ),
                new And(
                    new Named(UseRestrictionBuilderSupport.FEMALE),
                    new Named(UseRestrictionBuilderSupport.PEDIATRIC),
                    new Not(new Named(UseRestrictionBuilderSupport.NON_PROFIT))
                )
            )
        ));
    }

    @Test
    public void testChildren() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(false);
        dataUse.setHmbResearch(true);
        dataUse.setMethodsResearch(false);
        dataUse.setControlSetOption("No");
        dataUse.setPopulationStructure(false);
        dataUse.setCommercialUse(true);
        dataUse.setPediatric(true);
        dataUse.setIllegalBehavior(false);
        dataUse.setAddiction(false);
        dataUse.setSexualDiseases(false);
        dataUse.setStigmatizeDiseases(false);
        dataUse.setVulnerablePopulations(false);
        dataUse.setPsychologicalTraits(false);
        dataUse.setNonBiomedical(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(
            new And(
                new And(
                    new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
                    new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
                    new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
                ),
                new And(
                    new Named(UseRestrictionBuilderSupport.PEDIATRIC),
                    new Not(new Named(UseRestrictionBuilderSupport.NON_PROFIT))
                )
            )
        ));
    }

    @Test
    public void testAllData() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(false);
        dataUse.setHmbResearch(true);
        dataUse.setMethodsResearch(true);
        dataUse.setControlSetOption("Yes");
        dataUse.setPopulationStructure(true);
        dataUse.setCommercialUse(true);
        dataUse.setPediatric(false);
        dataUse.setIllegalBehavior(false);
        dataUse.setAddiction(false);
        dataUse.setSexualDiseases(false);
        dataUse.setStigmatizeDiseases(false);
        dataUse.setVulnerablePopulations(false);
        dataUse.setPsychologicalTraits(false);
        dataUse.setNonBiomedical(false);
        dataUse.getDiseaseRestrictions().add("http://purl.obolibrary.org/obo/DOID_4023");
        dataUse.getDiseaseRestrictions().add("http://purl.obolibrary.org/obo/DOID_9854");
        dataUse.getDiseaseRestrictions().add("http://purl.obolibrary.org/obo/DOID_0050738");
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(
            new And(
                new And(
                    new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
                    new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE),
                    new Named(UseRestrictionBuilderSupport.CONTROL)
                ),
                new Or(
                    new Named("http://purl.obolibrary.org/obo/DOID_4023"),
                    new Named("http://purl.obolibrary.org/obo/DOID_9854"),
                    new Named("http://purl.obolibrary.org/obo/DOID_0050738")

                ),
                new Not(new Named(UseRestrictionBuilderSupport.NON_PROFIT)))
            )
        );
    }

    @Test
    public void testControlsAndPopulation() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(false);
        dataUse.setHmbResearch(false);
        dataUse.setMethodsResearch(false);
        dataUse.setControlSetOption("Yes");
        dataUse.setPopulationStructure(true);
        dataUse.setCommercialUse(false);
        dataUse.setPediatric(false);
        dataUse.setIllegalBehavior(false);
        dataUse.setAddiction(false);
        dataUse.setSexualDiseases(false);
        dataUse.setStigmatizeDiseases(false);
        dataUse.setVulnerablePopulations(false);
        dataUse.setPsychologicalTraits(false);
        dataUse.setNonBiomedical(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(
            new And(
                new And(
                    new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
                    new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE),
                    new Named(UseRestrictionBuilderSupport.CONTROL)
                ),
                new Named(UseRestrictionBuilderSupport.NON_PROFIT))
            )
        );
    }


    @Test
    public void testMRPA() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setMethodsResearch(true);
        dataUse.setCommercialUse(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(DARUseCases.darDefaultMRPA));
    }

    @Test
    public void testMRPB() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setCommercialUse(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(DARUseCases.darDefaultMRPB));
    }

    @Test
    public void testMRPC() {
        DataUse dataUse = new DataUse();
        dataUse.setMethodsResearch(true);
        dataUse.setCommercialUse(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(DARUseCases.darDefaultMRPC));
    }

    @Test
    public void testCSA() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setControlSetOption("Yes");
        dataUse.setCommercialUse(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(DARUseCases.darDefaultCSA));
    }

    @Test
    public void testCSB() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setCommercialUse(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(DARUseCases.darDefaultCSB));
    }

    @Test
    public void testCSC() {
        DataUse dataUse = new DataUse();
        dataUse.setControlSetOption("Yes");
        dataUse.setCommercialUse(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(DARUseCases.darDefaultCSC));
    }

}
