import java.util.*;

public class VirtualDevice {
    private HashMap<String, Component> componentsMap;
    private String vd; // VD completo
    private String message; // Ultimos bits después de 24 en formato de texto

    public VirtualDevice(String vd) {
        String control = vd.substring(0, 2);
        String controlToBinary = Integer.toBinaryString(Integer.parseInt(control, 16));
        // Asegurar que controlToBinary tenga 8 bits
        while (controlToBinary.length() < 8) {
            controlToBinary = "0" + controlToBinary;
        }
        this.componentsMap = new HashMap<>();
        this.componentsMap.put("lcd", new Component(controlToBinary.substring(0, 1)));
        this.componentsMap.put("switch0", new Component(controlToBinary.substring(1, 2)));
        this.componentsMap.put("switch1", new Component(controlToBinary.substring(2, 3)));
        this.componentsMap.put("fan", new Component(controlToBinary.substring(3, 4)));
        this.componentsMap.put("lrgb", new Component(controlToBinary.substring(4, 5)));
        this.componentsMap.put("lred", new Component(controlToBinary.substring(5, 6)));
        this.componentsMap.put("lgreen", new Component(controlToBinary.substring(6, 7)));
        this.componentsMap.put("heat", new Component(controlToBinary.substring(7, 8)));
        // this.componentsMap.put("control", new Component(vd.substring(0, 2)));
        this.componentsMap.put("speed", new Component(vd.substring(2, 3)));
        this.componentsMap.put("space", new Component(vd.substring(3, 4)));
        this.componentsMap.put("slider0", new Component(vd.substring(4,6)));
        this.componentsMap.put("slider1", new Component(vd.substring(6, 8)));
        this.componentsMap.put("slider2", new Component(vd.substring(8, 10)));
        this.componentsMap.put("space", new Component(vd.substring(10, 11)));
        this.componentsMap.put("ledRgb", new Component(vd.substring(11, 17)));
        this.componentsMap.put("space", new Component(vd.substring(17, 18)));
        this.componentsMap.put("pickColor", new Component(vd.substring(18, 24)));

        this.vd = vd;
        String msg = vd.substring(24);
        // Convertir el mensaje hexadecimal a texto
        StringBuilder textoConvertido = new StringBuilder();
        for (int i = 0; i < msg.length(); i += 2) {
            String hex = msg.substring(i, i + 2);
            int decimal = Integer.parseInt(hex, 16);
            textoConvertido.append((char) decimal);
        }
        this.message = textoConvertido.toString();
    }

    public void execute(Command command){
        // Ejecutar el comando sobre esta instancia de VD
        String action = command.getAction();
        String component = command.getComponent();
        String value = command.getValue();

        if(action.equals("msg")){
            // Cambiar el valor del componente
            switch(component){
                case "lcd":
                    this.componentsMap.get("lcd").setValue(value);
                    break;
                case "switch0":
                    this.componentsMap.get("switch0").setValue(value);
                    break;
                case "switch1":
                    this.componentsMap.get("switch1").setValue(value);
                    break;
                case "fan":
                    this.componentsMap.get("fan").setValue(value);
                    break;
                case "lrgb":
                    this.componentsMap.get("lrgb").setValue(value);
                    break;
                case "lred":
                    this.componentsMap.get("lred").setValue(value);
                    break;
                case "lgreen":
                    this.componentsMap.get("lgreen").setValue(value);
                    break;
                case "heat":
                    this.componentsMap.get("heat").setValue(value);
                    break;
                case "speed":
                    this.componentsMap.get("speed").setValue(value);
                    break;
                case "slider0":
                    this.componentsMap.get("slider0").setValue(value);
                    break;
                case "slider1":
                    this.componentsMap.get("slider1").setValue(value);
                    break;
                case "slider2":
                    this.componentsMap.get("slider2").setValue(value);
                    break;
                case "ledRgb":
                    this.componentsMap.get("ledRgb").setValue(value);
                    break;
                case "pickColor":
                    this.componentsMap.get("pickColor").setValue(value);
                    break;
                case "message":
                    this.componentsMap.get("message").setValue(value);
                    break;
                default:
                    break;
            }
        }else if(action.equals("cmd")){
            // Cambiar la función del componente
            switch(component){
                case "control":
                    this.componentsMap.get("control").setFunction(value);
                    break;
                case "speed":
                    this.componentsMap.get("speed").setFunction(value);
                    break;
                case "slider0":
                    this.componentsMap.get("slider0").setFunction(value);
                    break;
                case "slider1":
                    this.componentsMap.get("slider1").setFunction(value);
                    break;
                case "slider2":
                    this.componentsMap.get("slider2").setFunction(value);
                    break;  
                case "ledRgb":
                    this.componentsMap.get("ledRgb").setFunction(value);
                    break;
                case "pickColor":
                    this.componentsMap.get("pickColor").setFunction(value);
                    break;
                case "message":
                    this.componentsMap.get("message").setFunction(value);
                    break;
                default:
                    break;
            }
        }
    }


