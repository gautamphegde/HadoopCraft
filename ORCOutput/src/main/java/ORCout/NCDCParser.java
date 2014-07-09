package ORCout;

/**
 * Created by gautam on 7/9/14.
 */
public class NCDCParser {

    private double airTemp;
    private String stationId;
    private double latitude;
    private double longitude;

    public double getAirTemp() {
        return airTemp;
    }

    public String getStationId() {
        return stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private final String MISSING = "9999";
    private final String invalidUsaf = "999999";
    private final String invalidWban = "99999";

    public boolean isValid() {
        return isValid;
    }

    private boolean isValid= false;

    public void parse(String line)
    {
        isValid = false;
        String air_temp_qual = line.substring(92,93);
        //String air_temp = line.substring(87,92);

        airTemp =0;
        String temp;
        temp = line.substring(88,92);


        if(air_temp_qual.matches("[01459]") && (!temp.equals(MISSING)))
        {

            isValid = true;

            if(line.charAt(87) == '+')
            {
                airTemp = (Integer.parseInt(line.substring(88,92)))/10.0;
            }
            else
                airTemp = (Integer.parseInt(line.substring(87,92)))/10.0;


            String usaf = line.substring(4,10);
            String wban = line.substring(10,15);


            if(!usaf.equals(invalidUsaf))
            {
                stationId = usaf;
            }
            else if(!wban.equals(invalidWban))
            {
                stationId = wban;
            }
            else
                stationId = "UNKNOWN";


            latitude =  Double.valueOf(line.substring(28,34))/1000 ;
            longitude = Double.valueOf(line.substring(34,41))/1000 ;
    }

    }
}
