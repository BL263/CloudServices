package it.group16.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableEurekaClient
class GatewayApplication{

    @Bean
    fun routes(builder:RouteLocatorBuilder):RouteLocator{
        return builder
            .routes()
            .route("svcCatalog"){
                it->it.path(false,"/catalog/**")
                .filters{ f -> f.rewritePath("catalog","")}
                .uri("lb://CatalogService")
            }
            .route("svcOrder"){
                    it->it.path(false,"/orders/**")
                .filters{ f -> f.rewritePath("orders","")}
                .uri("lb://OrderService")
            }
            .route("svcWallet"){
                    it->it.path(false,"/wallets/**")
                .filters{ f -> f.rewritePath("wallets","")}
                .uri("lb://WalletService")
            }
            .route("svcWarehouse"){
                    it->it.path(false,"/warehouses/**")
                .filters{ f -> f.rewritePath("warehouses","")}
                .uri("lb://WarehouseService")
            }


            .build()
    }

}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}
