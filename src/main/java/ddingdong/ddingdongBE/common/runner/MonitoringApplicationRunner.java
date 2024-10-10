package ddingdong.ddingdongBE.common.runner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@Slf4j
public class MonitoringApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Running Promtail & Node Exporter");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(
            "docker-compose",
            "-f", "/var/app/current/promtail/promtail-docker-compose.yml",
            "-f", "/var/app/current/nodeexporter/node-exporter-compose.yml",
            "up",
            "-d"
        );

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
            log.info("promtail is tracking info level log");
            log.warn("promtail is tracking warn level log");
            log.error("promtail is tracking error level log");
        } else {
            log.error("Failed to start Promtail. Exit code: {}", exitCode);
        }
    }
}
