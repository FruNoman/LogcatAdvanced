# LogcatAdvanced
This library using to obtain Logcat logs from differnt platform (Windows, Unix, Android)
Example:
1.Create Logger instance:
  For Android you shoud use empty constructor
  
     Logger logger = new Logger();

  For Windows\Unix systems you should set path to adb file
  
     Logger logger = new Logger("/path/to/adb") 

  If you have few connected devices you should set specific device udid
  
     Logger logger = new Logger("/path/to/adb","device udid") 

2.After that you can customize your logger output:

    logger
     .dump()                             // - Dump the log to the screen and exits.
     .tag("your tag name")               // - Set specific tag for filtering logs
     .tag("your tag name",Priority.INFO) // - Set specific tag with Priority (INFO,DEBUG etc) for log messages
     .format(Format.PROCESS)             // - Set the output format for log messages. The default is threadtime format
     .time("05-07 13:18:25.991")         // - Print the most recent lines since the specified time in format "MM-dd HH:mm:ss.sss"
     .time(new Date())                   // - Print the most recent lines since the specified time using java.util.Date instance
     .time(1557235222735)                // - Print the most recent lines since the specified time using long type
     .buffer(Buffer.SYSTEM)              // - Load an alternate log buffer for viewing, such as events or radio
     .pid(2110)                          // - Only print logs from the given PID.
     .clear()                            // - Clear (flush) the selected buffers and exit
     .count(20)                          // - Quit after printing <count> number of lines
  
  3. Then you can read logs from your logger instance
  
    List<Logger.Line> logs = logger.readLineLogs(); // - Read logs like Line object instance
        for (Logger.Line log : logs) {
            log.getDate();                          // - Return line's time in java.util.Date instance
            log.getTag();                           // - Return line's tag in String class format
            log.getPriority();                      // - Return line's priority in Priority class format
            log.getPid();                           // - Return line's pid in Integer class format
            log.getDescription();                   // - Return line's description in String class format
        }
 Or you can use method which returning logs in typical logcat format:
 
    List<String> logs = logger=readStringLogs();
    for(String log:logs){
        System.out.println(log);
    }
      
