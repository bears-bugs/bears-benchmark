package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;

/**
 * See TruthTableTests for the source of many of these tests. In most cases, the corresponding TT test will need to
 * be rewritten to accommodate future business rule development. New Consent test cases should be defined here and
 * current ones in TT should be removed.
 *
 * Note that the current differences involve aggregate research which is not currently being used in restrictions.
 */
public class ConsentUseCases {

    public static final String CANCER = "http://purl.obolibrary.org/obo/DOID_162";

    // Combined example from OD-329
    public static UseRestriction MRPdulUC1 = new Or(
        new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
        new Named(CANCER)
    );

    // Combined example from OD-330
    public static UseRestriction MRPdulUC2 =
        new Or(
            new And(
                new Named(CANCER),
                new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH))
            ),
            new Named(CANCER)
        );

    // Combined example from OD-331
    public static UseRestriction MRPdulUC3 = new Or(
        new And(
            new Everything(),
            new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH))
        ),
        new Everything()
    );

    // Combined example from OD-332
    public static UseRestriction MRPdulUC4 = new Or(
        new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
        new Named(CANCER)
    );


    // Combined example from OD-329
    public static UseRestriction CSdulUC1 = new Or(
        new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
        new Named(CANCER)
    );

    // Combined example from OD-335
    public static UseRestriction CSdulUC2 = new Or(
        new Or(
            new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
            new Named(CANCER)
        ),
        new And(
            new Or(
                new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
                new Named(CANCER)
            ),
            new Named(UseRestrictionBuilderSupport.CONTROL)
        )
    );

    // Combined example from OD-336
    public static UseRestriction CSdulUC3 = new Or(
        new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
        new Named(CANCER)
    );
    
}
