package myservice.mynamespace.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import myservice.mynamespace.data.Storage;
import myservice.mynamespace.repository.EntityMapperRepository;
import myservice.mynamespace.service.DemoEdmProvider;
import myservice.mynamespace.service.DemoEntityCollectionProcessor;
import myservice.mynamespace.service.DemoEntityProcessor;
import myservice.mynamespace.service.DemoPrimitiveProcessor;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.processor.PrimitiveProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * @author saransh
 */

@RestController
@RequestMapping(ODataController.URI)
@Slf4j
public class ODataController {

    protected static final String URI = "/odata";

    @Autowired
    EntityMapperRepository repository;
    @Autowired
    Storage storage;
    @Autowired
    EntityCollectionProcessor demoEntityCollectionProcessor;

    @Autowired
    EntityProcessor demoEntityProcessor;

    @Autowired
    PrimitiveProcessor demoPrimitiveProcessor;

    @RequestMapping(value = "*")
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.warn(repository.findByEntityName("Account").get().toString());
        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(new DemoEdmProvider(repository),
                new ArrayList<EdmxReference>());
//        Storage storage = new Storage();

            // create odata handler and configure it with EdmProvider and Processor
            ODataHttpHandler handler = odata.createHandler(edm);
            handler.register(demoEntityCollectionProcessor);
            handler.register(demoEntityProcessor);
            handler.register(demoPrimitiveProcessor);
            handler.process(new HttpServletRequestWrapper(request) {
                @Override
                public String getServletPath() {
                    return ODataController.URI;
                }
            }, response);

            // let the handler do the work


    }
}
