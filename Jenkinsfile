pipeline {
    agent any
    stages {
        stage('clone repo and clean') {
            steps {
                sh "mvn clean"
            }
        }
        stage('Test') {
            steps {
                sh "mvn test"
            }
        }

        stage('Clean Install') {
                    steps {
                        sh "mvn install"
                    }
                }
        stage("Build docker image"){
            steps{
                script{
                    sh "docker build -t javenockdocker/book-postgres ."
                }
            }
        }
         stage("Push image to Dockerhub"){
                    steps{
                        script{
                            withCredentials([string(credentialsId: 'javenockdocker', variable: 'javenockdockerpwd')]) {
                                sh "docker login -u javenockdocker -p ${javenockdockerpwd}"
                            }
                            sh "docker push javenockdocker/book-postgres"
                        }
                    }
                }

    }
}