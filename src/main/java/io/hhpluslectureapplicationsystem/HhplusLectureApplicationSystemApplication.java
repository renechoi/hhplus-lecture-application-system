package io.hhpluslectureapplicationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HhplusLectureApplicationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhplusLectureApplicationSystemApplication.class, args);
	}

}
