package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.actor.OntModelCache;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.*;

public class TextTranslationServiceImpl implements TextTranslationService {

    private OntologyTermSearchAPI api;
    private static final Logger logger = LoggerFactory.getLogger(TextTranslationServiceImpl.class);

    // This is a cache, used to associate a type (element of
    // the set { "disease", "organization", "commercial-status" }) to each named class.
    // It's an expensive operation (we're going to have to use a reasoner!) so we want
    // to cache it here in this class and not use it again if we don't need to.
    private Map<String, String> namedClassTypes;
    private StoreOntologyService storeOntologyService;

    @Inject
    public TextTranslationServiceImpl(StoreOntologyService storeOntologyService) {
        this.namedClassTypes = new ConcurrentHashMap<>();
        this.storeOntologyService = storeOntologyService;
    }

    @Override
    public String translateSample(String restrictionStr) throws IOException {
        return translate("sampleset", restrictionStr);
    }

    @Override
    public String translatePurpose(String restrictionStr) throws IOException {
        return translate("purpose", restrictionStr);
    }

    private String translate(String translateFor, String restrictionStr) throws IOException {
        UseRestriction restriction = UseRestriction.parse(restrictionStr);
        return translate(translateFor, restriction);
    }

    private String translate(String translateFor, UseRestriction restriction) {

        ArrayList<String> clauses = new ArrayList<>();

        boolean forSampleSet = translateFor.equals("sampleset");

        String disease = buildDiseaseClause(forSampleSet, restriction);
        if (disease != null) {
            clauses.add(disease);
        }

        String geography = buildGeographyClause(forSampleSet, restriction);
        if (geography != null) {
            clauses.add(geography);
        }

        String population = buildPopulationClause(forSampleSet, restriction);
        if (population != null) {
            clauses.add(population);
        }

        String commercial = buildNonProfitClause(forSampleSet, restriction);
        if (commercial != null) {
            clauses.add(commercial);
        }

        String research_type = buildResearchTypeClause(forSampleSet, restriction);
        if (research_type != null) {
            clauses.add(research_type);
        }

        String dataset_usage = buildDataSetUsageClause(forSampleSet, restriction);
        if (dataset_usage != null) {
            clauses.add(dataset_usage);
        }

        if (clauses.isEmpty()) {
            return forSampleSet
                ? "No restrictions."
                : "Any sample which has no restrictions.";
        }

        String first = String.format(
            forSampleSet
                ? "Samples %s."
                : "Any sample which %s.", clauses.remove(0));

        String rest = "";
        if (!clauses.isEmpty()) {
            rest = String.format(
                forSampleSet
                    ? " In addition, samples %s."
                    : " In addition, those samples %s.", buildAndClause(clauses));
        }

        return String.format("%s%s", first, rest);
    }

    // "Samples may only be used for studying men, and Asian-American populations."
    private String buildPopulationClause(boolean forSampleSet, UseRestriction r) {
        Set<String> labels = findLabeledTypedClasses("population", r);

        return labels.isEmpty() ? null
            : String.format("%s be used for the study of %s",
            forSampleSet ? "may only" : "can",
            buildAndClause(labels));
    }

    // "Samples may only be used for research at institutions in North America, Europe, or South America."
    private String buildGeographyClause(boolean forSampleSet, UseRestriction r) {
        Set<String> labels = findLabeledTypedClasses("geography", r);

        return labels.isEmpty() ? null
            : String.format("%s be used for research at institutions in %s",
            forSampleSet ? "may only" : "can",
            buildOrClause(labels));
    }

    // "Samples may not be used for commercial purposes."
    private String buildNonProfitClause(boolean forSampleSet, UseRestriction r) {
        if (hasTypedClass("commercial", r)) {
            return forSampleSet ? "may not be used for commercial purposes" : null;
        } else {
            return forSampleSet ? null : "can be used for commercial purposes";
        }
    }

    // "Samples may not be used for methods research purposes."
    private String buildResearchTypeClause(boolean useMay, UseRestriction r) {
        if (hasTypedClass("research_type", r)) {
            return useMay ? "may be used for methods research purposes" : null;
        } else {
            return useMay ? null : "can be used for methods research purposes";
        }
    }

    // "Samples may only be used for the purpose of studying breast cancer, thyroid cancer, or diabetes."
    private String buildDiseaseClause(boolean useMay, UseRestriction r) {
        Set<String> labels = findLabeledTypedClasses("disease", r);
        String diseaseNames;

        if (labels.isEmpty()) {
            return null;
        } else {
            diseaseNames = buildOrClause(labels);
        }

        return String.format("%s be used for the purpose of studying %s", useMay ? "may only" : "can", diseaseNames);
    }

    // "Samples may only be used for the purpose of studying breast cancer, thyroid cancer, or diabetes."
    private String buildDataSetUsageClause(boolean useMay, UseRestriction r) {
        Set<String> labels = findLabeledTypedClasses("organization", r);
        String usages;

        if (labels.isEmpty()) {
            return null;
        } else {
            usages = buildOrClause(labels);
        }

        return String.format("%s %s", useMay ? "may only" : "can", usages);
    }

    private String buildOrClause(Collection<String> labels) {
        String[] array = labels.toArray(new String[labels.size()]);
        if (array.length == 1) {
            return array[0];
        }
        if (array.length == 2) {
            return String.format("%s or %s", array[0], array[1]);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            if (i == array.length - 1) {
                sb.append("or ");
            }
            sb.append(array[i]);
        }

        return sb.toString();
    }

