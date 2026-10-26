package com.smarthire.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Smart Hire — AI-Powered Job Portal API")
                        .version("1.0.0")
                        .description("Production-style Spring Boot 3 & React REST API with JWT Security, Apache Tika Resume Parsing, OpenAI & Gemini LLM Integration, and In-App Notifications.")
                        .contact(new Contact().name("Smart Hire Engineering").email("support@smarthire.ai").url("https://smarthire.ai"))
                        .license(new License().name("Apache 2.0").url("https://spring.io/projects/spring-boot")))
                .servers(List.of(
                        new Server().url("/api").description("Default API Gateway Context")
                ))
                .tags(List.of(
                        new Tag().name("Authentication").description("Registration, login, and JWT token issuance"),
                        new Tag().name("Jobs").description("Public job searching, multi-filter queries, and job details"),
                        new Tag().name("Applications").description("Application submission, candidate tracking, and status pipeline"),
                        new Tag().name("Resumes").description("PDF/DOCX resume upload, text extraction, preview, and download"),
                        new Tag().name("AI Analysis").description("AI resume scoring, interview questions generation, and candidate coaching"),
                        new Tag().name("Companies").description("Company directory, profiles, and job aggregations"),
                        new Tag().name("Interviews").description("Interview scheduling, status management, and candidate meetings"),
                        new Tag().name("Notifications").description("In-app real-time notification center"),
                        new Tag().name("Recruiter").description("Recruiter job management, candidate ranking, and applicant pipeline"),
                        new Tag().name("Admin").description("Platform statistics and user moderation"),
                        new Tag().name("Users").description("Candidate and recruiter profile management")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT Bearer token obtained from /api/auth/login")));
    }
}
