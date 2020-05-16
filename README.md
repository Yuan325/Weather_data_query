# Weather_data_query
The data we will use is located at ftp://ftp.ncdc.noaa.gov/pub/data/ghcn/daily/ --> ghcnd_hcn.tar.gz.

The maven project included in this repo do not contains the weather data (file too large). You will have to download the file in order to run.

This program will present a set of questions including starting year, ending year, starting month, ending month, and whether to calculate max or min temperature. It will print the max (or min) five temperature that occurred in that range of years and months.

Program will utilize thread pools, futures, and callable to complete its processing.


# run:
Download and include the data file.
If run through maven, run via "java -cp target/my-app2-1.0-SNAPSHOT.jar com.yuanteoh.ics440pa2.Driver"
