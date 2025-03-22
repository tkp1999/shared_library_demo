package com.tkp1999.utils

def scanDockerImage(String trivyImage, String targetImage, String reportFormat) {
    script {
        def outputFile = "report.${reportFormat}"
        // pull docker image
        sh "docker pull ${trivyImage}"
        // Validate report format
        def validFormats = ["json", "html", "spdx", "csv", "table", "template", "sarif", "cyclonedx", "spdx-json", "github", "cosign-vuln"]
        if (!validFormats.contains(reportFormat)) {
            error "Invalid format! Supported formats: ${validFormats.join(', ')}"
        }

        // Run Trivy scan
        sh """
            docker run --rm -v /var/run/docker.sock:/var/run/docker.sock ${trivyImage} image --format ${reportFormat} --output ${outputFile} ${targetImage}
            echo "Scan completed. Report saved as ${outputFile}."
        """

        // Remove Trivy Docker image after scan
        sh "docker rmi ${trivyImage} || true"
    }
}
