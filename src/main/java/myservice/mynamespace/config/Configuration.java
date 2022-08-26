package myservice.mynamespace.config;

import myservice.mynamespace.data.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


/**
 * @author saransh
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

@Bean
    public Storage getStorage(){
    return new Storage();
}
}
