pipeline {
  agent { dockerfile true }
  
  stages {
    stage('Dockerbuild') {
      steps {
        checkout scm
        docker.build('webflux-r2dbc-demo:${env.BUILD_ID}')
      }
    }
  }
}
