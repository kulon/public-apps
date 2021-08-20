FROM image-registry.openshift-image-registry.svc:5000/openshift/fuse7-java-openshift:1.8
COPY target/*.jar /deployments
CMD /usr/local/s2i/run 
