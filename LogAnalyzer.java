/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author Claire Iroudayassamy
 * @version    2019.03.30
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    //Where to calculate the daily access counts.
    private int[] dayCounts;
    // Where to calculate the monthly access counts.
    private int[] monthCounts;
    // Where to calculate average mountly counts.
    private double[] monthlyAverages;
    
    
    
    // Use a LogfileReader to access the data.
    private LogfileReader reader;
    
    private String filename;

    /**
     * Create an object to analyze hourly web accesses.
     * 
     */
    public LogAnalyzer()
    { 
        // Create the array objects to hold the access counts.
        hourCounts = new int[24];
        dayCounts = new int[32];
        monthCounts = new int[13];
        
        monthlyAverages = new double[13];
        
        this.filename = "";
        // Create the reader to obtain the data.
        //reader = new LogfileReader();
    }
    
     /**
     * Create an object to analyze hourly web accesses.
     * 
     * @param filename The name of the file to analyze
     */
    public LogAnalyzer(String filename)
    { 
        // Create the array objects to hold the access counts.
        hourCounts = new int[24];
        dayCounts = new int[32];
        monthCounts = new int[13];
        
        monthlyAverages = new double[13];
        
        this.filename = filename;
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        reader = new LogfileReader(filename);
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }
    
    /**
     *  Analyze the daily access data from the log file.
     */
    public void analyzeDailyData()
    {
        reader = new LogfileReader(filename);
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int day = entry.getDay();
            dayCounts[day]++;
        }
    }
    
    /**
     *  Analyze the monthly access data from the log file.
     */
    public void analyzeMonthlyData()
    {
        reader = new LogfileReader(filename);
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int month = entry.getMonth();
            monthCounts[month]++;
        }
    }
    
    /**
     *  Calculate the average number of accesses per month.
     */
    public void averageAccessesPerMonth()
    {
        reader = new LogfileReader(filename);
        int year = 0;
        int yearCounter = 0; //Keeps track of the number of years
        
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            //Check if entry is for a new year
            if(year != entry.getYear()) {
                yearCounter++;
                year = entry.getYear();
            }
            int month = entry.getMonth();
            monthCounts[month]++;
        }
        
        for(int i=1; i < monthlyAverages.length; i++) {
            monthlyAverages[i] = (monthCounts[i] * 1.0) / yearCounter;
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
     *  Print the daily counts.
     *  These should have been set with a prior
     *  call to analyzeDailyData.
     */
    public void printDailyCounts()
    {
        System.out.println("Day : Count");
        for(int day=1; day < dayCounts.length; day++) {
            System.out.println(day + ": " + dayCounts[day]);
        }
    }
    
    /**
     *  Print the monthly counts.
     *  These should have been set with a prior
     *  call to analyzeMontlyData.
     */
    public void printMonthlyCounts()
    {
        System.out.println("Month : Count");
        for(int month=1; month < monthCounts.length; month++) {
            System.out.println(month + ": " + monthCounts[month]);
        }
    }
    
    /**
     *  Print the average monthly counts.
     *  These should have been set with a prior
     *  call to averageAccessesPerMonth.
     */
    public void printMontlyAverage()
    {
        System.out.println("Month : Avg. Count");
        for(int month=1; month < monthlyAverages.length; month++) {
            System.out.println(month + ": " + monthlyAverages[month]);
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
        analyzeHourlyData();
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
        analyzeHourlyData();
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
       analyzeHourlyData();
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
        analyzeHourlyData();
        for(int i=0; i<hourCounts.length; i++) {
            if(hourCounts[i] < lowest) {
                lowest = hourCounts[i];
                hour = i;
            }
        }
        return hour;   
    }
    
    /**
     *  Return the least busy day in the log file.
     */
    public int quietestDay()
    {
        int lowest = dayCounts[1];
        int day = 0;
        analyzeDailyData();
        for(int i=1; i<dayCounts.length; i++) {
            if(dayCounts[i] < lowest) {
                lowest = dayCounts[i];
                day = i;
            }
        }
        return day;
    }
    
    /**
     *  Return the busiest day in the log file.
     */
    public int busiestDay()
    {
        int highest = dayCounts[1];
        int day = 0;
        analyzeDailyData();
        for(int i=1; i<dayCounts.length; i++) {
            if(dayCounts[i] > highest) {
                highest = dayCounts[i];
                day = i;
            }
        }
        return day;
    }
    
    /**
     *  Return the least busy month in the log file.
     */
    public int quietestMonth() 
    {
        int lowest = monthCounts[1];
        int month = 0;
        analyzeMonthlyData();
        for(int i=1; i < monthCounts.length; i++) {
            if(monthCounts[i] < lowest) {
                lowest = monthCounts[i];
                month = i;
            }
        }
        return month;
    }
    
    /**
     *  Return the busiest month in the log file.
     */
    public int busiestMonth() {
        int highest = monthCounts[1];
        int month = 0;
        analyzeMonthlyData();
        for(int i=1; i < monthCounts.length; i++) {
            if(monthCounts[i] > highest) {
                highest = monthCounts[i];
                month = i;
            }
        }
        return month;
    }
}
