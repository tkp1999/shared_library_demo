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
def call(Closure body) {
    // Use the Closure's delegate to extract the parameters
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

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

        stage('Build') {
            script {
                // Example: Print the build number (or replace with actual build commands)
                echo "Building project with build number: ${config.buildNumber}"
            }
        }
    }
}


