package KafkaEmbeddedSpring.KafkaEmbeddedSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaEmbeddedSpringApplication {

	
	public class PrintEvenCharacters {
   	 public static void main(String[] args) {
	    SpringApplication.run(KafkaEmbeddedSpringApplication.class, args);
        String input = "I love my country";
        printEvenCharacters(input);
   	 }

   	 private static void printEvenCharacters(String input) {
        for (int i = 0; i < input.length(); i += 2) {
            char character = input.charAt(i);
            if (character != ' ') {
                System.out.print(character);
            }
        }
    }
}

}
