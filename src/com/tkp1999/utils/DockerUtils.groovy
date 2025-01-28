package com.tkp1999.utils
/*
class DockerUtils {
    def script

    DockerUtils(script) {
        this.script = script
    }

    def dockerbuild_and_push(String dockerRegistry, String imageName, String buildNumber) {
        script.echo "Building Docker image: ${dockerRegistry}/${imageName}:${buildNumber}"

        script.sh """
            //build with build number
            docker build -t ${dockerRegistry}/${imageName}:${buildNumber} .
            //tag the latest image built with tag : latest
            docker tag ${dockerRegistry}/${imageName}:${buildNumber} ${dockerRegistry}/${imageName}:latest
            //push images with botj the tags
            docker push ${dockerRegistry}/${imageName}:${buildNumber}
            docker push ${dockerRegistry}/${imageName}:latest
        """

        script.echo "Docker image pushed successfully to ${dockerRegistry}"
    }
}
*/


class DockerUtils {
    def script // Jenkins Pipeline context

    DockerUtils(script) {
        this.script = script
    }

    def dockerBuildAndPush(Map config) {
        def registryUrl = config.registryUrl ?: 'https://index.docker.io/v1/' // Default to Docker Hub if not provided
        def imageName = config.imageName ?: 'default-image-sharedlibrary'
        def tag = config.buildNumber ?: 'latest' // Default to latest if buildNumber is not passed

        script.echo "Building Docker image: ${registryUrl}/${imageName}:${tag}"

        script.withCredentials([script.usernamePassword(credentialsId: config.dockerCredentialsId, 
                                                        usernameVariable: 'DOCKER_USER', 
                                                        passwordVariable: 'DOCKER_PASS')]) {
            script.sh """
                echo "Logging in to Docker registry..."
                //echo \$DOCKER_PASS | docker login ${registryUrl} -u \$DOCKER_USER --password-stdin
                echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                docker build -t ${registryUrl}/${imageName}:${tag} .
                docker push ${registryUrl}/${imageName}:${tag}
                docker logout ${registryUrl}
            """
        }
        
        script.echo "Docker image pushed: ${registryUrl}/${imageName}:${tag}"
    }
}
