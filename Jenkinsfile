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

        stage('Start Docker Services') {
            steps {
                script {
                    sh 'docker-compose up -d'
                    sh 'docker-compose ps'
                }
            }
        }

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


        stage('Stop Docker Services') {
            steps {
                script {
                    sh 'docker-compose down'
                }
            }
        }

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
                    sh "kubectl config view"
                    def canGetPods = sh(script: "kubectl auth can-i get pods", returnStatus: true) == 0
                    if (!canGetPods) {
                        error "Jenkins lacks necessary permissions to manage Kubernetes resources"
                    }
                    echo "Kubernetes access verified successfully"
                }
            }
        }

        stage('Apply ConfigMaps and Secrets') {
            steps {
                script {
                    sh 'kubectl apply -f configmap.yaml'
                    sh 'kubectl apply -f secrets.yaml'
                    echo "ConfigMaps and Secrets applied successfully"
                }
            }
        }

        stage('Deploy and Verify Zookeeper') {
            steps {
                script {
                    sh "kubectl apply -f zookeeper-deployment.yaml"
                    sh "kubectl rollout status deployment/zookeeper"

                    // 验证 Zookeeper 是否完全就绪
                    def maxAttempts = 10
                    def attempt = 1
                    def zookeeperReady = false

                    while (attempt <= maxAttempts) {
                        try {
                            // 检查 Pod 运行状态
                            def podStatus = sh(script: """
                        kubectl get pods -l app=zookeeper -o jsonpath='{.items[*].status.phase}'
                    """, returnStdout: true).trim()

                            // 检查 ReadinessProbe 状态
                            def readyStatus = sh(script: """
                        kubectl get pods -l app=zookeeper -o jsonpath='{.items[*].status.conditions[?(@.type=="Ready")].status}'
                    """, returnStdout: true).trim()

                            if (podStatus == "Running" && readyStatus == "True") {
                                // 获取服务信息
                                def zookeeperIP = sh(script: "kubectl get service zookeeper-service -o jsonpath='{.spec.clusterIP}'", returnStdout: true).trim()
                                def zookeeperPort = "2181" // 固定端口

                                // 验证连接性
                                def zkTest = sh(script: """
                            kubectl run zk-test --image=busybox --restart=Never --rm -i --timeout=10s -- \
                            nc -z -v ${zookeeperIP} ${zookeeperPort}
                        """, returnStatus: true)

                                if (zkTest == 0) {
                                    echo "Zookeeper is fully operational at ${zookeeperIP}:${zookeeperPort}"
                                    zookeeperReady = true
                                    break
                                }
                            }
                        } catch (Exception e) {
                            echo "Attempt ${attempt}: Zookeeper verification failed: ${e.getMessage()}"
                        }

                        sleep 20
                        attempt++
                    }

                    if (!zookeeperReady) {
                        error "Zookeeper failed to become ready after ${maxAttempts} attempts"
                    }
                }
            }
        }

        stage('Deploy and Verify Kafka') {
            steps {
                script {
                    // 部署 Kafka
                    sh "kubectl apply -f kafka-deployment.yaml"
                    sh "kubectl rollout status deployment/kafka"

                    def maxAttempts = 10
                    def attempt = 1
                    def kafkaReady = false

                    while (attempt <= maxAttempts) {
                        try {
                            // 检查 Pod 状态
                            def podStatus = sh(script: """
                        kubectl get pods -l app=kafka -o jsonpath='{.items[*].status.phase}'
                    """, returnStdout: true).trim()

                            // 检查 ReadinessProbe 状态
                            def readyStatus = sh(script: """
                        kubectl get pods -l app=kafka -o jsonpath='{.items[*].status.conditions[?(@.type=="Ready")].status}'
                    """, returnStdout: true).trim()

                            if (podStatus == "Running" && readyStatus == "True") {
                                // 获取 Kafka 服务信息
                                def kafkaIP = sh(script: "kubectl get service kafka-service -o jsonpath='{.spec.clusterIP}'", returnStdout: true).trim()
                                def kafkaPort = "9092" // 固定端口

                                // 验证 Kafka 连接性
                                def kafkaTest = sh(script: """
                            kubectl run kafka-test --image=wurstmeister/kafka:2.13-2.8.1 --restart=Never --rm -i --timeout=20s -- \
                            bash -c 'echo dump | nc ${kafkaIP} ${kafkaPort}'
                        """, returnStatus: true)

                                if (kafkaTest == 0) {
                                    echo "Kafka is fully operational at ${kafkaIP}:${kafkaPort}"
                                    kafkaReady = true
                                    break
                                }
                            }
                        } catch (Exception e) {
                            echo "Attempt ${attempt}: Kafka verification failed: ${e.getMessage()}"
                        }

                        sleep 30
                        attempt++
                    }

                    if (!kafkaReady) {
                        error "Kafka failed to become ready after ${maxAttempts} attempts"
                    }
                }
            }
        }

