<div align="center">
<a href="https://github.com/mintUI9976/Signal"> <img src="https://i.postimg.cc/HnjVQNdQ/signal.png" /></a>
<h2>A library for java with the native java socket technology and in combination with the custom bytebuffer allocator technology by boonproject</h2>
  <hr />
  <a href="https://github.com/mintUI9976?tab=packages&repo_name=Signal"><img src="https://img.shields.io/badge/release-v1.2.1-9cf" /></a>
  <a href="https://github.com/mintUI9976/Signal"><img src="https://img.shields.io/github/languages/code-size/mintUI9976/Signal?color=orange" /></a>
  <a href="https://github.com/mintUI9976/Signal"><img src="https://img.shields.io/tokei/lines/github/mintUI9976/Signal?color=yellow" /></a>
  <a href="https://github.com/mintUI9976/Signal/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mintUI9976/Signal" /></a>
  <a href="https://github.com/mintUI9976/Signal/stargazers"><img src="https://img.shields.io/github/stars/mintUI9976/Signal?color=ff69b4" /></a>
  <a href=""><img src="https://img.shields.io/github/languages/count/mintUI9976/Signal?color=blueviolet" /></a>
  <img src="https://img.shields.io/badge/opensource-❤-9cf">
  <br />
  <br />
  <a href="https://github.com/boonproject">BoonProject</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="https://github.com/mintUI9976/Signal/blob/master/LICENSE">License</a>
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
maven { url 'https://jitpack.io' }
````

````xml
implementation 'com.github.mintUI9976:Signal:Tag'
````

<hr />

### Learn about Signal

- Signal example
  guide, [Signal Explained](https://github.com/mintUI9976/Signal/tree/master/src/main/java/com/zyonicsoftware/minereaper/signal/example)



