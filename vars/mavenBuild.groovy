import com.tkp1999.utils.GitUtils
def call(Map config) {
    node(config.nodeLabel) {
        stage('Checkout') {
            script {
                def gitUtils = new com.tkp1999.utils.GitUtils()
                gitUtils.checkoutRepo(
                    url: config.repoUrl,
                    branch: config.branch
                )
            }
        }
        /*
        stage('Build') {
            script {
                sh "mvn clean install"
            }
        }
        stage('Tag and Push') {
            script {
                def gitUtils = new com.company.utils.GitUtils()
                gitUtils.createAndPushTag(config.repoUrl, config.buildNumber)
            }
        }
        */
    }
}