//        stage('Deploy and Verify MySQL') {
//            steps {
//                script {
//                    sh "kubectl apply -f mysql-deployment.yaml"
//                    sh "kubectl rollout status deployment/mysql"
//
//                    def mysqlIP = sh(script: "kubectl get service mysql-service -o jsonpath='{.spec.clusterIP}'", returnStdout: true).trim()
//                    def mysqlPort = sh(script: "kubectl get service mysql-service -o jsonpath='{.spec.ports[0].port}'", returnStdout: true).trim()
//                    echo "MySQL is running at ${mysqlIP}:${mysqlPort}"
//
//                    def mysqlPods = sh(script: "kubectl get pods -l app=mysql -o jsonpath='{.items[*].status.phase}'", returnStdout: true).trim()
//                    echo "MySQL pods status: ${mysqlPods}"
//                }
//            }
//        }
//
//        stage('Deploy and Verify Redis') {
//            steps {
//                script {
//                    sh 'kubectl apply -f redis-deployment.yaml'
//                    sh "kubectl rollout status deployment/redis"
//
//                    def redisIP = sh(script: "kubectl get service redis-service -o jsonpath='{.spec.clusterIP}'", returnStdout: true).trim()
//                    def redisPort = sh(script: "kubectl get service redis-service -o jsonpath='{.spec.ports[0].port}'", returnStdout: true).trim()
//                    echo "Redis is running at ${redisIP}:${redisPort}"
//
//                    def redisPods = sh(script: "kubectl get pods -l app=redis -o jsonpath='{.items[*].status.phase}'", returnStdout: true).trim()
//                    echo "Redis pods status: ${redisPods}"
//                }
//            }
//        }

//        stage('Deploy User Service') {
//            steps {
//                script {
//                    sh 'kubectl apply -f deployment.yaml'
//                    sh "kubectl rollout status deployment/nexchange-userservice"
//                    echo "User Service deployed successfully"
//                }
//            }
//        }

//        stage('Verify User Service Deployment') {
//            steps {
//                script {
//                    def userServicePods = sh(script: "kubectl get pods -l app=nexchange-userservice -o jsonpath='{.items[*].status.phase}'", returnStdout: true).trim()
//                    echo "User Service pods status: ${userServicePods}"
//
//                    def userServiceDetails = sh(script: "kubectl describe deployment nexchange-userservice", returnStdout: true).trim()
//                    echo "User Service Deployment Details:\n${userServiceDetails}"
//
//                    def userServiceLogs = sh(script: "kubectl logs -l app=nexchange-userservice --tail=50", returnStdout: true).trim()
//                    echo "User Service Logs (last 50 lines):\n${userServiceLogs}"
//                }
//            }
//        }

//        stage('Get Service URL') {
//            steps {
//                script {
//                    def serviceURL = sh(script: "kubectl get service nexchange-userservice -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'", returnStdout: true).trim()
//                    echo "Service URL: http://${serviceURL}"
//
//                    def serviceDetails = sh(script: "kubectl get service nexchange-userservice -o yaml", returnStdout: true).trim()
//                    echo "Service Details:\n${serviceDetails}"
//                }
//            }
//        }
//
//        stage('Final Health Check') {
//            steps {
//                script {
//                    def allPods = sh(script: "kubectl get pods --all-namespaces", returnStdout: true).trim()
//                    echo "All pods across all namespaces:\n${allPods}"
//
//                    def allServices = sh(script: "kubectl get services --all-namespaces", returnStdout: true).trim()
//                    echo "All services across all namespaces:\n${allServices}"
//                }
//            }
//        }
    } 
} 
