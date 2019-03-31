import java.util.HashMap;
/**
 * The new and improved LogAnalyzer!
 * Read web server data and analyse access patterns...
 * But do it better this time!
 * 
 * @author Claire Iroudayassamy
 * @version    2019.03.30
 * 
 */
public class LogAnalyzerPro
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    //Where to calculate the daily access counts.
    private int[] dayCounts;
    // Where to calculate the monthly access counts.
    private int[] monthCounts;
    // Where to calculate average mountly counts.
    private double[] monthlyAverages;
    // Where to calculate access code counts.
    private int[] codeCounts;
    // Where to calculate yearly counts.
    private HashMap<Integer, Integer> yearCounts;
    
    // Determines if data has been analyzed.
    private boolean analyzed = false;
    
    // Use a LogfileReader to access the data.
    private LogfileReader reader;
    private LogfileReader reader2;

    /**
     * Create an object to analyze web accesses.
     * 
     */
    public LogAnalyzerPro()
    { 
        // Create the array objects to hold the access counts.
        hourCounts = new int[24];
        dayCounts = new int[32];
        monthCounts = new int[13];
        codeCounts = new int[3];
        monthlyAverages = new double[13];
        // Create the HashMap object to hold year access counts.
        yearCounts = new HashMap<Integer, Integer>();
        
        // Create the reader to obtain the data.
        reader = new LogfileReader();
        reader2 = new LogfileReader();
    }
    
    /**
     * Create an object to analyze web accesses.
     * 
     * @param filename The name of the file to analyze
     */
    public LogAnalyzerPro(String filename)
    { 
        // Create the array objects to hold the access counts.
        hourCounts = new int[24];
        dayCounts = new int[32];
        monthCounts = new int[13];
        monthlyAverages = new double[13];
        codeCounts = new int[3];
        // Create the HashMap objects to hold access counts.
        yearCounts = new HashMap<Integer, Integer>();
        
        reader = new LogfileReader(filename);
        reader2 = new LogfileReader(filename);
    }

    /**
     * Analyze access data from the log file.
     */
    public void analyzeData()
    {
        if(!analyzed) {
            while(reader.hasNext()) {
                LogEntry entry = reader.next();
                int hour = entry.getHour();
                int day = entry.getDay();
                int month = entry.getMonth();
                int year = entry.getYear();
                int code = entry.getCode();
                
                //increase array objects
                hourCounts[hour]++;
                dayCounts[day]++;
                monthCounts[month]++;
                //increase HashMap value
                if(!yearCounts.containsKey(year)) {
                    yearCounts.put(year,1);
                } else {
                    yearCounts.replace(year,yearCounts.get(year) +1);
                }
                //increase code array
                switch(code) {
                    case 200: codeCounts[0]++;
                              break;
                    case 404: codeCounts[1]++;
                              break;
                    case 403: codeCounts[2]++;
                              break;
                    default: System.out.println("Error! Access code not recognized.");
                }
            }
            analyzed = true;
        } else {
            System.out.println("Data has already been analyzed");
        }
    }
    
    /**
     *  Calculate the average number of accesses per month.
     */
    public void monthlyAverages()
    {
        if(analyzed) {
            for(int i=1; i < monthlyAverages.length; i++) {
                monthlyAverages[i] = (monthCounts[i] * 1.0) / yearCounts.size();
            }
        } else {
            System.out.println("Please analyze data first.");
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeData.
     */
    public void printHourlyCounts()
    {
        if(analyzed) {
            System.out.println("Hr: Count");
            for(int hour = 0; hour < hourCounts.length; hour++) {
                System.out.println(hour + ": " + hourCounts[hour]);
            }
        } else {
            System.out.println("Please analyze data first.");
        }
    }
    
    /**
     *  Print the daily counts.
     *  These should have been set with a prior
     *  call to analyzeData.
     */
    public void printDailyCounts()
    {
        if(analyzed) {
            System.out.println("Day : Count");
            for(int day=1; day < dayCounts.length; day++) {
                System.out.println(day + ": " + dayCounts[day]);
            }
        } else {
            System.out.println("Please analyze data first.");
        }
    }
    
    /**
     *  Print the monthly counts.
     *  These should have been set with a prior
     *  call to analyzeData.
     */
    public void printMonthlyCounts()
    {
        if(analyzed) {
            System.out.println("Month : Count");
            for(int month=1; month < monthCounts.length; month++) {
                System.out.println(month + ": " + monthCounts[month]);
            }
        } else {
            System.out.println("Please analyze data first.");
        }
    }
    
    /**
     *  Print the average monthly counts.
     *  These should have been set with a prior
     *  call to monthlyAverages.
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
        if(analyzed) {
            reader.printData();
        } else {
            System.out.println("Please analyze data first.");
        }
    }
    
    /**
     *  Return the number of accesses recorded in the log file.
     */
    public int numberOfAccesses()
    {
        int total = 0;
        if(analyzed) {
            for(int i=0; i<hourCounts.length; i++) {
                total += hourCounts[i];
            }
        } else {
            System.out.println("Please analyze data first.");
        }
        return total;
    }
    
    /**
     *  Return the busiest hour in the log file.
     */
    public int busiestHour()
    {
        int hour = 0;
        if(analyzed) {
            int highest = hourCounts[0];
            for(int i=0; i<hourCounts.length; i++) {
                if (hourCounts[i] > highest) {
                    highest = hourCounts[i];
                    hour = i;
                }
            }
        } else {
            System.out.println("Please analyze data first.");
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
       int hour = 0;
       if(analyzed) {
           int highest = hourCounts[0];
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
       } else {
            System.out.println("Please analyze data first.");
       }
       return hour; 
    }
    
    /**
     *  Return the least busy hour in the log file.
     */
    public int quietestHour()
    {
        int hour = 0;
        if(analyzed) {
            int lowest = hourCounts[0];
            for(int i=0; i<hourCounts.length; i++) {
                if(hourCounts[i] < lowest) {
                    lowest = hourCounts[i];
                    hour = i;
                }
            }
        } else {
            System.out.println("Please analyze data first.");
        }
        return hour;   
    }
    
    /**
     *  Return the least busy day in the log file.
     */
    public int quietestDay()
    {
        int day = 0;
        if(analyzed) {
            int lowest = dayCounts[1];
            for(int i=1; i<dayCounts.length; i++) {
                if(dayCounts[i] < lowest) {
                    lowest = dayCounts[i];
                    day = i;
                }
            }
        } else {
            System.out.println("Please analyze data first.");
        }
        return day;
    }
    
    /**
     *  Return the busiest day in the log file.
     */
    public int busiestDay()
    {
        int day = 0;
        if(analyzed) {
            int highest = dayCounts[1];
            for(int i=1; i<dayCounts.length; i++) {
                if(dayCounts[i] > highest) {
                    highest = dayCounts[i];
                    day = i;
                }
            }
        } else {
            System.out.println("Please analyze data first.");
        }
        return day;
    }
    
    /**
     *  Return the least busy month in the log file.
     */
    public int quietestMonth() 
    {
        int month = 0;
        if(analyzed) {
            int lowest = monthCounts[1];
            for(int i=1; i < monthCounts.length; i++) {
                if(monthCounts[i] < lowest) {
                    lowest = monthCounts[i];
                    month = i;
                }
            }
        } else {
            System.out.println("Please analyze data first.");
        }
        return month;
    }
    
    /**
     *  Return the busiest month in the log file.
     */
    public int busiestMonth() {
        int month = 0;
        if(analyzed) {
            int highest = monthCounts[1];
            for(int i=1; i < monthCounts.length; i++) {
                if(monthCounts[i] > highest) {
                    highest = monthCounts[i];
                    month = i;
                }
            }
        } else {
            System.out.println("Please analyze data first.");
        }
        return month;
    }
    
    /**
     *  Print counts of successful, not found, and
     *  forbidden acceses per month for a given year.
     *  
     *  @param year The year of the counts to print
     */
    public void accessCodeCount(int year)
    {
        LogEntry entry = reader2.next();
        int month = 1;
        int yr = entry.getYear();
        
        //Access code counters
        int code200 = 0;
        int code404 = 0;
        int code403 = 0;
        
        System.out.println("Year: " + year);
        System.out.println("Month : Code: Access Count");
        while(reader2.hasNext()) {
            if(yr != year) { //If entry isn't the year we want, move to next entry.
                entry = reader2.next();
                yr = entry.getYear();
            } else if(month != entry.getMonth()) {
                /*If entry month doesn't match
                  Print out current month details
                  and increase month by 1.
                  Reset code counters */
                System.out.println(month + ":  200 OK: \t   " + code200);
                System.out.println("    404 Not Found: " + code404);
                System.out.println("    403 Forbidden: " + code403 + "\n");
                month++;
                code200 = 0;
                code404 = 0;
                code403 = 0;
            } else if (month == entry.getMonth()) {
                switch(entry.getCode()) {
                    case 200: code200++;
                                break;
                    case 404: code404++;
                                break;
                    case 403: code403++;
                    default:    break;
                }
                //move on to the next item and reset the year
                entry = reader2.next();
                yr = entry.getYear();
            }
        }
        //Print the rest of months details if needed.
        while(month <= 12) {
            System.out.println(month + ":  200 OK: \t   " + code200);
            System.out.println("    404 Not Found: " + code404);
            System.out.println("    403 Forbidden: " + code403 + "\n");
            month++;
            code200 = 0;
            code404 = 0;
            code403 = 0;
        }
    }
}
