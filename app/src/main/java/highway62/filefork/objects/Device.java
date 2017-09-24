package highway62.filefork.objects;

public class Device {

    private long id;
    private String deviceName;
    private String deviceType;      // ANDROID, IPHONE etc

    private Device(long id, String deviceName, String deviceType) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    /**
     * Static factory method to return a new device instance
     */
    public static Device newDevice(long id, String deviceName, String deviceType){
        return new Device(id,deviceName,deviceType);
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

