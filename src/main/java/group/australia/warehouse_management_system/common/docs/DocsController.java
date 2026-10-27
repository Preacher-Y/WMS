package group.australia.warehouse_management_system.common.docs;

import jakarta.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class DocsController {

    @GetMapping("/docs")
    public String docs() {
        return "forward:/docs.html";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/docs";
    }

    // "/" sits outside the /api context path, so Spring never sees it; redirect it at the Tomcat level.
    @Bean
    static WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> rootRedirect(
            @Value("${server.servlet.context-path:}") String contextPath) {
        return factory -> factory.addEngineValves(new ValveBase() {
            @Override
            public void invoke(Request request, Response response) throws IOException, ServletException {
                if ("/".equals(request.getRequestURI())) {
                    response.sendRedirect(contextPath + "/docs");
                    return;
                }
                getNext().invoke(request, response);
            }
        });
    }
}
