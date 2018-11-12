package org.broadinstitute.dsde.consent.ontology.resources.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.DropwizardClientRule;
import org.broadinstitute.dsde.consent.ontology.resources.DataUseResource;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.parboiled.common.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DataUseTest {

    /**
     * This spins up a server so we can ensure that SchemaLoader will be able to resolve `data-use.json` URL
     */
    @ClassRule
    public static final DropwizardClientRule RULE = new DropwizardClientRule(new DataUseResource());

    static ObjectMapper MAPPER;
    static String DU_CONTENT;
    static JSONObject RAW_SCHEMA;
    static SchemaLoader LOADER;
    static Schema DU_SCHEMA;

    @BeforeClass
    public static void setUp() {
        MAPPER = new ObjectMapper();
        DU_CONTENT = FileUtils.readAllTextFromResource("data-use.json");
        RAW_SCHEMA = new JSONObject(DU_CONTENT);
        LOADER = SchemaLoader.builder().schemaJson(RAW_SCHEMA).
            resolutionScope(RULE.baseUri() + "/schemas/data-use").build();
        DU_SCHEMA = LOADER.load().build();
    }

    private void assertInvalidJson(String snippet) {
        try {
            DU_SCHEMA.validate(new JSONObject(snippet));
            fail("Snippet <" + snippet + "> should not have been able to pass schema validation");
        } catch (ValidationException e) {
            assertTrue("Snippet <" + snippet + "> correctly failed schema validation", true);
        }
    }

    private void assertValidJson(String snippet) {
        try {
            DU_SCHEMA.validate(new JSONObject(snippet));
            assertTrue(true);
        } catch (ValidationException e) {
            fail("snippet <" + snippet + "> did not pass validation: " + e.getMessage());
        }
    }

    /*
     * General schema validation tests
     */

    @Test
    public void validateDataUseJsonFile() throws ValidationException, IOException {
        try (InputStream schemaStream = new URL("http://json-schema.org/draft-04/schema").openStream()) {
            String schemaString = FileUtils.readAllText(schemaStream);
            JSONObject rawSchema = new JSONObject(new JSONTokener(schemaString));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(DU_CONTENT)); // throws a ValidationException if this object is invalid
        }
    }



    /*
     *  Tests that look at individual fields in DataUse
     */

    @Test
    public void generalUse() throws Exception {
        String json = "{ \"generalUse\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify general use", dataUse.getGeneralUse());
        assertValidJson(json);
        assertInvalidJson("{ \"generalUse\": \"string\" }");
    }

    @Test
    public void hmbResearch() throws Exception {
        String json = "{ \"hmbResearch\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify health/medical/biomedical research", dataUse.getHmbResearch());
        assertValidJson(json);
        assertInvalidJson("{ \"hmbResearch\": \"string\" }");
    }

    @Test
    public void diseaseRestrictions() throws Exception {
        String json = "{ \"diseaseRestrictions\": [\"one\", \"two\"] }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify disease restrictions", dataUse.getDiseaseRestrictions().size() == 2);
        assertValidJson(json);
        assertInvalidJson("{ \"diseaseRestrictions\": \"string\" }");
    }

    @Test
    public void populationOriginsAncestry() throws Exception {
        String json = "{ \"populationOriginsAncestry\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify population origins or ancestry use", dataUse.getPopulationOriginsAncestry());
        assertValidJson(json);
        assertInvalidJson("{ \"populationOriginsAncestry\": \"string\" }");
    }

    @Test
    public void populationStructure() throws Exception {
        String json = "{ \"populationStructure\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify population structure research", dataUse.getPopulationStructure());
        assertValidJson(json);
        assertInvalidJson("{ \"populationStructure\": \"string\" }");
    }

    @Test
    public void commercialUse() throws Exception {
        String json = "{ \"commercialUse\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify commecial use", dataUse.getCommercialUse());
        assertValidJson(json);
        assertInvalidJson("{ \"commercialUse\": \"string\" }");
    }

    @Test
    public void methodsResearch() throws Exception {
        String json = "{ \"methodsResearch\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify methods research use", dataUse.getMethodsResearch());
        assertValidJson(json);
        assertInvalidJson("{ \"methodsResearch\": \"string\" }");
    }

    @Test
    public void aggregateResearch() throws Exception {
        String json = "{ \"aggregateResearch\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify aggregate research use", dataUse.getAggregateResearch().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"aggregateResearch\": \"string\" }");
    }

    @Test
    public void gender() throws Exception {
        String json = "{ \"gender\": \"Male\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify Male gender", dataUse.getGender().equals("Male"));
        assertValidJson(json);
        assertInvalidJson("{ \"gender\": \"string\" }");
    }

    @Test
    public void controlSetOption() throws Exception {
        String json = "{ \"controlSetOption\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify a control set usage", dataUse.getControlSetOption().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"controlSetOption\": \"string\" }");
    }

    @Test
    public void populationRestrictions() throws Exception {
        String json = "{ \"populationRestrictions\": [\"one\", \"two\"] }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify a population restrictions", dataUse.getPopulationRestrictions().size() == 2);
        assertValidJson(json);
        assertInvalidJson("{ \"populationRestrictions\": \"string\" }");
    }

    @Test
    public void pediatric() throws Exception {
        String json = "{ \"pediatric\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify a pediatric usage", dataUse.getPediatric());
        assertValidJson(json);
        assertInvalidJson("{ \"pediatric\": \"string\" }");
    }

    @Test
    public void dateRestriction() throws Exception {
        String json = "{ \"dateRestriction\": \"date\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify a date restriction", dataUse.getDateRestriction().equals("date"));
        assertValidJson(json);
        assertInvalidJson("{ \"dateRestriction\": true }");
    }

    @Test
    public void recontacting() throws Exception {
        String json = "{ \"recontactingDataSubjects\": true, \"recontactMay\": \"may\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify whether recontacting subjects is allowed", dataUse.getRecontactingDataSubjects());
        assertValidJson(json);


        json = "{ \"recontactingDataSubjects\": true, \"recontactMust\": \"must\" }";
        dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify whether recontacting subjects is allowed", dataUse.getRecontactingDataSubjects());
        assertValidJson(json);

        assertInvalidJson("{ \"recontactingDataSubjects\": \"string\" }");
        assertInvalidJson("{ \"recontactingDataSubjects\": true }");
        assertInvalidJson("{ \"recontactingDataSubjects\": true, \"dateRestriction\": \"date\" }");
        assertInvalidJson("{ \"recontactingDataSubjects\": true, \"recontactMay\": true }");
        assertInvalidJson("{ \"recontactingDataSubjects\": true, \"recontactMust\": true }");

    }

    @Test
    public void recontactMay() throws Exception {
        String json = "{ \"recontactMay\": \"may\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify conditions under which subjects *may* be recontacted", dataUse.getRecontactMay().equals("may"));
        assertValidJson(json);
        assertInvalidJson("{ \"recontactMay\": true }");
    }

    @Test
    public void recontactMust() throws Exception {
        String json = "{ \"recontactMust\": \"must\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify conditions under which subjects *must* be recontacted", dataUse.getRecontactMust().equals("must"));
        assertValidJson(json);
        assertInvalidJson("{ \"recontactMust\": true }");
    }

    @Test
    public void genomicPhenotypicData() throws Exception {
        String json = "{ \"genomicPhenotypicData\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify if genomic and phenotypic data is available for future research", dataUse.getGenomicPhenotypicData().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"genomicPhenotypicData\": true }");
    }

    @Test
    public void otherRestrictions() throws Exception {
        String json = "{ \"otherRestrictions\": true, \"other\": \"other\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify if there are other conditions", dataUse.getOtherRestrictions());
        assertValidJson(json);
        assertValidJson("{ \"otherRestrictions\": true, \"other\": \"other\", \"ethicsApprovalRequired\": true }");
        assertValidJson("{ \"otherRestrictions\": true, \"other\": \"other\", \"geographicalRestrictions\": \"US\" }");
        assertValidJson("{ \"otherRestrictions\": true, \"other\": \"other\", \"cloudStorage\": \"Yes\" }");
        assertValidJson("{ \"otherRestrictions\": true, \"other\": \"other\", \"cloudStorage\": \"Yes\", \"ethicsApprovalRequired\": true }");
        assertInvalidJson("{ \"otherRestrictions\": true }");
        assertInvalidJson("{ \"otherRestrictions\": \"string\" }");
    }

    @Test
    public void other() throws Exception {
        String json = "{ \"other\": \"other\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify other restrictions", dataUse.getOther().equals("other"));
        assertValidJson(json);
        assertInvalidJson("{ \"other\": true }");
    }

    @Test
    public void cloudStorage() throws Exception {
        String json = "{ \"cloudStorage\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify cloud storage use", dataUse.getCloudStorage().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"cloudStorage\": \"string\" }");
    }

    @Test
    public void geographicalRestrictions() throws Exception {
        String json = "{ \"geographicalRestrictions\": \"geo\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify geographic restrictions", dataUse.getGeographicalRestrictions().equals("geo"));
        assertValidJson(json);
        assertInvalidJson("{ \"geographicalRestrictions\": true }");
    }

    @Test
    public void illegalBehavior() throws Exception {
        String json = "{ \"illegalBehavior\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue("DU should specify illegal behavior", dataUse.getIllegalBehavior());
        assertValidJson(json);
        assertInvalidJson("{ \"illegalBehavior\": \"string\" }");
    }

    @Test
    public void addiction() throws Exception {
        String json = "{ \"addiction\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getAddiction());
        assertValidJson(json);
        assertInvalidJson("{ \"addiction\": \"string\" }");
    }

    @Test
    public void sexualDiseases() throws Exception {
        String json = "{ \"sexualDiseases\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getSexualDiseases());
        assertValidJson(json);
        assertInvalidJson("{ \"sexualDiseases\": \"string\" }");
    }

    @Test
    public void stigmatizeDiseases() throws Exception {
        String json = "{ \"stigmatizeDiseases\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getStigmatizeDiseases());
        assertValidJson(json);
        assertInvalidJson("{ \"stigmatizeDiseases\": \"string\" }");
    }

    @Test
    public void vulnerablePopulations() throws Exception {
        String json = "{ \"vulnerablePopulations\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getVulnerablePopulations());
        assertValidJson(json);
        assertInvalidJson("{ \"vulnerablePopulations\": \"string\" }");
    }

    @Test
    public void psychologicalTraits() throws Exception {
        String json = "{ \"psychologicalTraits\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getPsychologicalTraits());
        assertValidJson(json);
        assertInvalidJson("{ \"psychologicalTraits\": \"string\" }");
    }

    @Test
    public void nonBiomedical() throws Exception {
        String json = "{ \"nonBiomedical\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getNonBiomedical());
        assertValidJson(json);
        assertInvalidJson("{ \"nonBiomedical\": \"string\" }");
    }

    @Test
    public void ethicsApprovalRequired() throws Exception {
        String json = "{ \"ethicsApprovalRequired\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getEthicsApprovalRequired());
        assertValidJson(json);
        assertInvalidJson("{ \"ethicsApprovalRequired\": \"string\" }");
    }

}
