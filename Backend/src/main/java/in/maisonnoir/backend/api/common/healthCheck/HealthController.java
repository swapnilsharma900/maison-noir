package in.maisonnoir.backend.api.common.healthCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", Instant.now().toString());
        response.put("service", "maison-noir-api");
        return response;
    }
    
    @GetMapping("/db")
    public Map<String, String> checkDatabases() {
        Map<String, String> status = new HashMap<>();
        
        try {
            // Test MySQL
            dataSource.getConnection().close();
            status.put("mysql", "✅ Connected");
        } catch (Exception e) {
            status.put("mysql", "❌ Failed: " + e.getMessage());
        }
        
        try {
            // Test MongoDB
            mongoTemplate.getDb().getName();
            status.put("mongodb", "✅ Connected");
        } catch (Exception e) {
            status.put("mongodb", "❌ Failed: " + e.getMessage());
        }
        
        return status;
    }
}



