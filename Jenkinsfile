pipeline {
    agent any

    stages {
        stage('Prepare') {
            steps {
                sh 'git pull origin master'
                sh 'cp /opt/RestAPI-Cinema/.env .'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker-compose build'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }

        stage('Health Check') {
            steps {
                script {
                    def maxRetries = 30
                    def retryCount = 0
                    def appPort = sh(script: "grep APP_PORT .env | cut -d'=' -f2", returnStdout: true).trim()
                    if (!appPort) {
                        appPort = '8081'
                    }
                    def healthUrl = "http://localhost:${appPort}/actuator/health"
                    
                    while (retryCount < maxRetries) {
                        retryCount++
                        echo "Health check attempt ${retryCount}/${maxRetries}..."
                        sleep(time: 5, unit: 'SECONDS')
                        
                        try {
                            def response = sh(script: "curl -sf ${healthUrl}", returnStdout: true).trim()
                            if (response.contains('UP')) {
                                echo "Health check passed: ${response}"
                                return
                            }
                        } catch (Exception e) {
                            echo "Waiting for app to start..."
                        }
                    }
                    
                    error("Health check failed after ${maxRetries} attempts")
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh 'docker image prune -f'
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
