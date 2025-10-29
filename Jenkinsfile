pipeline {
    agent any
    
    tools {
        maven 'Maven 3.8.7' // Pastikan nama ini sesuai dengan konfigurasi Maven di Jenkins Global Tool Configuration
        jdk 'JDK-21' // Pastikan nama ini sesuai dengan konfigurasi JDK di Jenkins Global Tool Configuration
    }
    
    environment {
        // Docker Registry Configuration
        DOCKER_REGISTRY = 'docker.io' // Ganti dengan registry Anda (docker.io, gcr.io, etc.)
        DOCKER_IMAGE_NAME = 'h4lfbaked/springboot-test-3n192jdkska29831'
        DOCKER_CREDENTIALS_ID = '7571965c-f489-4165-90a5-400dc497c05e' // ID credentials di Jenkins
        
        // Application Configuration
        APP_NAME = 'springboot-test'
        APP_VERSION = "1.0.0"
    }
    
    stages {
        stage('1. Checkout') {
            steps {
                echo '=== Stage 1: Checking out code from repository ==='
                checkout scm
                script {
                    env.GIT_COMMIT_MSG = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    env.GIT_AUTHOR = sh(script: 'git log -1 --pretty=%an', returnStdout: true).trim()
                    echo "Commit: ${env.GIT_COMMIT_MSG}"
                    echo "Author: ${env.GIT_AUTHOR}"
                }
            }
        }
        
        stage('2. Build & Test') {
            steps {
                echo '=== Stage 2: Building and Testing Application ==='
                sh 'mvn clean test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('3. Package') {
            steps {
                echo '=== Stage 3: Packaging Application ==='
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                    echo "JAR artifact created successfully"
                }
            }
        }
        
        stage('4. Docker Build & Push') {
            steps {
                echo '=== Stage 4: Building and Pushing Docker Image ==='
                script {
                    def dockerImage = docker.build("${DOCKER_IMAGE_NAME}:${APP_VERSION}")
                    
                    docker.withRegistry("https://${DOCKER_REGISTRY}", "${DOCKER_CREDENTIALS_ID}") {
                        dockerImage.push()
                        dockerImage.push('latest')
                    }
                    
                    echo "Docker image pushed: ${DOCKER_IMAGE_NAME}:${APP_VERSION}"
                }
            }
        }
        
        stage('5. Deploy') {
            steps {
                echo '=== Stage 5: Deploying Application ==='
                script {
                    def port = '8080'
                    def envSuffix = 'prod'
                    
                    if (env.BRANCH_NAME == 'develop') {
                        port = '8081'
                        envSuffix = 'dev'
                        echo "Deploying to Development environment..."
                    } else if (env.BRANCH_NAME == 'staging') {
                        port = '8082'
                        envSuffix = 'staging'
                        echo "Deploying to Staging environment..."
                    } else if (env.BRANCH_NAME == 'main') {
                        echo "Deploying to Production environment..."
                        input message: 'Deploy to Production?', ok: 'Deploy'
                    }
                    
                    sh """
                        docker stop ${APP_NAME}-${envSuffix} || true
                        docker rm ${APP_NAME}-${envSuffix} || true
                    """
                    
                    sh """
                        docker run -d \
                        --name ${APP_NAME}-${envSuffix} \
                        -p ${port}:8080 \
                        --restart unless-stopped \
                        ${DOCKER_IMAGE_NAME}:${APP_VERSION}
                    """
                    
                    echo "Waiting for application to start..."
                    sleep(time: 30, unit: 'SECONDS')
                    
                    sh """
                        curl -f http://localhost:${port}/api/health || exit 1
                    """
                    
                    echo "Application deployed successfully on port ${port}"
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            echo "Application Version: ${APP_VERSION}"
            echo "Docker Image: ${DOCKER_IMAGE_NAME}:${APP_VERSION}"
        }
        
        failure {
            echo 'Pipeline failed!'
            echo "Please check the logs for details."
        }
        
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
            sh 'docker image prune -f || true'
        }
    }
}