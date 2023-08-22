# file-shuttle

A simple cf pushable app that allows shuttling of a file.

Once pushed, it can be used as follows:

#Upload<br>
```curl file-shuttle-impressive-possum-lt.cfapps-27.slot-35.tanzu-gss-labs.vmware.com/upload -F 'file=@</path/to/file/filename>'```

#Download<br>
```curl file-shuttle-impressive-possum-lt.cfapps-27.slot-35.tanzu-gss-labs.vmware.com/download -O -J```

The filename should persist. Whatever the filename is in the upload, it should download with same name. Only one upload/download at a time.
