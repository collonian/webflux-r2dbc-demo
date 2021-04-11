node {
    script {
         GIT_COMMIT_HASH = sh "(git log -1 --pretty=format:%h)"
    }
    
    stage('Dockerbuild') {
        checkout scm
        app = docker.build('webflux-r2dbc-demo:$GIT_COMMIT_HASH')
    }
}
