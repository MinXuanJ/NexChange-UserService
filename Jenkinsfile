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

//       stage('Unit Test') {
//           steps {
//               script {
//                   sh "docker-compose ps"
//                   sh "mvn test"
//               }
//               junit '**/target/surefire-reports/*.xml'
//            }
//       }

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


//        stage('Stop Docker Services') {
//            steps {
//                script {
//                    sh 'docker-compose down'
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
                    def pvcExists = sh(
                            script: "kubectl get pvc zookeeper-pvc -o name 2>/dev/null || true",
                            returnStdout: true
                    ).trim()

                    if (!pvcExists) {
                        echo "Creating new PVC for ZooKeeper..."
                        sh "kubectl apply -f zookeeper-storage.yaml"

                        // 等待 PVC 绑定完成
                        def bound = false
                        def attempts = 0
                        def maxAttempts = 10

                        while (!bound && attempts < maxAttempts) {
                            def status = sh(
                                    script: "kubectl get pvc zookeeper-pvc -o jsonpath='{.status.phase}'",
                                    returnStdout: true
                            ).trim()

                            if (status == "Bound") {
                                bound = true
                                echo "PVC successfully bound"
                            } else if (status == "Pending") {
                                echo "PVC is still Pending, checking events for more details"
                                sh "kubectl describe pvc zookeeper-pvc"
                            }

                            attempts++
                            echo "Waiting for PVC to be bound (attempt ${attempts}/${maxAttempts})..."
                            sleep 30 // 等待30秒
                        }

                        if (!bound) {
                            error "PVC failed to bind after ${maxAttempts} attempts"
                        }
                    } else {
                        echo "ZooKeeper PVC already exists, skipping creation"
                    }

                    sh "kubectl apply -f zookeeper-deployment.yaml"
