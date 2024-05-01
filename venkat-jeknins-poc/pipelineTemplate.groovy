def call(appName, appCheckoutUrl, credentialsId) {
    pipeline {
        agent any

        stages {
            stage("Build ${appName}") {
                steps {
                    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        git branch: 'master', url: appCheckoutUrl, credentialsId: credentialsId
                        sh "mvn clean package"
                    }
                }
            }

            stage("Test ${appName}") {
                steps {
                    sh "mvn test"
                }
            }

            stage("Deploy ${appName}") {
                steps {
                    sh "echo 'Deploying ${appName}'"
                }
            }
        }

        post {
            success {
                echo "Build, test, and deploy successful for ${appName}!"
            }
            failure {
                echo "Build, test, or deploy failed for ${appName}!"
            }
        }
    }
}