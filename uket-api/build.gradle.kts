dependencies {
    implementation(project(":uket-api:infra"))
    implementation(project(":uket-api:gateway"))
}

allprojects {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}
