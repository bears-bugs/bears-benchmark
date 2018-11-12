package org.broadinstitute.dsde.consent.ontology;

import com.google.common.io.Resources;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Created by SantiagoSaucedo on 4/18/2016.
 */
public class AbstractTest {

    protected static StoreOntologyService getStorageServiceMock() throws GeneralSecurityException, IOException {
        URL urlOntologyConfiguration = Resources.getResource("ontologyConf.json");
        Collection<URL> ontologyUrls = Arrays.asList(
            Resources.getResource("diseases.owl"),
            Resources.getResource("duo.owl"),
            Resources.getResource("data-use.owl"));
        Collection<String> ontologyStrings = ontologyUrls.stream().map(URL::toString).collect(Collectors.toList());

        StoreOntologyService storeServiceMock = Mockito.mock(StoreOntologyService.class);
        Mockito.when(storeServiceMock.retrieveConfigurationFile()).
            thenReturn(Resources.toString(urlOntologyConfiguration, Charset.defaultCharset()));
        Mockito.when(storeServiceMock.retrieveConfigurationKeys()).thenReturn(ontologyStrings);
        Mockito.when(storeServiceMock.retrieveOntologyURLs()).thenReturn(ontologyUrls);

        return storeServiceMock;
    }

}