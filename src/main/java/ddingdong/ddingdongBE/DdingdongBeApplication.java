package ddingdong.ddingdongBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DdingdongBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdingdongBeApplication.class, args);
	}

}
