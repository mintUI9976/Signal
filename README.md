[![Loading-RedEugene](https://i.postimg.cc/HnjVQNdQ/signal.png "https://gitlab.zyonicsoftware.com")](https://gitlab.zyonicsoftware.com)

<div align="center">
  <h2>An api for java with the native Java socket technology and the boonproject</h2>
  <hr />
  <a href="https://gitlab.zyonicsoftware.com/mint9976/Signal/-/packages"><img src="https://img.shields.io/badge/release-v1.0.0-9cf" /></a>
  <a href="https://github.com/mintUI9976/Signal"><img src="https://img.shields.io/github/languages/code-size/mintUI9976/Signal?color=orange" /></a>
  <a href="https://github.com/mintUI9976/Signal"><img src="https://img.shields.io/tokei/lines/github/mintUI9976/Signal?color=yellow" /></a>
  <a href="https://github.com/mintUI9976/Signal/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mintUI9976/Signal" /></a>
  <a href="https://github.com/mintUI9976/Signal/stargazers"><img src="https://img.shields.io/github/stars/mintUI9976/Signal?color=ff69b4" /></a>
  <a href=""><img src="https://img.shields.io/github/languages/count/mintUI9976/Signal?color=blueviolet" /></a>
  <img src="https://img.shields.io/discord/743171495454441503?label=discord&color=cyan" />
  <img src="https://img.shields.io/badge/opensource-❤-9cf">
  <br />
  <br />
  <a href="https://github.com/boonproject">BoonProject</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="https://zyonicsoftware.com">Website</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="https://github.com/mintUI9976/Signal/blob/master/LICENSE">License</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="https://gitlab.zyonicsoftware.com">Gitlab</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="https://github.com/Zyonic-Software">Github</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="https://twitter.com/zyonicsoftware">Twitter</a>
  <br />
  <hr />
</div>

- usable with jdk 11 and above
- usable via Jvm hotspot and java9
- byte compression implemented
- better framework
- remove injection scopes / ipAddress check
- better thread allocation
    - [RedEugene](https://github.com/mintUI9976/RedEugene)
- configurable delay timeout
    - in combination with keep a live delay
    - client timed out event call (Server - Client side)
        - look
          at [SignalCaller](https://github.com/mintUI9976/Signal/blob/master/src/main/java/com/zyonicsoftware/minereaper/signal/caller/SignalCaller.java)
    - custom timeout inspector
- interception event implemented
- custom event system implemented
- custom packet system implemented
- live ByteBuf allocator(none specific length) big thanks to [boon](https://github.com/boonproject/boon)
    - look
      at [ReadingByteBuffer](https://github.com/mintUI9976/Signal/blob/master/src/main/java/com/zyonicsoftware/minereaper/signal/buffer/ReadingByteBuffer.java)
    - look
      at [WritingByteBuffer](https://github.com/mintUI9976/Signal/blob/master/src/main/java/com/zyonicsoftware/minereaper/signal/buffer/WritingByteBuffer.java)
    - look
      at [InputStreamThread](https://github.com/mintUI9976/Signal/blob/master/src/main/java/com/zyonicsoftware/minereaper/signal/incoming/InputStreamThread.java)
    - look
      at [OutputStreamThread](https://github.com/mintUI9976/Signal/blob/master/src/main/java/com/zyonicsoftware/minereaper/signal/outgoing/OutputStreamThread.java)

<hr />

### Implementation / Gradle

````xml
maven{url"https://gitlab.zyonicsoftware.com/api/v4/projects/144/packages/maven"}   
````

````xml
compile group:'com.zyonicsoftware.minereaper.signal',name:'Signal',version:'v1.0.0'
````

<hr />

### Implementation / Maven

````xml

<dependency>
    <groupId>com.zyonicsoftware.minereaper.signal</groupId>
    <artifactId>Signal</artifactId>
    <version>v1.0.0</version>
</dependency>
````

````xml
mvn dependency:get -Dartifact=com.zyonicsoftware.minereaper.signal:Signal:v1.0.0
````

````xml

<repositories>
    <repository>
        <id>gitlab-maven</id>
        <url>https://gitlab.zyonicsoftware.com/api/v4/projects/144/packages/maven</url>
    </repository>
</repositories>

<distributionManagement>
<repository>
    <id>gitlab-maven</id>
    <url>https://gitlab.zyonicsoftware.com/api/v4/projects/144/packages/maven</url>
</repository>

<snapshotRepository>
    <id>gitlab-maven</id>
    <url>https://gitlab.zyonicsoftware.com/api/v4/projects/144/packages/maven</url>
</snapshotRepository>
</distributionManagement>
````

<hr />

### Learn about Signal

- Signal example
  guide, [Signal Explained](https://gitlab.zyonicsoftware.com/mint9976/Signal/-/tree/master/src/main/java/com/zyonicsoftware/minereaper/signal/example)



