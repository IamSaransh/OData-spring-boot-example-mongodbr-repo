package myservice.mynamespace;

import lombok.extern.slf4j.Slf4j;
import myservice.mynamespace.mapper.Mapper;
import myservice.mynamespace.repository.EntityMapperRepository;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author saransh
 */
@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static final String NAMESPACE = "OData.Demo";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";

    // Entity Types Names
    public static final String ET_PRODUCT_NAME = "Account";

    // Entity Set Names
    public static final String ES_PRODUCTS_NAME = "Accounts";

    @Autowired
    private EntityMapperRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*
    command line runner to insert some dummy schema at the runtime:
    todo: make it read from a conf file
     */
    @Override
    public void run(String... args) throws Exception {

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
        repository.deleteAll();
        Mapper saved = repository.save(mapper);
        log.info("ID of inserted schema id {}", saved.getId());
        Optional<Mapper> mapperGotFromRepo = repository.findByEntityName("Account");
        log.info(String.valueOf(mapperGotFromRepo.get().getFieldStringTypeMap()));
    }

}
