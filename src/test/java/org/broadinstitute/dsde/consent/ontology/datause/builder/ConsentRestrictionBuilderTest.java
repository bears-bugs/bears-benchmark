package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class ConsentRestrictionBuilderTest {

    private UseRestrictionBuilder restrictionBuilder = new ConsentRestrictionBuilder();
    private UseRestriction everything = new Everything();

    @Test
    public void testGeneralUse() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, everything);
    }

    /**
     * All of the general use mixed cases are to ensure that GRU is ignored when
     * sub-conditions exist that would render it as a non-GRU use restriction
     */
    @Test
    public void testGeneralUseMixedCase1() {
        DataUse dataUse = new DataUseBuilder().
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                build();
        DataUse mc1 = new DataUseBuilder().
                setGeneralUse(true).
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc1));
    }

    @Test
    public void testGeneralUseMixedCase2() {
        DataUse dataUse = new DataUseBuilder().
                setPopulationRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                build();
        DataUse mc2 = new DataUseBuilder().
                setGeneralUse(true).
                setPopulationRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc2));
    }

    @Test
    public void testGeneralUseMixedCase3() {
        DataUse dataUse = new DataUseBuilder().
                setCommercialUse(true).
                build();
        DataUse mc3 = new DataUseBuilder().
                setGeneralUse(true).
                setCommercialUse(true).
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc3));
    }

    @Test
    public void testGeneralUseMixedCase4() {
        DataUse dataUse = new DataUseBuilder().
                setGender("Male").
                build();
        DataUse mc4 = new DataUseBuilder().
                setGeneralUse(true).
                setGender("Male").
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc4));
    }

    @Test
    public void testGeneralUseMixedCase5() {
        DataUse dataUse = new DataUseBuilder().
                setPediatric(true).
                build();
        DataUse mc5 = new DataUseBuilder().
                setGeneralUse(true).
                setPediatric(true).
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc5));
    }

    @Test
    public void testGeneralUseMixedCase6a() {
        DataUse dataUse = new DataUseBuilder().
                setMethodsResearch(true).
                build();
        DataUse mc6a = new DataUseBuilder().
                setGeneralUse(true).
                setMethodsResearch(true).
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc6a));
    }

    @Test
    public void testGeneralUseMixedCase6b() {
        DataUse dataUse = new DataUseBuilder().
                setMethodsResearch(false).
                build();
        DataUse mc6b = new DataUseBuilder().
                setGeneralUse(true).
                setMethodsResearch(false).
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc6b));
    }

    @Test
    public void testGeneralUseMixedCase7a() {
        DataUse dataUse = new DataUseBuilder().
                setControlSetOption("Yes").
                build();
        DataUse mc7a = new DataUseBuilder().
                setGeneralUse(true).
                setControlSetOption("Yes").
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc7a));
    }

    @Test
    public void testGeneralUseMixedCase7b() {
        DataUse dataUse = new DataUseBuilder().
                setControlSetOption("No").
                build();
        DataUse mc7b = new DataUseBuilder().
                setGeneralUse(true).
                setControlSetOption("No").
                build();
        Assert.assertEquals(
                restrictionBuilder.buildUseRestriction(dataUse),
                restrictionBuilder.buildUseRestriction(mc7b));
    }

    @Test
    public void testMRdulUC1() {
        DataUse dataUse = new DataUseBuilder().
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                build();
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC1);
    }

    @Test
    public void testMRdulUC2() {
        DataUse dataUse = new DataUseBuilder().
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                setMethodsResearch(false).
                build();
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC2);
    }

    @Test
    public void testMRdulUC3() {
        DataUse dataUse = new DataUseBuilder().
                setMethodsResearch(false).
                build();
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC3);
    }

    @Test
    public void testMRdulUC4() {
        DataUse dataUse = new DataUseBuilder().
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                setMethodsResearch(true).
                build();
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC4);
    }

    @Test
    public void testCSdulUC1() {
        DataUse dataUse = new DataUseBuilder().
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                build();
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.CSdulUC1);
    }

    @Test
    public void testCSdulUC2() {
        DataUse dataUse = new DataUseBuilder().
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                setControlSetOption("Yes").
                build();
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.CSdulUC2);
    }

    @Test
    public void testCSdulUC3() {
        DataUse dataUse = new DataUseBuilder().
                setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER)).
                setControlSetOption("No").
                build();
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.CSdulUC3);
    }

}