//                    sh "kubectl wait --for=condition=ready pod -l app=zookeeper --timeout=300s"

                    def zookeeperPod = sh(
                            script: "kubectl get pod -l app=zookeeper -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

//                    // 验证 Zookeeper 状态
//                    def zkHealth = sh(
//                            script: "kubectl exec ${zookeeperPod} -- bash -c 'echo ruok | nc localhost 2181'",
//                            returnStdout: true
//                    ).trim()
//
//                    echo "Zookeeper health check response: ${zkHealth}"
                    echo "Zookeeper logs:"
                    sh "kubectl logs ${zookeeperPod} --tail=20"
                }
            }
        }

        stage('Deploy and Verify Kafka') {
            steps {
                script {
                    def kafkaPvcExists = sh(
                            script: "kubectl get pvc kafka-pvc -o name 2>/dev/null || true",
                            returnStdout: true
                    ).trim()

                    if (!kafkaPvcExists) {
                        echo "Creating new PVC for Kafka..."
                        sh "kubectl apply -f kafka-storage.yaml"

                        // 等待 PVC 绑定
                        def bound = false
                        def attempts = 0
                        def maxAttempts = 10

                        while (!bound && attempts < maxAttempts) {
                            def status = sh(
                                    script: "kubectl get pvc kafka-pvc -o jsonpath='{.status.phase}'",
                                    returnStdout: true
                            ).trim()

                            if (status == "Bound") {
                                bound = true
                                echo "Kafka PVC successfully bound"
                            } else if (status == "Pending") {
                                echo "PVC is still Pending, checking events for more details"
                                sh "kubectl describe pvc kafka-pvc"
                            }

                            attempts++
                            echo "Waiting for Kafka PVC to be bound (attempt ${attempts}/${maxAttempts})..."
                            sleep 30
                        }

                        if (!bound) {
                            error "Kafka PVC failed to bind after ${maxAttempts} attempts"
                        }
                    } else {
                        echo "Kafka PVC already exists, skipping creation"
                    }

                    sh "kubectl wait --for=condition=ready pod -l app=zookeeper --timeout=300s"
                    sh "kubectl apply -f kafka-deployment.yaml"
                    sh "kubectl wait --for=condition=ready pod -l app=kafka --timeout=300s"

                    def kafkaPod = sh(
                            script: "kubectl get pod -l app=kafka -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

                    // 验证 Kafka 状态
                    echo "Kafka logs:"
                    sh "kubectl logs ${kafkaPod} --tail=20"

                    // 测试 Kafka 功能
                    def kafkaTopics = sh(
                            script: """
                    kubectl exec ${kafkaPod} -- bash -c \
                    'kafka-topics.sh --list --bootstrap-server localhost:9092'
                """,
                            returnStdout: true
                    ).trim()

                    echo "Kafka topics: ${kafkaTopics}"
                }
            }
        }

        stage('Verify Message Queue') {
            steps {
                script {
                    sh '''
                echo "Verifying ZooKeeper status..."
                kubectl get pods -l app=zookeeper
                
                echo "Verifying Kafka status..."
                kubectl get pods -l app=kafka
                
                echo "Checking Kafka topics..."
                KAFKA_POD=$(kubectl get pod -l app=kafka -o jsonpath='{.items[0].metadata.name}')
                kubectl exec $KAFKA_POD -- kafka-topics.sh --list --bootstrap-server localhost:9092
                
                echo "Checking PVC status..."
                kubectl get pvc zookeeper-pvc
                kubectl get pvc kafka-pvc
            '''
                }
            }
        }

        stage('Create PV and PVC for MySQL') {
            steps {
                script {
                    // 检查PVC是否存在
                    def pvcStatus = sh(
                            script: "kubectl get pvc mysql-pvc-user --ignore-not-found -o jsonpath='{.status.phase}'",
                            returnStdout: true
                    ).trim()

                    if (pvcStatus == "Bound") {
                        echo "PVC 'mysql-pvc-user' already exists, skipping creation."
                    } else {
                        // 如果 PVC 不存在，执行创建
                        echo "PVC 'mysql-pvc-user' not found, creating..."
                        sh "kubectl apply -f mysql-user-service-pv.yaml"
                        sh "kubectl apply -f mysql-user-service-pvc.yaml"

                        // 等待 PVC 准备就绪
//                        sh "kubectl wait --for=condition=bound pvc/mysql-pvc-user --timeout=300s"

                        echo "PV and PVC for MySQL created successfully."
                    }
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

//                    // 5. Check services connection
//                    echo "Checking service connectivity..."
//                    sh """
//                echo "Database Connection Info:"
//                kubectl logs ${podName} | grep -i "database"
//
//                echo "\nKafka Connection Info:"
//                kubectl logs ${podName} | grep -i "kafka"
//
//                echo "\nRedis Connection Info:"
//                kubectl logs ${podName} | grep -i "redis"
//
//                echo "\nApplication Health:"
//                kubectl logs ${podName} | grep -i "started"
//            """


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
                    // 获取用户服务的 Pod 名称
                    def userServicePod = sh(
                            script: "kubectl get pod -l app=nexchange-userservice -o jsonpath='{.items[0].metadata.name}'",
                            returnStdout: true
                    ).trim()

                    echo "Testing Service Connections..."

                    // 测试 MySQL 连接
                    sh """
                echo "Testing MySQL Connection:"
                kubectl run mysql-test --rm -i --restart=Never --image=mysql:8.0 -- \
                mysql -h mysql-user-service -uroot -padmin -e 'SELECT 1' || true
            """

                    // 测试 Redis 连接
                    sh """
                echo "Testing Redis Connection:"
                kubectl run redis-test --rm -i --restart=Never --image=redis -- \
                redis-cli -h redis-service ping || true
            """

                    // 测试 Kafka 连接
                    sh """
                echo "Testing Kafka Connection:"
                kubectl run kafka-test --rm -i --restart=Never --image=confluentinc/cp-kafka:latest -- \
                kafka-topics --bootstrap-server kafka-service:9092 --list || true
            """
                }
            }
        }

        stage('Verify User Service Deployment') {
            steps {
                script {
                    // 等待 Pod 就绪
                    sh """
                kubectl wait --for=condition=ready pod -l app=nexchange-userservice --timeout=300s || true
            """

                    // 获取详细状态
                    def podStatus = sh(
                            script: '''
                    echo "Pod Status:"
                    kubectl get pods -l app=nexchange-userservice -o wide
                    
                    echo "\nPod Logs:"
                    kubectl logs -l app=nexchange-userservice --tail=50 --all-containers
                    
                    echo "\nPod Description:"
                    kubectl describe pods -l app=nexchange-userservice
                ''',
                            returnStdout: true
                    ).trim()

                    echo "Deployment Status:\n${podStatus}"

                    // 检查初始化容器状态
                    def initContainerStatus = sh(
                            script: "kubectl logs -l app=nexchange-userservice -c wait-for-services || true",
                            returnStdout: true
                    ).trim()

                    echo "Init Container Logs:\n${initContainerStatus}"
                }
            }
        }
//        stage('Analysis Pods and Logs') {
//            steps {
//                script {
//                    sh "./check-all-services.sh"
//                }
//            }
//        }

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
