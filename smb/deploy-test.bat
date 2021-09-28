oc create -f secret-msft-network-share-credentials.yaml
oc create -f persistentvolume-doj-network-share-pv.yaml
oc create -f persistentvolumeclaim-doj-network-share-pvc.yaml
oc create -f deployment-smb-network-share-test.yaml