    // Devuelve el VD completo en dependencia de los atributos
    public String buildVD(){//Sin msg
        String vd = buildControl() + this.componentsMap.get("speed").value + this.componentsMap.get("space").value + this.componentsMap.get("slider0").value + this.componentsMap.get("slider1").value + this.componentsMap.get("slider2").value + this.componentsMap.get("space").value + this.componentsMap.get("ledRgb").value + this.componentsMap.get("space").value + this.componentsMap.get("pickColor").value;
        return vd;
    }

    public String buildControl(){
        String control = "";
        control += this.componentsMap.get("control").value;
        control += this.componentsMap.get("speed").value;
        control += this.componentsMap.get("slider0").value;
        control += this.componentsMap.get("slider1").value;
        control += this.componentsMap.get("slider2").value;
        control += this.componentsMap.get("ledRgb").value;
        control += this.componentsMap.get("pickColor").value;
        control += this.componentsMap.get("message").value;
        
        // Convertir los primeros 8 bits a hexadecimal
        int decimal = Integer.parseInt(control.substring(0,8), 2);
        String hex = String.format("%02X", decimal);
        return hex;
    }

    public HashMap<String, Component> getComponentsMap(){
        return this.componentsMap;
    }  

    // Getters
    public String getControl() {
        return this.componentsMap.get("control").value;
    }

    public String getSpeed() {
        return this.componentsMap.get("speed").value;
    }

    public String getSlider0() {
        return this.componentsMap.get("slider0").value;
    }

    public String getSlider1() {
        return this.componentsMap.get("slider1").value;
    }

    public String getSlider2() {
        return this.componentsMap.get("slider2").value;
    }

    public String getLedRgb() {
        return this.componentsMap.get("ledRgb").value;
    }

    public String getPickColor() {
        return this.componentsMap.get("pickColor").value;
    }

    public String getMessage() {
        return this.componentsMap.get("message").value;
    }

    // Setters
    public void setControl(String control) {
        this.componentsMap.get("control").setValue(control);
    }

    public void setSpeed(String speed) {
        this.componentsMap.get("speed").setValue(speed);
    }

    public void setSlider0(String slider0) {
        this.componentsMap.get("slider0").setValue(slider0);
    }

    public void setSlider1(String slider1) {
        this.componentsMap.get("slider1").setValue(slider1);
    }

    public void setSlider2(String slider2) {
        this.componentsMap.get("slider2").setValue(slider2);
    }

    public void setLedRgb(String ledRgb) {
        this.componentsMap.get("ledRgb").setValue(ledRgb);
    }

    public void setPickColor(String pickColor) {
        this.componentsMap.get("pickColor").setValue(pickColor);
    }

    public void setMessage(String message) {
        this.componentsMap.get("message").setValue(message);
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        VirtualDevice other = (VirtualDevice) obj;
        
        return getControl().equals(other.getControl()) &&
               getSpeed().equals(other.getSpeed()) &&
               getSlider0().equals(other.getSlider0()) &&
               getSlider1().equals(other.getSlider1()) &&
               getSlider2().equals(other.getSlider2()) &&
               getLedRgb().equals(other.getLedRgb()) &&
               getPickColor().equals(other.getPickColor()) &&
               getMessage().equals(other.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getControl(), getSpeed(), getSlider0(), getSlider1(), 
                          getSlider2(), getLedRgb(), getPickColor(), getMessage());
    }
}//se puede recorrer por hashmap o por lista de componentes
