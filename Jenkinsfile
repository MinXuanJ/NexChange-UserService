pipeline {
    agent any

    environment {
        MYSQL_PASSWORD = credentials('NEXCHANGE_USERSERVICE_MYSQL_PASSWORD')
        JWT_SECRET = credentials('NEXCHANGE_USERSERVICE_JWT_SECRET')
        DOCKER_CREDENTIALS = 'docker_hub_credentials'
        DOCKER_IMAGE = "jmx7139/nexchange-userservice"
        SONAR_PROJECT_KEY = 'MinXuanJ_NexChange-UserService'
        SONAR_ORGANIZATION_KEY = 'NexChange'
        SONAR_HOST_URL = 'https://sonarcloud.io'
//        SONAR_LOGIN = '6850d62da33742ee455c430f10fabdda0f4803c2'
    }

    parameters {
        string(name: 'JAR_NAME', defaultValue: 'NexChange-UserService', description: 'The name of the JAR file')
    }

    stages {
        stage('Build and Package') {
            steps {
                script {
                    sh "mvn clean package -DskipTests"
                }
            }
        }

//        stage('Unit Test') {
//            steps {
//                script {
//                    sh "mvn test"
//                }
//                junit '**/target/surefire-reports/*.xml'
//             }
//        }

        stage('Static Code Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'NEXCHANGE_SONARCLOUD_USERSERVICE_TOKEN', variable: 'SONAR_LOGIN')]) {
                        withSonarQubeEnv('SonarQube') {
                            sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                    -Dsonar.organization=$SONAR_ORGANIZATION_KEY \
                    -Dsonar.host.url=$SONAR_HOST_URL \
                    -Dsonar.login=$SONAR_LOGIN  \
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t $DOCKER_IMAGE ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker_hub_credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh "docker login -u $USERNAME -p $PASSWORD"
                        sh "docker tag $DOCKER_IMAGE $DOCKER_IMAGE:latest"
                        sh "docker push $DOCKER_IMAGE:latest"
                        sh "docker images" 
                    }
                }
            }
        }

        stage('Verify Push') {
            steps {
                script {
                    sh "docker pull $DOCKER_IMAGE:latest"
                    sh "docker images"
                }
            }
        }
    } 
} 
