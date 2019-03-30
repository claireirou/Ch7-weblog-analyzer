/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     * 
     */
    public LogAnalyzer()
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }
    
     /**
     * Create an object to analyze hourly web accesses.
     * 
     * @param filename The name of the file to analyze
     */
    public LogAnalyzer(String filename)
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the reader to obtain the data.
        reader = new LogfileReader(filename);
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }
    
    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
    
    /**
     *  Return the number of access recorded in the log file.
     */
    public int numberOfAccesses()
    {
        int total = 0;
        for(int i=0; i<hourCounts.length; i++) {
            total += hourCounts[i];
        }
        return total;
    }
    
    /**
     *  Return the busiest hour in the log file.
     */
    public int busiestHour()
    {
        int highest = hourCounts[0];
        int hour =0;
        for(int i=0; i<hourCounts.length; i++) {
            if (hourCounts[i] > highest) {
                highest = hourCounts[i];
                hour = i;
            }
        }
        return hour; 
    }
    
    /**
     *  Return the busiest two-hour period in the log file.
     *  
     * @return the first hour of the two-hour period
     */
    public int busiestTwoHour()
    {
       int highest = hourCounts[0];
       int hour = 0;
       for(int i=0; i<hourCounts.length - 1; i++) {
           if((hourCounts[i] + hourCounts[i+1]) > highest) {
               highest = hourCounts[i] + hourCounts[i+1];
               hour = i;
            }
        }
       if(hourCounts[23] + hourCounts[0] > highest) {
           highest = hourCounts[23] + hourCounts[0];
           hour = 23;
        }
       return hour; 
    }
    
    /**
     *  Return the least busy hour in the log file.
     */
    public int quietestHour()
    {
        int lowest = hourCounts[0];
        int hour = 0;
        for(int i=0; i<hourCounts.length; i++) {
            if(hourCounts[i] < lowest) {
                lowest = hourCounts[i];
                hour = i;
            }
        }
        return hour;   
    }
}
