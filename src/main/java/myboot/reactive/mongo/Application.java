package myboot.reactive.mongo;

import myboot.reactive.mongo.entity.Product;
import myboot.reactive.mongo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository _proProductRepository) {
		return args -> {
			Mono<Void> voidMono = _proProductRepository.deleteAll();	//시작시 DB전부삭제
			voidMono.doOnSuccess(x -> System.out.println("Delete Ok"))
					.subscribe();

			//초기데이터 등록
			Flux<Product> productFlux = Flux.just(
					new Product(null, "Big Latte", 10, 2.99),
					new Product(null, "Big Toast", 10, 2.49),
					new Product(null, "Green Tea", 15, 1.99)
			)
			.flatMap(_proProductRepository::save);	//저장시 사용

			productFlux.thenMany(_proProductRepository.findAll())		//등록 후 조회를 바로 처리하는 기능
					.subscribe(System.out::println);
		};
	}

}
