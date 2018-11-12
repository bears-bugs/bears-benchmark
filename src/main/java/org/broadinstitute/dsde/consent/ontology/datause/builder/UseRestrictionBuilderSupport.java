package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.apache.commons.collections4.CollectionUtils;
import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.Or;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UseRestrictionBuilderSupport {

    public static final String PEDIATRIC = "http://www.broadinstitute.org/ontologies/DUOS/pediatric";
    public static final String FEMALE = "http://www.broadinstitute.org/ontologies/DUOS/female";
    public static final String MALE = "http://www.broadinstitute.org/ontologies/DUOS/male";
    public static final String GIRLS = "http://www.broadinstitute.org/ontologies/DUOS/girls";
    public static final String BOYS = "http://www.broadinstitute.org/ontologies/DUOS/boys";


    /* Non-profit is subclass of DUO Data Use Requirements */
    public static final String DUO_DATA_USE_REQUIREMENTS = "http://purl.obolibrary.org/obo/DUO_0000017";
    public static final String NON_PROFIT = "http://purl.obolibrary.org/obo/DUO_0000018";

    /* Control are subclasses of Dataset Usage */
    public static final String DATASET_USAGE = "http://www.broadinstitute.org/ontologies/DUOS/dataset_usage";
    public static final String CONTROL = "http://www.broadinstitute.org/ontologies/DUOS/control";

    /* Population Structure is a subclass of DUO Primary Category*/
    public static final String DUO_PRIMARY_CATEGORY = "http://purl.obolibrary.org/obo/DUO_0000002";
    public static final String POPULATION_STRUCTURE = "http://purl.obolibrary.org/obo/DUO_0000011";

    /* Aggregate Research is a subclass of Research Type */
    public static final String RESEARCH_TYPE = "http://www.broadinstitute.org/ontologies/DUOS/research_type";

    /* Methods Research is a subclass of DUO Secondary Category */
    public static final String DUO_SECONDARY_CATEGORY = "http://purl.obolibrary.org/obo/DUO_0000003";
    public static final String METHODS_RESEARCH = "http://purl.obolibrary.org/obo/DUO_0000015";
    /*
     * Note that aggregate research is not used to build a DUR, but there are existing consents that were created
     * with it so we still need to test for it.
     */
    public static final String AGGREGATE_RESEARCH = "http://www.broadinstitute.org/ontologies/DUOS/aggregate_research";

    /**
     * Convenience method for dealing with the many nullable fields of DataUseSchema
     *
     * @param nullableThing Nullable Object
     * @return present or not
     */
    public static Boolean isPresent(Object nullableThing) {
        return Optional.ofNullable(nullableThing).isPresent();
    }

    /**
     * Convenience method for dealing with the many nullable fields of DataUseSchema
     *
     * @param nullableBoolean Nullable Boolean object
     * @return Boolean or false
     */
    public static Boolean getOrElseFalse(Boolean nullableBoolean) {
        return Optional.ofNullable(nullableBoolean).orElse(false);
    }

    /**
     * Convenience method for dealing with the many nullable fields of DataUseSchema
     *
     * @param nullableBoolean Nullable Boolean object
     * @return Boolean or true
     */
    public static Boolean getOrElseTrue(Boolean nullableBoolean) {
        return Optional.ofNullable(nullableBoolean).orElse(true);
    }

    /**
     * Utility method to build up an OR restriction from a list of strings.
     *
     * @param ontologyClasses List of ontology terms
     * @return UseRestriction
     */
    public static UseRestriction buildORRestrictionFromClasses(List<String> ontologyClasses) throws IllegalArgumentException {
        List<String> validClasses = ontologyClasses.stream().
            filter(s -> !s.isEmpty()).
            collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(validClasses)) {
            if (validClasses.size() == 1) {
                return new Named(validClasses.get(0));
            } else {
                Named[] named = validClasses.stream().map(Named::new).
                    toArray(size -> new Named[validClasses.size()]);
                return new Or(named);
            }
        }
        throw new IllegalArgumentException("Ontology Classes cannot be empty");
    }

    /**
     * Utility method to build up an AND restriction from a collection of UseRestrictions.
     *
     * @param useRestrictions List of UseRestrictions
     * @return And UseRestriction
     */
    static UseRestriction buildAndRestriction(List<UseRestriction> useRestrictions) throws IllegalArgumentException {
        if (CollectionUtils.isNotEmpty(useRestrictions)) {
            if (useRestrictions.size() == 1) {
                return useRestrictions.get(0);
            } else {
                UseRestriction[] ands = new UseRestriction[useRestrictions.size()];
                ands = useRestrictions.toArray(ands);
                return new And(ands);
            }
        }
        throw new IllegalArgumentException("Use Restrictions cannot be empty");
    }

}
