pipeline {
    agent any

    environment {
        MYSQL_PASSWORD = credentials('NEXCHANGE_USERSERVICE_MYSQL_PASSWORD')
        JWT_SECRET = credentials('NEXCHANGE_USERSERVICE_JWT_SECRET')
        DOCKER_CREDENTIALS = 'docker_hub_credentials'
        DOCKER_IMAGE = "jmx7139/nexchange-userservice"
        SONAR_PROJECT_KEY = 'MinXuanJ_NexChange-UserService'
        SONAR_ORGANIZATION_KEY = 'nexchange'
        SONAR_HOST_URL = 'https://sonarcloud.io'
//        SONAR_LOGIN = '6850d62da33742ee455c430f10fabdda0f4803c2'
    }

    parameters {
        string(name: 'JAR_NAME', defaultValue: 'NexChange-UserService', description: 'The name of the JAR file')
    }

    stages {

        stage('Start Docker Services') {
            steps {
                script {
                    sh 'docker-compose up -d'
                }
            }
        }

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
        stage('Cleanup Docker Images') {
            steps {
                script {
                    // Corrected command to keep only the last 2 images
                    sh '''
                docker images --format "{{.ID}} {{.Repository}} {{.Tag}} {{.CreatedAt}}" | grep $DOCKER_IMAGE | sort -r | awk 'NR>2 {print $1}' | xargs -r docker rmi
            '''
                    sh "docker images"
                }
            }
        }

        stage('Stop Docker Services') {
            steps {
                script {
                    sh 'docker-compose down'
                }
            }
        }

        stage('Deploy Docker Secret') {
            steps {
                script {
                    def namespace = "default"
                    def secretName = "docker-hub-secret"

                    // 检查 Secret 是否存在
                    def secretExists = sh(
                            script: "kubectl get secret ${secretName} -n ${namespace} --ignore-not-found",
                            returnStatus: true
                    ) == 0

                    if (!secretExists) {
                        echo "Docker Hub Secret 不存在，正在创建..."
                        // 使用 Jenkins 的凭据插件获取 DockerHub 用户名和密码
                        withCredentials([usernamePassword(credentialsId: 'docker_hub_credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            // 创建 Secret，不包含 Docker server 和 email
                            sh """
                        kubectl create secret docker-registry ${secretName} \
                        --docker-username=${DOCKER_USERNAME} \
                        --docker-password=${DOCKER_PASSWORD} \
                        -n ${namespace}
                    """
                        }
                        echo "Docker Hub Secret 创建成功。"
                    } else {
                        echo "Docker Hub Secret 已存在，跳过创建步骤。"
                    }
                }
            }
        }

        stage('Deploy Zookeeper') {
            steps {
                script {
                    sh 'kubectl apply -f zookeeper-deployment.yaml'
                }
            }
        }

        stage('Deploy Kafka') {
            steps {
                script {
                    sh 'kubectl apply -f kafka-deployment.yaml'
                }
            }
        }

        stage('Deploy MySQL') {
            steps {
                script {
                    sh 'kubectl apply -f mysql-deployment.yaml'
                }
            }
        }

        stage('Deploy Redis') {
            steps {
                script {
                    sh 'kubectl apply -f redis-deployment.yaml'
                }
            }
        }

        stage('Deploy User Service') {
            steps {
                script {
                    sh 'kubectl apply -f deployment.yaml'
                }
            }
        }
    } 
} 
