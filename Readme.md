This is a subproject, a microservice demo, that is pre-loaded into the GOGS.io repository.

This microservice will expose a set of methods.  On the deployment (pod) init (container init) 
it will generate a series of delays for each method using a parameterized average and standard
deviation.  The microservice methods will simply iterate through the series for that method
performing a sleep for the specified time before returning a response.

Uses the [OpenJDK docker image]https://hub.docker.com/_/openjdk/).  The tentative design ia s 
microservice that exposes a small number (3-5) of interfaces labeled alphabetically (A, B, C,...).
On startup, the microservice will read a parameter file which will specify, for each service, a 
response time average, standard deviation, and n value.  It will use these parameters to build a 
delay dataset fitting those parameters.  Each call to a service will pull the next delay from the 
list and sleep for that amount of time before responding to the caller.

Current state of the demo:
Login to the shellinabox interface.
cd ~/ms-demo
mvn clean package
java -jar `ls target/*.jar` &

curl http://localhost:8080/A

Output is at the "start" of the next prompt line.

Update: Feb 9

The jenkins pipeline now constructs a docker image using a multi-branch docker build in the dockerfile.
The resulting image can be run (docker run -p hostport:8080 <img>) and a curl 
http://localhost:hostport/[A..F] does respond accordingly.  The next steps are to have the pipeline 
publish the image to Nexus and deploy it into K8s.



