package com.example.myrestservice.model;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class CurrentConditions {
    private String datetime;
    private long datetimeEpoch;
    private double temp;
    private double feelslike;
    private double humidity;
    private double dew;
    private double precip;
    private double precipprob;
    private double snow;
    private double snowdepth;
    private Preciptype[] preciptype;
    private double windgust;
    private double windspeed;
    private double winddir;
    private double pressure;
    private double visibility;
    private double cloudcover;
    private double solarradiation;
    private double solarenergy;
    private double uvindex;
    private Conditions conditions;
    private Icon icon;
    private Weather.Source source;
    private String sunrise;
    private Long sunriseEpoch;
    private String sunset;
    private Long sunsetEpoch;
    private Double moonphase;
    private Double tempmax;
    private Double tempmin;
    private Double feelslikemax;
    private Double feelslikemin;
    private Double precipcover;
    private Double severerisk;
    private Description description;
    private CurrentConditions[] hours;

    public String getDatetime() { return datetime; }
    public void setDatetime(String value) { this.datetime = value; }

    public long getDatetimeEpoch() { return datetimeEpoch; }
    public void setDatetimeEpoch(long value) { this.datetimeEpoch = value; }

    public double getTemp() { return temp; }
    public void setTemp(double value) { this.temp = value; }

    public double getFeelslike() { return feelslike; }
    public void setFeelslike(double value) { this.feelslike = value; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double value) { this.humidity = value; }

    public double getDew() { return dew; }
    public void setDew(double value) { this.dew = value; }

    public double getPrecip() { return precip; }
    public void setPrecip(double value) { this.precip = value; }

    public double getPrecipprob() { return precipprob; }
    public void setPrecipprob(double value) { this.precipprob = value; }

    public double getSnow() { return snow; }
    public void setSnow(double value) { this.snow = value; }

    public double getSnowdepth() { return snowdepth; }
    public void setSnowdepth(double value) { this.snowdepth = value; }

    public Preciptype[] getPreciptype() { return preciptype; }
    public void setPreciptype(Preciptype[] value) { this.preciptype = value; }

    public double getWindgust() { return windgust; }
    public void setWindgust(double value) { this.windgust = value; }

    public double getWindspeed() { return windspeed; }
    public void setWindspeed(double value) { this.windspeed = value; }

    public double getWinddir() { return winddir; }
    public void setWinddir(double value) { this.winddir = value; }

    public double getPressure() { return pressure; }
    public void setPressure(double value) { this.pressure = value; }

    public double getVisibility() { return visibility; }
    public void setVisibility(double value) { this.visibility = value; }

    public double getCloudcover() { return cloudcover; }
    public void setCloudcover(double value) { this.cloudcover = value; }

    public double getSolarradiation() { return solarradiation; }
    public void setSolarradiation(double value) { this.solarradiation = value; }

    public double getSolarenergy() { return solarenergy; }
    public void setSolarenergy(double value) { this.solarenergy = value; }

    public double getUvindex() { return uvindex; }
    public void setUvindex(double value) { this.uvindex = value; }

    public Conditions getConditions() { return conditions; }
    public void setConditions(Conditions value) { this.conditions = value; }

    public Icon getIcon() { return icon; }
    public void setIcon(Icon value) { this.icon = value; }


    public Weather.Source getSource() { return source; }
    public void setSource(Weather.Source value) { this.source = value; }

    public OffsetTime getSunrise() {
        return sunrise != null ? OffsetTime.parse(sunrise, DateTimeFormatter.ISO_OFFSET_TIME) : null;
    }

    public void setSunrise(OffsetTime sunrise) {
        this.sunrise = sunrise != null ? sunrise.format(DateTimeFormatter.ISO_OFFSET_TIME) : null;
    }
    public Long getSunriseEpoch() { return sunriseEpoch; }
    public void setSunriseEpoch(Long value) { this.sunriseEpoch = value; }

    public OffsetTime getSunset() {
        return sunset != null ? OffsetTime.parse(sunset, DateTimeFormatter.ISO_OFFSET_TIME) : null;
    }

    public void setSunset(OffsetTime sunset) {
        this.sunset = sunset != null ? sunset.format(DateTimeFormatter.ISO_OFFSET_TIME) : null;
    }
    public Long getSunsetEpoch() { return sunsetEpoch; }
    public void setSunsetEpoch(Long value) { this.sunsetEpoch = value; }

    public Double getMoonphase() { return moonphase; }
    public void setMoonphase(Double value) { this.moonphase = value; }

    public Double getTempmax() { return tempmax; }
    public void setTempmax(Double value) { this.tempmax = value; }

    public Double getTempmin() { return tempmin; }
    public void setTempmin(Double value) { this.tempmin = value; }

    public Double getFeelslikemax() { return feelslikemax; }
    public void setFeelslikemax(Double value) { this.feelslikemax = value; }

    public Double getFeelslikemin() { return feelslikemin; }
    public void setFeelslikemin(Double value) { this.feelslikemin = value; }

    public Double getPrecipcover() { return precipcover; }
    public void setPrecipcover(Double value) { this.precipcover = value; }

    public Double getSevererisk() { return severerisk; }
    public void setSevererisk(Double value) { this.severerisk = value; }

    public Description getDescription() { return description; }
    public void setDescription(Description value) { this.description = value; }

    public CurrentConditions[] getHours() { return hours; }
    public void setHours(CurrentConditions[] value) { this.hours = value; }
}