package com.tkp1999.utils

class GitUtils {
    def checkoutRepo(Map args) {
        def repoUrl = args.url
        def branch = args.branch ?: 'main'

        println "Checking out repository: ${repoUrl} on branch: ${branch}"
        checkout([$class: 'GitSCM',
                  branches: [[name: "*/${branch}"]],
                  userRemoteConfigs: [[url: repoUrl]]
        ])
    }

    def createAndPushTag(String repoUrl, String buildNumber) {
        def tagName = "build-${buildNumber}"
        println "Creating tag: ${tagName}"
        sh "git tag ${tagName}"
        sh "git push ${repoUrl} ${tagName}"
    }
}
