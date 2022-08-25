package myservice.mynamespace.controller;

import lombok.extern.log4j.Log4j2;
import myservice.mynamespace.data.Storage;
import myservice.mynamespace.service.DemoEdmProvider;
import myservice.mynamespace.service.DemoEntityCollectionProcessor;
import myservice.mynamespace.service.DemoEntityProcessor;
import myservice.mynamespace.service.DemoPrimitiveProcessor;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
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
@Log4j2
public class ODataController {

    protected static final String URI = "/odata";

    @RequestMapping(value = "*")
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(new DemoEdmProvider(),
                new ArrayList<EdmxReference>());
        Storage storage = new Storage();

            // create odata handler and configure it with EdmProvider and Processor
            ODataHttpHandler handler = odata.createHandler(edm);
            handler.register(new DemoEntityCollectionProcessor(storage));
            handler.register(new DemoEntityProcessor(storage));
            handler.register(new DemoPrimitiveProcessor(storage));
            handler.process(new HttpServletRequestWrapper(request) {
                // Spring MVC matches the whole path as the servlet path
                // Olingo wants just the prefix, ie upto /OData/V1.0, so that it
                // can parse the rest of it as an OData path. So we need to override
                // getServletPath()
                @Override
                public String getServletPath() {
                    return ODataController.URI;
                }
            }, response);

            // let the handler do the work


    }
}
