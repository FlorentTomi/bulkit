plugins {
    idea
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "8.7"
    distributionType = Wrapper.DistributionType.ALL

}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}