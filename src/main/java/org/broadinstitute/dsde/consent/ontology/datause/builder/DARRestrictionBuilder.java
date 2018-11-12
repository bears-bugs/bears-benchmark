package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.apache.commons.collections4.CollectionUtils;
import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

import java.util.ArrayList;
import java.util.List;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.*;

/**
 * Apply data-access-request-specific business rules when generating use restrictions
 */
public class DARRestrictionBuilder implements UseRestrictionBuilder {
    
    public UseRestriction buildUseRestriction(DataUse dataUse) {

        if (isPresent(dataUse.getGeneralUse()) && dataUse.getGeneralUse()) {
            return new Everything();
        }

        List<UseRestriction> operandList = new ArrayList<>();

        //
        //    Research related entries
        //
        List<UseRestriction> methodsList = new ArrayList<>();
        if (isPresent(dataUse.getMethodsResearch()) && dataUse.getMethodsResearch()) {
            methodsList.add(new Named(METHODS_RESEARCH));
        } else {
            methodsList.add(new Not(new Named(METHODS_RESEARCH)));
        }
        if (getOrElseFalse(dataUse.getPopulationStructure())) {
            methodsList.add(new Named(POPULATION_STRUCTURE));
        } else {
            methodsList.add(new Not(new Named(POPULATION_STRUCTURE)));
        }
        if (isPresent(dataUse.getControlSetOption()) && dataUse.getControlSetOption().equalsIgnoreCase("Yes")) {
            methodsList.add(new Named(CONTROL));
        } else {
            methodsList.add(new Not(new Named(CONTROL)));
        }
        
        if (CollectionUtils.isNotEmpty(methodsList)) {
            operandList.add(buildAndRestriction(methodsList));
        }

        //
        //    Diseases related entries
        //
        if (!dataUse.getDiseaseRestrictions().isEmpty()) {
            operandList.add(buildORRestrictionFromClasses(dataUse.getDiseaseRestrictions()));
        }

        //
        //    gender, age and commercial status entries
        //
        List<UseRestriction> purposesList = new ArrayList<>();
        if (isPresent(dataUse.getGender())) {
            if (dataUse.getGender().equalsIgnoreCase("male")) {
                purposesList.add(new Named(MALE));
            } else if (dataUse.getGender().equalsIgnoreCase("female")) {
                purposesList.add(new Named(FEMALE));
            }
        }

        if (getOrElseFalse(dataUse.getPediatric())) {
            purposesList.add(new Named(PEDIATRIC));
        }

        if (getOrElseFalse(dataUse.getCommercialUse())) {
            purposesList.add(new Not(new Named(NON_PROFIT)));
        } else {
            purposesList.add(new Named(NON_PROFIT));
        }

        //
        //    Compose all restrictions into an And one ...
        //
        if (CollectionUtils.isNotEmpty(purposesList)) {
            operandList.add(buildAndRestriction(purposesList));
        }

        return new And(operandList.toArray(new UseRestriction[operandList.size()]));
    }

}
