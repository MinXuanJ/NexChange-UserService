pipeline{
    agent any

    parameters{
        string(name: 'JAR_NAME', defaultValue:'NexChange-UserService', description:'The name of the JAR file')

        stages{
            stage('Build'){
                steps{
                    sh "mvn clean package -DjarName=${params.JAR_NAME}"
                }
            }

        }
    }

}