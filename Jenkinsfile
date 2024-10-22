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
                    sh "kubectl wait --for=condition=ready pod -l app=zookeeper --timeout=60s"

                    def zookeeperPod = sh(
                            script: "kubectl get pod -l app=zookeeper -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

                    // 验证 Zookeeper 状态
                    def zkHealth = sh(
                            script: "kubectl exec ${zookeeperPod} -- bash -c 'echo ruok | nc localhost 2181'",
                            returnStdout: true
                    ).trim()

                    echo "Zookeeper health check response: ${zkHealth}"
                    echo "Zookeeper logs:"
                    sh "kubectl logs ${zookeeperPod} --tail=20"
                }
            }
        }

        stage('Deploy and Verify Kafka') {
            steps {
                script {
                    sh "kubectl apply -f kafka-deployment.yaml"
                    sh "kubectl wait --for=condition=ready pod -l app=kafka --timeout=120s"

                    def kafkaPod = sh(
                            script: "kubectl get pod -l app=kafka -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

                    // 验证 Kafka 状态
                    echo "Kafka logs:"
                    sh "kubectl logs ${kafkaPod} --tail=20"

                    // 测试 Kafka 功能
                    sh """
                kubectl exec ${kafkaPod} -- bash -c \
                'kafka-topics.sh --list --bootstrap-server localhost:9092'
            """
                }
            }
        }

        stage('Deploy and Verify Database Services') {
            steps {
                script {
                    // 部署 MySQL
                    sh "kubectl apply -f mysql-deployment.yaml"
                    sh "kubectl wait --for=condition=ready pod -l app=mysql --timeout=120s"

                    // 部署 Redis
                    sh "kubectl apply -f redis-deployment.yaml"
                    sh "kubectl wait --for=condition=ready pod -l app=redis --timeout=60s"

                    def mysqlPod = sh(
                            script: "kubectl get pod -l app=mysql -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

                    sh """
                        echo "Creating database if not exists..."
                        kubectl exec ${mysqlPod} -- mysql -uroot -padmin -e '
                        CREATE DATABASE IF NOT EXISTS NexChangeUserDB;
                        SHOW DATABASES;
                        '
                    """

                    // 输出服务信息
                    def mysqlInfo = sh(
                            script: """
                    echo "MySQL Service: \$(kubectl get service mysql-user-service -o jsonpath='{.spec.clusterIP}:{.spec.ports[0].port}')"
                    echo "MySQL Database: \$(kubectl get cm userservice-config -o jsonpath='{.data.DB_NAME}')"
                    echo "MySQL Pods Status: \$(kubectl get pods -l app=mysql -o jsonpath='{.items[*].status.phase}')"
                """,
                            returnStdout: true
                    ).trim()

                    def redisInfo = sh(
                            script: """
                    echo "Redis Service: \$(kubectl get service redis-service -o jsonpath='{.spec.clusterIP}:{.spec.ports[0].port}')"
                    echo "Redis Pods Status: \$(kubectl get pods -l app=redis -o jsonpath='{.items[*].status.phase}')"
                """,
                            returnStdout: true
                    ).trim()

                    echo "Database Services Status:\n${mysqlInfo}\n${redisInfo}"


                }
            }
        }
        stage('Deploy User Service') {
            steps {
                script {
                    // 1. Verifying dependencies
                    sh """
                echo "Verifying dependencies..."
                kubectl wait --for=condition=ready pod -l app=mysql --timeout=60s
                kubectl wait --for=condition=ready pod -l app=redis --timeout=60s
                kubectl wait --for=condition=ready pod -l app=kafka --timeout=60s
                kubectl wait --for=condition=ready pod -l app=zookeeper --timeout=60s
            """

                    // 2. Deploy User Service
                    sh 'kubectl apply -f deployment.yaml'
                    sh "kubectl rollout status deployment/nexchange-userservice"

                    // 3. Wait Pod Readiness
                    sh "kubectl wait --for=condition=ready pod -l app=nexchange-userservice --timeout=180s"

                    // 4. Get Pod Status
                    def podName = sh(
                            script: "kubectl get pod -l app=nexchange-userservice -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

                    // 5. Check services connection
                    echo "Checking service connectivity..."
                    sh """
                echo "Database Connection Info:"
                kubectl logs ${podName} | grep -i "database"
                
                echo "\nKafka Connection Info:"
                kubectl logs ${podName} | grep -i "kafka"
                
                echo "\nRedis Connection Info:"
                kubectl logs ${podName} | grep -i "redis"
                
                echo "\nApplication Health:"
                kubectl logs ${podName} | grep -i "started"
            """


                    echo "Application Logs:"
                    sh """
                echo "Recent Logs:"
                kubectl logs ${podName} --tail=50
                
                echo "\nApplication Status:"
                kubectl exec ${podName} -- curl -s http://localhost:8081/actuator/health || true
            """

                    // 7. 如果是 LoadBalancer，输出访问信息
                    echo "Access Information:"
                    sh """
                if kubectl get service nexchange-userservice -o jsonpath='{.spec.type}' | grep -q 'LoadBalancer'; then
                    echo "LoadBalancer URL:"
                    kubectl get service nexchange-userservice -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'
                fi
            """

                }
            }
        }

        stage('Test Service Connections') {
            steps {
                script {
                    def userServicePod = sh(
                            script: "kubectl get pod -l app=nexchange-userservice -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

                    echo "Testing Service Connections..."

                    // 测试 MySQL 连接 (使用 curl 替代 nc)
                    sh """
               echo "Testing MySQL Connection with curl:"
               kubectl exec ${userServicePod} -- curl mysql-user-service:3306 || true
               
               echo "\nTesting MySQL Authentication:"
               kubectl exec \$(kubectl get pod -l app=mysql -o jsonpath='{.items[0].metadata.name}') -- \
               mysql -uroot -padmin -e 'SELECT 1' || true
           """

                    // 测试 Redis 连接 (使用 curl 替代 nc)
                    sh """
               echo "\nTesting Redis Connection with curl:"
               kubectl exec ${userServicePod} -- curl redis-service:6379 || true
               
               echo "\nTesting Redis Functionality:"
               kubectl exec \$(kubectl get pod -l app=redis -o jsonpath='{.items[0].metadata.name}') -- \
               redis-cli ping || true
           """

                    // 测试 Kafka 连接 (使用 curl 替代 nc)
                    sh """
               echo "\nTesting Kafka Connection with curl:"
               kubectl exec ${userServicePod} -- curl kafka-service:9092 || true
               
               echo "\nTesting Kafka Topics:"
               kubectl exec \$(kubectl get pod -l app=kafka -o jsonpath='{.items[0].metadata.name}') -- \
               kafka-topics.sh --bootstrap-server localhost:9092 --list || true
           """

                    // 检查应用健康状态
                    sh """
               echo "\nChecking Application Health:"
               kubectl exec ${userServicePod} -- curl -s http://localhost:8081/actuator/health || true
           """

                    // 检查 Kafka, MySQL, Redis 连接状态
                    sh """
               echo "\nChecking Kafka Connection Info:"
               kubectl logs ${userServicePod} | grep -i "Kafka connection successful"
               
               echo "\nChecking MySQL Connection Info:"
               kubectl logs ${userServicePod} | grep -i "Connected to MySQL"
               
               echo "\nChecking Redis Connection Info:"
               kubectl logs ${userServicePod} | grep -i "Connected to Redis"
            """
                }
            }
        }


        stage('Verify User Service Deployment') {
            steps {
                script {
                    def userServicePods = sh(script: "kubectl get pods -l app=nexchange-userservice -o jsonpath='{.items[*].status.phase}'", returnStdout: true).trim()
                    echo "User Service pods status: ${userServicePods}"

                    def userServiceDetails = sh(script: "kubectl describe deployment nexchange-userservice", returnStdout: true).trim()
                    echo "User Service Deployment Details:\n${userServiceDetails}"

                    def userServiceLogs = sh(script: "kubectl logs -l app=nexchange-userservice --tail=50", returnStdout: true).trim()
                    echo "User Service Logs (last 50 lines):\n${userServiceLogs}"
                }
            }
        }

        stage('Get Cluster Ip') {
            steps {
                script {
                    def serviceClusterIP = sh(script: "kubectl get service nexchange-userservice -o jsonpath='{.spec.clusterIP}'", returnStdout: true).trim()
                    echo "Service ClusterIP: ${serviceClusterIP}"
                }
            }
        }


    } 
} 
