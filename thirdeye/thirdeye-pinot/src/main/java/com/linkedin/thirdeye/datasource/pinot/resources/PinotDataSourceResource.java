package com.linkedin.thirdeye.datasource.pinot.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Preconditions;
import com.linkedin.thirdeye.datasource.ThirdEyeCacheRegistry;
import com.linkedin.thirdeye.datasource.pinot.PinotQuery;
import com.linkedin.thirdeye.datasource.pinot.PinotThirdEyeDataSource;
import com.linkedin.thirdeye.datasource.pinot.resultset.ThirdEyeResultSet;
import com.linkedin.thirdeye.datasource.pinot.resultset.ThirdEyeResultSetGroup;
import com.linkedin.thirdeye.datasource.pinot.resultset.ThirdEyeResultSetSerializer;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/pinot-data-source")
@Produces(MediaType.APPLICATION_JSON)
public class PinotDataSourceResource {
  private static final Logger LOG = LoggerFactory.getLogger(PinotDataSourceResource.class);
  private static final ObjectMapper OBJECT_MAPPER;
  static {
    SimpleModule module = new SimpleModule("ThirdEyeResultSetSerializer", new Version(1, 0, 0, null, null, null));
    module.addSerializer(ThirdEyeResultSet.class, new ThirdEyeResultSetSerializer());
    OBJECT_MAPPER = new ObjectMapper();
    OBJECT_MAPPER.registerModule(module);
  }
  private static final String URL_ENCODING = "UTF-8";

  private PinotThirdEyeDataSource pinotDataSource;

  /**
   * Returns the JSON string of the ThirdEyeResultSetGroup of the given Pinot query.
   *
   * @param pql       the given Pinot query.
   * @param tableName the table name at which the query targets.
   *
   * @return the JSON string of the ThirdEyeResultSetGroup of the query; the string could be the exception message if
   * the query is not executed successfully.
   */
  @GET
  @Path("/query")
  public String executePQL(@QueryParam("pql") String pql, @QueryParam("tableName") String tableName)
      throws UnsupportedEncodingException {
    initPinotDataSource();

    String resultString;
    String decodedPql = URLDecoder.decode(pql, URL_ENCODING);
    String decodedTableName = URLDecoder.decode(tableName, URL_ENCODING);
    PinotQuery pinotQuery = new PinotQuery(decodedPql, decodedTableName);
    try {
      ThirdEyeResultSetGroup thirdEyeResultSetGroup = pinotDataSource.executePQL(pinotQuery);
      resultString = OBJECT_MAPPER.writeValueAsString(thirdEyeResultSetGroup);
    } catch (ExecutionException | JsonProcessingException e) {
      LOG.error("Failed to execute PQL ({}) due to the exception:", pinotQuery);
      // TODO: Expand the definition of ThirdEyeResultSetGroup to include a field of error message?
      resultString = e.toString();
    }

    return resultString;
  }

  /**
   * Initializes the pinot data source if it is still not initialized.
   */
  private void initPinotDataSource() {
    if (pinotDataSource == null) {
      Preconditions.checkNotNull(ThirdEyeCacheRegistry.getInstance(),
          "Failed to get Pinot data source because ThirdEye cache registry is not initialized.");
      pinotDataSource = (PinotThirdEyeDataSource) ThirdEyeCacheRegistry.getInstance().getQueryCache()
          .getDataSource(PinotThirdEyeDataSource.DATA_SOURCE_NAME);
      Preconditions
          .checkNotNull(pinotDataSource, "Failed to get Pinot data source because it is not initialized in ThirdEye.");
    }
  }
}
