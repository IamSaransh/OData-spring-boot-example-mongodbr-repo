package myservice.mynamespace.mapper;

import lombok.*;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

import javax.persistence.*;
import java.util.Map;

/**
 * @author saransh
 */

/**
 * TODO: get some sort of method to insert this data into the database
 * @Info: for now, I have a data.sql file to store this database
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuppressWarnings("JpaAttributeTypeInspection")
public class Mapper {
    @Id
    private String id;
    private String namespace;
    private String containerName;
    private String entitySetName;
    private String entityName;
    private String idFieldName;
    private Map<String, EdmPrimitiveTypeKind> fieldStringTypeMap;
    private Map<String, String> mappingToBackendModel;


}
