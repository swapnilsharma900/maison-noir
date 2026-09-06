package in.maisonnoir.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping(value = {"/", "/{path:[^\\.]*"})
    public String forwardToIndex(HttpServletRequest request) {
        System.out.println("🔄 Forwarding to index.html for path: " + request.getRequestURI());
        return "forward:/index.html";
    }
}
