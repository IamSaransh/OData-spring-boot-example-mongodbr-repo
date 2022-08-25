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

import java.util.*;
import java.util.stream.Collectors;

import myservice.mynamespace.repository.DummyRepository;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;

import static myservice.mynamespace.repository.DummyRepository.CONTAINER_NAME;

public class DemoEdmProvider extends CsdlAbstractEdmProvider {
  public static final String ENTITY_NAME  = "Account";
  DummyRepository dummyRepository = new DummyRepository();


   final FullQualifiedName CONTAINER =
          new FullQualifiedName(dummyRepository.getEntitySchema().getNamespace(), CONTAINER_NAME);

  // Entity Types Names

  final FullQualifiedName ET_ACCOUNT_FQN = new FullQualifiedName(dummyRepository.getEntitySchema().getNamespace(), ENTITY_NAME);


  // Entity Set Names


  @Override
  public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
    // this method is called for one of the EntityTypes that are configured in the Schema
    if(ET_ACCOUNT_FQN.equals(entityTypeName)){

      CsdlPropertyRef propertyRef = new CsdlPropertyRef();
      propertyRef.setName(dummyRepository.getEntitySchema().getIdFieldName());

      // configure EntityType
      CsdlEntityType entityType = new CsdlEntityType();
      entityType.setName(dummyRepository.getEntitySchema().getEntityName());
//      entityType.setProperties(Arrays.asList(id, name, description));
      Map<String, EdmPrimitiveTypeKind> entityFields  = dummyRepository.getEntitySchema().getFieldStringTypeMap();
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
    if(entityContainer.equals(CONTAINER)){
      if(entitySetName.equals("Accounts")){
        CsdlEntitySet entitySet = new CsdlEntitySet();
        entitySet.setName(dummyRepository.getEntitySchema().getEntitySetName());
        entitySet.setType(DummyRepository.ET_ACCOUNT_FQN);

        return entitySet;
      }
    }

    return null;

  }

  @Override
  public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {
    // This method is invoked when displaying the service document
    // at e.g. http://localhost:8080/DemoService/DemoService.svc
    if(entityContainerName == null || entityContainerName.equals(CONTAINER)){
      CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
      entityContainerInfo.setContainerName(CONTAINER);
      return entityContainerInfo;
    }

    return null;

  }

  @Override
  public List<CsdlSchema> getSchemas() {
    // create Schema
    CsdlSchema schema = new CsdlSchema();
    schema.setNamespace(DummyRepository.NAMESPACE);

    // add EntityTypes
    List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
    entityTypes.add(getEntityType(DummyRepository.ET_ACCOUNT_FQN));
    schema.setEntityTypes(entityTypes);

    // add EntityContainer
    schema.setEntityContainer(getEntityContainer());

    // finally
    List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
    schemas.add(schema);

    return schemas;

  }

  @Override
  public CsdlEntityContainer getEntityContainer() {
    // create EntitySets
    List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
    entitySets.add(getEntitySet(CONTAINER, DummyRepository.ES_ACCOUNTS_NAME));

    // create EntityContainer
    CsdlEntityContainer entityContainer = new CsdlEntityContainer();
    entityContainer.setName(CONTAINER_NAME);
    entityContainer.setEntitySets(entitySets);

    return entityContainer;

  }



}
