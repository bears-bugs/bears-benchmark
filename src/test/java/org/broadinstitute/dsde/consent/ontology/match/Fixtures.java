package org.broadinstitute.dsde.consent.ontology.match;

import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Created by davidan on 5/2/17.
 */
class Fixtures {
    
    static final String cancerNode = "http://purl.obolibrary.org/obo/DOID_162";
    static final List<String> cancerNodeList = Collections.singletonList(cancerNode);

    static final String bipolarNode = "http://purl.obolibrary.org/obo/DOID_0060166";
    static final List<String> bipolarNodeList = Collections.singletonList(bipolarNode);

    // Health/medical/biomedical research
    static final class HMB {
       static final DataUse uc1 = new DataUseBuilder().setHmbResearch(true).build();
       static final DataUse uc2 = new DataUseBuilder().setHmbResearch(true).setMethodsResearch(false).build();
       static final DataUse uc3 = new DataUseBuilder().setGeneralUse(true).setMethodsResearch(false).build();
       static final DataUse uc4 = new DataUseBuilder().setHmbResearch(true).setMethodsResearch(true).build();
    }

    // Methods research prohibited
    static final class MRP {
        static final DataUse uc1 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).build();
        static final DataUse uc2 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setMethodsResearch(false).build();
        static final DataUse uc3 = new DataUseBuilder().setGeneralUse(true).setMethodsResearch(false).build();
        static final DataUse uc4 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setMethodsResearch(true).build();

        static final DataUse mrpa = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setMethodsResearch(true).build();
        static final DataUse mrpb = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).build();
        static final DataUse mrpc = new DataUseBuilder().setMethodsResearch(true).build();
    }

    // Control set usage
    static final class CS {
        static final DataUse uc1 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).build();
        static final DataUse uc2 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setControlSetOption("No").build();
        static final DataUse uc3 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setControlSetOption("Yes").build();
        static final DataUse uc4 = new DataUseBuilder().setDiseaseRestrictions(bipolarNodeList).build();

        static final DataUse csa = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setControlSetOption("Yes").build();
        static final DataUse csb = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).build();
        static final DataUse csc = new DataUseBuilder().setControlSetOption("Yes").build();
        static final DataUse csd = new DataUseBuilder().setDiseaseRestrictions(bipolarNodeList).setControlSetOption("Yes").build();
    }

}