    private String buildAndClause(Collection<String> labels) {
        String[] array = labels.toArray(new String[labels.size()]);
        if (array.length == 1) {
            return array[0];
        }
        if (array.length == 2) {
            return String.format("%s and %s", array[0], array[1]);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            if (i == array.length - 1) {
                sb.append("and ");
            }
            sb.append(array[i]);
        }

        return sb.toString();
    }

    private String getNamedClassLabel(Named n) {
        try {
            return api.findById(n.getName()).getLabel();
        } catch (IOException e) {
            e.printStackTrace(System.err);

            String[] array = n.getName().split("/");
            return array[array.length - 1];
        }
    }

    private String getNamedClassType(Named n) {
        String result = namedClassTypes.get(n.getName());
        if (result == null && !namedClassTypes.containsKey(n.getName())) {
            result = findNamedClassType(n);
            namedClassTypes.put(n.getName(), result);
        }
        return result;
    }

    private Collection<URL> getOntologyUrls() {
        try {
            return storeOntologyService.retrieveOntologyURLs();
        } catch (IOException e) {
            logger.error("Unable to retrieve ontology URLs from the ontology storage service: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private OntModel getModel() {
        Collection<URL> urls = getOntologyUrls();
        try {
            OntModelCache ontModelCache = OntModelCache.INSTANCE;
            return ontModelCache.getOntModel(urls);
        } catch (Exception e) {
            StringBuilder builder = new StringBuilder("Unable to instantiate the required ontologies: ").
                    append(e.getMessage()).
                    append("\n for ontology urls: ");
            for (URL url: urls) {
                builder.append("\n URL:").append(url.toString());
            }
            logger.error(builder.toString());
            throw new RuntimeException(builder.toString());
        }
    }

    private String findNamedClassType(Named n) {

        OntClass cls = getModel().getOntClass(n.getName());

        OntClass disease = getModel().getOntClass("http://purl.obolibrary.org/obo/DOID_4");
        if (cls.hasSuperClass(disease)) {
            return "disease";
        }

        OntClass duoDUR = getModel().getOntClass(DUO_DATA_USE_REQUIREMENTS);
        if (cls.hasSuperClass(duoDUR)) {
            return "commercial";
        }

        OntClass researchType = getModel().getOntClass(RESEARCH_TYPE);
        OntClass duoSecondary = getModel().getOntClass(DUO_SECONDARY_CATEGORY);
        if (cls.hasSuperClass(researchType) || cls.hasSuperClass(duoSecondary)) {
            return "research_type";
        }

        OntClass datasetUsage = getModel().getOntClass(DATASET_USAGE);
        OntClass duoPrimary = getModel().getOntClass(DUO_PRIMARY_CATEGORY);
        if (cls.hasSuperClass(datasetUsage) || cls.hasSuperClass(duoPrimary)) {
            return "dataset_usage";
        }

        return "other";
    }

    private Set<String> findLabeledTypedClasses(String type, UseRestriction r) {
        Set<Named> named = findNamedClasses(new NamedTypePredicate(type), r);
        Set<String> labels = new LinkedHashSet<>();
        named.forEach((n) -> { labels.add(getNamedClassLabel(n)); });
        return labels;
    }

    private Set<Named> findNamedClasses(RestrictionPredicate pred, UseRestriction r) {
        FilterVisitor visitor = new FilterVisitor(pred);
        r.visit(visitor);
        Set<Named> named = new HashSet<>();
        visitor.getMatched().forEach((n) -> named.add(((Named) n)));
        return named;
    }

    private boolean hasTypedClass(String type, UseRestriction r) {
        return hasClass(new NamedTypePredicate(type), r);
    }

    private boolean hasClass(RestrictionPredicate pred, UseRestriction r) {
        FindVisitor visitor = new FindVisitor(pred);
        r.visit(visitor);
        return visitor.isFound();
    }

    private class NamedTypePredicate implements RestrictionPredicate {

        private final String type;

        public NamedTypePredicate(String t) {
            this.type = t;
        }

        @Override
        public boolean accepts(UseRestriction r) {
            return (r instanceof Named)
                && getNamedClassType((Named) r).equals(type);
        }
    }

    @Inject
    public void setApi(OntologyTermSearchAPI api) {
        this.api = api;
    }

}

interface RestrictionPredicate {

    boolean accepts(UseRestriction r);
}

abstract class SimpleUseRestrictionVisitor implements UseRestrictionVisitor {

    @Override
    public void startChildren() {
    }

    @Override
    public void endChildren() {
    }
}

class FilterVisitor extends SimpleUseRestrictionVisitor {

    private final RestrictionPredicate predicate;
    private final ArrayList<UseRestriction> matched;

    public FilterVisitor(RestrictionPredicate p) {
        this.predicate = p;
        matched = new ArrayList<>();
    }

    @Override
    public boolean visit(UseRestriction r) {

        if (predicate.accepts(r)) {
            matched.add(r);
        }
        return true;
    }

    public Collection<UseRestriction> getMatched() {
        return matched;
    }

}

class FindVisitor extends SimpleUseRestrictionVisitor {

    private final RestrictionPredicate predicate;
    private boolean found;

    public FindVisitor(RestrictionPredicate p) {
        this.predicate = p;
        found = false;
    }

    @Override
    public boolean visit(UseRestriction r) {

        if (predicate.accepts(r)) {
            found = true;
            return false;
        }
        return true;
    }

    public boolean isFound() {
        return found;
    }

}
