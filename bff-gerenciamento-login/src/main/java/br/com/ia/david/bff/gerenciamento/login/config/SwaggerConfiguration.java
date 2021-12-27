package br.com.ia.david.bff.gerenciamento.login.config;

import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;
import static org.apache.commons.lang3.ArrayUtils.removeElements;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_NAME = "name";

    @Autowired
    private ApiDefinition apiDefinition;

    @Bean
    public Docket customImplementation(
            @Value("${springfox.documentation.info.title}") String title,
            @Value("${springfox.documentation.info.version}") String version,
            @Value("${springfox.documentation.info.description}") String description,
            @Value("${springfox.documentation.base-package}") String basePackage) {

        final Tag[] tags = createTags();
        final Tag firstTag = tags[0];

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(title, description, version))
                .directModelSubstitute(LocalDate.class, String.class)
                .useDefaultResponseMessages(false)
                .tags(firstTag, nullToEmpty(removeElements(tags, firstTag), Tag[].class))
                .select()
                    .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build();
    }

    ApiInfo apiInfo(final String title, final String description, final String version) {

        return new ApiInfoBuilder()
            .title(title)
            .description(description)
            .version(version)
            .build();
    }

    Tag[] createTags() {

        final Set<Tag> tagSet = apiDefinition.getApis()
            .stream()
            .filter(definition -> definition.containsKey(TAG_NAME))
            .filter(definition -> definition.containsKey(TAG_DESCRIPTION))
            .map(definition -> new Tag(definition.get(TAG_NAME), definition.get(TAG_DESCRIPTION)))
            .collect(Collectors.toSet());

        final Tag[] tags = new Tag[tagSet.size()];
        tagSet.toArray(tags);
        return tags;
    }

    @Component
    @ConfigurationProperties("springfox.documentation")
    @Getter
    @Setter
    static final class ApiDefinition {

        private List<Map<String, String>> apis;
    }

}