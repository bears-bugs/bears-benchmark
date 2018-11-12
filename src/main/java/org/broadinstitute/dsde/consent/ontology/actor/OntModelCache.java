package org.broadinstitute.dsde.consent.ontology.actor;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

public enum OntModelCache {

    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(OntModelCache.class);

    private final Map<Collection<URL>, OntModel> modelCache = new HashMap<>();

    @SuppressWarnings("unused")
    public final Boolean matchPurpose(MatchWorkerMessage message) throws Exception {
        OntModel model = getOntModel(message.getUrlCollection());
        String consentId = UUID.randomUUID().toString();
        String purposeId = UUID.randomUUID().toString();
        OntClass consent = addNamedEquivalentClass(model, consentId, message.getMatchPair().getConsent());
        OntClass purpose = addNamedSubClass(model, purposeId, message.getMatchPair().getPurpose());

        Properties properties = new Properties();
        properties.setProperty("USE_CLASSIFICATION_MONITOR", "none");
        PelletOptions.setOptions(properties);

        // Pellet classification can have trouble with OWL.Thing or OWL.Nothing consents/purposes
        try {
            ((PelletInfGraph) model.getGraph()).classify();
        } catch (NullPointerException e) {
            log.warn("Non-fatal exception classifying: " + e.getMessage());
        }
        OntClass reclassifiedConsentClass = model.getOntClass(consentId);
        Boolean hasSuperClass = purpose.hasSuperClass(reclassifiedConsentClass);

        // If we run into any errors cleaning up the model, then remove it so it can be re-cached.
        try {
            purpose.remove();
        } catch (Exception e) {
            log.warn("Non-fatal exception removing purpose: " + e.getMessage());
            modelCache.remove(message.getUrlCollection());
        }
        try {
            consent.remove();
        } catch (Exception e) {
            log.warn("Non-fatal exception removing consent: " + e.getMessage());
            modelCache.remove(message.getUrlCollection());
        }
        return hasSuperClass;
    }

    /**
     * Handle looking up from the cache a model based on known resource URLs. Any group of ontology URLs can be
     * turned into an OntModel and from there, that object can be distributed out to callers
     *
     * @param resources Collection of Resources in the form of publicly accessible URLs or File resource URLs
     * @return OntModel
     * @throws Exception
     */
    public OntModel getOntModel(final Collection<URL> resources) throws Exception {
        synchronized (modelCache) {
            if (!modelCache.containsKey(resources)) {
                log.debug("Generating OntModel from resources: " + resources);
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
                for (URL resource : resources) {
                    log.debug("Loading resource: " + resource);
                    try (InputStream is = resource.openStream()) {
                        OWLOntology ontology;
                        try {
                            ontology = manager.loadOntologyFromOntologyDocument(is);
                        } catch (OWLOntologyAlreadyExistsException e) {
                            log.warn("Duplicate ontology exists, skipping this one: " + e.getMessage());
                            break;
                        }
                        HashMap<String, OWLAnnotationProperty> annotationProperties = new HashMap<>();
                        ontology.annotationPropertiesInSignature().forEach(property -> {
                            //noinspection OptionalGetWithoutIsPresent
                            annotationProperties.put(property.getIRI().getRemainder().get(), property);
                        });

                        OWLAnnotationProperty deprecated = annotationProperties.get("deprecated");
                        Predicate<OWLAnnotationProperty> hasDeprecated = op -> deprecated != null && op.equals(deprecated);
                        ontology.classesInSignature().forEach(
                            owlClass -> {
                                boolean anyDeprecated = owlClass.annotationPropertiesInSignature().anyMatch(hasDeprecated);
                                if (anyDeprecated) {
                                    log.info("Class has deprecated terms: " + owlClass);
                                }
                                // Do not load deprecated classes.
                                if (!anyDeprecated) {
                                    String id = owlClass.toStringID();
                                    OntClass ontClass = model.createClass(id);
                                    EntitySearcher.getSuperClasses(owlClass, ontology).forEach(
                                        expr -> {
                                            if (expr instanceof OWLClass) {
                                                OWLClass cexpr = (OWLClass) expr;
                                                // this ignores some obvious restriction / intersection classes.
                                                OntClass superClass = model.createClass(cexpr.toStringID());
                                                ontClass.addSuperClass(superClass);
                                            }
                                        });
                                }
                            });
                    }
                }
                modelCache.put(resources, model);
            } else {
                log.debug("Retrieving OntModel from cache.");
            }
            return modelCache.get(resources);
        }
    }

    private OntClass addNamedEquivalentClass(OntModel model, String name, UseRestriction restriction) {
        OntClass cls = model.createClass(name);
        cls.addEquivalentClass(restriction.createOntologicalRestriction(model));
        return cls;
    }

    private OntClass addNamedSubClass(OntModel model, String name, UseRestriction restriction) {
        OntClass cls = model.createClass(name);
        cls.addSuperClass(restriction.createOntologicalRestriction(model));
        return cls;
    }

}
