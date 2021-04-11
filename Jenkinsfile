pipeline {
    environment {
        GIT_COMMIT_HASH = sh "(git log -1 --pretty=format:%h)"
    }
    stages {
        stage('Dockerbuild') {
            steps {
                checkout scm
                app = docker.build('webflux-r2dbc-demo:$GIT_COMMIT_HASH')
            }
        }
    }
}
