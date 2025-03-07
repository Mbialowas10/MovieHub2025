## remove api key 

1. remove gradle.properties from github by running the following commands:

```dsl
   git rm --cached gradle.properties
   git commit -m "Stop tracking gradle.properties"
   git push origin <branch-name>
```
   
