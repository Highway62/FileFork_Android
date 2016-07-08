package highway62.filefork;

public class Device {

    private long id;
    private String deviceName;
    private String deviceType;      // ANDROID, IPHONE etc

    public Device(long id, String deviceName, String deviceType) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

}

