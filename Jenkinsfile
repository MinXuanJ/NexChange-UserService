pipeline{
    agent any

    environment{
        DOCKER_CREDENTIALS = 'docker_hub_credentails'
        DOCKER_IMAGE = "jmx7139/nexchange-userservice"
    }

    parameters{
        string(name: 'JAR_NAME', defaultValue:'NexChange-UserService', description:'The name of the JAR file')

        stages{
            stage('Build and Package'){
                steps{
                    script
                    {
                        sh "mvn clean package"
                    }  
                }
            }
            stage(name: 'Test'){
                steps{
                    script{
                        sh "mvn test"
                    }
                }
            }
            stage(name: 'Build Docker Image'){
                steps{
                    script{
                        sh "docker build -t $DOCKER_IMAGE ."
                    }
                }
            }
            stage(name: 'Push Docker Image'){
                steps{
                    script{
                        withCredentials([usernamePassword (credentialsId: 'docker_hub_credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
                            sh "docker login -u $USERNAME -p $PASSWORD"
                            sh "docker tag $DOCKER_IMAGE $DOCKER_IMAGE:latest"
                            sh "docker push $DOCKER_IMAGE:latest"
                        }
                    }
                }

        }
    }
}