package org.broadinstitute.dsde.consent.ontology.resources.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataUseValidatorTest {

    @Test
    public void testNull() {
        DataUseValidator validator = new DataUseValidator(null);
        assertFalse(validator.getIsValid());
    }

    @Test
    public void testGeneralUse() {

        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        DataUseValidator validator = new DataUseValidator(dataUse);
        assertTrue(validator.getIsValid());

        dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        dataUse.getDiseaseRestrictions().add("restriction");
        validator = new DataUseValidator(dataUse);
        assertFalse(validator.getIsValid());
    }

    @Test
    public void testRecontact() {
        DataUse dataUse = new DataUse();
        dataUse.setRecontactingDataSubjects(true);
        dataUse.setRecontactMay("weekends");
        DataUseValidator validator = new DataUseValidator(dataUse);
        assertTrue(validator.getIsValid());

        dataUse = new DataUse();
        dataUse.setRecontactingDataSubjects(true);
        dataUse.setRecontactMust("weekends");
        validator = new DataUseValidator(dataUse);
        assertTrue(validator.getIsValid());

        dataUse = new DataUse();
        dataUse.setRecontactingDataSubjects(true);
        validator = new DataUseValidator(dataUse);
        assertFalse(validator.getIsValid());
    }

    @Test
    public void testOther() {
        DataUse dataUse = new DataUse();
        dataUse.setOtherRestrictions(true);
        dataUse.setOther("weekends");
        DataUseValidator validator = new DataUseValidator(dataUse);
        assertTrue(validator.getIsValid());

        dataUse = new DataUse();
        dataUse.setOtherRestrictions(true);
        dataUse.setCloudStorage("Yes");
        validator = new DataUseValidator(dataUse);
        assertTrue(validator.getIsValid());

        dataUse = new DataUse();
        dataUse.setOtherRestrictions(true);
        dataUse.setEthicsApprovalRequired(true);
        validator = new DataUseValidator(dataUse);
        assertTrue(validator.getIsValid());

        dataUse = new DataUse();
        dataUse.setOtherRestrictions(true);
        dataUse.setGeographicalRestrictions("US");
        validator = new DataUseValidator(dataUse);
        assertTrue(validator.getIsValid());

        dataUse = new DataUse();
        dataUse.setOtherRestrictions(true);
        validator = new DataUseValidator(dataUse);
        assertFalse(validator.getIsValid());
    }

}
