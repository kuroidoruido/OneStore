package onestore.config;

import javax.inject.Singleton;

import io.micronaut.context.annotation.Value;

@Singleton
public class AppConfig {
    public @Value("${jsondb.path:undefined-path}") String dbPath;
}