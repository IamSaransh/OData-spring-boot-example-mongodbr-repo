/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package myservice.mynamespace.service;

import myservice.mynamespace.repository.EntityMapperRepository;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DemoEdmProvider extends CsdlAbstractEdmProvider {
  public static final String ENTITY_NAME  = "Account";

  EntityMapperRepository repository;
  @Autowired
  public DemoEdmProvider(EntityMapperRepository repository) {
    this.repository = repository;
  }







  @Override

  public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
    // this method is called for one of the EntityTypes that are configured in the Schema
    FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(repository.findByEntityName(ENTITY_NAME).get().getNamespace()
            , repository.findByEntityName(ENTITY_NAME).get().getEntityName());
    if(ET_PRODUCT_FQN.equals(entityTypeName)){

      //create EntityType properties
//      CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
//      CsdlProperty name = new CsdlProperty().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
//      CsdlProperty  description = new CsdlProperty().setName("Description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

      // create PropertyRef for Key element
      CsdlPropertyRef propertyRef = new CsdlPropertyRef();
      propertyRef.setName(repository.findByEntityName(ENTITY_NAME).get().getIdFieldName());

      // configure EntityType
      CsdlEntityType entityType = new CsdlEntityType();
      entityType.setName(ENTITY_NAME);
//      entityType.setProperties(Arrays.asList(id, name, description));
      Map<String, EdmPrimitiveTypeKind> entityFields  = repository.findByEntityName(ENTITY_NAME).get().getFieldStringTypeMap();
      List<CsdlProperty> csdlPropertyList = entityFields.entrySet().stream()
              .map((x) ->  new CsdlProperty().setName(x.getKey()).setType(x.getValue().getFullQualifiedName()))
              .collect(Collectors.toList());
      entityType.setProperties(csdlPropertyList);
      entityType.setKey(Collections.singletonList(propertyRef));

      return entityType;
    }

    return null;

  }

  @Override
  public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName)  {
    FullQualifiedName CONTAINER = new FullQualifiedName(repository.findByEntityName(ENTITY_NAME).get().getNamespace(),
            repository.findByEntityName(ENTITY_NAME).get().getContainerName());
    FullQualifiedName ET_PRODUCT_FQN
            = new FullQualifiedName(repository.findByEntityName(ENTITY_NAME).get().getNamespace()
            , repository.findByEntityName(ENTITY_NAME).get().getEntityName());

    final String ES_PRODUCT_NAME = repository.findByEntityName(ENTITY_NAME).get().getEntitySetName();
    if(entityContainer.equals(CONTAINER)){
      if(entitySetName.equals(repository.findByEntityName(ENTITY_NAME).get().getEntitySetName())){
        CsdlEntitySet entitySet = new CsdlEntitySet();
        entitySet.setName(ES_PRODUCT_NAME);
        entitySet.setType(ET_PRODUCT_FQN);

        return entitySet;
      }
    }

    return null;

  }

  @Override
  public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {
    // This method is invoked when displaying the service document 
    // at e.g. http://localhost:8080/DemoService/DemoService.svc
    FullQualifiedName CONTAINER = new FullQualifiedName(repository.findByEntityName(ENTITY_NAME).get().getNamespace(),
            repository.findByEntityName(ENTITY_NAME).get().getContainerName());
    if(entityContainerName == null || entityContainerName.equals(CONTAINER)){
      CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
      entityContainerInfo.setContainerName(CONTAINER);
      return entityContainerInfo;
    }

    return null;

  }

  @Override
  public List<CsdlSchema> getSchemas() {
    FullQualifiedName ET_PRODUCT_FQN
            = new FullQualifiedName(repository.findByEntityName(ENTITY_NAME).get().getNamespace()
            , repository.findByEntityName(ENTITY_NAME).get().getEntityName());
    // create Schema
    CsdlSchema schema = new CsdlSchema();
    schema.setNamespace(repository.findByEntityName(ENTITY_NAME).get().getNamespace());

    // add EntityTypes
    List<CsdlEntityType> entityTypes = new ArrayList<>();
    entityTypes.add(getEntityType(ET_PRODUCT_FQN));
    schema.setEntityTypes(entityTypes);

    // add EntityContainer
    schema.setEntityContainer(getEntityContainer());

    // finally
    List<CsdlSchema> schemas = new ArrayList<>();
    schemas.add(schema);

    return schemas;

  }

  @Override
  public CsdlEntityContainer getEntityContainer() {
    // create EntitySets
    FullQualifiedName CONTAINER = new FullQualifiedName(repository.findByEntityName(ENTITY_NAME).get().getNamespace(),
            repository.findByEntityName(ENTITY_NAME).get().getContainerName());
    List<CsdlEntitySet> entitySets = new ArrayList<>();
    entitySets.add(getEntitySet(CONTAINER, repository.findByEntityName(ENTITY_NAME).get().getEntitySetName()));

    // create EntityContainer
    CsdlEntityContainer entityContainer = new CsdlEntityContainer();
    entityContainer.setName(repository.findByEntityName(ENTITY_NAME).get().getEntitySetName());
    entityContainer.setEntitySets(entitySets);

    return entityContainer;

  }



}
