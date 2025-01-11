
package com.tkp1999.utils

class GitUtils {
    def script // To store the pipeline script context

    // Constructor to accept the pipeline script context
    GitUtils(script) {
        this.script = script
    }

    def checkoutRepo(Map args) {
        def repoUrl = args.url
        def branch = args.branch ?: 'main'

        script.println "Checking out repository: ${repoUrl} on branch: ${branch}"
        script.checkout([$class: 'GitSCM',
                         branches: [[name: "*/${branch}"]],
                         userRemoteConfigs: [[url: repoUrl]]
        ])
    }
}


