node {
    stage('Checkout') {
        checkout scm
    }
    stage('Build image') {
        def GIT_COMMIT_HASH = sh "(git log -1 --pretty=format:%h)"
        app = docker.build("webflux-r2dbc-demo:${GIT_COMMIT_HASH}")
    }
    /*
    stage('Push image') {
      docker.withRegistry('https://some-registry', 'registry-credential-id') {
        app.push()
        app.push("latest")
      }
    }
    */
}
