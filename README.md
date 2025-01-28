# shared_library_repo
Demo shared_library
### jenkinsfile #######
@Library("shared-library") _

node {
    // Define the build number with fallback if not set
    //def buildNumber = "${env.BUILD_NUMBER ?: 'local-build'}"
    //echo "Build Number: ${buildNumber}"
    def buildNumber = env.BUILD_NUMBER ?: 'local-build'
    echo "Build Number: ${buildNumber}"
    
    // Call mavenBuild function with parameters
    //mavenBuild {
    //    branch = 'main'
    //    repoUrl = 'https://github.com/tkp1999/ansible_zone.git'
    //    buildNumber = buildNumber
    //}
    mavenBuild {
        branch = 'main'
        repoUrl = 'https://github.com/tkp1999/ansible_zone.git'
        jdkVersion = "JDK17" 
        mavenVersion = "Maven3.9"
        dockerRegistry = "my-docker-registry.com"
        imageName = "my-app"
        //buildNumber = buildNumber
    }
}

############

######working jenkinsfile#######

@Library("shared-library") _

node {
    // Define the build number with fallback if not set
    //def buildNumber = "${env.BUILD_NUMBER ?: 'local-build'}"
    //echo "Build Number: ${buildNumber}"
    def buildNumber = env.BUILD_NUMBER ?: 'local-build'
    echo "Build Number: ${buildNumber}"
    
    // Call mavenBuild function with parameters
    //mavenBuild {
    //    branch = 'main'
    //    repoUrl = 'https://github.com/tkp1999/ansible_zone.git'
    //    buildNumber = buildNumber
    //}
    mavenBuild {
        branch = 'master'
        repoUrl = 'https://github.com/tkp1999/spring-boot-mongo-docker.git'
        jdkVersion = "JDK17" 
        mavenVersion = "Maven3.9"
        dockerRegistry = "tkp1999"
        imageName = "my-app-shared_library"
        dockerCredentialsId = "docker-credentials"
        //buildNumber = buildNumber
    }
}
