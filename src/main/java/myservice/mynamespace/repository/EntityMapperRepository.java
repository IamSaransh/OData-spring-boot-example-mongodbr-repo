package myservice.mynamespace.repository;
import myservice.mynamespace.mapper.Mapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * @author saransh
 */
@Repository
@EnableMongoRepositories({"myservice.mynamespace.repository"})
public interface EntityMapperRepository  extends MongoRepository<Mapper, String>{
    Optional<Mapper> findById(String id);
    Optional<Mapper> findByEntityName(String name);
}
