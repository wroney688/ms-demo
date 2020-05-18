node() {
  docker.withRegistry('http://k8s-master:31002', 'nexus-admin') {
    def app
    def commit_id
    stage('Checkout') {
      checkout scm
      sh "git rev-parse HEAD > .git/commit-id"
      commit_id = readFile('.git/commit-id').trim()
      println commit_id
    }

    stage('Build') {
      def (PH, PP) = "$HTTP_PROXY".replace("http://", "").tokenize(':')
      def NP = "$NO_PROXY".replace(",","|")
      def SP="false"
      if (PH==null) PH="" else SP="true"
      if (PP==null) PP=""
      if (NP==null) NP=""
      def MVN_ARGS="-DsetProxy=$SP -DproxyHost=$PH -DproxyPort=$PP -DproxyHost=$PH -DproxyPort=$PP -DnonProxyHosts=$NP"
      app = docker.build("roneymis/ms-demo", "--build-arg MVN_ARGS='$MVN_ARGS' --build-arg http_proxy=$HTTP_PROXY --build-arg no_proxy=$NO_PROXY --build-arg https_proxy=$HTTPS_PROXY -f Dockerfile .")
    }

    stage('Publish Build Image') {
      sh "docker login -u admin -p admin123 http://k8s-master:31002"
      app.push "${BRANCH_NAME}-${commit_id}"
    }

    stage('Deploy Test Environments') {
      withEnv(["COMMIT_ID=${commit_id}"]) {
          sh 'cat deploy_qa.yaml | envsubst | kubectl --kubeconfig=/var/jenkins_home/.kube/config create -f -'
      }
    }

    try {
      stage('Test') {
        withEnv(["COMMIT_ID=${commit_id}"]) {
          def targ_ip = sh(script: "kubectl --kubeconfig=/var/jenkins_home/.kube/config get services -n demo-pt -l 'name=ms-demo-pt-${BUILD_ID}' -o=custom-columns=IP:.spec.clusterIP | grep -v IP", returnStdout: true).trim()
          def targ_port = sh(script: "kubectl --kubeconfig=/var/jenkins_home/.kube/config get services -n demo-pt -l 'name=ms-demo-pt-${BUILD_ID}' -o=custom-columns=Port:.spec.ports[0].port | grep -v Port", returnStdout: true).trim()

          def PTjob = build job: 'PT-pipeline', 
                                      propagate: false,
                                      parameters: [[$class:'StringParameterValue', name:'APP_UNDER_TEST', value:'MS-DEMO'],
                                                   [$class:'StringParameterValue', name:'TEST_PLAN_BRANCH', value:"${BRANCH_NAME}"],
                                                   [$class:'TextParameterValue', name:'APP_ENV', value:"http://${targ_ip}:${targ_port}"]]
          child = PTjob.getNumber().toString()
          echo "PT-Pipeline: ${child}"
          ptResult =  copyArtifacts(projectName: 'PT-pipeline',
                                    filter: 'thisScorecard.html',
                                    fingerprintArtifacts: true,
                                    flatten: true,
                                    selector: [$class: 'SpecificBuildSelector',
                                               buildNumber: PTjob.getNumber().toString()]
                                      )
          archiveArtifacts artifacts: "thisScorecard.html", fingerprint: true
          ptResult =  copyArtifacts(projectName: 'PT-pipeline',
                                    filter: '**/*result.xml',
                                    fingerprintArtifacts: true,
                                    flatten: true,
                                    selector: [$class: 'SpecificBuildSelector',
                                               buildNumber: PTjob.getNumber().toString()]
                                      )
          archiveArtifacts artifacts: "**/*result.xml", fingerprint: true
          junit('**/*result.xml')
          currentBuild.result = PTjob.result
        }
      }

      if (currentBuild.currentResult == 'SUCCESS') {
        stage('Publish Version') {
          app.push "${BRANCH_NAME}-latest"
        }
        stage('Deploy Staging Env') {
          kubernetesDeploy(
                          kubeconfigId: 'kube_adm',
                          configs: 'deploy_stage.yaml',
                          enableConfigSubstitution: true
                          )
        }
      }
    }
    finally {
      stage('Reclaiming Test Environments') {
        withEnv(["COMMIT_ID=${commit_id}"]) {
            sh 'cat deploy_qa.yaml | envsubst | kubectl --kubeconfig=/var/jenkins_home/.kube/config delete -f -'
        }
      }
    }
  }
  cleanWs()
}