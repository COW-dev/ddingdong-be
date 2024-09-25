package ddingdong.ddingdongBE.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@Profile("prod")
@Slf4j
public class PromtailDockerComposeRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Running PromtailDockerComposeRunner");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("docker-compose", "-f", "/var/app/current/promtail/promtail-docker-compose.yml", "up", "-d");

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.warn(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            log.info("Promtail started successfully using Docker Compose.");
        } else {
            log.error("Failed to start Promtail. Exit code: " + exitCode);
        }
    }
}
