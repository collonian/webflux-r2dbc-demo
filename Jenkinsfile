node {
    stage('Checkout') {
        checkout scm
    }
    stage('Build image') {
        GIT_COMMIT_HASH = sh "(git log -1 --pretty=format:%h)"
        app = docker.build('webflux-r2dbc-demo:$GIT_COMMIT_HASH')
    }
}
