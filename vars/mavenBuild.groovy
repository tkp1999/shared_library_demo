import com.tkp1999.utils.*

import com.tkp1999.utils.GitUtils

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



