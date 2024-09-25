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

        // Docker Compose 명령어를 실행할 ProcessBuilder 설정
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("docker-compose", "-f", "/var/app/current/promtail/promtail-docker-compose.yml", "up", "-d");

        // ProcessBuilder로 프로세스 실행
        Process process = processBuilder.start();

        // 프로세스 출력 로그 확인
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line); // Docker Compose 실행 결과를 로그로 출력
            }
        }

        // 에러 로그 확인
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.error(line); // Docker Compose 실행 중 발생한 에러를 로그로 출력
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
