package com.mongodb.starter;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.swagger.v3.oas.models.OpenAPI;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.servers.Server;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoDBConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Autowired
    private Environment env;

    @Value("${openapi.server.url}")
    private String serverUrl;

    @Bean
    public MongoClient mongoClient() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        String mongoUri = env.getProperty("ATLASSEARCHURI", connectionString);
        return MongoClients.create(MongoClientSettings.builder()
                                                      .applyConnectionString(new ConnectionString(mongoUri))
                                                      .codecRegistry(codecRegistry)
                                                      .build());
    }

    @Bean
    public OpenAPI customOpenAPI() {
        // Create a new server with your custom domain and path
        Server customServer = new Server();
    
        customServer.setUrl(serverUrl);
        customServer.setDescription("Custom Domain");

        // Add the server to the OpenAPI definition
        return new OpenAPI().addServersItem(customServer);
    }
}
