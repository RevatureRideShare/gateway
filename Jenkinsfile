pipeline {

    agent any
    
    triggers {
    	pollSCM('') // Enabling being build on Push
  	}
  	
  	options{
  		disableConcurrentBuilds()
  		buildDiscarder(logRotator(numToKeepStr: '10'))
  	}

    environment{
        SONAR_SCANNER_VERSION="4.2.0.1873"
        SONAR_SCANNER_HOME="$HOME/.sonar/sonar-scanner-$SONAR_SCANNER_VERSION-linux"
        PATH="$SONAR_SCANNER_HOME/bin:$PATH"
        SONAR_SCANNER_OPTS="-server" 
        ORG="RevatureRideShare"
        REPO="gateway"   
        BRANCH="feature/2-jenkinsfile"    
    }

    stages {

        stage('Clean') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage ('Build') {
            steps {                
                // Run in non-interactive (batch) mode
                sh 'mvn -U -B -DskipTests clean package'
            }
        }

        stage('Checkstyle') { // Code smells
            steps {
                sh 'mvn verify checkstyle:checkstyle'
            }
        }

        stage('Sonar Analysis') { 
            // performs a sonar analysis and sends code to sonarcloud
            steps {
                script{
                    if (env.CHANGE_ID) {
                        echo 'Pull Request Detected...'

                        withSonarQubeEnv(credentialsId: 'b44ffadc-08d5-11ea-8d71-362b9e155667', installationName:'SonarCloud'){                           
                            sh """
                             sonar-scanner \
                                -Dsonar.login=${env.SONAR_LOGIN_TOKEN} \
                                -Dsonar.pullrequest.base=${env.CHANGE_TARGET} \
                                -Dsonar.pullrequest.provider=GitHub \
                                -Dsonar.pullrequest.key=${env.CHANGE_ID} \
                                -Dsonar.pullrequest.github.repository=${env.ORG}/${env.REPO} \
                                -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH}
                            """
                        }
                    } else {
                            withSonarQubeEnv(credentialsId: 'b44ffadc-08d5-11ea-8d71-362b9e155667', installationName:'SonarCloud'){
                                sh """
                                sonar-scanner \
                                -Dsonar.login=${env.SONAR_LOGIN_TOKEN} \
                                """
                            }
                    }   
                }
            }
        }
        
  		stage ('Quality Gate') {
  			steps{
                script{ // Hard code the Analysis URL sent to Slack 
                        // TODO: find the official url via sonar plugin
                    def urlComponents = env.JOB_NAME.split("/")
                    def urlJobName = urlComponents[0]
                    def urlSonarCloudLink = "https://sonarcloud.io/dashboard?id=RevatureRideShare_" + urlJobName
                    slackSend message: "Build Started - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>) " +
                    "View the SonarCloud analysis - " + urlSonarCloudLink
                } 
  				timeout(time: 1, unit: 'MINUTES'){
  					waitForQualityGate abortPipeline: true
  				}
  			}
  		}
	    stage('Store to S3'){
		    steps{
			sh 'aws s3 cp --recursive ./target s3://rideshare-client/jars'
		    }
	    }
        
        stage ('Deploy') {
            steps {
                script{
                    if(env.BRANCH_NAME == BRANCH ){
                        withCredentials([[$class  : 'UsernamePasswordMultiBinding',
                                  credentialsId   : 'PCF_LOGIN',
                                  usernameVariable: 'USERNAME',
                                  passwordVariable: 'PASSWORD']]) {
                        sh 'cf events rideshare-security-service'
					    sh 'cf logs rideshare-security-service --recent'
                        sh 'cf login -a http://api.run.pivotal.io -u $USERNAME -p $PASSWORD \
                        -o "Revature Training" -s development'
                        sh 'cf push'
                        }
                    }   
                }
            }
        }
    }
  
    post{
        always{
            deleteDir()
        }
    }
}
