package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HtmlTranslator implements TextTranslationService {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(HtmlTranslator.class);
    private OntologyTermSearchAPI api;

    @Inject
    public void setApi(OntologyTermSearchAPI api) {
        this.api = api;
    }

    public enum TranslateFor {
        SAMPLE("Samples are restricted for use under the following conditions:"),
        PURPOSE("Any datasets for which the following uses are allowed:");

        TranslateFor(String description) { this.description = description; }

        String description;
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

    }

    @Override
    public String translateSample(String restrictionStr) throws IOException {
        UseRestriction restriction = UseRestriction.parse(restrictionStr);
        return translateFor(TranslateFor.SAMPLE, restriction);
    }

    @Override
    public String translatePurpose(String restrictionStr) throws IOException {
        UseRestriction restriction = UseRestriction.parse(restrictionStr);
        return translateFor(TranslateFor.PURPOSE, restriction);
    }

    private static String wrapParagraph(final String item) {
        return "\n<p>" + item + "</p>\n";
    }

    private String translateFor(TranslateFor translateFor, UseRestriction restriction) {
        try {
            return wrapParagraph(translateFor.getDescription()) + wrapParagraph(restriction.getDescriptiveLabel(api));
        } catch (IOException e) {
            log.error(e.getMessage());
            return wrapParagraph(translateFor.getDescription()) + wrapParagraph(restriction.toString());
        }
    }

}
