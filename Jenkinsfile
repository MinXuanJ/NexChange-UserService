pipeline {
    agent any

    environment {
        MYSQL_PASSWORD = credentials('NEXCHANGE_USERSERVICE_MYSQL_PASSWORD') // stored in Jenkins Server
        JWT_SECRET = credentials('NEXCHANGE_USERSERVICE_JWT_SECRET') // stored in Jenkins Server
        DOCKER_CREDENTIALS = 'docker_hub_credentials' // stored in Jenkins Server
        DOCKER_IMAGE = "jmx7139/nexchange-userservice"
        SONAR_ORGANIZATION_KEY = 'nexchange' // Sonar Cloud Organization Key
        SONAR_PROJECT_KEY = 'MinXuanJ_NexChange-UserService' // Sonar Cloud Project Key
        SONAR_HOST_URL = 'https://sonarcloud.io' // Sonar Cloud URL
//        SONAR_LOGIN = '6850d62da33742ee455c430f10fabdda0f4803c2'
//      KUBECONFIG = '/var/lib/jenkins/.kube/config' // Jenkins Server KubeConfig
    }

    parameters {
        string(name: 'JAR_NAME', defaultValue: 'NexChange-UserService', description: 'The name of the JAR file')
    }

    stages {
        stage('Checkout'){
            steps{
                checkout scm
            }
        }

//        stage('Start Docker Services') {
//            steps {
//                script {
//                    sh 'docker-compose up -d'
//                    sh 'docker-compose ps'
//                }
//            }
//        }
//
//        stage('Unit Test') {
//            steps {
//                script {
//                    sh "docker-compose ps"
//                    sh "mvn test"
//                }
//                junit '**/target/surefire-reports/*.xml'
//             }
//        }

        stage('Build and Package') {
            steps {
                script {
                    sh "mvn clean package -DskipTests"
                }
            }
        }

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
                    // 删除超过2个的镜像，保留最新的
                    sh '''
                docker images --format "{{.ID}} {{.Repository}} {{.Tag}} {{.CreatedAt}}" | grep $DOCKER_IMAGE | sort -r | awk 'NR>2 {print $1}' | xargs -r docker rmi
            '''
                    sh "docker images"
                }
            }
        }

//
//        stage('Stop Docker Services') {
//            steps {
//                script {
//                    sh 'docker-compose down'
//                }
//            }
//        }

//        stage('Deploy Docker Secret') {
//            steps {
//                script {
//                    def namespace = "default"
//                    def secretName = "docker-hub-secret"
//
//                    // 检查 Secret 是否存在，如果存在则跳过创建
//                    def secretExists = sh(
//                            script: "kubectl get secret ${secretName} -n ${namespace} --ignore-not-found",
//                            returnStatus: true
//                    ) == 0
//
//                    if (!secretExists) {
//                        echo "Docker Hub Secret 不存在，请手动创建或确认。"
//                        error("Secret not found. Please create it manually and retry.")
//                    } else {
//                        echo "Docker Hub Secret 已存在，跳过创建步骤。"
//                    }
//                }
//            }
//        }

        stage('Verify Kubernetes Access') {
            steps {
                script {
                    sh "kubectl config view"  // 查看 kubeconfig 配置
                    sh "kubectl auth can-i get pods"  // 验证权限
                }
            }
        }
        stage('Apply ConfigMaps and Secrets') {
            steps {
                sh 'kubectl apply -f configmap.yaml'
                sh 'kubectl apply -f secrets.yaml'
            }
        }

        stage('Deploy Zookeeper') {
            steps {
                script {
                    sh "kubectl apply -f zookeeper-deployment.yaml" //KUBECONFIG=${KUBECONFIG}
                    sh "kubectl get pods -l app=zookeeper"
                    sh "kubectl get svc zookeeper-service"
                }
            }
        }

        stage('Verify Zookeeper YAML') {
            steps {
                script {
                    sh 'ls -la zookeeper-deployment.yaml'
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
        stage('Verify Deployment') {
            steps {
                sh 'kubectl get pods -l app=nexchange-userservice'
                sh 'kubectl get services nexchange-userservice'
                sh 'kubectl rollout status deployment/nexchange-userservice'
            }
        }
        stage('Get Service URL') {
            steps {
                script {
                    def serviceURL = sh(script: "kubectl get service nexchange-userservice -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'", returnStdout: true).trim()
                    echo "Service URL: http://${serviceURL}"
                }
            }
        }
    } 
} 
