# LogcatAdvanced
This library using to obtain Logcat logs from different platforms (Windows, Unix, Android)
<h1>How to</h1>

<p>1. Add the JitPack repository to your build file</p>
  <h3>Maven</h3>
      
      	<repositories>
	<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
	</repository>
	</repositories>
	
  <h3>Gradle</h3>
  
  	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  <p>2. Add the dependency</p>
    <h3>Maven</h3>
    
   	 <dependency>
   	 <groupId>com.github.FruNoman</groupId>
	<artifactId>LogcatAdvanced</artifactId>
	<version>0.0.9</version>
    	</dependency>
	
<h3>Gradle</h3>

	dependencies {
			implementation 'com.github.FruNoman:LogcatAdvanced:0.0.9'
		}
                        
  <h2>1.Create Logger instance:</h2>
  For Android you shoud use empty constructor
  
     Logcat logger = new Logcat();

  For Windows\Unix systems you should set path to adb file
  
     Logcat logcat = new Logcat("/path/to/adb") 

  If you have few connected devices you should set specific device udid
  
     Logcat logcat = new Logcat("/path/to/adb","device udid") 

<h2>2.After that you can customize your logger output:</h2>

    Logcat.Logger logger = 
      logcat
     .dump()                             // - Dump the log to the screen and exits.
     .tag("your tag name")               // - Set specific tag for filtering logs
     .tag("your tag name",Priority.INFO) // - Set specific tag with Priority (INFO,DEBUG etc) for log messages
     .format(Format.PROCESS)             // - Set the output format for log messages. The default is threadtime format
     .time("05-07 13:18:25.991")         // - Print the most recent lines since the specified time in format "MM-dd HH:mm:ss.sss"
     .time(new Date())                   // - Print the most recent lines since the specified time using java.util.Date instance
     .time(1557235222735)                // - Print the most recent lines since the specified time using long type
     .buffer(Buffer.SYSTEM)              // - Load an alternate log buffer for viewing, such as events or radio
     .pid(2110)                          // - Only print logs from the given PID
     .pid("mediaserver")		 // - Only print logs from the given process name (pidOf process)
     .clear()                            // - Clear (flush) the selected buffers and exit
     .count(20)                          // - Quit after printing <count> number of lines
     .regex("log regex")		 // - Only print lines where the log message matches expression.
     .prune()				 // - Print (read) the current white and black lists and takes no arguments
     .prune("black list pids")		 // - Write the black lists to adjust the logging content for a specific purpose.
     .tail(10)				 // - Print the most recent number of lines since the specified time
     .build()				 // - Start collect logs
  
  <h2>3. Then you can read logs from your logger instance</h2>
  
    Logcat.Line log = null; 
        while ((log = logger.readLine())!=null)
            log.getDate();                          // - Return line's time in java.util.Date instance
            log.getTag();                           // - Return line's tag in String class format
            log.getPriority();                      // - Return line's priority in Priority class format
            log.getPid();                           // - Return line's pid in Integer class format
            log.getDescription();                   // - Return line's description in String class format
        }
      
