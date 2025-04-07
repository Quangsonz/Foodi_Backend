package G9.Foodi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact")
public class Contact {
    @Id
    private String id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
} 