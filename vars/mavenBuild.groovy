import com.tkp1999.utils.*

import com.tkp1999.utils.GitUtils

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
    def buildNumber = System.getenv('BUILD_NUMBER') ?: 'local-build'

    // Now config contains the parameters passed from Jenkinsfile
    echo "Parameters received in mavenBuild:"
    echo "Branch: ${config.branch}"
    echo "Repo URL: ${config.repoUrl}"
    echo "Build Number: ${config.buildNumber}"

    // Validation for parameters
    if (!config.repoUrl?.trim()) {
        error "Git repository URL is empty. Checkout requires a valid repository URL."
    }
    if (!config.branch?.trim()) {
        error "Branch is empty. Please specify a valid branch."
    }

    // Proceed with build process
    node {
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
            // Maven build command or any other required commands can be added here
            //sh "mvn clean install -DskipTests -DbuildNumber=${config.buildNumber}"
        }
        
        // Add any other stages (e.g., test, deploy) as required.
    }
}

