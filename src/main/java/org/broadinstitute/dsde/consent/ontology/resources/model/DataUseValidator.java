package org.broadinstitute.dsde.consent.ontology.resources.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Validator is used to make fine-grained checks on different combinations of Data Use questions.
 */
public class DataUseValidator {

    private DataUse dataUse = null;
    private List<String> validationErrors = null;

    public DataUseValidator(DataUse dataUse) {
        this.dataUse = dataUse;
        this.validationErrors = new ArrayList<>();
        validate();
    }

    public Boolean getIsValid() {
        return validationErrors.isEmpty();
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    /**
     * Internal validation method called when validator is instantiated.
     */
    private void validate() {
        if (dataUse == null) {
            validationErrors.add("Data Use cannot be null");
        } else {
            if (!validateGeneralUse(dataUse)) {
                validationErrors.add("General Use cannot have other restrictions");
            }
            if (!validateRecontact(dataUse)) {
                validationErrors.add("Recontacting Data Subjects requires conditions");
            }
            if (!validateOther(dataUse)) {
                validationErrors.add("'Other' restrictions require an explanation");
            }
        }
    }

    private Boolean getOrElseFalse(Boolean nullable) {
        return Optional.ofNullable(nullable).orElse(false);
    }

    private Boolean getOrElseFalse(String nullable) {
        List<String> falsyValues = Arrays.asList("Unspecified", "Unknown", "No");
        return Optional.ofNullable(nullable).isPresent()
            && !nullable.isEmpty()
            && falsyValues.stream().noneMatch(nullable::equalsIgnoreCase);
    }

    /**
     * Other use is enforced in json-schema. Here for an OO approach.
     * If other is specified, we need one of the sub-conditions to be present as well.
     *
     * @param dataUse The data use to check
     * @return Boolean: valid or not
     */
    @SuppressWarnings("SimplifiableIfStatement")
    private Boolean validateOther(DataUse dataUse) {
        if (getOrElseFalse(dataUse.getOtherRestrictions())) {
            return
                getOrElseFalse(dataUse.getCloudStorage()) ||
                getOrElseFalse(dataUse.getEthicsApprovalRequired()) ||
                getOrElseFalse(dataUse.getGeographicalRestrictions()) ||
                getOrElseFalse(dataUse.getOther());
        }
        return true;
    }

    /**
     * Recontact use is enforced in json-schema. Here for an OO approach.
     * If recontacting is allowed, we need to have some condition for that.
     *
     * @param dataUse The data use to check
     * @return Boolean: valid or not
     */
    private Boolean validateRecontact(DataUse dataUse) {
        if (getOrElseFalse(dataUse.getRecontactingDataSubjects())) {
            boolean mayExists = getOrElseFalse(dataUse.getRecontactMay());
            boolean mustExists = getOrElseFalse(dataUse.getRecontactMust());
            return (mayExists || mustExists);
        }
        return true;
    }

    /**
     * Check general use.
     *
     * @param dataUse The data use to check
     * @return Boolean: valid or not
     */
    private Boolean validateGeneralUse(DataUse dataUse) {
        if (getOrElseFalse(dataUse.getGeneralUse())) {
            if (
                getOrElseFalse(dataUse.getHmbResearch()) ||
                !dataUse.getDiseaseRestrictions().isEmpty() ||
                getOrElseFalse(dataUse.getPopulationOriginsAncestry()) ||
                getOrElseFalse(dataUse.getCommercialUse()) ||
                getOrElseFalse(dataUse.getMethodsResearch()) ||
                getOrElseFalse(dataUse.getAggregateResearch()) ||
                getOrElseFalse(dataUse.getControlSetOption()) ||
                getOrElseFalse(dataUse.getGender()) ||
                getOrElseFalse(dataUse.getPediatric()) ||
                !dataUse.getPopulationRestrictions().isEmpty() ||
                getOrElseFalse(dataUse.getDateRestriction()) ||
                getOrElseFalse(dataUse.getOtherRestrictions()) ||
                getOrElseFalse(dataUse.getOther()) ||
                getOrElseFalse(dataUse.getCloudStorage()) ||
                getOrElseFalse(dataUse.getEthicsApprovalRequired()) ||
                getOrElseFalse(dataUse.getGeographicalRestrictions()) ||
                getOrElseFalse(dataUse.getIllegalBehavior()) ||
                getOrElseFalse(dataUse.getAddiction()) ||
                getOrElseFalse(dataUse.getStigmatizeDiseases()) ||
                getOrElseFalse(dataUse.getPsychologicalTraits()) ||
                getOrElseFalse(dataUse.getNonBiomedical())
                ) {
                return false;
            }
        }
        return true;
    }


}
