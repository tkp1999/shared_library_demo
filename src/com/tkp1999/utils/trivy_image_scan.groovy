package com.tkp1999.utils

class trivy_image_scan {
    def script

    trivy_image_scan(script) {
        this.script = script
    }

    def scanDockerImage(String trivyImage, String targetImage, String reportFormat) {
        script.sh "docker pull ${trivyImage}"
        //def outputFile = "report.${reportFormat}"
        def outputFile = "/reports/report.${reportFormat}" 
        def validFormats = ["json", "html", "spdx", "csv", "table", "template", "sarif", "cyclonedx", "spdx-json", "github", "cosign-vuln"]
        
        if (!validFormats.contains(reportFormat)) {
            script.error "Invalid format! Supported formats: ${validFormats.join(', ')}"
        }

        script.sh """
            docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/reports ${trivyImage} image --format ${reportFormat} --output ${outputFile} ${targetImage}
            echo "Scan completed. Report saved as ${outputFile}."
            pwd
        """
        
        script.sh "docker rmi ${trivyImage} || true"
    }
}
