package it.group16.catalogservice

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@EnableEurekaClient
@RestController
class CatalogServiceApplication {

    @Bean
    fun defaultCustomizer(): Customizer<ReactiveResilience4JCircuitBreakerFactory> {
        return Customizer { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.ofDefaults())
                    .build()
            }
        }
    }

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator {
        return builder
            .routes()
            .route("svcCatalog") { it ->
                it.path(true, "/catalog/**")
                    .filters { f ->
                        f.circuitBreaker { it ->
                            it.setFallbackUri("forward:/catalogFailure")
                        }
                        f.rewritePath("catalog", "/")
                    }
                    .uri("lb://CatalogService")
            }
            .route("svcOrder") { it ->
                it.path(true, "/orders/**")
                    .filters { f ->
                        f.circuitBreaker { it ->
                            it.setFallbackUri("forward:/orderFailure")
                        }
                        f.rewritePath("orders", "/")
                    }
                    .uri("lb://OrderService")
            }
            .route("svcWallet") { it ->
                it.path(false, "/wallets/**")
                    .filters { f ->
                        f.circuitBreaker { it ->
                            it.setFallbackUri("forward:/walletFailure")
                        }
                        f.rewritePath("wallets", "/")
                    }
                    .uri("lb://WalletService")
            }
            .route("svcWarehouse") { it ->
                it.path(false, "/warehouses/**")
                    .filters { f ->
                        f.circuitBreaker { it ->
                            it.setFallbackUri("forward:/warehouseFailure")
                        }
                        f.rewritePath("warehouses", "/")
                    }
                    .uri("lb://WarehouseService")
            }


            .build()
    }

    @GetMapping("/catalogFailure")
    fun catalogFailure(): String {
        return "Catalog Service is unavailable please try again later"
    }

    @GetMapping("/orderFailure")
    fun orderFailure(): String {
        return "Order Service is unavailable please try again later"
    }
    @GetMapping("/walletFailure")
    fun walletFailure(): String {
        return "Wallet Service is unavailable please try again later"
    }
    @GetMapping("/warehouseFailure")
    fun warehouseFailure(): String {
        return "Warehouse Service is unavailable please try again later"
    }
}

fun main(args: Array<String>) {
    runApplication<CatalogServiceApplication>(*args)
}
