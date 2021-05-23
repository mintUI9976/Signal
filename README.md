# Signal ![Version Status ](https://img.shields.io/badge/JDK-11-red?style=for-the-badge) ![Version Status ](https://img.shields.io/badge/RELEASE-v0.7.1-blue?style=for-the-badge)

## An api for java with the native Java socket technology and the [boonproject](https://github.com/boonproject/boon).

________________________________________

- useable with jdk 11 and above
- useable via Jvm hotspot and java9
- byte compression implemented
- better framework
- remove injection scopes / ipAddress check
- asynchrony job's (better threaded) big thanks to [Wisp-Scheduler](https://github.com/Coreoz/Wisp)
- allocated packet byte length
- configurable delay timeout
- interception event implemented
- ez to use packet system

________________________________________

## Implementation / Gradle

```java
maven{
        url"https://gitlab.zyonicsoftware.com/api/v4/projects/144/packages/maven"
        name"GitLab"
        credentials(HttpHeaderCredentials){
        name='Private-Token'
        value=System.getenv("ZYONIC_PERSONAL_GITLAB_TOKEN")
        }
        authentication{
        header(HttpHeaderAuthentication)
        }
        }
```

```java
compile group:'com.zyonicsoftware.minereaper.signal',name:'Signal',version:'v0.7.0'
```

________________________________________

## Learn about Signal

- Signal example
  guide, [Signal Explained](https://gitlab.zyonicsoftware.com/mint9976/Signal/-/tree/master/src/main/java/com/zyonicsoftware/minereaper/signal/example)



