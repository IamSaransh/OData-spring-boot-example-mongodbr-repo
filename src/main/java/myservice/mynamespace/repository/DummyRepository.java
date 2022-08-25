package myservice.mynamespace.repository;

import myservice.mynamespace.mapper.Mapper;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;


import java.util.HashMap;
import java.util.Map;

/**
 * @author saransh
 */

public class DummyRepository {
    // Service Namespace
    public static final String NAMESPACE = "OData.Demo";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // Entity Types Names
    public static final String ET_ACCOUNT_NAME = "Account";
    public static final FullQualifiedName ET_ACCOUNT_FQN = new FullQualifiedName(NAMESPACE, ET_ACCOUNT_NAME);

    // Entity Set Names
    public static final String ES_ACCOUNTS_NAME = "Accounts";

    /**
     * this is just mocking the repository, this will come from the repo;
     * @return
     */
    public Mapper getEntitySchema(){
        Map<String, EdmPrimitiveTypeKind> fieldsMap = new HashMap<>();
        fieldsMap.put("ID", EdmPrimitiveTypeKind.Int32);
        fieldsMap.put("Name", EdmPrimitiveTypeKind.String);
        fieldsMap.put("Description", EdmPrimitiveTypeKind.String);

        Map<String,String> serverToClientFieldMap = new HashMap<>();
        serverToClientFieldMap.put("ID", "partyId");
        serverToClientFieldMap.put("Name", "partyName");
        serverToClientFieldMap.put("Description", "partyDesc");

        Mapper mapper = Mapper.builder()
                .namespace(NAMESPACE)
                .containerName(CONTAINER_NAME)
                .entitySetName(ES_ACCOUNTS_NAME)
                .entityName(ET_ACCOUNT_NAME)
                .idFieldName("ID")
                .fieldStringTypeMap(fieldsMap)
                .mappingToBackendModel(serverToClientFieldMap)
                .build();
        return mapper;
    }
}
