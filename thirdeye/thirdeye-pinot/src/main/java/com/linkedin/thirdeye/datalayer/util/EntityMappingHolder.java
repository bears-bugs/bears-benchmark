package com.linkedin.thirdeye.datalayer.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.linkedin.thirdeye.datalayer.entity.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityMappingHolder {
  private static final Logger LOG = LoggerFactory.getLogger(EntityMappingHolder.class);

  //Map<TableName,EntityName>
  BiMap<String, String> tableToEntityNameMap = HashBiMap.create();
  Map<String, LinkedHashMap<String, ColumnInfo>> columnInfoPerTable = new HashMap<>();
  //DB NAME to ENTITY NAME mapping
  Map<String, BiMap<String, String>> columnMappingPerTable = new HashMap<>();

  public void register(Connection connection, Class<? extends AbstractEntity> entityClass,
      String tableName) throws Exception {
    tableName = tableName.toLowerCase();
    LOG.info("GENERATING MAPPING FOR TABLE: {}", tableName);
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    String catalog = null;
    String schemaPattern = null;
    String columnNamePattern = null;
    LinkedHashMap<String, ColumnInfo> columnInfoMap = new LinkedHashMap<>();
    tableToEntityNameMap.put(tableName, entityClass.getSimpleName());
    columnMappingPerTable.put(tableName, HashBiMap.<String, String>create());
    boolean foundTable = false;
    for (String tableNamePattern : new String[] {tableName.toLowerCase(),
        tableName.toUpperCase()}) {
      try (ResultSet rs =
          databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
        while (rs.next()) {
          foundTable = true;
          String columnName = rs.getString(4);
          ColumnInfo columnInfo = new ColumnInfo();
          columnInfo.columnNameInDB = columnName.toLowerCase();
          columnInfo.sqlType = rs.getInt(5);
          columnInfoMap.put(columnName.toLowerCase(), columnInfo);
        }
      }
    }
    if (!foundTable) {
      throw new RuntimeException("Unable to find table: " + tableName);
    }
    List<Field> fields = new ArrayList<>();
    getAllFields(fields, entityClass);
    for (String dbColumn : columnInfoMap.keySet()) {
      boolean success = false;
      for (Field field : fields) {
        field.setAccessible(true);
        String entityColumn = field.getName();
        if (dbColumn.toLowerCase().equals(entityColumn.toLowerCase())) {
          success = true;
        }
        String dbColumnNormalized = dbColumn.replaceAll("_", "").toLowerCase();
        String entityColumnNormalized = entityColumn.replaceAll("_", "").toLowerCase();
        if (dbColumnNormalized.equals(entityColumnNormalized)) {
          success = true;
        }
        if (success) {
          columnInfoMap.get(dbColumn).columnNameInEntity = entityColumn;
          columnInfoMap.get(dbColumn).field = field;
          LOG.debug("Mapped {} to {}", dbColumn, entityColumn);
          columnMappingPerTable.get(tableName).put(dbColumn, entityColumn);
          break;
        }
      }
      if(!success) {
        LOG.error("Unable to map [" + dbColumn + "] to any field in table [" + entityClass
            .getSimpleName() + "] !!!");
      }
    }
    columnInfoPerTable.put(tableName, columnInfoMap);
  }

  public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
    fields.addAll(Arrays.asList(type.getDeclaredFields()));
    if (type.getSuperclass() != null) {
      fields = getAllFields(fields, type.getSuperclass());
    }
    return fields;
  }
}


class ColumnInfo {
  String columnNameInDB;
  int sqlType;
  String columnNameInEntity;
  Field field;
}
