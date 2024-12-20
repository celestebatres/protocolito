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
        this.componentsMap.put("lrgb_color", new Component(vd.substring(11, 17)));
        this.componentsMap.put("space", new Component(vd.substring(17, 18)));
        this.componentsMap.put("pick_color", new Component(vd.substring(18, 24)));
        this.componentsMap.put("msg", new Component(vd.substring(24)));
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
            if(component.equals("msg")){
                this.componentsMap.get("msg").setValue(stringToHex(value));
                return;
            }
            this.componentsMap.get(component).setValue(value);
        }else if(action.equals("cmd")){
            this.componentsMap.get(component).setFunction(value);
        }
    }

    public static String stringToHex(String input) {
        int targetLength = 32;
        if (input.length() == targetLength) {
        } else if (input.length() < targetLength) {
            // Rellenar con espacios en blanco a la derecha
            input = String.format("%-" + targetLength + "s", input);
        } else {
            // Si es mayor, truncar al tamaño requerido
            input =  input.substring(0, targetLength);
        }
        
        StringBuilder hexString = new StringBuilder();
        
        for (char c : input.toCharArray()) {
            hexString.append(Integer.toHexString((int) c));
        }
        
        return hexString.toString();
    }

    public void eraseTextArea(){
        this.componentsMap.get("msg").setValue("");
    }

    public String toHex(String value){
        int decimal = Integer.parseInt(value, 2);
        String hex = String.format("%02X", decimal);
        return hex;
    }

    // Devuelve el VD completo en dependencia de los atributos
    public String buildVD(){//Sin msg
        System.out.println("Speed22: " + this.componentsMap.get("speed").value);
        String vd = buildControl() + this.componentsMap.get("speed").value + this.componentsMap.get("space").value + this.componentsMap.get("slider0").value + this.componentsMap.get("slider1").value + this.componentsMap.get("slider2").value + this.componentsMap.get("space").value + this.componentsMap.get("lrgb_color").value + this.componentsMap.get("space").value + this.componentsMap.get("pick_color").value + this.componentsMap.get("msg").value;
        return vd;
    }

    public String buildControl(){
        
        // Obtener los 8 bits del control
        String control = "";
        control += this.componentsMap.get("lcd").value;
        control += this.componentsMap.get("switch0").value; 
        control += this.componentsMap.get("switch1").value;
        control += this.componentsMap.get("fan").value;
        control += this.componentsMap.get("lrgb").value;
        control += this.componentsMap.get("lred").value;
        control += this.componentsMap.get("lgreen").value;
        control += this.componentsMap.get("heat").value;
        
        // Convertir binario a decimal
        int decimal = Integer.parseInt(control, 2);
        // Convertir los primeros 8 bits a hexadecimal
        // int decimal = Integer.parseInt(control.substring(0,8), 2);
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
        return this.componentsMap.get("pick_color").value;
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

    public void setLedRgb(String lrgb_color) {
        this.componentsMap.get("lrgb_color").setValue(lrgb_color);
    }

    public void setPickColor(String pick_color) {
        this.componentsMap.get("pick_color").setValue(pick_color);
    }

    public void setMessage(String msg) {
        this.componentsMap.get("msg").setValue(msg);
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

    public static String decimalToHex(String decimalString) {
        try {
            // Convertir el string decimal a un entero
            int decimal = Integer.parseInt(decimalString);
            // Convertir el entero a hexadecimal
            return Integer.toHexString(decimal).toUpperCase();
        } catch (NumberFormatException e) {
            // Manejo de errores en caso de formato inválido
            throw new IllegalArgumentException("El input no es un número decimal válido: " + decimalString);
        }
    }
}//se puede recorrer por hashmap o por lista de componentes
