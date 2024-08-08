package com.prototyne.apiPayload.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI PrototyneAPI() {
        Info info = new Info()
                .title("Prototyne API")
                .description("""
						### Prototyne REST API 명세 문서입니다.
						- #### 자물쇠 버튼으로 `Authorization` 헤더 설정이 가능합니다.
						- #### API 사용 관련 문의는 **디스코드**의 front-end-info 채널에 남겨주세요.
						- #### API 관련 버그 및 수정 소요, 제안 등이 있다면 <a href="https://github.com/UMC-PROTOTYNE/PROTOTYNE_BE" target="_blank">깃허브 이슈</a>에 등록해주시면 감사하겠습니다.
						""")
                .version("v0.1");
        return new OpenAPI()
                .info(info)
                .components(authComponent());
    }
    private static Components authComponent() {
        return new Components().addSecuritySchemes("session-token",
                new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization"));
    }
}