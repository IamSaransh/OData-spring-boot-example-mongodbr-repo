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
    public static final String ET_PRODUCT_NAME = "Product";
    public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);

    // Entity Set Names
    public static final String ES_PRODUCTS_NAME = "Products";

    /**
     * this is just mocking the repository, this will come from the repo;
     * @return
     */
    public Mapper getEntitySchema(){
        Map<String, EdmPrimitiveTypeKind> fieldsMap = new HashMap<>();
        fieldsMap.put("ID", EdmPrimitiveTypeKind.Int32);
        fieldsMap.put("Name", EdmPrimitiveTypeKind.String);
        fieldsMap.put("Description", EdmPrimitiveTypeKind.String);

        Map<String,String> engineToClient = new HashMap<>();
        engineToClient.put("ID", "partyId");
        engineToClient.put("Name", "partyName");
        engineToClient.put("Description", "partyDesc");

        Mapper mapper = Mapper.builder()
                .namespace(NAMESPACE)
                .containerName(CONTAINER_NAME)
                .entitySetName(ES_PRODUCTS_NAME)
                .entityName(ET_PRODUCT_NAME)
                .idFieldName("ID")
                .fieldStringTypeMap(fieldsMap)
                .mappingToBackendModel(engineToClient)
                .build();
        return mapper;
    }
}
