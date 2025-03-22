import com.tkp1999.utils.*

import com.tkp1999.utils.GitUtils
import com.tkp1999.utils.DockerUtils

/*
def call(Map config) {
    node(config.nodeLabel) {
        stage('Checkout') {
            script {
                // Pass the pipeline script context (this) to GitUtils
                def gitUtils = new GitUtils(this)
                gitUtils.checkoutRepo(
                    url: config.repoUrl,
                    branch: config.branch
                )
            }
        }
    }
}
*/
/*
def call(Closure body) {
    // Use the Closure's delegate to extract the parameters
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    echo "Parameters received in mavenBuild:"
    echo "Branch: ${config.branch}"
    echo "Repo URL: ${config.repoUrl}"
    echo "Build Number: ${config.buildNumber}"
    node {
        stage('Checkout') {
            script {
                // Initialize GitUtils and perform checkout
                def gitUtils = new GitUtils(this)
                gitUtils.checkoutRepo(
                    url: config.repoUrl,
                    branch: config.branch
                )
            }
        }
        */
/*
        stage('Build') {
            script {
                // Example: Print the build number (or replace with actual build commands)
                echo "Building project with build number: ${config.buildNumber}"
            }
        }
        */
        /*
    }
}

*/

def call(body) {
    def config = [:] // Initialize a Map to store parameters
    
    // Execute the closure to gather parameters from Jenkinsfile
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    //added line to get the buildNumber
    //def buildNumber = System.getenv('BUILD_NUMBER') ?: 'local-build'
    //echo "Build Number: ${buildNumber}"

    if (!config.buildNumber) {
        config.buildNumber = env.BUILD_NUMBER ?: 'local-build'
    }

    // Now config contains the parameters passed from Jenkinsfile
    echo "Parameters received in mavenBuild:"
    echo "Branch: ${config.branch}"
    echo "Repo URL: ${config.repoUrl}"
    echo "Build Number: ${config.buildNumber}"

    // added later
   
    echo "JDK Version: ${config.jdkVersion}"
    echo "Maven Version: ${config.mavenVersion}"
    echo "Docker Registry: ${config.dockerRegistry}"
    echo "Image Name: ${config.imageName}"
    echo "docker credentialsid: ${config.dockerCredentialsId}"
    echo "trivy image: ${config.trivyImage}"
    echo "targetImage: ${config.targetImage}"
    echo "reportFormat: ${config.reportFormat}"

    //give path of custom workspace directory

    //def customWorkspace = "/var/lib/jenkins/workspace/${config.imageName}-${config.buildNumber}"
    def customWorkspace = "/var/lib/jenkins/workspace/${config.imageName}"

    echo "Using custom workspace: ${customWorkspace}"

    // Apply build retention policy(keep only latest 10 builds)
    properties([
        buildDiscarder(logRotator(numToKeepStr: '10')) // Keep only the latest 10 builds
    ])

    // Validation for parameters
    if (!config.repoUrl?.trim()) {
        error "Git repository URL is empty. Checkout requires a valid repository URL."
    }
    if (!config.branch?.trim()) {
        error "Branch is empty. Please specify a valid branch."
    }

    

    // Proceed with build process
    node {
        ws(customWorkspace) {
        //added on 28Jan
        // use java and maven that is configured in tools section in jenkins gui
        env.JAVA_HOME = tool config.jdkVersion
        env.MAVEN_HOME = tool config.mavenVersion
        env.PATH = "${env.JAVA_HOME}/bin:${env.MAVEN_HOME}/bin:${env.PATH}"

        stage('Clean Workspace') {
            deleteDir() // Deletes the workspace before new build
        }
        // Checkout stage
        stage('Checkout') {
            script {
                def gitUtils = new com.tkp1999.utils.GitUtils(this)
                gitUtils.checkoutRepo(
                    url: config.repoUrl,
                    branch: config.branch
                )
            }
        }

        // Build stage
        stage('Build') {
            
            echo "Building project with build number: ${config.buildNumber}"
            sh "${env.MAVEN_HOME}/bin/mvn clean install -DskipTests -DbuildNumber=${config.buildNumber}"
            // Maven build command or any other required commands can be added here
            //sh "mvn clean install -DskipTests -DbuildNumber=${config.buildNumber}"
        }
        //to build docker image and push to dockerhub
        stage('Docker Build & Push') {
            script {
                //def dockerUtils = new DockerUtils(this)
                //dockerUtils.dockerbuild_and_push(config.dockerRegistry, config.imageName, config.buildNumber)
                    def dockerUtils = new DockerUtils(this)
                    dockerUtils.dockerBuildAndPush(
                    registryUrl: config.registryUrl,
                    imageName: config.imageName,
                    buildNumber: config.buildNumber,
                    dockerCredentialsId: config.dockerCredentialsId
                )
            }
        }

        // stage to scan trivy image
        stage('Trivy Scan') {
                script {
                    def trivyScanner = new com.tkp1999.utils.trivy_image_scan()
                    trivyScanner.scanDockerImage(
                        trivyImage: config.trivyImage,
                        targetImage: config.targetImage,
                        reportFormat: config.reportFormat
                    )
                }
            }
        //stage to keep latest 10 builds and delete older build results
        
        stage('keeping only the latest 10 folders in jenkins workspace') {
                script {
                    def buildDir = "/var/lib/jenkins/workspace/${config.imageName}-*"
                    echo "Cleaning up old builds, keeping only the latest 10..."

                    sh """
                        ls -td ${buildDir} | tail -n +11 | xargs rm -rf || true
                    """
                }
            }
            
      } 
        
    }
}